package ad2pro.fw.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import jxl.JXLException;
import jxl.Sheet;
import jxl.Workbook;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * This class consist of all the common operations which are performed in the web application
 */
public class CommonFunctions {
	
	private Driver driver;
	static WebDriver mywebDriver;
	SeleniumHelper shelp;
	WebDriverWait wait;
	Actions actions;
	static int targetDay = 31;
	static int targetMonth = 12;
	static int currenttDate = 0;
	static int currenttMonth = 0;
	int currenttYear = 0;
	static int jumMonthBy = 0;
	static boolean increment = true;
	static boolean flag = false;
	JavascriptExecutor js;
	private static String basePath = System.getProperty("user.dir");
	RandomNumberGeneration rn;
	LocatorsProperty rd = new LocatorsProperty();
	PropertiesFileParser prop = new PropertiesFileParser();
	
	
	public CommonFunctions(Driver driver)//Constructor
	{
		this.driver = driver;
	}
	
	//Helps to Open the URL
	public void openURL(String url){
		driver.openURL(url);
	}

	//Helps to closed the driver
	public void close(){
		driver.closeDriver();
	}
	
	//Helps to fetch excel data informations based on the file path and sheet name(Note:- Only XLS format applicable)
		static int rowNum;
		public String[][] fetchDataFromExcel(String path, String sheetName) throws IOException{
			
			File excel = new File(path);
			FileInputStream fs = new FileInputStream(excel);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet ws = wb.getSheet(sheetName);
			
			rowNum = ws.getLastRowNum()+1;
			int colNum = ws.getRow(0).getLastCellNum();
			
			String data[][] = new String[rowNum][colNum];
			
			for(int i=0;i<rowNum;i++){
				HSSFRow row = ws.getRow(i);
				for(int j=0;j<colNum;j++){
					
					HSSFCell cell = row.getCell(j);
					String value = cellToString(cell);
				
					data[i][j] = value;
				}
				//System.out.println("value is:"+data[i][0] + data[i][1] + data[i][2] + data [i][8]);
			}
			wb.close();
			fs.close();
			
			return data;	
		}
	
		private static String cellToString(HSSFCell cell) {
			int type;
			Object result;
			String strReturn = null;
			if(cell == null){
				strReturn = "";
			}
			else{
				switch(cell.getCellType()){
				case Cell.CELL_TYPE_NUMERIC:
					result = cell.getNumericCellValue();
					strReturn = result.toString();
					break;
				case Cell.CELL_TYPE_STRING:
					result = cell.getStringCellValue();
					strReturn = result.toString();
					break;
				default:
					strReturn = null;
				}
			}
			return strReturn;
		}
	
		
	//Helps to make your script wait until all the elements in page are get loaded
	public int waitForPageLoad() throws IOException
	{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("Waiting for page and its elements to be loaded completely");
		}
		js = driver.getJSExecutor();
		try
		{
			for (int i = 0; i < 50; i++) 
			{
				if ((Boolean) js.executeScript("return document.readyState == 'complete' && jQuery.active == 0"))
				{
					//flag = true;
					rd.setStatus(1);
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
			rd.setStatus(3);
			AppLogger.LOGGER.info("Unable to load the page within set time limit"+e.getMessage());
			System.out.println("Unable to load the page within set time limit");
		}
		return rd.getStatus();
	}
	
	//Helps to perform click operations in all sort of button
	public int click_element(String strLocType, String strLocValue) throws IOException {
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("Click Operation started on :" + strLocValue.toString());
		}
		WebElement element = null;
		try {
			switch (strLocType) {
			case "id":
				element = driver.findElement(strLocType, strLocValue);
				break;
			case "xpath":
				element = driver.findElement(strLocType, strLocValue);
				break;
			case "name":
				element = driver.findElement(strLocType, strLocValue);
				break;
			case "className":
				element = driver.findElement(strLocType, strLocValue);
				break;
			case "linkText":
				element = driver.findElement(strLocType, strLocValue);
				break;
			case "css":
				element = driver.findElement(strLocType, strLocValue);
				break;
			}
			if (element != null) {
				element.click();
				System.out.println("clicked elemnt name:"+element);
				rd.setStatus(1);
			}
			
		} catch (Exception e) {
			rd.setStatus(3);
			AppLogger.LOGGER.info("Exception in clicking element: " + strLocValue.toString() + "\n" + e.getMessage());
			//Assert.fail("Exception in clicking element: " + strLocValue.toString());
		}
		return rd.getStatus();
	}
	
	//Helps to enter the text in the texfield
	public int SendKeys(String strLocType, String strLocValue, String input) throws IOException {
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("Send Keys Operation started on " + strLocValue.toString());
		}
		WebElement element = null;
		try {
			switch (strLocType) {
			case "id":
				element = driver.findElement(strLocType, strLocValue);
				break;
			case "xpath":
				element = driver.findElement(strLocType, strLocValue);
				break;

			case "name":
				element = driver.findElement(strLocType, strLocValue);
				break;

			case "className":
				element = driver.findElement(strLocType, strLocValue);
				break;

			case "linkText":
				element = driver.findElement(strLocType, strLocValue);
				break;
			case "css":
				element = driver.findElement(strLocType, strLocValue);
				break;
			}
			if (element != null) {
				System.out.println("it came to True condition:");
				element.sendKeys(input.trim());
				System.out.println("The typed content:"+input.trim());
				//sucess = true;
				rd.setStatus(1);
			} 
		} catch (Exception e) {
			rd.setStatus(3);
			AppLogger.LOGGER.info("Exception in Send Keys to element: " + strLocValue.toString() + "\n" + e.getMessage());
		}
		//System.out.println("the returnup = "+lp.getFailStatus()+":"+lp.getautomationStatus()+":"+lp.getPassStatus());
		
