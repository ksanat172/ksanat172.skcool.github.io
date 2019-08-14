package railwayenquiry;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import static utilities.BaseClass.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import com.jayway.jsonpath.JsonPath;

public class RailwayEnquiryAPI {
	HttpClient httpclient=HttpClientBuilder.create().build();
	String source="";
	String destination="";
	String date="";
	String trainName="";
	String trainNo="";
	int row=1;

	public void getAPIRequiredDetails() throws ClientProtocolException, IOException, ParseException{
		String source=journeyInfoSheetName.getFieldValue("From");
		String destination=journeyInfoSheetName.getFieldValue("To");
		String journeyDate=journeyInfoSheetName.getFieldValue("Date");
		Calendar c=Calendar.getInstance();
		String date[]=journeyDate.split("-");
		c.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
		this.date=String.valueOf(c.get(Calendar.YEAR))+String.format("%02d",c.get(Calendar.MONTH))+String.format("%02d",c.get(Calendar.DATE));
		String url="https://www.irctc.co.in/eticketing/protected/mapps1/tbstns/"+source+"/"+destination+"/"+this.date+"?dateSpecific=N&ftBooking=N&redemBooking=N&journeyType=GN&captcha=";
		HttpGet get=new HttpGet(url);
		get.addHeader("greq","0");
		HttpResponse response=httpclient.execute(get);
		BufferedInputStream bis=new BufferedInputStream(response.getEntity().getContent());
		int i;
		StringBuilder st=new StringBuilder();
		while((i=bis.read())!=-1){
			st.append((char)i);
		}
		bis.close();
		JSONObject obj=new JSONObject(st.toString());
		int availableTrains=obj.getJSONArray("trainBtwnStnsList").length();

		for(i=0;i<availableTrains;i++){
			HashMap<String,String> map=new HashMap<String,String>();
			map.clear();
			trainNo=JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].trainNumber").toString();
			trainName=JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].trainName").toString();
			this.source=JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].fromStnCode").toString();
			this.destination=JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].toStnCode").toString();
			map.put("Train No", trainNo);
			map.put("Train Name",trainName);
			map.put("Distance", JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].distance").toString()+"Kms");
			map.put("Departure time(From Source)", "Departing from "+this.source+" at "+JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].departureTime").toString());
			map.put("Arrival Time(To Destination)", "Arriving to "+this.destination+" at "+JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].arrivalTime").toString());
			map.put("Travel time", JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].duration").toString());
			try{
				int availableClasses=obj.getJSONArray("trainBtwnStnsList").getJSONObject(i).getJSONArray("avlClasses").length();
				for(int j=0;j<availableClasses;j++){
					map.put("Class", JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].avlClasses["+j+"]").toString());
					getSeatAvailabilityDetails(map,JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].avlClasses["+j+"]").toString());
				}}
			catch(JSONException e){
				map.put("Class", JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].avlClasses").toString());
				getSeatAvailabilityDetails(map,JsonPath.read(st.toString(), "$.trainBtwnStnsList["+i+"].avlClasses").toString());
			}
		}
		enquirySheetName.writeAllDataAtOnce();
	}

	public void getSeatAvailabilityDetails(HashMap<String,String> map,String Class) throws UnsupportedOperationException, IOException{
		StringBuilder str=new StringBuilder();
		HttpPost post=new HttpPost("https://www.irctc.co.in/eticketing/protected/mapps1/avlFareenquiry/"+trainNo+"/"+date+"/"+source+"/"+destination+"/"+Class+"/GN/N");
		post.addHeader("greq", "0");
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Referer","https://www.irctc.co.in/nget/train-list");
		httpclient=HttpClientBuilder.create().build();
		String param=CONSTANTS.getProperty("PARAM");
		StringEntity entity=new StringEntity(param);
		post.setEntity(entity);
		HttpResponse response=httpclient.execute(post);
		BufferedInputStream bis=new BufferedInputStream(response.getEntity().getContent());
		int i;
		while((i=bis.read())!=-1){
			str.append((char)i);
		}
		bis.close();
		JSONObject obj=new JSONObject(str.toString());
		int length=obj.getJSONArray("avlDayList").length();
		for(i=0;i<length;i++){
			Iterator<String> it=map.keySet().iterator();
			while(it.hasNext()){
				String key=it.next();
				enquirySheetName.collectDataForWrite(row, key, map.get(key));			
			}
			enquirySheetName.collectDataForWrite(row, "Fare", JsonPath.read(str.toString(), "$.totalFare").toString()+"Rs");
			enquirySheetName.collectDataForWrite(row,"Availability Date",JsonPath.read(str.toString(), "$.avlDayList["+i+"].availablityDate").toString());
			enquirySheetName.collectDataForWrite(row++,"Availability Status",JsonPath.read(str.toString(), "$.avlDayList["+i+"].availablityStatus").toString());
		}
	}
}
