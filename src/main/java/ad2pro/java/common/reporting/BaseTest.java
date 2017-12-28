package ad2pro.java.common.reporting;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import ad2pro.fw.utils.Config;
import ad2pro.fw.utils.Driver;
import ad2pro.fw.utils.PropertiesFileParser;

public class BaseTest
{
	WebDriver suitedriver;
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String basePath = System.getProperty("user.dir");
	public static String browserType;
	public static String appURL="https://jdmanage-qc.2adpro.com/view/index#";
	Driver driver;
	PropertiesFileParser prop = new PropertiesFileParser();
	
@BeforeSuite
	
	public void setup(ITestContext context)
	{
		Reporter.log("Executing testcase on OS" + OS);
		Config con = new Config();
		
		try 
		{
			con.Browser = prop.getProperty("browser");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println(con.Browser);
		System.out.println(basePath);
		try 
		{
			con.DriverLocation = basePath+prop.getProperty("winChromeDriver");
		} 
		catch (IOException e1) 
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
			driver.openURL(prop.getProperty("qaURL"));
		} 
		catch (IOException e) 
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