		return rd.getStatus();
	}
		
	//Helps to clear the text field information
		public int clear_element(String strLocType, String strLocValue) throws IOException {
			String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
			if(data[10][2].equalsIgnoreCase("Two")){
				AppLogger.LOGGER.info("Clear Operation started on :" + strLocValue.toString());
			}
			WebElement element = null;
			try {
				switch (strLocType) {
				case "id":
					element = driver.findElement(strLocType, strLocValue);
					break;
				case "xpath":
					element = driver.findElement(strLocType, strLocValue);
					break;

				case "name":
					element = driver.findElement(strLocType, strLocValue);
					break;

				case "className":
					element = driver.findElement(strLocType, strLocValue);
					break;

				case "linkText":
					element = driver.findElement(strLocType, strLocValue);
					break;
				case "css":
					element = driver.findElement(strLocType, strLocValue);
					break;
				}
				if (element != null) {
					element.clear();
					rd.setStatus(1);
					System.out.println("it came to True condition:");
				} 
			} catch (Exception e) {
				rd.setStatus(3);
				AppLogger.LOGGER.info("Exception in clearing element: " + strLocValue.toString() + "\n" + e.getMessage());
				Assert.fail("Exception in clearing element: " + strLocValue.toString());
			}
			return rd.getStatus();
		}

	
	//Helps to make your script wait until particular element in page are get visible	
		public int waitforElement(String strLocType,String strLocValue) throws IOException{
			String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
			if(data[10][2].equalsIgnoreCase("Two")){
				AppLogger.LOGGER.info("Waiting for element "+ strLocValue.toString());
			}
			WebElement element = null;
			try {
				switch (strLocType) {
				case "id":
					element = driver.findElement(strLocType, strLocValue);
					break;
				case "xpath":
					element = driver.findElement(strLocType, strLocValue);
					break;

				case "name":
					element = driver.findElement(strLocType, strLocValue);
					break;

				case "className":
					element = driver.findElement(strLocType, strLocValue);
					break;

				case "linkText":
					element = driver.findElement(strLocType, strLocValue);
					break;
				case "css":
					element = driver.findElement(strLocType, strLocValue);
					break;
				}
				if (element != null) {
					wait = driver.getWebDrivertWait();
					wait.until(ExpectedConditions.visibilityOf(element));
					wait.until(ExpectedConditions.elementToBeClickable(element));
					rd.setStatus(1);
				}	
			} catch (Exception e) {
				rd.setStatus(3);
				AppLogger.LOGGER.info("Error in waiting for element :"+strLocValue.toString() +"\n" +e.getMessage());
				System.out.println("Error in waiting for element :"+strLocValue.toString());
			}
			return rd.getStatus();	
		}

	
	//Helps to make your script wait for default 10 sec
	public void sleep() throws Exception{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("thread starts ");
		}
		Thread.sleep(10000);
	}

	//Helps to make your script wait for default 2 sec
	public void medSleep() throws Exception{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("thread starts ");
		}
		Thread.sleep(2000);
	}
	
	//Helps to make your script wait for 12 Mins which can used in FTP upload
	public void longSleep() throws InterruptedException, IOException{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("Long thread starts ");
		}
		TimeUnit.MINUTES.sleep(5);
	}
	
	//Helps to make your script wait for 2 Mins
	public void mediumSleep() throws InterruptedException, IOException{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("Long thread starts ");
		}
		TimeUnit.MINUTES.sleep(2);
	}
	
	//Helps to validate the page title of the browser based on your given I/P
	public int pageTitle(String input) throws IOException{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("Started validating page title");
		}
		try {
			String ActualPageTitle = driver.getTitle();
			System.out.println("actal title:"+ActualPageTitle);
			if(ActualPageTitle.equalsIgnoreCase(input)){
				rd.setStatus(1);
			}
			else{
				rd.setStatus(2);
			}
		} catch (Exception e) {
			rd.setStatus(3);
			AppLogger.LOGGER.info("Expected page title does not match :"+input.toString() +"\n" +e.getMessage());
			//Assert.fail("Expected page title does not match :"+input.toString());
		}
		return rd.getStatus();
	}
	
	//Helps to perform click operation using action method, which can used for all sort button clicks
	public int clickByAction(String strLocType,String strLocValue) throws IOException{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("clickByAction starts "+ strLocValue.toString());
		}
		WebElement element = null;
		try {
			switch (strLocType) {
			case "id":
				element = driver.findElement(strLocType, strLocValue);
				break;
			case "xpath":
				element = driver.findElement(strLocType, strLocValue);
				break;

			case "name":
				element = driver.findElement(strLocType, strLocValue);
				break;

			case "className":
				element = driver.findElement(strLocType, strLocValue);
				break;

			case "linkText":
				element = driver.findElement(strLocType, strLocValue);
				break;
			case "css":
				element = driver.findElement(strLocType, strLocValue);
				break;
			}
			if (element != null) {
				driver.getActions().moveToElement(element).click().perform();
				rd.setStatus(1);
			}
		} 
		catch (Exception e) {
			rd.setStatus(3);
			AppLogger.LOGGER.info("Error in clickByAction for element :"+strLocValue.toString() +"\n" +e.getMessage());
		}
		return rd.getStatus();
	}
	
	//Helps to validate the text present in web application based on your I/P
	public int validateText(String strLocType,String strLocValue,String input) throws IOException{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("validateOrderNumber starts "+ strLocValue.toString());
		}
		String element = null;
		try {
			switch (strLocType) {
			case "id":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;
			case "xpath":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;

			case "name":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;

			case "className":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;

			case "linkText":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;
			case "css":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;
			}
			if (element != null) {
				rd.setStatus(1);
				if(element.trim().equalsIgnoreCase(input.trim())){
					System.out.println("Displayed content from interface:"+element);
					System.out.println("Displayed content mathches with your input:"+input);
					if(data[10][2].equalsIgnoreCase("Two")){
						AppLogger.LOGGER.info("Displayed content mathches with your input:"+input);
					}
					rd.setStatus(1);
				}
				else{
					System.out.println("Displayed content from interface:"+element);
					System.out.println("Displayed content does not mathches with your input:"+input);
					if(data[10][2].equalsIgnoreCase("Two")){
						AppLogger.LOGGER.info("Displayed content does not mathches with your input:"+input);
					}
					rd.setStatus(2);
				}
			}			
		} 
		catch (Exception e) {
			rd.setStatus(3);
			AppLogger.LOGGER.info("Error in validateText for element "+strLocValue.toString() +"\n" +e.getMessage());
		}
		return rd.getStatus();
	}
	
	//Helps to validate the partial text present in web application based on your I/P
	public int validatePartialText(String strLocType,String strLocValue,String input) throws IOException{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("validateOrderNumber starts "+ strLocValue.toString());
		}
		String element = null;
		try {
			switch (strLocType) {
			case "id":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;
			case "xpath":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;

			case "name":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;

			case "className":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;

			case "linkText":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;
			case "css":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;
			}
			if (element != null) {
				rd.setStatus(1);
				if(element.trim().contains(input.trim())){
					System.out.println("Displayed content from interface:"+element);
					System.out.println("Displayed content partial mathches with your input:"+input);
					if(data[10][2].equalsIgnoreCase("Two")){
						AppLogger.LOGGER.info("Displayed content partial mathches with your input:"+input);
					}
					rd.setStatus(1);
				}
				else{
					System.out.println("Displayed content from interface:"+element);
					System.out.println("Displayed content does not partial mathches with your input:"+input);
					if(data[10][2].equalsIgnoreCase("Two")){
						AppLogger.LOGGER.info("Displayed content does not partial mathches with your input:"+input);
					}
					rd.setStatus(2);
				}
			}	
		} 
		catch (Exception e) {
			rd.setStatus(3);
			AppLogger.LOGGER.info("Error in validateText for element "+strLocValue.toString() +"\n" +e.getMessage());
		}
		return rd.getStatus();
	}
	
	//Helps to validate the tag presence in web application based on your tag I/P
	public int validateTag(String strLocType,String strLocValue) throws IOException{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("validateOrderNumber starts "+ strLocValue.toString());
		}
		boolean element = false;
		try {
			switch (strLocType) {
			case "id":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;
			case "xpath":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;

			case "name":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;

			case "className":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;

			case "linkText":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;
			case "css":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;
			}
			if (element) {
					System.out.println("Displayed content mathches with your tag input:"+strLocValue);
					if(data[10][2].equalsIgnoreCase("Two")){
						AppLogger.LOGGER.info("Displayed content mathches with your tag input:"+strLocValue);
					}
					rd.setStatus(1);
			}
			else{
				System.out.println("Displayed content does not mathches with your tag input:"+strLocValue);
				if(data[10][2].equalsIgnoreCase("Two")){
					AppLogger.LOGGER.info("Displayed content does not mathches with your tag input:"+strLocValue);
				}
				rd.setStatus(2);
			}
			
		} 
		catch (Exception e) {
			rd.setStatus(3);
			AppLogger.LOGGER.info("Error in validateTag for element "+strLocValue.toString() +"\n" +e.getMessage());
		}
		return rd.getStatus();
	}
	
	//Helps to validate the tag is not present in web application based on your tag I/P
	public int checkTagPrecence(String strLocType,String strLocValue) throws IOException{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("checktag presence starts "+ strLocValue.toString());
		}
		boolean element = false;
		try {
			switch (strLocType) {
			case "id":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;
			case "xpath":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;

			case "name":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;

			case "className":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;

			case "linkText":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;
			case "css":
				element = driver.findElement(strLocType, strLocValue).isDisplayed();
				break;
			}
			if (element) {
					System.out.println("Displayed content mathches with your tag input which should not:"+strLocValue);
					if(data[10][2].equalsIgnoreCase("Two")){
						AppLogger.LOGGER.info("Displayed content mathches with your tag input which should not::"+strLocValue);
					}
					rd.setStatus(2);
			}
			else{
				System.out.println("Displayed content does not present which it makes pass:"+strLocValue);
				if(data[10][2].equalsIgnoreCase("Two")){
					AppLogger.LOGGER.info("Displayed content does not present which it makes pass:"+strLocValue);
				}
				rd.setStatus(1);
			}
			
		} 
		catch (Exception e) {
			rd.setStatus(1);
			AppLogger.LOGGER.info("Error in validateTag for element "+strLocValue.toString() +"\n" +e.getMessage());
		}
		return rd.getStatus();
	}
	
	//Helps to get text form the webelement
	public int getText(String strLocType,String strLocValue) throws IOException{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("getText starts "+ strLocValue.toString());
		}
		String element = null;
		try {
			switch (strLocType) {
			case "id":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;
			case "xpath":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;

			case "name":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;

			case "className":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;

			case "linkText":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;
			case "css":
				element = driver.findElement(strLocType, strLocValue).getText();
				break;
			}
			if (element != null) {
				System.out.println("The text value is:"+element);
				rd.setStatus(1);
			}
			
		} 
		catch (Exception e) {
			rd.setStatus(3);
			AppLogger.LOGGER.info("Error in validateText for element "+strLocValue.toString() +"\n" +e.getMessage());
		}
		return rd.getStatus();
	}
	
	
	//Helps to select the options in the dropdown based on your given I/P
	public int selectFromListGroupByText(String strLocType,String strLocValue,String input) throws IOException 
	{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("selectFromListGroupByText started : "+strLocValue.toString());
		}
		List<WebElement> element = null;
		try {
			switch (strLocType) {
			case "id":
				element = driver.findElements(strLocType, strLocValue);
				break;
			case "xpath":
				element = driver.findElements(strLocType, strLocValue);
				break;

			case "name":
				element = driver.findElements(strLocType, strLocValue);
				break;

			case "className":
				element = driver.findElements(strLocType, strLocValue);
				break;

			case "linkText":
				element = driver.findElements(strLocType, strLocValue);
				break;
			case "css":
				element = driver.findElements(strLocType, strLocValue);
				break;
			}
			if (element != null) {
				rd.setStatus(1);
				for (WebElement ele: element)
				{
					if (ele.getText().equals(input))
					{
						if(data[10][2].equalsIgnoreCase("Two")){
							AppLogger.LOGGER.info("Found the option:" +ele.getText()+" that you were looking for dropdown and clicking the same");
						}
						ele.click();
						rd.setStatus(1);
						break;
					}
					else{
						if(data[10][2].equalsIgnoreCase("Two")){
							AppLogger.LOGGER.info("Error in selectFromListGroupByText :" +strLocValue.toString());
						}
						rd.setStatus(3);
						break;
					}
				}
			}	
		}
			catch (Exception e)
			{
				rd.setStatus(3);
				AppLogger.LOGGER.info("Error in selectFromListGroupByText :" +strLocValue.toString() +"\n"+e.getMessage());
				  //Assert.fail("Error in selectFromListGroupByText :" +strLocValue.toString());
			}
		return rd.getStatus();
	}
	
	//Helps to get number of rows in a webtable
	public List<WebElement> getTablerows(String strLocType,String strLocValue) throws IOException
	{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("getTablerows started : "+strLocValue.toString());
		}
		List<WebElement> element = null;
			try
			{
				switch (strLocType) {
				case "id":
					element = driver.findElements(strLocType, strLocValue);
					break;
				case "xpath":
					element = driver.findElements(strLocType, strLocValue);
					break;

				case "name":
					element = driver.findElements(strLocType, strLocValue);
					break;

				case "className":
					element = driver.findElements(strLocType, strLocValue);
					break;

				case "linkText":
					element = driver.findElements(strLocType, strLocValue);
					break;
				case "css":
					element = driver.findElements(strLocType, strLocValue);
					break;
			}
				if (element != null) {
						element = driver.findElements("tagName","tr");
						AppLogger.LOGGER.info("Displayed content mathches with your input:");
						return element;
					}
				else{
					AppLogger.LOGGER.info("Exception in finding table: "+element.toString());
					Assert.fail("Exception in finding table: "+element.toString());
					return null;
				}
			}
			catch (Exception e)
			{
				AppLogger.LOGGER.info("Exception in finding table: "+element.toString()+"\n"+e.getMessage());
				Assert.fail("Exception in finding table: "+element.toString());
				return null;
			}
	}
	
	//Helps to scroll to the particular element in the web page
	public int scrollToElement(String strLocType,String strLocValue) throws IOException
	{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("scrollToElement starts "+ strLocValue.toString());
		}
		WebElement element = null;
		try {
			switch (strLocType) {
			case "id":
				element = driver.findElement(strLocType, strLocValue);
				break;
			case "xpath":
				element = driver.findElement(strLocType, strLocValue);
				break;

			case "name":
				element = driver.findElement(strLocType, strLocValue);
				break;

			case "className":
				element = driver.findElement(strLocType, strLocValue);
				break;

			case "linkText":
				element = driver.findElement(strLocType, strLocValue);
				break;
			case "css":
				element = driver.findElement(strLocType, strLocValue);
				break;
			}
			if (element != null) {
				driver.getActions().moveToElement(element).perform();
				//actions.moveToElement(element);
			  	//actions.perform();
				//new Actions(mywebDriver).moveToElement(element).perform();
				rd.setStatus(1);
				}
		} 
		catch (Exception e) {
			rd.setStatus(3);
			 AppLogger.LOGGER.info("Error scrolling to desired element " +element.toString() +"\n"+e.getMessage());
		}
		return rd.getStatus();
	}
	
	//Helps to Verify the web page URL with your given input
	public int verifyPageURL(String expPageURL) throws IOException{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("verifyPageURL starts ");
		}
		  try {
		   String actPageURL = driver.CurrentURL();
		     System.out.println("actal title:"+actPageURL);
		   if(actPageURL.equalsIgnoreCase(expPageURL)){
			   rd.setStatus(1);
		   }
		   else{
			   rd.setStatus(2);
		   }
		  } catch (Exception e) {
			  rd.setStatus(3);
		   AppLogger.LOGGER.info("Expected page title does not match :"+expPageURL.toString() +"\n" +e.getMessage());
		   //Assert.fail("Expected page title does not match :"+input.toString());
		  }
		  return rd.getStatus();
		 }

	
	public static void getCurrentDayMonth() {

	    Calendar cal = Calendar.getInstance();
	    currenttDate = cal.get(Calendar.DAY_OF_MONTH);
	    currenttMonth = cal.get(Calendar.MONTH) + 1;
	    System.out.println("the cur date:"+currenttDate);
	    System.out.println("the cur month:"+currenttMonth);
	    
	}

	public static void getTargetDayMonthYear(String dateString) {
		System.out.println("the dateString:"+dateString);
	    int firstIndex = dateString.indexOf("/");
	    System.out.println("the firstIndex:"+firstIndex);
	    

	    String day = dateString.substring(0, firstIndex);
	    targetDay = Integer.parseInt(day);
	    System.out.println("the targetDay:"+targetDay);
	    

	    String month = dateString.substring(firstIndex + 1);
	    targetMonth = Integer.parseInt(month);
	    System.out.println("the targetMonth:"+targetMonth);
	}

	public static void calculateToHowManyMonthToJump() {
	
	    if ((targetMonth - currenttMonth) > 0) {
	        jumMonthBy = targetMonth - currenttMonth;

	    } else {
	        jumMonthBy = currenttMonth - targetMonth;
	        increment = false;
	    }
	}

	//Helps to select your date in all new UI datepicker
	public void dataFun(String dateToSet) throws InterruptedException, IOException {
	    // TODO Auto-generated method stub
	    getCurrentDayMonth();
	    getTargetDayMonthYear(dateToSet);
	    calculateToHowManyMonthToJump();
	    
	    for (int i = 0; i < jumMonthBy; i++) {
	        if (increment) {
	        	click_element("xpath","//*[@id='ui-datepicker-div']/div/a[2]/span");
	        } else {
	        	click_element("xpath","//*[@id='ui-datepicker-div']/div/a[1]/span");
	        }
	        Thread.sleep(1000);
	    }
	    driver.findElement("linkText",Integer.toString(targetDay)).click();
	}
	
	//Helps to select your date in all old UI datepicker
	public void oldDataFun(String dateToSet,String value) throws InterruptedException, IOException {
	    // TODO Auto-generated method stub
	    
		getCurrentDayMonth();
	    getTargetDayMonthYear(dateToSet);
	    calculateToHowManyMonthToJump();
	    
	    for (int i = 0; i < jumMonthBy; i++) {
	        if (increment) {
	        	click_element("xpath",".//*[@id='dp-popup']/div[2]/a[2]");
	        } else {
	        	click_element("xpath",".//*[@id='dp-popup']/div[2]/a[2]");
	        }
	        Thread.sleep(1000);
	    }
	    driver.findElement("xpath",value).click();
	}
	
	//Helps to move the downloaded file form default file download location to our project downloads file location
	public int changeFilePath(String actualFilePath,String destinationPath) throws IOException{
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("changeFilePath starts ");
		}
		try{
	    	   File afile =new File(actualFilePath);

	    	   if(afile.renameTo(new File(destinationPath + afile.getName()))){
	    		System.out.println("File is moved successful!");
	    		if(data[10][2].equalsIgnoreCase("Two")){
	    			AppLogger.LOGGER.info("File is moved successful!");
	    		}
	    		rd.setStatus(1);
	    	   }else{
	    		System.out.println("File is failed to move!");
	    		if(data[10][2].equalsIgnoreCase("Two")){
	    			AppLogger.LOGGER.info("File is failed to move!");
	    		}
	    		rd.setStatus(3);
	    	   }

	    	}catch(Exception e){
	    		rd.setStatus(3);
	    		AppLogger.LOGGER.info("File is failed to move!,Error");
	    		e.printStackTrace();
	    	}
		return rd.getStatus();
	}
	
	//Helps to convert excel date into normal date format:-M/d/yyyy
	public static String dateConversion(String dateValue){
		String dates = dateValue;
		double itemDouble = Double.parseDouble(dates);
		Date javaDate= DateUtil.getJavaDate((double) itemDouble);
        String dateVal = new SimpleDateFormat("M/d/yyyy").format(javaDate);
        System.out.println("the Converted date format:"+dateVal);
		return dateVal;
	}
	
	//Helps to convert excel date into simple date format:-M/d/yy
	public String simpleDateConversion(String dateValue){
		String dates = dateValue;
		double itemDouble = Double.parseDouble(dates);
		Date javaDate= DateUtil.getJavaDate((double) itemDouble);
        String dateVal = new SimpleDateFormat("M/d/yy").format(javaDate);
        System.out.println("the Converted date format:"+dateVal);
		return dateVal;
	}
	
	//Helps to convert excel time into simple time format:-hh:mm a
	public String simpleTimeConversion(String dateValue){
		String dates = dateValue;
		double itemDouble = Double.parseDouble(dates);
		Date javaDate= DateUtil.getJavaDate((double) itemDouble);
        String dateVal = new SimpleDateFormat("hh:mm a").format(javaDate);
        System.out.println("the Converted date format:"+dateVal);
		return dateVal;
	}
	
	//Helps to convert excel datetime into simple datetime format:-M-d-yyyy HH:mm
	public static String dateTimeConversion(String dateValue){
		String dates = dateValue;
		double itemDouble = Double.parseDouble(dates);
		Date javaDate= DateUtil.getJavaDate((double) itemDouble);
        String dateVal = new SimpleDateFormat("M-d-yyyy HH:mm").format(javaDate);
        System.out.println("the Converted date format:"+dateVal);
		return dateVal;
	}
	
	//Helps to convert excel datetimesec into simple datetimesec format:-yyyy-MM-dd HH:mm:ss
	public static String dateTimeSecConversion(String dateValue){
		String dates = dateValue;
		double itemDouble = Double.parseDouble(dates);
		Date javaDate= DateUtil.getJavaDate((double) itemDouble);
        String dateVal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(javaDate);
        System.out.println("the Converted date format:"+dateVal);
		return dateVal;
	}
	
	//Helps to create new tab in your browser
	public void createNewTab() throws InterruptedException, AWTException{
		Robot r = new Robot();                          
		r.keyPress(KeyEvent.VK_CONTROL); 
		r.keyPress(KeyEvent.VK_T); 
		r.keyRelease(KeyEvent.VK_CONTROL); 
		r.keyRelease(KeyEvent.VK_T);

		Thread.sleep(5000);
		/*String handle=driver.getWindowHandle();
	     
	    for(String winHandle : driver.getWindowHandles()){ // new window HANDLE
	      System.out.println(winHandle);
	      mywebDriver.switchTo().window(winHandle);
	      System.out.println(driver.getTitle());
	      mywebDriver.switchTo().window(winHandle);
	    Thread.sleep(1000); 
	    System.out.println(driver.getTitle());
	    }*/
	    
		//mywebDriver.get(Url);
		
	}
	
	//Helps to switch tab
	public void switchTab() throws AWTException, InterruptedException{
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_TAB);
		r.keyRelease(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_TAB);
		Thread.sleep(5000);
		//mywebDriver.navigate().refresh();
	}
	
	public void newWindowHandle() throws InterruptedException{
		//String handle=driver.getWindowHandle();
	     
	    for(String winHandle : driver.getWindowHandles()){ // new window HANDLE
	      System.out.println(winHandle);
	      mywebDriver.switchTo().window(winHandle);
	    Thread.sleep(1000); 
	    System.out.println(driver.getTitle());
	    }	
	}
	
	public void oldWindowHandle(String input,String input1) throws InterruptedException{
		String handle =null;
		if(input.equalsIgnoreCase("yes")){
			handle=driver.getWindowHandle();
			System.out.println(driver.getTitle());
			Thread.sleep(1000);
		}
		if(input.equalsIgnoreCase("no")){
			for(String winHandle : driver.getWindowHandles()){ // new window HANDLE
			      System.out.println(winHandle);
			      mywebDriver.switchTo().window(winHandle);
			    Thread.sleep(1000); 
			    System.out.println(driver.getTitle());
			    }
		}
			
		if(input1.equalsIgnoreCase("yes")){
			mywebDriver.switchTo().window(handle);
		}
	}
	public void Swap() throws AWTException, InterruptedException{
		System.out.println("It came to swap");
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_CONTROL); 
		r.keyPress(KeyEvent.VK_T); 
		r.keyRelease(KeyEvent.VK_CONTROL); 
		r.keyRelease(KeyEvent.VK_T);
		Thread.sleep(5000);
		
	}
	
	//Helps to get the instance of the current old browser window
	public void clickKeyTab() throws AWTException{
		System.out.println("It came to keystab");
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_TAB);
		r.keyRelease(KeyEvent.VK_TAB);
		System.out.println("It released keystab");
	}
	
	//Helps to get the instance of the new browser window
	 static ArrayList<String> tabs;
	 public void WindowHandle() throws InterruptedException, AWTException{
	  //String handle=driver.getWindowHandle();
	  tabs = new ArrayList<String> (driver.getWindowHandles());
	  System.out.println("The tabs:"+tabs+":"+driver.getTitle());
	  //Switch to new window
	  
	  //driver.switchTo().window(tabs.get(1));
	  driver.switchTo(tabs.get(1));
	  System.out.println("The tabget1:"+driver.getTitle());
	  //driver.close();//do some action in new window(2nd tab)
	   
	  
	 }
	
	//Helps to get the instance of the old browser window
	 public void WindowHandleOld() throws InterruptedException, AWTException{
	  //Switch to main/parent window
	  driver.getTitle();
	  driver.switchTo(tabs.get(0));
	  //mywebDriver.switchTo().window(tabs.get(0));
	  System.out.println("The tabget0:"+driver.getTitle());//do some action in main window(1st tab)
	 }
	
	
