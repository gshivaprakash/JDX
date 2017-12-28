package ad2pro.fw.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import jxl.JXLException;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import ad2pro.fw.utils.RandomNumberGeneration;
import ad2pro.java.common.reporting.CoreUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class contains all selenium helper that can used to our test execution 
 * as if now we are handling those operations form commonfuncions file
 */

public class SeleniumHelper
{
	private WebDriver drivers;
	Alert alert;
	Actions actions;
	WebDriverWait wait;
	//JavascriptExecutor js;
	LocatorsProperty lp;

	private static Driver driver;
	//static boolean flag = false;
	WebDriver mywebDriver;
	private static String basePath = System.getProperty("user.dir");
	
	
	RandomNumberGeneration rvg = new RandomNumberGeneration();
	PropertiesFileParser prop = new PropertiesFileParser();

	public SeleniumHelper(Driver driver)
	{
		this.driver = driver;
		//this.mywebDriver = driver.getDriver();
	}
	
	public void DownloadFiles() throws AWTException, InterruptedException
	{
		
		FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.download.dir","D:/JobDirect/Resources/Downloads" );
		}
	
	
	/**
	 * Method to check if the element is displayed and enabled on any exception retry looking for element else fails
	 * @param element
	 * @return
	 * @throws IOException 
	 */
	public WebElement isReady(WebElement element) throws IOException
	{
		try 
		{
			if ((element.isDisplayed() == true) && (element.isEnabled() == true)) 
			{
				return element;
			}

			else
			{
				Assert.fail("Element "+element.toString()+" identified but it is not visible or enabled");
				return null;
			}

		}
		catch (Exception e)
		{
			AppLogger.LOGGER.info("Element "+element.toString()+" is not visiable or enabled, Going to retry");
			return retry(element);
		}
	}
	
	/**
	 * Method to check if the element is displayed and enabled on any exception retry looking for element else fails
	 * @param element
	 * @return 
	 * @return
	 */
	public List<WebElement> isReady(List<WebElement> element)
	{
		try 
		{
			if ((((WebElement) element).isDisplayed() == true) && (((WebElement) element).isEnabled() == true)) 
			{
				return element;
			}

			else
			{
				Assert.fail("Element "+element.toString()+" identified but it is not visible or enabled");
				return null;
			}

		}
		catch (Exception e)
		{
			AppLogger.LOGGER.info("Element "+element.toString()+" is not visiable or enabled, Going to retry");
			return null;
		}
	}
	
	
	/**
	 * Method to retry looking for a element
	 * @param 
	 * @return WebElement
	 * @throws IOException 
	 */
	public WebElement retry(WebElement e) throws IOException 
	{
		AppLogger.LOGGER.info("Retry searching of element " +e.toString());
		String locaterinfo = e.toString();
		System.out.println(locaterinfo);
		String by = locaterinfo.substring(locaterinfo.indexOf("'") + 1, locaterinfo.lastIndexOf("'"));
		System.out.println(by);
		String locater = by.substring(by.indexOf(":") + 1);
		System.out.println(locater);
		String identifier = by.substring(by.indexOf(".") + 1, by.indexOf(":")).trim();
		System.out.println(identifier);
		WebElement e1 = driver.findElement(identifier, locater);
		if (e1 != null)
		{
			return e1;
		}
		return null;
	}
	

	/**
	 * Method to perform click operation
	 * @param element
	 * @throws IOException 
	 */
	public void Click(WebElement element) throws IOException
	{
		System.out.println(element.toString());
		if ((isReady(element)) != null)
		{
			AppLogger.LOGGER.info("Click Operation started on :" + element.toString());
			try
			{
				scrollToElement(element);
				element.click();
				AppLogger.LOGGER.info("Successfully clicked element: "+element.toString());
			}

			catch (Exception e) 
			{
				AppLogger.LOGGER.info("Exception in clicking element: " +element.toString() + "\n" +e.getMessage());
				Assert.fail("Exception in clicking element: " +element.toString());
			}
		}
	}
	
