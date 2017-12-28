package ad2pro.fcn.tests;

import java.io.IOException;
import java.net.InetAddress;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import ad2pro.fw.utils.AppLogger;
import ad2pro.fw.utils.CommonFunctions;
import ad2pro.fw.utils.Config;
import ad2pro.fw.utils.Driver;
import ad2pro.fw.utils.PropertiesFileParser;
import jxl.read.biff.BiffException;

public class BaseTest
{
	WebDriver suitedriver;
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String osType= System.getProperty("os.arch");
	private static String osVersion= System.getProperty("os.version");
	private static String sysEnv = System.getenv("PROCESSOR_IDENTIFIER");
	private static InetAddress ip;
	private static String basePath = System.getProperty("user.dir");
	Driver driver;
	PropertiesFileParser prop = new PropertiesFileParser();
	CommonFunctions cf = new CommonFunctions(driver);
	
	/**
	 * This method helps you to setup pre-Testexecution Configuration
	 * Like :- Type of Browser instance, Driver location setup, Browser maximize and time 
	 */
	
@BeforeSuite
	public void setup(ITestContext context) throws IOException, BiffException
	{
	String[][] data = cf.fetchDataFromExcel(basePath+prop.getProperty("runConfig"), prop.getProperty("sheetName"));
	
	if(data[10][2].equalsIgnoreCase("Two")){
		ip = InetAddress.getLocalHost();
		AppLogger.LOGGER.info("Executing testcase on OS:-" + OS);
		AppLogger.LOGGER.info("Current host name :-" + ip.getHostName());
		AppLogger.LOGGER.info("Current IP address :-" + ip.getHostAddress());
		AppLogger.LOGGER.info("Operating system type :-" + osType);
		AppLogger.LOGGER.info("Operating system version :-" + osVersion);
		AppLogger.LOGGER.info("Operating system Info :-" + sysEnv);
	}
	Config con = new Config();
		try 
		{
			con.Browser = data[1][2];
			if(con.Browser.equalsIgnoreCase("chrome")){
				con.Browser = "chrome";
			}
			else if(con.Browser.equalsIgnoreCase("firefox")){
				con.Browser = "firefox";
			}
			else if(con.Browser.equalsIgnoreCase("ie")){
				con.Browser = "ie";
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		System.out.println(con.Browser);
		System.out.println(basePath);
		try 
		{
			if(data[2][2].equalsIgnoreCase("chromeOn")){
				con.DriverLocation = basePath+data[3][1];
			}
			else if(data[2][2].equalsIgnoreCase("ieOn")){
				con.DriverLocation = basePath+data[4][1];
			}
			
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		System.out.println(con.DriverLocation);
		con.RetryCount = 2;
		con.ExplicitWait = 90;
		con.ImplicitWait = 15;
		driver = new Driver(con);
		driver.getDriver();
		driver.getImplicitWait();
		try 
		{
			driver.openURL(data[5][1]);	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		suitedriver = driver.getDriver();
		driver.maximize();
		context.setAttribute("DriverObject", driver);
	}
	
	@AfterSuite
	
	public void cleanup()
	{
		System.out.println("Quit Driver");
		//suitedriver.quit();
	}

}