/*public static String[][] fetchDataFromExcel(String path, String sheetName) throws IOException{
	File excel;
	FileInputStream fs = null;
	HSSFWorkbook wb = null;
	HSSFSheet ws = null;
	String data[][] = new String[2][2]
		try{
			excel = new File(path);
			fs = new FileInputStream(excel);
			wb = new HSSFWorkbook(fs);
			ws = wb.getSheet(sheetName);
		}catch (IOException e)
		{
			System.out.println("Unable to locate specified path");
		}

		rowNum = ws.getLastRowNum()+1;
		int colNum = ws.getRow(0).getLastCellNum();
		
		String data[][] = new String[rowNum][colNum];
		
		for(int i=0;i<rowNum;i++){
			HSSFRow row = ws.getRow(i);
			for(int j=0;j<colNum;j++){
				
				HSSFCell cell = row.getCell(j);
				String value = cellToString(cell);
			
				data[i][j] = value;
			}
			//System.out.println("value is:"+data[i][0] + data[i][1] + data[i][2] + data [i][8]);
		}
		wb.close();
		fs.close();
		
		return data;	
	}*/
	
	
	public static int fetchRowCountFromExcel(){
		return rowNum;
	}
	
	/*static int rowNums;
	public static String[][] fetchDataFromExcels(String path, String sheetName) throws IOException{
		
		File excel = new File(path);
		FileInputStream fs = new FileInputStream(excel);
		XSSFWorkbook workbook = new XSSFWorkbook(fs);
		XSSFSheet ws = workbook.getSheet(sheetName);
		
		rowNums = ws.getLastRowNum()+1;
		int colNum = ws.getRow(0).getLastCellNum();
		
		String data[][] = new String[rowNums][colNum];
		
		for(int i=0;i<rowNums;i++){
			XSSFRow row = ws.getRow(i);
			for(int j=0;j<colNum;j++){
				
				XSSFCell cell = row.getCell(j);
				String value = cellToStrings(cell);
			
				data[i][j] = value;
			}
			//System.out.println("value is:"+data[i][0] + data[i][1] + data[i][2] + data [i][8]);
		}
		workbook.close();
		fs.close();
		
		return data;	
	}
	
	
	public static int fetchRowCountFromExcels(){
		return rowNums;
	}*/

	
	/*private static String cellToStrings(XSSFCell cell) {
		int type;
		Object result;
		String strReturn = null;
		if(cell == null){
			strReturn = "";
		}
		else{
			switch(cell.getCellType()){
			case Cell.CELL_TYPE_NUMERIC:
				result = cell.getNumericCellValue();
				strReturn = result.toString();
				break;
			case Cell.CELL_TYPE_STRING:
				result = cell.getStringCellValue();
				strReturn = result.toString();
				break;
			default:
				strReturn = null;
			}
		}
		return strReturn;
	}*/
	
	//Helps to get total no of rows in excel(XLS format)
	public int getTotalRow(String path, String sheetName) throws IOException, JXLException{
		
		String FilePath = path;
		FileInputStream fs = new FileInputStream(FilePath);
		Workbook wb = Workbook.getWorkbook(fs);
		// TO get the access to the sheet
		Sheet sh = wb.getSheet(sheetName);
		// To get the number of rows present in sheet
		int totalNoOfRows = sh.getRows();
		//System.out.println("the end tot row value:"+totalNoOfRows);
		// To get the number of columns present in sheet
		int totalNoOfCols = sh.getColumns();
		//System.out.println("the end tot col value:"+totalNoOfCols);
		
		wb.close();
		fs.close();
		
		return totalNoOfRows;	
	}

	//Helps to get total no of cols in excel(XLS format)
	public static int getColRow(String path, String sheetName) throws IOException, JXLException{
		
		String FilePath = path;
		FileInputStream fs = new FileInputStream(FilePath);
		Workbook wb = Workbook.getWorkbook(fs);
		// TO get the access to the sheet
		Sheet sh = wb.getSheet(sheetName);
		// To get the number of rows present in sheet
		int totalNoOfRows = sh.getRows();
		//System.out.println("the end tot row value:"+totalNoOfRows);
		// To get the number of columns present in sheet
		int totalNoOfCols = sh.getColumns();
		//System.out.println("the end tot col value:"+totalNoOfCols);
		
		wb.close();
		fs.close();
		
		return totalNoOfCols;	
	}
	
	//Helps to handle windows download pop-up alerts based on the browser
	public int fileDownload() throws AWTException, IOException 
	{	
		String[][] data = fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("File download starts");
		}
		if(data[1][2].equalsIgnoreCase("firefox"))
		{
			try
			{
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_DOWN);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				System.out.println("File downloaded in firefox sucessfully");
				rd.setStatus(1);
 			}
			catch (Exception e)
			{
				System.out.println("Error in file download in firefox");
				AppLogger.LOGGER.info("Error in file download in firefox");
				//Assert.fail("Error in file download");
				rd.setStatus(3);
			}
		}
		else if(data[1][2].equalsIgnoreCase("chrome")){
			System.out.println("File downloaded in chrome sucessfully");
			rd.setStatus(1);
		}
		else if(data[1][2].equalsIgnoreCase("ie"))
		{
			try
			{
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				rd.setStatus(1);
 			}
			catch (Exception e)
			{
				AppLogger.LOGGER.info("Error in file download in ie");
				//Assert.fail("Error in file download");
				rd.setStatus(3);
			}
		}
		return rd.getStatus();
	}
	
	//Helps to upload creative/files which are needed for the respective appplicatios
	public void selectCreativesFiles(String files) throws AWTException, InterruptedException
	{
		   StringSelection stringSelection = new StringSelection(files);
		   Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		   Robot robot = new Robot();
		   Thread.sleep(5000);
		   robot.keyPress(KeyEvent.VK_CONTROL);
		   robot.keyPress(KeyEvent.VK_V);
		   robot.keyRelease(KeyEvent.VK_V);
		   robot.keyRelease(KeyEvent.VK_CONTROL);
		   robot.keyPress(KeyEvent.VK_ENTER);
		  // Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		   robot.keyRelease(KeyEvent.VK_ENTER);
		}
	
	
	public void uploadFile(String filePath) {
		try
		{
			wait = driver.getWebDrivertWait();
			WebElement upload = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='control-group jsonform-error- form-fields']//input[@type='file']")));
			upload.sendKeys(filePath);

			Thread.sleep(1000);
			AppLogger.LOGGER.info("Successfully Uploaded File");
		
		}
		catch (Exception e)
		{
			Assert.fail("Error While Uploading file: "+e.getMessage());
		}
	
		
}
	
	//Helps to write on the excel based on the row, col, filepath, sheetname and input(Only XLS format)
