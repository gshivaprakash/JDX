package ad2pro.fw.utils;

import java.awt.AWTException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import ad2pro.java.common.reporting.CoreUtils;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class remains as base to initiate the webdriver instance
 */
public class Driver
{
	
	private static WebDriver driver;
	Config config;
	WebDriverWait wait;
	JavascriptExecutor js;
	RandomNumberGeneration rvg = new RandomNumberGeneration();
	boolean flag = false;
	private static String basePath = System.getProperty("user.dir"); // helps you to get the base location of your files used in the FW
	static PropertiesFileParser prop = new PropertiesFileParser(); // Object creation to access values in the config file
	static CommonFunctions cf = new CommonFunctions((Driver) driver);

	public Driver(Config config) 
	{
		this.config = config;
		driver = getDriver();
	}
	
	public void switchTo(String nameOrHandle){
		  driver.switchTo().window(nameOrHandle);
		 }
	
	public WebDriverWait getWebDrivertWait()
	{
		return  wait = new WebDriverWait(driver, config.ExplicitWait);
	}
	
	public Timeouts getImplicitWait()
	{
		return driver.manage().timeouts().implicitlyWait(config.ImplicitWait, TimeUnit.SECONDS);
	}
	
	public JavascriptExecutor getJSExecutor()
	{
		return js = (JavascriptExecutor) driver;
	}
	
	
	
	/**
	 * Method to return Driver
	 * @return driver object for the selected browser
	 */
	public WebDriver getDriver() 
	{
		if (driver == null)
		{
			switch (config.Browser.toLowerCase()) 
			{
				case "firefox":
					driver = new FirefoxDriver();
					AppLogger.LOGGER.info(config.Browser + "Browser Opened");
					break;
	
				case "chrome":
					System.setProperty("webdriver.chrome.driver", config.DriverLocation);
					driver = new ChromeDriver();
					AppLogger.LOGGER.info(config.Browser + "Browser Opened");
					break;
	
				case "ie":
					System.setProperty("webdriver.ie.driver", config.DriverLocation);
					driver = new InternetExplorerDriver();
					AppLogger.LOGGER.info(config.Browser + "Browser Opened");
	
					break;
			}
		}
		return driver;
	}
	
	/**
	 * Method to close the active/focused driver instance
	 */
	public void closeDriver() 
	{
		if (driver != null)
		{
			driver.close();
			AppLogger.LOGGER.info("driver Closed");
		}
	}
	
	/**
	 * Method to close all driver instances and safely end session
	 */
	public void quitDriver()
	{
		if (driver != null)
		{
			driver.quit();
			AppLogger.LOGGER.info("All drivers Closed");
		}
	}

	public void openURL(String url)
	{
		driver.navigate().to(url);
		AppLogger.LOGGER.info("Page Opened for the url " + url);
	}

	public void maximize() 
	{
		driver.manage().window().maximize();
	}
	
	public void manage(){
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
	}
	
	public String getPageSource()
	{
		return driver.getPageSource();
	}

	public String getTitle()
	{
		return driver.getTitle();
	}

	public String getWindowHandle()
	{
		return driver.getWindowHandle();
	}
	
	 public Set<String> getWindowHandles() {
		  // TODO Auto-generated method stub
		  return driver.getWindowHandles();
		 }
	
	Actions action;
	public Actions getActions()
	{
		return action = new Actions(driver);
	}
	
	public void swithToAlert()
	{
		driver.switchTo().alert();
	}
	
	//Helps to accept alerts which pop-ups in the web application
	public void acceptAlert()
	{
		driver.switchTo().alert().accept();
	}
	
	public String getAlertText()
	{
		return driver.switchTo().alert().getText();
	}
	
	public void switchToActiveElement()
	{
		driver.switchTo().activeElement();
	}
	
	//Helps to referesh the current web page
	public void pageRefersh()
	{
		driver.navigate().refresh();
	}
	
	public String CurrentURL()
	 {
	  return driver.getCurrentUrl();
	 }
	
	//Helps to identify webelements based on your input in the testdata file
	public List<WebElement> findElements(String locType, String value) throws IOException
	{
		By locator = null;
		List<WebElement> element = null;
		int retrycount = 0;
		String[][] data = cf.fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("Find Elements Stareted on:"+value);
		}
		try 
		{
			switch (locType.toLowerCase())
			{
				case "id":
					locator = By.id(value);
					element = driver.findElements(locator);
					break;
	
				case "xpath":
					locator = By.xpath(value);
					element = driver.findElements(locator);
					break;
	
				case "name":
					locator = By.name(value);
					element = driver.findElements(locator);
					break;
				
				case "className":
					locator = By.className(value);
					element = driver.findElements(locator);
					break;
	
				case "linktext":
					locator = By.linkText(value);
					element = driver.findElements(locator);
					break;
	
				case "partiallinktext":
					locator = By.partialLinkText(value);
					element = driver.findElements(locator);
					break;
	
				case "tagName":
					locator = By.tagName(value);
					element = driver.findElements(locator);
					break;
					
				case "css":
					locator = By.cssSelector(value);
					element = driver.findElements(locator);
					break;
			// write for other locator methods
			}

		}
		catch (Exception e)
		{
			while (retrycount != config.RetryCount) {
				AppLogger.LOGGER.info("Exception in Find Element with " + locType.toString());
				retrycount = retrycount + 1;
				try
				{
					switch (locType.toLowerCase())
					{
						case "id":
							locator = By.id(value);
							element = driver.findElements(locator);
							break;
			
						case "xpath":
							locator = By.xpath(value);
							element = driver.findElements(locator);
							break;
			
						case "name":
							locator = By.name(value);
							element = driver.findElements(locator);
							break;
						
						case "className":
							locator = By.className(value);
							element = driver.findElements(locator);
							break;
			
						case "linktext":
							locator = By.linkText(value);
							element = driver.findElements(locator);
							break;
			
						case "partiallinktext":
							locator = By.partialLinkText(value);
							element = driver.findElements(locator);
							break;
			
						case "tagName":
							locator = By.tagName(value);
							element = driver.findElements(locator);
							break;
							
						case "css":
							locator = By.cssSelector(value);
							element = driver.findElements(locator);
							break;
					// write for other locator methods

					}
					element = (new WebDriverWait(driver, config.ExplicitWait))
								.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
					if (element != null)
					{
						return element;
					}
				}
				catch (org.openqa.selenium.TimeoutException ez)
				{
					continue;
				}
			}
		}

