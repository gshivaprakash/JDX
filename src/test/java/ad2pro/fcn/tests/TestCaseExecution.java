package ad2pro.fcn.tests;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import ad2pro.applications.function.JdtrafficFunctions;
import ad2pro.applications.function.StoreFunctions;
import ad2pro.applications.function.VaultFunctions;
import ad2pro.fw.utils.AppLogger;
import ad2pro.fw.utils.CommonFunctions;
import ad2pro.fw.utils.Driver;
import ad2pro.fw.utils.LocatorsProperty;
import ad2pro.fw.utils.PropertiesFileParser;
import ad2pro.fw.utils.SeleniumHelper;


/**
 * This class helps you to execute all your operation in any web application
 */

@Listeners(ad2pro.java.common.reporting.DemoListener.class)
public class TestCaseExecution {
	private static String basePath = System.getProperty("user.dir"); // helps you to get the base location of your files used in the FW
	PropertiesFileParser prop = new PropertiesFileParser(); // Object creation to access values in the config file
	Driver driver;
	boolean result;
	int results;

	/**
	 * This method act as an engine to perform all your operations
	 */
	@Test()
	public void jdTraffic(ITestContext test) {
		System.out.println("Starting test");
		try {

			driver = (Driver) test.getAttribute("DriverObject");

			// Object Creation for necessary classes
			SeleniumHelper shelp = new SeleniumHelper(driver);
			CommonFunctions cf = new CommonFunctions(driver);
			JdtrafficFunctions jdt = new JdtrafficFunctions(driver);
			StoreFunctions store = new StoreFunctions(driver);
			VaultFunctions vf = new VaultFunctions(driver);

			System.out.println("The project path:" + basePath);
			
			//Fetching the Run configuration excel file data information
			String[][] configurationData = cf.fetchDataFromExcel(basePath + prop.getProperty("runConfig"),prop.getProperty("sheetName"));

			//Fetching the Functionality sheet, excel data information respective to the application selected in RUN CONFIG file
			String[][] functionalityData = cf.fetchDataFromExcel(basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[7][2]));
			int totFunctionalityCount = cf.getTotalRow(basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[7][2]));