public int updateExcelSheet(int row, int col,String filepath, String sheet, String status) throws IOException{
	try {
		FileInputStream fsIP= new FileInputStream(new File(filepath)); //Read the spreadsheet that needs to be updated
        System.out.println("Inside updateExcelSheet");
                
        HSSFWorkbook wb = new HSSFWorkbook(fsIP); //Access the workbook
                  
        HSSFSheet worksheet = wb.getSheet(sheet); //Access the worksheet, so that we can update / modify it.
                  
        Cell cell = null; // declare a Cell object
        Row row1=worksheet.getRow(row);
        //cell = worksheet.getRow(row).getCell(col);   // Access the second cell in second row to update the value
        cell=row1.getCell(col);
        System.out.println(status);
        if (cell==null){
        	cell = row1.createCell(col);
            cell.setCellType(cell.CELL_TYPE_STRING);          
            cell.setCellValue(status);  // Get current cell value value and overwrite the value
            }
        else{
        	cell.setCellValue(status);  // Get current cell value value and overwrite the value
        	}
        fsIP.close(); //Close the InputStream
        
        FileOutputStream output_file =new FileOutputStream(new File(filepath));  //Open FileOutputStream to write updates          
        wb.write(output_file); //write changes 
        output_file.close();  //close the stream
        rd.setStatus(1);
        } catch (Exception e) {
        	// TODO: handle exception
        	e.printStackTrace();
        	rd.setStatus(3);
        	}
	return rd.getStatus();
	}

