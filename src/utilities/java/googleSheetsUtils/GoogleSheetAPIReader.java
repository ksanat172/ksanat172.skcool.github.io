package googleSheetsUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GoogleSheetAPIReader {
	List<List<Object>> sheetData=null;
	String sheetName=null;
	public static String googleSheetID=null;

	public GoogleSheetAPIReader(String sheetName){
		if(sheetName.equals("Default test name")==false){
		Sheets service = null;
		ValueRange response=null;
		try {
			this.sheetName=sheetName;
			service = GoogleSheetAPILibrary.getSheetsService();
			try{
				response = service.spreadsheets().values().get(googleSheetID, sheetName+"!A1:Z1000").execute();}
			catch(SocketTimeoutException e){
				response = service.spreadsheets().values().get(googleSheetID, sheetName+"!A1:Z1000").execute();
			}
			sheetData = response.getValues();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
	}

	public int getRowCount(){
		return sheetData.size();
	}

	public String getCellData(int columnNo,int rowNo){
		List<List<Object>> rows=sheetData;
		try{
		if(columnNo!=-1){
			if(rows.get(rowNo).size()>columnNo){
				return getStringValue(rows.get(rowNo).get(columnNo)).trim();}
			else{
				return "";
			}
		}
		else{
			return "";
		}}
		catch(IndexOutOfBoundsException e){
			return "";
		}
	}
	
	public String getCellData(String columnName,int rowNo){
		List<List<Object>> rows=sheetData;
		int columnNo=getColumnNo(columnName);
		if(columnNo!=-1){
			if(rows.get(rowNo).size()>columnNo){
				return getStringValue(rows.get(rowNo).get(columnNo)).trim();}
			else{
				return "";
			}
		}
		else{
			return "";
		}
	}

	public String getCellData(String field,String columnName){
		int rowNo=getFieldRowNo(field);
		System.out.println("Row No:"+rowNo);
		if(rowNo!=-1){
			List<List<Object>> rows=sheetData;
			int columnNo=-1;
			for(int i=0;i<rows.get(0).size();i++){
				if(getStringValue(rows.get(0).get(i)).equals(columnName)){
					columnNo=i;
					break;
				}
			}
			if(columnNo!=-1){
				if(rows.get(rowNo).size()>columnNo){
					return getStringValue(rows.get(rowNo).get(columnNo)).trim();}else{
						return "";
					}}
			else{
				return "";
			}}else{
				return "";
			}
	}

	public String getCellData(String field1,String field2,String columnName){
		int rowNo=getFieldRowNo(field1,field2);
		if(rowNo!=-1){
			List<List<Object>> rows=sheetData;
			int columnNo=-1;
			for(int i=0;i<rows.get(0).size();i++){
				if(getStringValue(rows.get(0).get(i)).equals(columnName)){
					columnNo=i;
					break;
				}
			}
			if(columnNo!=-1){
				if(rows.get(rowNo).size()>columnNo){
					return getStringValue(rows.get(rowNo).get(columnNo)).trim();}else{
						return "";
					}}
			else{
				return "";
			}}else{
				return "";
			}
	}

	public int getColumnNo(String columnName){
		List<List<Object>> rows=sheetData;
		int columnNo=-1;
		for(int i=0;i<rows.get(0).size();i++){
			if(getStringValue(rows.get(0).get(i)).equals(columnName)){
				columnNo=i;
				break;
			}
		}
		Assert.assertNotEquals(columnNo, -1,"Specified column:"+columnName+" not found");
		return columnNo;
	}

	public int getFieldRowNo(String field){
		int rowNum=-1;
		List<List<Object>> rows=sheetData;
		FOR:for(int i=1;i<rows.size();i++){
			for(int j=0;j<rows.get(i).size();j++){
				if(getStringValue(rows.get(i).get(j)).equals(field)){
					rowNum=i;
					break FOR;
				}
			}
		}
		Assert.assertNotEquals(rowNum, -1,"Specified field:"+field+" not found");
		return rowNum;
	}

	public int getFieldRowNo(String field1,String field2){
		int rowNum=-1;
		int rowNum2=-1;
		List<List<Object>> rows=sheetData;
		FOR:for(int i=1;i<rows.size();i++){
			for(int j=0;j<rows.get(i).size();j++){
				if(getStringValue(rows.get(i).get(j)).equals(field1)){
					rowNum2=0;
					for(int k=0;k<rows.get(i).size();k++){
						if(getStringValue(rows.get(i).get(k)).equals(field2)){
							rowNum=i;
							break FOR;
						}
					}
				}
			}
		}
		Assert.assertNotEquals(rowNum2, -1,"Field1:"+field1+" not found");
		Assert.assertNotEquals(rowNum,  -1,"Field2:"+field2+" not found");
		return rowNum;
	}

	public int getFieldColumnNo(String field){
		int columnNo=-1;
		List<List<Object>> rows=sheetData;
		FOR:for(int i=0;i<rows.size();i++){
			for(int j=0;j<rows.get(i).size();j++){
				if(getStringValue(rows.get(i).get(j)).equals(field)){
					columnNo=j;
					break FOR;
				}
			}
		}
		Assert.assertNotEquals(columnNo, -1,"Field:"+field+" not found");
		return columnNo;
	}

	public String getFieldValue(String field){
		List<List<Object>> rows=sheetData;
		String value=null;
		FOR:for(int i=0;i<rows.size();i++){
			for(int j=0;j<rows.get(i).size();j++){
				if(getStringValue(rows.get(i).get(j)).equals(field)){
					value=getStringValue(rows.get(i+1).get(j));
					break FOR;
				}
			}
		}
		Assert.assertNotNull(value,"Field:"+field+" not found");
		return value;
	}

	public String getStringValue(Object o){
		return ((String)o).trim();
	}

	public void writeValue(int rowNo,int columnNo,String value) throws IOException {
		String range = sheetName+"!"+(char)(columnNo+65)+String.valueOf(rowNo+1)+":"+(char)(columnNo+65);
		List<List<Object>> values = Arrays.asList(Arrays.asList(value));
		ValueRange body = new ValueRange().setValues(values);
		Sheets service = null;
		try {
			service = GoogleSheetAPILibrary.getSheetsService();
			service.spreadsheets().values().update(googleSheetID, range, body).setValueInputOption("RAW").execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeValue(int rowNo,String columnName,String value) {
		int columnNo=getColumnNo(columnName);
		String range = sheetName+"!"+(char)(columnNo+65)+String.valueOf(rowNo+1)+":"+(char)(columnNo+65);
		List<List<Object>> values = Arrays.asList(Arrays.asList(value));
		ValueRange body = new ValueRange().setValues(values);
		Sheets service = null;
		try {
			service = GoogleSheetAPILibrary.getSheetsService();
			service.spreadsheets().values().update(googleSheetID, range, body).setValueInputOption("RAW").execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void writeValue(String field,String columnName,String value) {
		int rowNo=getFieldRowNo(field);
		int columnNo=getColumnNo(columnName);
		String range = sheetName+"!"+(char)(columnNo+65)+String.valueOf(rowNo+1)+":"+(char)(columnNo+65);
		List<List<Object>> values = Arrays.asList(Arrays.asList(value));
		ValueRange body = new ValueRange().setValues(values);
		Sheets service = null;
		try {
			service = GoogleSheetAPILibrary.getSheetsService();
			service.spreadsheets().values().update(googleSheetID, range, body).setValueInputOption("RAW").execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	List<ValueRange> data = new ArrayList<ValueRange>();
	public void collectDataForWrite(int rowNo,String columnName,String value){
		int columnNo=getColumnNo(columnName);
		String range = sheetName+"!"+(char)(columnNo+65)+String.valueOf(rowNo+1)+":"+(char)(columnNo+65);
		List<List<Object>> values = Arrays.asList(Arrays.asList(value));
		ValueRange body = new ValueRange().setValues(values).setRange(range);
		data.add(body);
	}
	
	public void writeAllDataAtOnce() throws IOException{
		BatchUpdateValuesRequest requestBody = new BatchUpdateValuesRequest();
		requestBody.setValueInputOption("RAW");
		requestBody.setData(data);

		Sheets sheetsService = GoogleSheetAPILibrary.getSheetsService();
		Sheets.Spreadsheets.Values.BatchUpdate request =sheetsService.spreadsheets().values().batchUpdate(googleSheetID, requestBody);
		request.execute();
	}
	
	public void cleanSheet() throws IOException{
		List<List<Object>> values = Arrays.asList(Arrays.asList(""));
		List<ValueRange> data = new ArrayList<ValueRange>();
		ValueRange body;
		for(int i=2;i<1000;i++){
			for(int j=0;j<26;j++){
				body = new ValueRange().setValues(values).setRange(sheetName+"!"+(char)(j+65)+String.valueOf(i)+":"+(char)(j+65));
				data.add(body);}
		}

		// TODO: Assign values to desired fields of `requestBody`:
		BatchUpdateValuesRequest requestBody = new BatchUpdateValuesRequest();
		requestBody.setValueInputOption("RAW");
		requestBody.setData(data);

		Sheets sheetsService = GoogleSheetAPILibrary.getSheetsService();
		Sheets.Spreadsheets.Values.BatchUpdate request =sheetsService.spreadsheets().values().batchUpdate(googleSheetID, requestBody);
		request.execute();
	}

}