package ad2pro.java.common.reporting;


import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;



		public class GlobalVars {
				  
			  public static WebDriver driver;	  
			  
			
			  public static HashMap<String,List<String>> testCasesResutls=new HashMap<String,List<String>>();
			  
			  public static String clientLogo="http://creative.2adpro.com/Staging/2adpro/2adprologo/2adpro.png";
			  
			  public static String testHTMLResultPath="";
			  public static String TestResultsPath="";
			  public static String strScreenshotPath="";
			  public static String strLogFilePath="";
			  
			  //For Reporting Purpose --- From Application
			  public static String strApplicationName="2adpro";
			  public static String strTestType="Regression";
			  public static String strEnv="jdtraffic";
			  public static String strUrl="https://jdtraffic-new.2adpro.com/index/login";
			  public static String strBuild="0.0";
			  public static String strVersion="0.0";
			  
			  
/*******************************************************************************************************************************************************************
																End of GlobalVars.
********************************************************************************************************************************************************************/
		}