public static void updateTime(int row, int col,String filepath, String sheet) throws IOException{
	try {
		FileInputStream fsIP= new FileInputStream(new File(filepath)); //Read the spreadsheet that needs to be updated
        System.out.println("Inside updateExcelSheet");
                
        HSSFWorkbook wb = new HSSFWorkbook(fsIP); //Access the workbook
                  
        HSSFSheet worksheet = wb.getSheet(sheet); //Access the worksheet, so that we can update / modify it.
                  
        Cell cell = null; // declare a Cell object
        Row row1=worksheet.getRow(row);
        //cell = worksheet.getRow(row).getCell(col);   // Access the second cell in second row to update the value
        cell=row1.getCell(col);
        if (cell==null){
        	cell = row1.createCell(col);
            cell.setCellType(cell.CELL_TYPE_NUMERIC);          
            CellStyle cellStyle = wb.createCellStyle();
            CreationHelper createHelper = wb.getCreationHelper();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
            cell.setCellValue(new Date());
            cell.setCellStyle(cellStyle);
            }
        else{
        	 cell.setCellType(cell.CELL_TYPE_NUMERIC);          
             CellStyle cellStyle = wb.createCellStyle();
             CreationHelper createHelper = wb.getCreationHelper();
             cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
             cell.setCellValue(new Date());
             cell.setCellStyle(cellStyle);  // Get current cell value value and overwrite the value
        	}
        fsIP.close(); //Close the InputStream
        
        FileOutputStream output_file =new FileOutputStream(new File(filepath));  //Open FileOutputStream to write updates          
        wb.write(output_file); //write changes 
        output_file.close();  //close the stream
        } catch (Exception e) {
        	// TODO: handle exception
        	e.printStackTrace();
        	}
	}