	/**
	 * Method to sendkeys or enter text to text fields 
	 * @param element
	 * @param text
	 * @throws IOException 
	 */
	public void SendKeys(WebElement element, String text) throws IOException 
	{
		System.out.println(element.toString());
		if ((isReady(element)) != null) 
		{
			AppLogger.LOGGER.info("Send Keys Operation started on " + element.toString());
			try 
			{
				scrollToElement(element);
				element.clear();
				element.sendKeys(text);
				AppLogger.LOGGER.info("Successfully entered text to element: "+element.toString());
			}
			catch (Exception e) 
			{
				AppLogger.LOGGER.info("Exception in Send Keys to element: "+element.toString() +"\n" + e.getMessage());
				Assert.fail("Exception in Send Keys to element: "+element.toString());
			}
		}
	}
	
	public  void SendKeysToElementList(List<WebElement> element) throws IOException
	{
		AppLogger.LOGGER.info("Entering text to list of text fields");
		for(WebElement ele: element)
		{
			scrollToElement(ele);
			SendKeys(ele, rvg.getRandomNumberAsString(3));
		}
	}
	
	public void getTextofElementList(List<WebElement> ele) 
	{
		for(WebElement e: ele)
		{
			System.out.println(e.getText());
		}
	}
	
	public void ClcikElementsList(List<WebElement> element) throws IOException
	{
		AppLogger.LOGGER.info("Clicking or Selecting Elements in the List");
		for(WebElement ele: element)
		{
			scrollToElement(ele);
			Click(ele);
		}
	}
	
	/**
	 * Method to get the number of rows present in the table
	 * @param tableName
	 * @return row count
	 * @throws IOException 
	 */
	public int getRowSize(WebElement element) throws IOException
	{
		int rowsCount = 0;
		if ((isReady(element))!=null)
		{
			AppLogger.LOGGER.info("Getting rows count of table "+element.toString());
			try
			{
				List<WebElement> rows_table = element.findElements(By.tagName("tr"));
				rowsCount = rows_table.size();
				AppLogger.LOGGER.info("Rows count in table "+element.toString() + " is :"+rowsCount);
				return rowsCount;
			}
			
			catch (Exception e)
			{
				AppLogger.LOGGER.info("Excpetion in finding table: "+element.toString()+ "\n" +e.getMessage());
				Assert.fail("Excpetion in finding table: " +element.toString());
				return 0;
			}
		}
		else
			return 0;
	}
	
	/**
	 * Method to get reference to all rows in the table
	 * @param element
	 * @return List of rows in the table as web element
	 * @throws IOException 
	 */
	public List<WebElement> getTablerows(WebElement element) throws IOException
	{
		List<WebElement> tableRows = null;
		if ((isReady(element))!=null)
		{
			try
			{
				tableRows = element.findElements(By.tagName("tr"));
				return tableRows;
			}
			catch (Exception e)
			{
				AppLogger.LOGGER.info("Exception in finding table: "+element.toString()+"\n"+e.getMessage());
				Assert.fail("Exception in finding table: "+element.toString());
				return null;
			}
		}
		else 
			return null;
	}
	
	/**
	 * Method to select from drop down which has one or more options group and options within it
	 * @param WebElement
	 */
	public void selectFromOptionGroup(List<WebElement> element) 
	{
		Reporter.log("Selecting a random item from dropdown with optiongroup and options");
		try
		{
			int optionGrpSize = element.size();
			Reporter.log("The number of optiongroup in dropdown are: "+optionGrpSize);
			int num = rvg.getRandomNoInRange(optionGrpSize - 1, 0);
			List<WebElement> options = element.get(num).findElements(By.tagName("option"));
			int optionSize = options.size();
			Reporter.log("The number of options in the randomly selected optiongroup are :" +optionSize);
			int n = rvg.getRandomNoInRange(optionSize - 1, 0);
			scrollToElement(options.get(n));
			Click(options.get(n));
			
		}
		catch (Exception e)
		{
			AppLogger.LOGGER.info("Exception in selecting random option from optionGroup dropdown: \t" +e.getMessage());
			Assert.fail("Exception in selecting random option from optionGroup dropdown");
		}
	}
	