			//Fetching the Scenario sheet, excel data information respective to the application selected in RUN CONFIG file
			String[][] scenarioData = cf.fetchDataFromExcel(basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[8][2]));
			int totscenarioCount = cf.getTotalRow(basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[8][2]));

			//Fetching the TestData sheet, excel data information respective to the application selected in RUN CONFIG file
			String[][] testData = cf.fetchDataFromExcel(basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[9][2]));

			System.out.println("The total number of functionality" + ":-" + totFunctionalityCount);
			System.out.println("The total number of scenario" + ":-" + totscenarioCount);

			// Looping based on our excel row count
			for (int c = 0; c < totFunctionalityCount; c++) {
				if (functionalityData[c][2].equalsIgnoreCase("yes")) {
					String tcName = functionalityData[c][0];
					System.out.println("The testcase name is::" + tcName);
					int startTc = cf.getMatchStartRow(tcName, basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[8][2]));
					// System.out.println("The testcase value"+startTc);
					int endTc = cf.getMatchEndRow(tcName, basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[8][2]));
					// System.out.println("The testcase end value"+endTc);
					for (int d = startTc; d <= endTc; d++) {
						if (scenarioData[d][4].equalsIgnoreCase("yes")) {
							String tsName = scenarioData[d][1];
							System.out.println("The testscenario name is::" + tsName);
							int a = cf.getMatchStartRow(tsName, basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[9][2]));
							// System.out.println("The testcase value"+a);
							int b = cf.getMatchEndRow(tsName, basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[9][2]));
							for (int i = a; i <= b; i++) {
								switch (testData[i][2]) {
								case "openURL"://Helps to Open the URL 				
									cf.openURL(testData[i][6]);
									break;
								case "verifyPageURL"://Helps to Verify the web page URL with your given input
									results = cf.verifyPageURL(testData[i][6]);
									System.out.println("the result page" + result);
									break;
								case "clear_element"://Helps to clear the text field information
									results = cf.clear_element(testData[i][4], testData[i][5]);
									System.out.println("the result page click:" + result);
									break;
								case "SenKeys"://Helps to enter the text in the texfield 
									results = cf.SendKeys(testData[i][4], testData[i][5], testData[i][6]);
									break;
								case "scrollToElement"://Helps to scroll to the particular element in the web page
									results = cf.scrollToElement(testData[i][4], testData[i][5]);
									break;
								case "Click_element"://Helps to perform click operations in all sort of button 
									results = cf.click_element(testData[i][4], testData[i][5]);
									System.out.println("the result page click:" + result);
									break;
								case "waitForPageLoad"://Helps to make your script wait until all the elements in page are get loaded
									results = cf.waitForPageLoad();
									break;
								case "waitforElement"://Helps to make your script wait until particular element in page are get visible
									results = cf.waitforElement(testData[i][4], testData[i][5]);
									break;
								case "end"://when u have only step of test data, you can this functions next to it, which helps to stop your excution
									break;
								case "FTPUpload"://(JDtrafic):-helps to upload file via FTP
									results = jdt.FTPUpload(testData[i][7], testData[i][8]);
									break;
								case "urnDropdownValidation"://(JDtrafic):-helps to validate there is only one tracking no when search in search bar in JDT
									results = jdt.urnDropdownValidation(testData[i][4], testData[i][5]);
									break;
								case "wait"://Helps to make your script wait for default 10 sec
									cf.sleep();
									break;
								case "medWait"://Helps to make your script wait for default 2 sec
									cf.medSleep();
									break;
								case "longWait"://Helps to make your script wait for 12 Mins which can used in FTP upload
									cf.longSleep();
									break;
								case "mediumWait"://Helps to make your script wait for 2 Mins
									cf.mediumSleep();
									break;
								case "dateFun"://Helps to select your date in all new UI datepicker 
									cf.dataFun(testData[i][6]);
									break;
								case "oldDateFun"://Helps to select your date in all old UI datepicker
									cf.oldDataFun(testData[i][6], testData[i][5]);
									break;
								case "pageTitleValidation"://Helps to validate the page title of the browser based on your given I/P
									results = cf.pageTitle(testData[i][6]);
									System.out.println("the result page" + result);
									break;
								case "clickByAction"://Helps to perform click operation using action method, which can used for all sort button clicks
									results = cf.clickByAction(testData[i][4], testData[i][5]);
									break;
								case "validateText"://Helps to validate the text present in web application based on your I/P
									results = cf.validateText(testData[i][4], testData[i][5], testData[i][6]);
									break;
								case "validatePartialText"://Helps to validate the partial text present in web application based on your I/P
									results = cf.validatePartialText(testData[i][4], testData[i][5], testData[i][6]);
									break;
								case "validateTag"://Helps to validate the tag presence in web application based on your tag I/P
									results = cf.validateTag(testData[i][4], testData[i][5]);
									break;
								case "checkTagPrecence"://Helps to validate the tag is not present in web application based on your tag I/P
									results = cf.checkTagPrecence(testData[i][4], testData[i][5]);
									break;
								case "AcceptAlert"://Helps to accept alerts which pop-ups in the web application
									driver.acceptAlert();
									break;
								case "refersh"://Helps to referesh the current web page 
									driver.pageRefersh();
									break;
								case "selectFromListGroupByText"://Helps to select the options in the dropdown based on your given I/P
									results = cf.selectFromListGroupByText(testData[i][4], testData[i][5],testData[i][6]);
									break;
								case "fileDownload"://Helps to handle windows download pop-up alerts based on the browser
									results = cf.fileDownload();
									break;
								case "changeFilePath"://Helps to move the downloaded file form default file download location to our project downloads file location
									results = cf.changeFilePath(testData[i][7], testData[i][8]);
									break;
								case "adReportValidation"://(JDTtaffic):Helps to validate the downloaded addetail report info with JDTraffic old interface
									results = jdt.adReportValidation();
									break;
								case "dfpAdDetailReportValidation"://(JDTtaffic):Helps to validate the downloaded addetail report info with DFP inetrace info
									results = jdt.dfpAdDetailReportValidation();
									break;
								case "productionReportValidation"://(JDTtaffic):Helps to validate the downloaded porduction report info with JDT inetrace info
									results = jdt.productionReportValidation(testData[i][5]);
									break;
								case "holdReportValidation"://(JDTtaffic):Helps to validate the downloaded hold report info with JDT inetrace info
									results = jdt.holdReportValidation(testData[i][5]);
									break;
								case "rushReportValidation"://(JDTtaffic):Helps to validate the downloaded rush report info with JDT inetrace info
									results = jdt.rushReportValidation(testData[i][5]);
									break;
								case "creativeReportValidation"://(JDTtaffic):Helps to validate the downloaded creative report info with JDT inetrace info
									results = jdt.creativeReportValidation();
									break;
								case "adTagValidation"://(JDTtaffic):Helps to download adtag report from JDT still validation needed to be added
									result = jdt.adTagValidation();
									break;
								case "getQueManagerInfoValidation"://(JDTtaffic):Helps to validate QUE manager info with JDTRAffic info, still scripts in progress 
									result = jdt.getQueManagerInfoValidation();
									break;
								case "createNewTab"://Helps to create new tab in your browser
									cf.createNewTab();
									break;
								case "switchTab"://Helps to switch tab
									cf.switchTab();
									break;
								case "WindowHandle"://Helps to get the instance of the new browser window
									cf.WindowHandle();
									break;
								case "WindowHandleOld"://Helps to get the instance of the old browser window
									cf.WindowHandleOld();
									break;
								case "clickKeyTab"://Helps to get the instance of the current old browser window
									cf.clickKeyTab();
									break;
								case "dashBoardValidation"://(JDTtaffic):Helps to perform dashboard operation
									jdt.dashBoardValidation(testData[i][3], testData[i][4], testData[i][5],testData[i][6]);
									break;
								case "dashboardCountValidaion"://(JDTtaffic):Helps to validate total counts for 2adproprview/all region
									results = jdt.dashboardCountValidaion(testData[i][4]);
									break;
								case "selectCreativesFiles"://Helps to upload creative/files which are needed for the respective appplicatios
									cf.selectCreativesFiles(testData[i][6]);
									break;
								case "adProBriefValidation"://(Store):-helps to validate brief page page info
									store.adProBriefValidation(testData[i][5], testData[i][6]);
									break;
								case "vaultListandUplaodReportValidation"://(Vault):Helps to validate assetlist/assetupload report info with vault interace info
									results = vf.vaultListandUplaodReportValidation(testData[i][5], testData[i][6],testData[i][7]);
									break;
								case "vaultDownloadandNotDownloadReportValidation"://(Vault):Helps to validate assetDownloaded/assetnotDownloaded report info with vault interace info
									results = vf.vaultDownloadandNotDownloadReportValidation(testData[i][5],testData[i][6], testData[i][7]);
									break;
								case "vaultAssetViewedReportValidation"://(Vault):Helps to validate assetviewed list report info with vault interace info
									results = vf.vaultAssetViewedReportValidation(testData[i][5], testData[i][6],testData[i][7]);
									break;
								case "vaultWhishListValidation"://(Vault):Helps to validate wish list functionality
									results=vf.vaultWhishListValidation(testData[i][4]);
									break;
								case "close":
									cf.close();//Helps to closed the driver
									break;
								}
								/*if (!result) {
									//cf.updateExcelSheet(d, 5, basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[8][2]), "fail");
									break;
								}
								if (results==3) {
									cf.updateExcelSheet(d, 5, basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[8][2]), "automationfail");
									if(configurationData[6][2].equalsIgnoreCase("JDTrafficTestDataLoc")){
										driver.takeScreenShot("JDTraffic");
									}else if(configurationData[6][2].equalsIgnoreCase("JDTestDataLoc")){
										driver.takeScreenShot("JD");
									}else if(configurationData[6][2].equalsIgnoreCase("JDXTestDataLoc")){
										driver.takeScreenShot("JDX");
									}else if(configurationData[6][2].equalsIgnoreCase("JDManageTestDataLoc")){
										driver.takeScreenShot("JDManage");
									}else if(configurationData[6][2].equalsIgnoreCase("JDVaultTestDataLoc")){
										driver.takeScreenShot("JDVault");
									}else if(configurationData[6][2].equalsIgnoreCase("2adTestDataLoc")){
										driver.takeScreenShot("Store");
									}else{
										driver.takeScreenShot("Genral");
									}
									*/
								if (results==3) {
									cf.updateExcelSheet(d, 5, basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[8][2]), "automationfail");
									if(configurationData[6][2].equalsIgnoreCase("JDXTestDataLoc")){
										driver.takeScreenShot("JDX");
									}else{
										driver.takeScreenShot("Genral");
									}
									AppLogger.LOGGER.info("######################################");
									AppLogger.LOGGER.info(tcName+" Status :: "+"automationfail");
									AppLogger.LOGGER.info("######################################");
									break;
								}
								/*else if (results==2) {
									cf.updateExcelSheet(d, 5, basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[8][2]), "validationfail");
									if(configurationData[6][2].equalsIgnoreCase("JDTrafficTestDataLoc")){
										driver.takeScreenShot("JDTraffic");
									}else if(configurationData[6][2].equalsIgnoreCase("JDTestDataLoc")){
										driver.takeScreenShot("JD");
									}else if(configurationData[6][2].equalsIgnoreCase("JDXTestDataLoc")){
										driver.takeScreenShot("JDX");
									}else if(configurationData[6][2].equalsIgnoreCase("JDManageTestDataLoc")){
										driver.takeScreenShot("JDManage");
									}else if(configurationData[6][2].equalsIgnoreCase("JDVaultTestDataLoc")){
										driver.takeScreenShot("JDVault");
									}else if(configurationData[6][2].equalsIgnoreCase("2adTestDataLoc")){
										driver.takeScreenShot("Store");
									}else{
										driver.takeScreenShot("Genral");
									}
									*/
								else if (results==2) {
									cf.updateExcelSheet(d, 5, basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[8][2]), "validationfail");
									if(configurationData[6][2].equalsIgnoreCase("JDXTestDataLoc")){
										driver.takeScreenShot("JDX");
									}else{
										driver.takeScreenShot("Genral");
									}
									AppLogger.LOGGER.info("######################################");
									AppLogger.LOGGER.info(tcName+" Status :: "+"validationfail");
									AppLogger.LOGGER.info("######################################");
									break;
								}
								else if (i == b) {
									cf.updateExcelSheet(d, 5, basePath + prop.getProperty(configurationData[6][2]),prop.getProperty(configurationData[8][2]), "pass");
									AppLogger.LOGGER.info("######################################");
									AppLogger.LOGGER.info(tcName+" Status :: "+"pass");
									AppLogger.LOGGER.info("######################################");
								}
							}
						}
					}
					Thread.sleep(1000);
				}

			}
		} catch (IOException e) {
			System.out.println("Unable to locate specified path");
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