static int row;
static boolean flag1 = false;
public static int getStartRow(String path, String sheetName) throws IOException, JXLException{
	
	String FilePath = path;
	FileInputStream fs = new FileInputStream(FilePath);
	Workbook wb = Workbook.getWorkbook(fs);
	// TO get the access to the sheet
	Sheet sh = wb.getSheet(sheetName);
	// To get the number of rows present in sheet
	int totalNoOfRows = sh.getRows();
	//System.out.println("the start tot row value:"+totalNoOfRows);
	// To get the number of columns present in sheet
	int totalNoOfCols = sh.getColumns();
	System.out.println("the start tot col value:"+totalNoOfCols);
	
	for(row=0;row<totalNoOfRows;row++){
		
		for(int col=0;col<totalNoOfCols;col++){
			String s = sh.getCell(col,row).getContents();
			//System.out.println("the loop value is:"+s);
			if(s.equalsIgnoreCase("start")){
				//System.out.println("the inside start row value is:"+row);
				flag1=true;
				break;
			}
		}
		//System.out.println("start Row value is:"+row);
		if(flag1==true){
			break;
		}
	}
	wb.close();
	fs.close();
	
	return row;	
}


static int i;
static boolean flag2 = false;
public static int getEndRow(String path, String sheetName) throws IOException, JXLException{
	
	String FilePath = path;
	FileInputStream fs = new FileInputStream(FilePath);
	Workbook wb = Workbook.getWorkbook(fs);
	// TO get the access to the sheet
	Sheet sh = wb.getSheet(sheetName);
	// To get the number of rows present in sheet
	int totalNoOfRows = sh.getRows();
	//System.out.println("the end tot row value:"+totalNoOfRows);
	// To get the number of columns present in sheet
	int totalNoOfCols = sh.getColumns();
	//System.out.println("the end tot col value:"+totalNoOfCols);
	
	for(i=0;i<totalNoOfRows;i++){
		
		for(int col=0;col<totalNoOfCols;col++){
			String s = sh.getCell(col,i).getContents();
			if(s.equalsIgnoreCase("end")){
				System.out.println("the inside end row value is:"+i);
				flag2=true;
				break;
			}
		}
		System.out.println("end Row value is:"+i);
		if(flag2==true){
			break;
		}
	}
	wb.close();
	fs.close();
	
	return i;	
}