	/***
	 * Method to select desired option from options dropdown using text
	 * @param ele
	 * @param value
	 */
	public void selectFromOptionsbyText (List<WebElement> element, String value) 
	{
		AppLogger.LOGGER.info("Selecting desired option from options dropdown by text");
		try
		{
			for (WebElement ele: element)
			{
				if (ele.getText().equals(value))
				{
					AppLogger.LOGGER.info("Found the option" +ele.getText()+" that you were looking for dropdown and clicking the same");
					Click(ele);
					break;
				}
			}
		}
		catch (Exception e)
		{
			AppLogger.LOGGER.info("Exception in selecting option from option dropdown with: \t" +e.getMessage());
			Assert.fail("Exception in selecting option from option dropdown with");
		}
	}
	
	/**
	 * Method to select from drop down which had options and unable to use Select method
	 * @param WebElement
	 * @throws IOException 
	 */
	public void selectFromOptions(WebElement element) throws IOException 
	{
		AppLogger.LOGGER.info("Selecting a random option from options list dropdown: "+element.toString());
		System.out.println(element.toString());
		if((isReady(element))!=null)
		{
			try
			{
				List<WebElement> options = element.findElements(By.tagName("option"));
				int optionSize = options.size();
				int n = rvg.getRandomNoInRange(optionSize - 1, 0);
				scrollToElement(options.get(n));
				Click(options.get(n));

			}
			catch (Exception e)
			{
				AppLogger.LOGGER.info("Error when selecting from dropdown " +element.toString() + " of options list \n" +e.getMessage());
				Assert.fail("Error when selecting from dropdown " +element.toString());
			}
			
		}
	}
	
	/**
	 * Method to select random option from drop down using index
	 * @param WebElement
	 * @throws IOException 
	 */
	public void selectbyIndex(WebElement ele) throws IOException
	{
		AppLogger.LOGGER.info("Selecting a value from dropdown using random index");
		if ((isReady(ele))!=null)
		{
			try
			{
				scrollToElement(ele);
				Select sel = new Select(ele);
				int opt = ((sel.getOptions().size())-1);
				int rnum = rvg.getRandomNumber(opt);
				Reporter.log("Selecting option with index "+rnum);
				sel.selectByIndex(rnum);
			}
			catch (Exception e)
			{
				AppLogger.LOGGER.info("Error in selecting from dropdown: "+ele.toString() +" using random index \n" +e.getMessage());
				Assert.fail("Error in selecting from dropdown: "+ele.toString());
			}
		}
	}
	
	/**
	 * Method to select from drop down using matching text
	 * @param WebElement
	 * @param Matching Text
	 * @throws IOException 
	 */
	public void selectbyText(WebElement ele, String text) throws IOException 
	{
		AppLogger.LOGGER.info("Selecting from dropdown by desired text");
		System.out.println("Selection from dropdown");
		if ((isReady(ele))!=null)
		{
			System.out.println(ele.toString());
			try
			{
				System.out.println("Initilizinng select");
				scrollToElement(ele);
				Select sel = new Select(ele);
				System.out.println(text);
				sel.selectByVisibleText(text);
			}
			catch (Exception e)
			{
				AppLogger.LOGGER.info("Error in selecting from dropdown "+ele.toString() +" using desired text" +text +"\n" +e.getMessage());
				Assert.fail("Error in selecting from dropdown "+ele.toString());
			}
		}
	}
	
	/**
	 * Method to scroll to specific web-element position
	 * @param elementName
	 * @throws IOException 
	 */
	public void scrollToElement(WebElement element) throws IOException
	{
		AppLogger.LOGGER.info("Scrlling to element "+element.toString());
		  actions = driver.getActions();
		  if ((isReady(element))!=null)
		  {
			  try
			  {
				  AppLogger.LOGGER.info("Successfully scrolled to element: "+element.toString());
				  	actions.moveToElement(element);
				  	actions.perform();
			  }
			  catch (Exception e)
			  {
				  AppLogger.LOGGER.info("Error scrolling to desired element " +element.toString() +"\n"+e.getMessage());
				  Assert.fail("Error scrolling to desired element " +element.toString());
			  }
		  }
		 
	}
	
