package railwayenquiry;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static utilities.BaseClass.*;
import org.apache.http.client.ClientProtocolException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.Request;

import googleSheetsUtils.GoogleSheetAPIReader;

public class RailwayEnquiryAPITest {

	RailwayEnquiryAPI lib;

	@BeforeClass
	public void init() throws IOException{
		lib=new RailwayEnquiryAPI();
		enquirySheetName.cleanSheet();
	}

	@Test(priority=1)
	public void enterJourneyDetails() throws ClientProtocolException, IOException, ParseException{
		lib.getAPIRequiredDetails();
	}

	
}