		if (element != null) 
		{
			return element;
		}
		AppLogger.LOGGER.info("Element not found :" + locType.toString()+":-" + value.toString());
	//	Assert.fail("Element not found"+ locType.toString()+":-" + value.toString());
		return null;
	}

	
	/**
	 * 
	 * @param locType
	 * @param value
	 * @return
	 * @throws IOException 
	 */
	//Helps to identify webelement based on your input in the testdata file
	public WebElement findElement(String locType, String value) throws IOException
	{
		By locator = null;
		WebElement element = null;
		int retrycount = 0;
		String[][] data = cf.fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("Find Element Stareted on- " + value.toString());
		}
		try 
		{
			switch (locType.toLowerCase())
			{
				case "id":
					locator = By.id(value);
					element = driver.findElement(locator);
					break;
	
				case "xpath":
					locator = By.xpath(value);
					element = driver.findElement(locator);
					break;
	
				case "name":
					locator = By.name(value);
					element = driver.findElement(locator);
					break;
				
				case "className":
					locator = By.className(value);
					element = driver.findElement(locator);
					break;
	
				case "linktext":
					locator = By.linkText(value);
					element = driver.findElement(locator);
					break;
	
				case "partiallinktext":
					locator = By.partialLinkText(value);
					element = driver.findElement(locator);
					break;
	
				case "tagName":
					locator = By.tagName(value);
					element = driver.findElement(locator);
					break;
					
				case "css":
					locator = By.cssSelector(value);
					element = driver.findElement(locator);
					break;
			// write for other locator methods

			}

		}
		catch (Exception e)
		{
			while (retrycount != config.RetryCount) {
				AppLogger.LOGGER.info("Exception in Find Element with " + locType.toString()+":-" + value.toString());
				retrycount = retrycount + 1;
				try
				{
					switch (locType.toLowerCase())
					{
						case "id":
							locator = By.id(value);
							element = driver.findElement(locator);
							break;
			
						case "xpath":
							locator = By.xpath(value);
							element = driver.findElement(locator);
							break;
			
						case "name":
							locator = By.name(value);
							element = driver.findElement(locator);
							break;
						
						case "className":
							locator = By.className(value);
							element = driver.findElement(locator);
							break;
			
						case "linktext":
							locator = By.linkText(value);
							element = driver.findElement(locator);
							break;
			
						case "partiallinktext":
							locator = By.partialLinkText(value);
							element = driver.findElement(locator);
							break;
			
						case "tagName":
							locator = By.tagName(value);
							element = driver.findElement(locator);
							break;
							
						case "css":
							locator = By.cssSelector(value);
							element = driver.findElement(locator);
							break;
					// write for other locator methods

					}
					element = (new WebDriverWait(driver, config.ExplicitWait))
								.until(ExpectedConditions.presenceOfElementLocated(locator));
					if (element != null)
					{
						return element;
					}
				}
				catch (org.openqa.selenium.TimeoutException ez)
				{
					continue;
				}
			}
		}

		if (element != null) 
		{
			return element;
		}
		AppLogger.LOGGER.info("Element not found :" + locType.toString()+":-" + value.toString());
		//Assert.fail("Element not found"+ locType.toString()+":-" + value.toString());
		System.out.println("it came to Null condition:");
		return null;
	}
	
	//Helps to take screenshot when a method fail to perform its operations
	public static String takeScreenShot(String application) throws IOException {
		String[][] data = cf.fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
		if(data[10][2].equalsIgnoreCase("Two")){
			AppLogger.LOGGER.info("takeScreenShot Stareted on- " + application.toString());
		}
		String strScreenshotName=null;
		try
		{
			RandomNumberGeneration ran=new RandomNumberGeneration();
			String today=ran.getToDayDate();
			String scrShotFolder=ran.getDateAndTime();
			
			
			String name = ".\\ScreenShots\\" +application;
			CoreUtils.createDir(name);
			
			/*name = name + "\\" + Functionality;
			CoreUtils.createDir(name);
			
			name = name + "\\" + TestScenario;
			CoreUtils.createDir(name);
			
			name = name + "\\" + scrShotFolder;
			CoreUtils.createDir(name);*/

			strScreenshotName = name + "\\" +application+"_"+ scrShotFolder + ".png";
			File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			
			File sreenshotFile= new File(strScreenshotName);
			
			FileUtils.copyFile(f,sreenshotFile);
				
			strScreenshotName=sreenshotFile.getCanonicalPath();
			System.out.println("Screenshot Path ::"+strScreenshotName);
		}
		catch(Exception e)
		{
			System.out.println("Error Occured while taking the Screenshot... "+e.getMessage());
			Assert.fail("Error Occured while taking the Screenshot... "+e.getMessage());
		}
		//strScreenshotName = ". Screen Shot : " + strScreenshotName;
		
		return strScreenshotName;
	}


}
