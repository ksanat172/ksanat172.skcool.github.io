package railenquiry;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RailEnquiryLib {

	@FindBy(css="input[placeholder='From*']")
	private WebElement source;

	public WebElement getSource() {
		return source;
	}
	
	@FindBy(css="input[placeholder='To*']")
	private WebElement destination;

	public WebElement getDestination() {
		return destination;
	}
	
	@FindBy(css="input[placeholder='Journey Date(dd-mm-yyyy)*']")
	private WebElement journeyDate;

	public WebElement getJourneyDate() {
		return journeyDate;
	}
	
	@FindBy(css="button[label='Find Trains']")
	private WebElement submitButton;

	public WebElement getSubmitButton() {
		return submitButton;
	}
	
	@FindBy(xpath="//*[contains(text(),'Please Wait')]")
	private WebElement waitIcon;

	public WebElement getWaitIcon() {
		return waitIcon;
	}
	
	@FindBy(css="select[formcontrolname='classInput']")
	private List<WebElement> classSelection;

	public List<WebElement> getClassSelection() {
		return classSelection;
	}
	
	@FindBy(css=".trainName")
	private List<WebElement> trainName;

	public List<WebElement> getTrainName() {
		return trainName;
	}
	
	@FindBy(xpath="//span[@class='trainName']/..")
	private List<WebElement> trainNumber;

	public List<WebElement> getTrainNumber() {
		return trainNumber;
	}
	
	@FindBy(css=".train_avl_enq_box.fromAndToStn>div>div:nth-of-type(2)>div:nth-of-type(1) h5")
	private List<WebElement> departureTime;

	public List<WebElement> getDepartureTime() {
		return departureTime;
	}
	
	@FindBy(css=".train_avl_enq_box.fromAndToStn>div>div:nth-of-type(2)>div:nth-of-type(2) h5")
	private List<WebElement> arrivalTime;

	public List<WebElement> getArrivalTime() {
		return arrivalTime;
	}
	
	@FindBy(css=".train_avl_enq_box.fromAndToStn>div>div:nth-of-type(2)>div:nth-of-type(3) h5")
	private List<WebElement> journeyTime;

	public List<WebElement> getJourneyTime() {
		return journeyTime;
	}
	
	@FindBy(css=".avl_fare_box")
	private WebElement fare;

	public WebElement getFare() {
		return fare;
	}
	
	@FindBy(css=".waitingstatus")
	private List<WebElement> waitingStaus;

	public List<WebElement> getWaitingStaus() {
		return waitingStaus;
	}
	
	@FindBy(xpath="//span[@class='waitingstatus']/../preceding-sibling::div/span[1]")
	private List<WebElement> waitingStatusDate;
	
	public List<WebElement> getWaitingStatusDate(){
		return waitingStatusDate;
	}
	
	@FindBy(css=".ui-datepicker-trigger.ui-calendar-button")
	private WebElement datePopupIcon;

	public WebElement getDatePopupIcon() {
		return datePopupIcon;
	}
	
	@FindBy(css="#check-availability")
	private List<WebElement> checkAvailabilityBtn;

	public List<WebElement> getCheckAvailabilityBtn() {
		return checkAvailabilityBtn;
	}
	
}