	/**
	 * Method to randomly select an option from dropdown list
	 * @param ele
	 * @param elementList
	 * @throws IOException 
	 */
	public void selectFromListGrop(WebElement ele, List<WebElement> elementList) throws IOException 
	{
		AppLogger.LOGGER.info("Selecting from dropdown list "+ele.toString());
		if((isReady(ele))!=null)
		{
			try
			{
				Click(ele);
				int elementListSize = elementList.size();
				int randomNumber = rvg.getRandomNoInRange(elementListSize - 1, 0);
				scrollToElement(elementList.get(randomNumber));
				Click(elementList.get(randomNumber));

			}
			catch (Exception e)
			{
				AppLogger.LOGGER.info("Error scrolling to desired element " +ele.toString() +"\n"+e.getMessage());
				  Assert.fail("Error scrolling to desired element " +ele.toString());
		
			}
		
		}
	}
	

	/**
	 * Method to wait until web page is completely loaded. It uses document property state and JQquery state
	 * @return true if the page is completed loaded within set time limit else returns false
	 * @throws InterruptedException
	 */
	/*public boolean waitForPageLoad()
	{
		AppLogger.LOGGER.info("Waiting for page and its elements to be loaded completely");
		js = driver.getJSExecutor();
		try
		{
			for (int i = 0; i < 50; i++) 
			{
				if ((Boolean) js.executeScript("return document.readyState == 'complete' && jQuery.active == 0"))
				{
					flag = true;
					break;
				}
				else 
				{
					Thread.sleep(1500);
				}
			}
		
		}
		catch (Exception e)
		{
			AppLogger.LOGGER.info("Unable to load the page within set time limit"+e.getMessage());
			System.out.println("Unable to load the page within set time limit");
		}
		return flag;
	}*/
	
	
	
	/**
	 * Method to check if the element is displayed
	 * @param WebElement
	 * @return true if the element is displayed else return false
	 */
	/*public boolean isElementPresent(WebElement ele)
	{
	     try
	        {
	        	return flag = ele.isDisplayed();
	           
	        }
	        catch(NoSuchElementException e)
	     	{
	        	AppLogger.LOGGER.info("The element "+ele.toString() +" is not displayed yet...");
	        	return false;
	        }
    }*/
	
	/**
	 * Method to wait for element until its visible and clickable
	 * @param WebElement
	 */
	public void waitforElement(WebElement ele)
	{
		AppLogger.LOGGER.info("Waiting for element "+ele.toString());
		try
		{
			wait = driver.getWebDrivertWait();
			wait.until(ExpectedConditions.visibilityOf(ele));
			wait.until(ExpectedConditions.elementToBeClickable(ele));
		}
		catch (Exception e)
		{
			AppLogger.LOGGER.info("Error in waiting for element "+ele.toString() +"\n" +e.getMessage());
			Assert.fail("Error in waiting for element "+ele.toString());
		}
	}
	
	/**
	 * Method to wait until web element is disappeared n DOM
	 * @param WebElement
	 */
	public void waitUntilElementInvisible(By ele) 
	{
		AppLogger.LOGGER.info("Waiting for the element "+ele.toString()+" do disapear");
		try
		{
			wait = driver.getWebDrivertWait();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(ele));
		}
		catch (Exception e)
		{
			AppLogger.LOGGER.info("Error waiting for element "+ele.toString() +" it still might be present in dom \n"+e.getMessage());
			Assert.fail("Error waiting for element "+ele.toString());
		}
		
	}
	
	public String getText(WebElement ele) throws IOException
	{
		AppLogger.LOGGER.info("Fetching text of the webElement " +ele.toString());
		if((isReady(ele))!=null)
		{
			try
			{
				System.out.println("Fetching Text");
				return ele.getText();
 			}
			catch (Exception e)
			{
				AppLogger.LOGGER.info("Error fetching text of element " +ele.toString() +"\n"+e.getMessage());
				  Assert.fail("Error fetching text of element " +ele.toString());
				  return null;
			}
		}
		else
		{
			Assert.fail("Unable to find element "+ele.toString());
			return null;
		}
	}
	
	public void switchToActiveWindow()
	{
		mywebDriver.switchTo().activeElement();
		System.out.println("Successfully Navigated to active Window");
	}
	
	public void switctoIFrame(WebElement element)
	{
		mywebDriver.switchTo().frame(element);
	}

   //Method to read excel file into hashmap <String,Properties>
