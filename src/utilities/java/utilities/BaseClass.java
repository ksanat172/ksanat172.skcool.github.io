package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import googleSheetsUtils.GoogleSheetAPIReader;

public class BaseClass {

	public static WebDriver driver;
	public static Properties CONSTANTS;
	public static GoogleSheetAPIReader journeyInfoSheetName;
	public static GoogleSheetAPIReader enquirySheetName;
	FileInputStream fis;

	@BeforeSuite(alwaysRun=true)
	private void init() throws IOException{
		fis=new FileInputStream("./src/main/resources/Constants.Properties");
		CONSTANTS=new Properties();
		CONSTANTS.load(fis);
		GoogleSheetAPIReader.googleSheetID="11MeheaRbY5Qs75HTixwDpvby4suRSHxBhyR6ZidcxWI";
		journeyInfoSheetName=new GoogleSheetAPIReader("Journey Info");
		enquirySheetName=new GoogleSheetAPIReader("Enquiry");
	}

	@AfterSuite(alwaysRun=true)
	private void teardown(){
		//driver.quit();
	}
}