//Helps to find the start row of scenario/testdata
public static int getMatchStartRow(String tsName, String path, String sheetName) throws IOException, JXLException{
	int rows;
	boolean flag3 = false;
	String FilePath = path;
	FileInputStream fs = new FileInputStream(FilePath);
	Workbook wb = Workbook.getWorkbook(fs);
	// TO get the access to the sheet
	Sheet sh = wb.getSheet(sheetName);
	// To get the number of rows present in sheet
	int totalNoOfRows = sh.getRows();
	//System.out.println("The start tot row value:"+totalNoOfRows);
	// To get the number of columns present in sheet
	int totalNoOfCols = sh.getColumns();
	//System.out.println("The start tot col value:"+totalNoOfCols);
	for(rows=0;rows<totalNoOfRows;rows++){
		
		for(int col=0;col<totalNoOfCols;col++){
			String s = sh.getCell(col,rows).getContents();
			//System.out.println("the loop value is:"+s);
			if(s.equalsIgnoreCase(tsName+"_start")){
				//System.out.println("The inside start row value is:"+rows);
				flag3=true;
				break;
			}
		}
		//System.out.println("start Row value is:"+rows);
		if(flag3==true){
			break;
		}
	}
	return rows;	
}

//Helps to find the end row of scenario/testdata
public static int getMatchEndRow(String tsName, String path, String sheetName) throws IOException, JXLException{
	int rowNum;
	boolean flag4 = false;
	String FilePath = path;
	FileInputStream fs = new FileInputStream(FilePath);
	Workbook wb = Workbook.getWorkbook(fs);
	// TO get the access to the sheet
	Sheet sh = wb.getSheet(sheetName);
	// To get the number of rows present in sheet
	int totalNoOfRows = sh.getRows();
	//System.out.println("The end tot row value:"+totalNoOfRows);
	//To get the number of columns present in sheet
	int totalNoOfCols = sh.getColumns();
	//System.out.println("The end tot col value:"+totalNoOfCols);
	for(rowNum=0;rowNum<totalNoOfRows;rowNum++){
		
		for(int col=0;col<totalNoOfCols;col++){
			String s = sh.getCell(col,rowNum).getContents();
			//System.out.println("the loop value is:"+s);
			if(s.equalsIgnoreCase(tsName+"_end")){
				//System.out.println("The inside End row value is:"+rowNum);
				flag4=true;
				break;
			}
		}
		//System.out.println("End Row value is:"+rowNum);
		if(flag4==true){
			break;
		}
		
	}
	wb.close();
	fs.close();
	return rowNum;	
}

}