/*public static Map readExcelFile(String filepath, String sheet)throws BiffException, IOException {
	//Hashmap with record object as value
    Map<String, LocatorsProperty> mapNew = new HashMap();
    try {
    	String FilePath = filepath;
    	FileInputStream fs = new FileInputStream(FilePath);
    	Workbook wb = Workbook.getWorkbook(fs);
    	// TO get the access to the sheet
    	Sheet sh = wb.getSheet(sheet);
    	// To get the number of rows present in sheet
    	int totalNoOfRows = sh.getRows();
    	// To get the number of columns present in sheet
    	int totalNoOfCols = sh.getColumns();
    	// String u=new String();
    	for (int row = 1; row < totalNoOfRows; row++) {
    		LocatorsProperty rd = new LocatorsProperty();
    		for(int col = 0; col < totalNoOfCols ; col++){
    			if (col == 1) {
					rd.setValue(sh.getCell(col, row).getContents());    
					} 
    			if (col == 2){
    				rd.setValue(sh.getCell(col, row).getContents());    
    			}
    			if (col == 3){
    				rd.setElementValue(sh.getCell(col, row).getContents());
    			}
    			mapNew.put(sh.getCell(col, row).getContents(), rd);
    			}
    		}
    	fs.close();
    	} catch (Exception e) {
    		System.out.println("Error finding keyword in credentials.xls file");
    		e.printStackTrace();
    		}
    return mapNew;
    }*/

public void getscreenshot(String scrnShotName, String errorMessage) throws Exception 
{

	try{
		 File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
         //The below method will save the screen shot in d drive with name "screenshot.png"
		 String scrnShotName1= new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
         FileUtils.copyFile(scrFile, new File("./screenshot/"+scrnShotName+"-"+scrnShotName1+".png"));
      
         Assert.fail(errorMessage);
	}catch(Exception e){
		e.printStackTrace();
		Assert.fail(errorMessage);       
	}

}

public static String getScreenShotName(String scrnShotName){
	String screenshotName = null;
	String scrnShotName1= new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
	screenshotName = scrnShotName+"-"+scrnShotName1 ;
	return screenshotName;

}




/*public static int getTotalRows(String path, String sheetName) throws IOException, JXLException{
	
	String FilePath = path;
	FileInputStream fs = new FileInputStream(FilePath);
	XSSFWorkbook wb = new XSSFWorkbook(fs);
	// TO get the access to the sheet
	XSSFSheet sh = wb.getSheet(sheetName);
	// To get the number of rows present in sheet
	int rowNums = sh.getLastRowNum()+1;
	System.out.println("the end tot row value:"+rowNums);

	wb.close();
	fs.close();
	
	return rowNums;	
}

public static int getColRows(String path, String sheetName) throws IOException, JXLException{
	
	String FilePath = path;
	FileInputStream fs = new FileInputStream(FilePath);
	XSSFWorkbook wb = new XSSFWorkbook(fs);
	// TO get the access to the sheet
	XSSFSheet sh = wb.getSheet(sheetName);
	// To get the number of rows present in sheet
	int colNum = sh.getRow(0).getLastCellNum();
	//System.out.println("the end tot row value:"+totalNoOfRows);
	// To get the number of columns present in sheet
	System.out.println("the end tot col value:"+colNum);
	
	wb.close();
	fs.close();
	
	return colNum;	
}*/
}



	
	
