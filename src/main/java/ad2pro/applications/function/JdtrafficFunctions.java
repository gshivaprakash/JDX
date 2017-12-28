package ad2pro.applications.function;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import ad2pro.fw.utils.AppLogger;
import ad2pro.fw.utils.CommonFunctions;
import ad2pro.fw.utils.Driver;
import ad2pro.fw.utils.LocatorsProperty;
import ad2pro.fw.utils.PropertiesFileParser;
import ad2pro.fw.utils.SeleniumHelper;
import jxl.JXLException;

/**
 * This class consist of needed operations that are related to JDTraffic application
 */
public class JdtrafficFunctions {
	
	private Driver driver;
	public static FTPClient ftpClient = null;
	private static String basePath = System.getProperty("user.dir");
	PropertiesFileParser prop = new PropertiesFileParser();
	LocatorsProperty rd = new LocatorsProperty();
	
	public JdtrafficFunctions(Driver driver)//constructor
	{
		this.driver = driver;
	}
			
	
	//helps to validate there is only one tracking no when search in search bar in JDT
	public int urnDropdownValidation(String strLocType,String strLocValue){
		AppLogger.LOGGER.info("Urn dropdown validation is started:" + strLocValue.toString());
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
				int listSize = element.size();
				System.out.println("Dropdown list size is:"+listSize);
				if(listSize == 1){
					System.out.println("We have only one order for the respctive URN number");
					rd.setStatus(1);
				}
				else if(listSize > 1){
					System.out.println("We should not have multiple orders in the same URN number");
					AppLogger.LOGGER.info("We have multiple orders in the same URN number it should not be like this!!!");
					rd.setStatus(2);
				}
				else if(listSize == 0){
					System.out.println("The respective order you are looking for is not yet created");
					AppLogger.LOGGER.info("The respective order you are looking for is not yet created!!!");
					rd.setStatus(2);
				}
			}
		} catch (Exception e) {
			rd.setStatus(3);
			AppLogger.LOGGER.info("Exception in dropdown validation (OR) The respective order is not yet created in jdTraffic: " + strLocValue.toString() + "\n" + e.getMessage());
			Assert.fail("Exception in dropdown validation (OR) The respective order is not yet created in jdTraffic: " + strLocValue.toString());
		}
		return rd.getStatus();
	}
	
	//Helps to validate the downloaded addetail report info with JDTraffic old interface
	public int adReportValidation(){
		//boolean sucess = false;
		boolean validationOne = false;
		boolean validationTwo = false;
		CommonFunctions cf = new CommonFunctions(driver);
		try{
			String[][] reportData = cf.fetchDataFromExcel("D:/JobDirect/Resources/Downloads/AdDetailReport.xls", "AdDetailReport");
			int totreportDataCount = cf.getTotalRow("D:/JobDirect/Resources/Downloads/AdDetailReport.xls", "AdDetailReport");
			System.out.println("The totreportDataCount:"+totreportDataCount);
			if(totreportDataCount > 1){	
				String trackingNo = reportData[2][1];
				System.out.println("The tracking no:"+trackingNo);
				cf.click_element("xpath","//div[@class='navLinks']/a[text()='ACTIVE JOBS']");
				Thread.sleep(5000);
				cf.SendKeys("id", "search", trackingNo);
				cf.waitforElement("xpath","//select[@id='search_results']/optgroup[@label='Orders']");
				urnDropdownValidation("xpath","//select[@id='search_results']/optgroup[@label='Orders']/option");
				Thread.sleep(5000);
				cf.clickByAction("xpath","//select[@id='search_results']/optgroup[@label='Orders']/option[1]");
				Thread.sleep(9000);
				//boolean trackingNoStatus = validateText("xpath","//div[@id='activeAdDetails']/div[@class='activeAdInnerWrap']/div[@class='activeAdWrap jobdetail']/div[1]",trackingNo);
				String lineItemName = reportData[2][12];
				System.out.println("The lineItemName attribuet name:"+lineItemName);
				//mywebDriver.findElement(By.cssSelector("a[linename='"+lineItemName+"']")).click();
				cf.scrollToElement("css","a[linename='"+lineItemName+"']");
				//String refAttribute=mywebDriver.findElement(By.cssSelector("a[linename='"+lineItemName+"']")).getAttribute("ref");
				String refAttribute=driver.findElement("css","a[linename='"+lineItemName+"']").getAttribute("ref");
				System.out.println("The ref attribuet name:"+refAttribute);
				//String assignedValue=mywebDriver.findElement(By.xpath("//div[@ref='"+refAttribute+"']/div[7]")).getText();
				String assignedValue=driver.findElement("xpath","//div[@ref='"+refAttribute+"']/div[7]").getText();
				System.out.println("The Assigned name:"+assignedValue);
				if(assignedValue.length() == 0){
					cf.click_element("css","a[linename='"+lineItemName+"']");
					cf.click_element("id","popup_cancel");
				}
				else{
					//mywebDriver.findElement(By.xpath("//div[@ref='"+refAttribute+"']/div[7]")).click();
					driver.findElement("xpath","//div[@ref='"+refAttribute+"']/div[7]").click();
				}
				Thread.sleep(5000);
				cf.scrollToElement("css","input[value='"+lineItemName+"']");
				//String nameAttribute=mywebDriver.findElement(By.cssSelector("input[value='"+lineItemName+"']")).getAttribute("name");
				String nameAttribute=driver.findElement("css","input[value='"+lineItemName+"']").getAttribute("name");
				System.out.println("The attribuet name:"+nameAttribute);
				String lineItemNo = null;
				if(nameAttribute.length()==17){
					lineItemNo = nameAttribute.substring(9,10);
				}
				else if(nameAttribute.length()==18){
					lineItemNo = nameAttribute.substring(9,11);
				}
				else if(nameAttribute.length()==19){
					lineItemNo = nameAttribute.substring(9,12);
				}
				System.out.println("The lineItemNo name:"+lineItemNo);
				//String itemName=mywebDriver.findElement(By.xpath("//formsection[@name='lineitemBase"+lineItemNo+"']/div[1]/div[1]/div/input")).getAttribute("value");
				String itemName=driver.findElement("xpath","//formsection[@name='lineitemBase"+lineItemNo+"']/div[1]/div[1]/div/input").getAttribute("value");
				System.out.println("The lineItem name:"+itemName);
				
				//String Quantity=mywebDriver.findElement(By.xpath("//formsection[@name='lineitemBase"+lineItemNo+"']/div[2]/div[4]/div/input")).getAttribute("value");
				String Quantity=driver.findElement("xpath","//formsection[@name='lineitemBase"+lineItemNo+"']/div[2]/div[4]/div/input").getAttribute("value");
				System.out.println("The Quantity name:"+Quantity);
				String q = Quantity+".0";
				System.out.println("The modified Quantity name:"+q);
				//String Rate=mywebDriver.findElement(By.xpath("//formsection[@name='lineitemBase"+lineItemNo+"']/div[2]/div[5]/div/input")).getAttribute("value");
				String Rate=driver.findElement("xpath","//formsection[@name='lineitemBase"+lineItemNo+"']/div[2]/div[5]/div/input").getAttribute("value");
				System.out.println("The Rate name:"+Rate);
				//String trackingNumber=mywebDriver.findElement(By.xpath("//formsection[@class='assetDetailWrap']/div[1]/div[@ref='trackno']/div/input")).getAttribute("value");
				String trackingNumber=driver.findElement("xpath","//formsection[@class='assetDetailWrap']/div[1]/div[@ref='trackno']/div/input").getAttribute("value");
				System.out.println("The trackingNo name:"+trackingNumber);
				//String orderName=mywebDriver.findElement(By.xpath("//formsection[@class='assetDetailWrap']/div[2]/div[@ref='name']/div/input")).getAttribute("value");
				String orderName=driver.findElement("xpath","//formsection[@class='assetDetailWrap']/div[2]/div[@ref='name']/div/input").getAttribute("value");
				System.out.println("The orderName name:"+orderName);
				//String salesRepName=mywebDriver.findElement(By.xpath("//formsection[@class='assetDetailWrap']/div[3]/div[@ref='salesrep']/div/input")).getAttribute("value");
				String salesRepName=driver.findElement("xpath","//formsection[@class='assetDetailWrap']/div[3]/div[@ref='salesrep']/div/input").getAttribute("value");
				System.out.println("The salesRepName name:"+salesRepName);
				//String budget=mywebDriver.findElement(By.xpath("//formsection[@class='assetDetailWrap']/div[3]/div[@ref='budget']/div/input")).getAttribute("value");
				String budget=driver.findElement("xpath","//formsection[@class='assetDetailWrap']/div[3]/div[@ref='budget']/div/input").getAttribute("value");
				System.out.println("The budget name:"+budget);
				//String startDate = mywebDriver.findElement(By.cssSelector("input[name='lineItem["+lineItemNo+"][startDate][date]']")).getAttribute("value");
				String startDate = driver.findElement("css","input[name='lineItem["+lineItemNo+"][startDate][date]']").getAttribute("value");
				System.out.println("The value name:"+startDate);
				//String endDate = mywebDriver.findElement(By.cssSelector("input[name='lineItem["+lineItemNo+"][endDate][date]']")).getAttribute("value");
				String endDate = driver.findElement("css","input[name='lineItem["+lineItemNo+"][endDate][date]']").getAttribute("value");
				System.out.println("The value name:"+endDate);
				//(budget.trim().equals(reportData[2][24].trim()))
				//String a = reportData[2][22];
				//System.out.println("The Quantity excel name:"+a);
				//String b = reportData[2][23];
				//System.out.println("The value excel name:"+b);
				if(lineItemName.trim().equals(itemName.trim()) && (q.trim().equals(reportData[2][22].trim())) && (Rate.trim().equals(reportData[2][23].trim())) && (trackingNumber.trim().equals(trackingNo.trim()))  && (orderName.trim().equals(reportData[2][2].trim())) && (salesRepName.trim().equals(reportData[2][8].trim())) && (startDate.trim().equals(cf.dateConversion(reportData[2][16]))) && (endDate.trim().equals(cf.dateConversion(reportData[2][17])))){
					System.out.println("The lineitem,Quantity,rate,trackingNo,orderName,salesRepName,startDate,endDate value matches");
					validationOne = true;
					System.out.println("The Validation one status is:"+validationOne);
				}
				else{
					System.out.println("The ineitem/Quantity/rate/trackingNo/orderName/salesRepName/startDate/endDate name does not matches:");
					validationOne = false;
					System.out.println("The Validation one status is:"+validationOne);
				}
				String c = reportData[2][14];
				//mywebDriver.findElement(By.xpath("//formsection[@name='lineitemBase"+lineItemNo+"']/div[1]/div[2]/div/div/span")).getText();
				
				System.out.println("The product xcel name:"+c);
				String d = reportData[2][21];
				//mywebDriver.findElement(By.xpath("//formsection[@name='lineitemBase"+lineItemNo+"']/div[2]/div[3]/div/div/span")).getText();
				System.out.println("The rateType xcel name:"+d);
				int productStatus = cf.validateText("xpath", "//formsection[@name='lineitemBase"+lineItemNo+"']/div[1]/div[2]/div/div/span", reportData[2][14]);
				int lineIemTypeStatus = cf.validateText("xpath", "//formsection[@name='lineitemBase"+lineItemNo+"']/div[2]/div[6]/div/div/span", reportData[2][13]);
				int rateTypeStatus = cf.validateText("xpath", "//formsection[@name='lineitemBase"+lineItemNo+"']/div[2]/div[3]/div/div/span", reportData[2][21]);
				//validateText("xpath", "//formsection[@name='lineitemBase"+lineItemNo+"']/div[2]/div[6]/div/div/span", reportData[2][20]);
				int regionStatus = cf.validateText("xpath", "//formsection[@class='assetDetailWrap']/div[1]/div[@ref='region']/div/div/span", reportData[2][0]);
				cf.scrollToElement("xpath","//input[@value='Targeting']");
				cf.click_element("xpath","//input[@value='Targeting']");
				cf.scrollToElement("css","input[value='"+lineItemName+"']");
				String website = reportData[2][27];
				System.out.println("The website xcel name:"+website);
				int homeWebsiteStatus = cf.validateText("xpath", "//div[@id='adTargeting"+lineItemNo+"']/div[@class='vTab']/div[5]/div[@class='clr criteriaBx nano has-scrollbar']/div[@class='content']/div[@id='inventory_selected']/div/div/div[3]", reportData[2][27]);
				
				if(productStatus == 1 && lineIemTypeStatus == 1 && rateTypeStatus == 1 && regionStatus == 1 && homeWebsiteStatus == 1){
					validationTwo = true;
					System.out.println("The Validation two status is:"+validationTwo);
				}
				else{
					System.out.println("The trackingNoStatus/productStatus/lineIemTypeStatus/rateTypeStatus/regionStatus/homeWebsiteStatus name does not matches:");
					validationTwo = false;
					System.out.println("The Validation two status is:"+validationTwo);
				}
				
				if(validationOne && validationTwo){
					rd.setStatus(1);
				}
				else{
					rd.setStatus(2);
				}
			}
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
		return rd.getStatus();
	}
	
	//Helps to validate the downloaded addetail report info with DFP inetrace info
	public int dfpAdDetailReportValidation(){
		boolean validationLineItem = false;
		boolean validationSalesRep = false;
		boolean validationStartDate = false;
		boolean validationEndDate = false;
		boolean validationStartTime = false;
		boolean validationEndTime = false;
		boolean validationLineItemType = false;
		boolean validationQuantity = false;
		boolean validationUnit = false;
		boolean validationRate = false;
		boolean validationCostType = false;
		boolean validationAdUnit = false;
		boolean validationStatusDfp = false;
		boolean validationAdvertLink = false;
		CommonFunctions cf = new CommonFunctions(driver);
		
		try{
		String[][] reportData = cf.fetchDataFromExcel("D:/JobDirect/Resources/Downloads/AdDetailReport.xls", "AdDetailReport");
		int totreportDataCount = cf.getTotalRow("D:/JobDirect/Resources/Downloads/AdDetailReport.xls", "AdDetailReport");
		System.out.println("The totreportDataCount:"+totreportDataCount);
		String advertLinkDFP =null;
		String adUnitDFP = null;
		if(totreportDataCount > 1){	
			//SimpleDateFormat formatter = new SimpleDateFormat("m/d/yyyy h:mm");
			for(int i=1;i<3;i++){
				if(i>1){
					cf.clear_element("xpath", "//div[@class='LVPPHAC-Y-c']/input");
				}
				String trackingNo = reportData[i][1];
				System.out.println("The tracking no in excel:"+trackingNo);
				String lineItemName = reportData[i][12];
				System.out.println("The lineItemName attribuet name in excel:"+lineItemName);
				Thread.sleep(5000);
				cf.SendKeys("xpath", ".//*[@id='gwt-debug-global-search-display']/div/input", lineItemName);
				//System.out.println("The LineItem stat:"+stat);

				cf.waitforElement("xpath","//div[@class='search-suggest-box-popup']");
				cf.validateTag("xpath","//b[text()='"+lineItemName+"']");
				cf.click_element("css",".search-item");
				Thread.sleep(5000);
				cf.validateTag("xpath","//strong[text()='"+lineItemName+"']");
				
				String lineItemNameDFP = driver.findElement("xpath",".//*[@id='gwt-debug-lineItem-txtLineItemName']").getAttribute("value");
				System.out.println("The LineItemNameDFP name:"+lineItemNameDFP);
				
				//String salesRepEmailDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-formField-editContainer']/div/div[1]/div/div/input")).getAttribute("value");
				String salesRepEmailDFP = driver.findElement("xpath",".//*[@id='gwt-debug-formField-editContainer']/div/div[1]/div/div/input").getAttribute("value");
				System.out.println("The salesRepEmailDFP name:"+salesRepEmailDFP);
				
				//String startDateDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-lineItemType-startDateBox']/input[1]")).getAttribute("value");
				String startDateDFP = driver.findElement("xpath",".//*[@id='gwt-debug-lineItemType-startDateBox']/input[1]").getAttribute("value");
				System.out.println("The startDateDFP name:"+startDateDFP);
				
				//String startTimeDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-lineItemType-startDateBox']/input[2]")).getAttribute("value");
				String startTimeDFP = driver.findElement("xpath",".//*[@id='gwt-debug-lineItemType-startDateBox']/input[2]").getAttribute("value");
				System.out.println("The startTimeDFP name:"+startTimeDFP);
				
				//String endDateDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-lineItemType-endDateBox']/input[1]")).getAttribute("value");
				String endDateDFP = driver.findElement("xpath",".//*[@id='gwt-debug-lineItemType-endDateBox']/input[1]").getAttribute("value");
				System.out.println("The endDateDFP name:"+endDateDFP);
				
				//String endTimeDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-lineItemType-endDateBox']/input[2]")).getAttribute("value");
				String endTimeDFP = driver.findElement("xpath",".//*[@id='gwt-debug-lineItemType-endDateBox']/input[2]").getAttribute("value");
				System.out.println("The endTimeDFP name:"+endTimeDFP);
				
				//String lineItemTypeDFP = mywebDriver.findElement(By.xpath("//div[@id='gwt-debug-lineItemTypePriority-lstLineItemTypes']/div[2]")).getText();
				String lineItemTypeDFP = driver.findElement("xpath","//div[@id='gwt-debug-lineItemTypePriority-lstLineItemTypes']/div[2]").getText();
				System.out.println("The lineItemTypeDFP name:"+lineItemTypeDFP);
				
				//String quantityDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-lineItemPrimaryGoal-txtQuantity']")).getAttribute("value");
				String quantityDFP = driver.findElement("xpath",".//*[@id='gwt-debug-lineItemPrimaryGoal-txtQuantity']").getAttribute("value");
				System.out.println("The quantityDFP name:"+quantityDFP);
				
				//String unitTypeDFP = mywebDriver.findElement(By.xpath("//div[@id='gwt-debug-lineItemPrimaryGoal-lstUnitTypes']/div[2]")).getText();
				String unitTypeDFP = driver.findElement("xpath","//div[@id='gwt-debug-lineItemPrimaryGoal-lstUnitTypes']/div[2]").getText();
				System.out.println("The unitTypeDFP name:"+unitTypeDFP);
				
				//String rateDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-lineItemType-txtCost']")).getAttribute("value");
				String rateDFP = driver.findElement("xpath",".//*[@id='gwt-debug-lineItemType-txtCost']").getAttribute("value");
				System.out.println("The rateDFP name:"+rateDFP);
				
				//String costTypeDFP = mywebDriver.findElement(By.xpath("//div[@id='gwt-debug-lineItemType-lstCostTypes']/div[2]")).getText();
				String costTypeDFP = driver.findElement("xpath","//div[@id='gwt-debug-lineItemType-lstCostTypes']/div[2]").getText();
				System.out.println("The costTypeDFP name:"+costTypeDFP);
				
				//String totalValueDFP = mywebDriver.findElement(By.xpath("//label[@id='gwt-debug-lineItemType-lblTotalCost']")).getText();
				String totalValueDFP = driver.findElement("xpath","//label[@id='gwt-debug-lineItemType-lblTotalCost']").getText();
				System.out.println("The totalValueDFP name:"+totalValueDFP);
				
				//String productTypeDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-selectedCriteriaPanel']/div[2]/div/div/div[3]/div[2]/div/div[2]/div[3]/span[2]")).getText();
				//System.out.println("The productTypeDFP name:"+productTypeDFP);
				
				if(reportData[i][27].length() > 1){
					//adUnitDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-cart-item-"+reportData[i][27]+"']/div[2]")).getText();
					adUnitDFP = driver.findElement("xpath",".//*[@id='gwt-debug-cart-item-"+reportData[i][27]+"']/div[2]").getText();
					System.out.println("The adUnitDFP name:"+adUnitDFP);
				}
				
				
				//String statusDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-LineItemDetail-header']/table/tbody/tr/td[2]/div[3]/div[2]/div[2]")).getText();
				String statusDFP = driver.findElement("xpath",".//*[@id='gwt-debug-LineItemDetail-header']/table/tbody/tr/td[2]/div[3]/div[2]/div[2]").getText();
				System.out.println("The statusDFP name:"+statusDFP);
				
				//String headerproductTypeDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-LineItemDetail-header']/table/tbody/tr/td[2]/div[3]/div[3]/div[2]")).getText();
				String headerproductTypeDFP = driver.findElement("xpath",".//*[@id='gwt-debug-LineItemDetail-header']/table/tbody/tr/td[2]/div[3]/div[3]/div[2]").getText();
				System.out.println("The headerproductTypeDFP name:"+headerproductTypeDFP);
				
				//validateTag("xpath","//div[contains(@id,'gwt-debug-cart-item-')]/div[text()='"+reportData[i][14]+"']");
				
				cf.click_element("xpath",".//*[@id='gwt-debug-LineItemDetail-overviewTab']");
				Thread.sleep(5000);
				
				if(reportData[i][10].length() > 1){
					//advertLinkDFP = mywebDriver.findElement(By.xpath(".//*[@id='gwt-debug-EntityCellTableWidget-t.creativeLineItem.table-cellTableContent']/tbody[1]/tr/td[8]/div/span")).getText();
					advertLinkDFP = driver.findElement("xpath",".//*[@id='gwt-debug-EntityCellTableWidget-t.creativeLineItem.table-cellTableContent']/tbody[1]/tr/td[8]/div/span").getText();
					System.out.println("The advertLinkDFP name:"+advertLinkDFP);
				}
				
				String startDate = cf.simpleDateConversion(reportData[i][16]);
				String startTime = cf.simpleTimeConversion(reportData[i][16]);
				
				String endDate = cf.simpleDateConversion(reportData[i][17]);
				String endTime = cf.simpleTimeConversion(reportData[i][17]);
				
				String conQuantityDFP = quantityDFP.replaceAll(",", "");
				System.out.println("The converted quantityDFP name:"+conQuantityDFP);
				
				String conRateDFP = rateDFP.substring(1);
				System.out.println("The converted conRateDFP name:"+conRateDFP);
				
				String conTotalValueDFP = totalValueDFP.substring(1);
				System.out.println("The contotalValueDFP conRateDFP name:"+conTotalValueDFP);
				
				String conQuantityvalue = reportData[i][22].substring(0,reportData[i][22].length()-2);
				System.out.println("The conQuantityvalue name:"+conQuantityvalue);
				
				// && (reportData[2][24].trim().contains(conTotalValueDFP.trim())) && (productTypeDFP.trim().equalsIgnoreCase(reportData[i][29].trim())) 
				
				/*System.out.println("The reportData[i][12] conRateDFP name:"+reportData[i][12]);
				System.out.println("The reportData[i][9] conRateDFP name:"+reportData[i][9]);
				System.out.println("The reportData[i][13] conRateDFP name:"+reportData[i][13]);
				System.out.println("The reportData[i][22] conRateDFP name:"+reportData[i][22]);
				System.out.println("The reportData[i][20] conRateDFP name:"+reportData[i][20]);
				System.out.println("The reportData[i][23] conRateDFP name:"+reportData[i][23]);
				System.out.println("The reportData[i][24] conRateDFP name:"+reportData[i][24]);
				System.out.println("The reportData[i][29] conRateDFP name:"+reportData[i][29]);
				System.out.println("The reportData[i][25] conRateDFP name:"+reportData[i][25]);
				System.out.println("The reportData[i][27] conRateDFP name:"+reportData[i][27]);
				System.out.println("The reportData[i][10] conRateDFP name:"+reportData[i][10]);*/
				
				/*if(lineItemNameDFP.trim().equals(reportData[i][12].trim()) && (salesRepEmailDFP.trim().equals(reportData[i][9].trim())) && (lineItemTypeDFP.trim().equalsIgnoreCase(reportData[i][13].trim())) && (conQuantityDFP.trim().equals(conQuantityvalue.trim())) && (unitTypeDFP.trim().equalsIgnoreCase(reportData[i][20].trim())) && (conRateDFP.trim().equals(reportData[i][23].trim())) && (startDate.trim().equals(startDateDFP)) && (endDate.trim().equals(endDateDFP)) && (startTime.trim().equals(startTimeDFP)) && (endTime.trim().equals(endTimeDFP)) && (statusDFP.trim().equalsIgnoreCase(reportData[i][25].trim())) && (adUnitDFP.trim().equalsIgnoreCase(reportData[i][27].trim()))){
					System.out.println("The lineitem,Quantity,rate,trackingNo,orderName,salesRepName,startDate,endDate value matches");
					validationOne = true;
					System.out.println("The Validation one status is:"+validationOne);
					if(reportData[i][10].length() > 1){
						System.out.println("The advertLinkDFP if  condition is:"+advertLinkDFP);
						System.out.println("The reportData[i][10].length() if  condition is:"+reportData[i][10].length());
						if(advertLinkDFP.trim().equals(reportData[i][10].trim())){
							System.out.println("The advertLinkDFP matches with excel value");
							validationOne = true;
						}
						else{
							System.out.println("The advertLinkDFP not matches with excel value");
							validationOne = false;
						}
					}
				}
				else{
					System.out.println("The ineitem/Quantity/rate/trackingNo/orderName/salesRepName/startDate/endDate name does not matches:");
					validationOne = false;
					System.out.println("The Validation one status is:"+validationOne);
				}*/
				
				if(lineItemNameDFP.trim().equals(reportData[i][12].trim())){
					validationLineItem = true;
					System.out.println("The lineItemNameDFP matches with excel value");
				}else{
					validationLineItem = false;
					System.out.println("The lineItemNameDFP not matches with excel value");
				}
				
				if(salesRepEmailDFP.trim().equals(reportData[i][9].trim())){
					validationSalesRep = true;
					System.out.println("The salesRepEmailDFP matches with excel value");
				}else{
					validationSalesRep = false;
					System.out.println("The salesRepEmailDFP not matches with excel value");
				}
				
				if(lineItemTypeDFP.trim().equalsIgnoreCase(reportData[i][13].trim())){
					validationLineItemType = true;
					System.out.println("The lineItemTypeDFP matches with excel value");
				}else{
					validationLineItemType = false;
					System.out.println("The validationLineItemType not matches with excel value");
				}
				
				if(conQuantityDFP.trim().equals(conQuantityvalue.trim())){
					validationQuantity = true;
					System.out.println("The conQuantityDFP matches with excel value");
				}else{
					validationQuantity = false;
					System.out.println("The conQuantityDFP not matches with excel value");
				}
				
				if(unitTypeDFP.trim().equalsIgnoreCase(reportData[i][20].trim())){
					validationUnit = true;
					System.out.println("The unitTypeDFP matches with excel value");
				}else{
					validationUnit = false;
					System.out.println("The unitTypeDFP not matches with excel value");
				}
				
				if(conRateDFP.trim().equals(reportData[i][23].trim())){
					validationRate = true;
					System.out.println("The conRateDFP matches with excel value");
				}else{
					validationRate = false;
					System.out.println("The conRateDFP not matches with excel value");
				}
				
				if(costTypeDFP.trim().equalsIgnoreCase(reportData[i][21].trim())){
					validationCostType = true;
					System.out.println("The coseDFP matches with excel value");
				}else{
					validationCostType = true;
					System.out.println("The coseDFP not matches with excel value");
				}
				
				if(startDate.trim().equals(startDateDFP)){
					validationStartDate = true;
					System.out.println("The startDateDfp matches with excel value");
				}else{
					validationStartDate = false;
					System.out.println("The startDateDfp not matches with excel value");
				}
				
				if(endDate.trim().equals(endDateDFP)){
					validationEndDate = true;
					System.out.println("The endDateDfp matches with excel value");
				}else{
					validationEndDate = false;
					System.out.println("The endDateDfp not matches with excel value");
				}
				
				if(startTime.trim().equals(startTimeDFP)){
					validationStartTime = true;
					System.out.println("The startTimeDfp matches with excel value");
				}else{
					validationStartTime = false;
					System.out.println("The startTimeDfp not matches with excel value");
				}
				
				if(endTime.trim().equals(endTimeDFP)){
					validationEndTime = true;
					System.out.println("The endTimeDfp matches with excel value");
					
				}else{
					validationEndTime = false;
					System.out.println("The validationEndTime not matches with excel value");
				}
				
				if(statusDFP.trim().equalsIgnoreCase(reportData[i][25].trim())){
					validationStatusDfp = true;
					System.out.println("The statusDfp matches with excel value");
				}else{
					validationStatusDfp = false;
					System.out.println("The statusDfp not matches with excel value");
				}
				
				if(reportData[i][27].length() > 1){
					if(adUnitDFP.trim().equalsIgnoreCase(reportData[i][27].trim())){
						validationAdUnit = true;
						System.out.println("The AdUnitDfp matches with excel value");
					}else{
						validationAdUnit = false;
						System.out.println("The AdUnitDfp not matches with excel value");
					}
				}

				if(reportData[i][10].length() > 1){
					System.out.println("The advertLinkDFP if  condition is:"+advertLinkDFP);
					System.out.println("The reportData[i][10].length() if  condition is:"+reportData[i][10].length());
					if(advertLinkDFP.trim().equals(reportData[i][10].trim())){
						System.out.println("The advertLinkDFP matches with excel value");
						validationAdvertLink = true;
					}
					else{
						System.out.println("The advertLinkDFP not matches with excel value");
						validationAdvertLink = false;
					}
				}
				
				if(validationLineItem && validationSalesRep && validationLineItemType && validationQuantity && validationUnit && validationRate && validationCostType
						&& validationStartDate && validationEndDate && validationStartTime && validationEndTime && validationStatusDfp){
					rd.setStatus(1);
					if(reportData[i][27].length() > 1){
						if(validationAdUnit){
							rd.setStatus(1);
						}else{
							rd.setStatus(2);
						}
					}
					if(reportData[i][10].length() > 1){
						if(validationAdvertLink){
							rd.setStatus(1);
						}else{
							rd.setStatus(2);
						}
					}
				}
				else{
					rd.setStatus(2);
				}
			}
		}
    	}catch(Exception e){
    		rd.setStatus(3);
    		e.printStackTrace();
    	}
	return rd.getStatus();
	}

	//Helps to download adtag report from JDT still validation needed to be added
	public boolean adTagValidation() throws JXLException, InterruptedException{
		boolean sucess = false;
		boolean validationOne = false;
		boolean validationTwo = false;
		CommonFunctions cf = new CommonFunctions(driver);
		try {
			String[][] reportData = cf.fetchDataFromExcel("D:/JobDirect/Resources/Downloads/AdTagReport.xls", "AdTags");
			int totreportDataCount = cf.getTotalRow("D:/JobDirect/Resources/Downloads/AdTagReport.xls", "AdTags");
			System.out.println("The totreportDataCount:"+totreportDataCount);
			if(totreportDataCount > 1){	
				String trackingNo = reportData[2][1];
				System.out.println("The tracking no:"+trackingNo);
				cf.click_element("xpath","//div[@class='navLinks']/a[text()='ACTIVE JOBS']");
				Thread.sleep(5000);
				cf.SendKeys("id", "search", trackingNo);
				cf.waitforElement("xpath","//select[@id='search_results']/optgroup[@label='Orders']");
				urnDropdownValidation("xpath","//select[@id='search_results']/optgroup[@label='Orders']/option");
				Thread.sleep(5000);
				cf.clickByAction("xpath","//select[@id='search_results']/optgroup[@label='Orders']/option[1]");
				Thread.sleep(9000);
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sucess;
	}
	
	//Helps to validate QUE manager info with JDTRAffic info, still scripts in progress 
	public boolean getQueManagerInfoValidation(){
		boolean sucess = false;
		boolean validationOne = false;
		boolean validationTwo = false;
		CommonFunctions cf = new CommonFunctions(driver);
		try {
			//String title = mywebDriver.findElement(By.xpath(".//*[@id='json']/div/ul/li[3]/ul/li[1]/span[2]")).getText();
			String title = driver.findElement("xpath",".//*[@id='json']/div/ul/li[3]/ul/li[1]/span[2]").getText();
			System.out.println("The Que manager title name:"+title);
			
			String[] words=title.split(":");
			System.out.println("The Que manager tracking no:"+words[1]);
			
			String trackingNo = words[1].substring(0,words[1].length()-1);
			System.out.println("The trackingNo name:"+trackingNo);
			
			cf.createNewTab();
			cf.WindowHandle();
			cf.openURL("https://jdtraffic.2adpro.com");
			
			cf.SendKeys("name","username","anga");
			cf.SendKeys("xpath",".//*[@id='loginform']/input[2]","welcome");
			cf.click_element("id","login_submit");
			Thread.sleep(10000);
			cf.SendKeys("id", "search", trackingNo);
			cf.waitforElement("xpath","//select[@id='search_results']/optgroup[@label='Orders']");
			urnDropdownValidation("xpath","//select[@id='search_results']/optgroup[@label='Orders']/option");
			Thread.sleep(5000);
			cf.clickByAction("xpath","//select[@id='search_results']/optgroup[@label='Orders']/option[1]");
			Thread.sleep(9000);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sucess;
	}
	
	
	//Helps to validate the downloaded porduction report info with JDT inetrace info
	public int productionReportValidation(String strLocValue){
		boolean validationOne = false;
		boolean validationTwo = false;
		boolean validationThree = false;
		boolean validationFour = false;
		boolean validationFive = false;
		boolean validationSix = false;
		boolean validationSeven = false;
		CommonFunctions cf = new CommonFunctions(driver);
		try {
			//List<WebElement> dataRows = mywebDriver.findElements(By.xpath(strLocValue));
			List<WebElement> dataRows = driver.findElements("xpath", strLocValue);
			//int dataCols = mywebDriver.findElements(By.xpath("//table[@class='dashboardDetails']/thead/tr/th")).size();
			int dataCols = driver.findElements("xpath","//table[@class='dashboardDetails']/thead/tr/th").size();
			System.out.println("The hardcode table rows:"+dataRows.size());
			System.out.println("The hardcode table cols:"+dataCols);
			if(dataRows.size() > 1){
				String[][] reportData = cf.fetchDataFromExcel("D:/JobDirect/Resources/Downloads/OrderStatus.xls", "OrderStatus");
				for(int i=1;i<2;i++){
					for(int j=1; j<=dataCols; j++){
						boolean flag = false;
						//String regionData = mywebDriver.findElement(By.xpath("//table[@class='dashboardDetails']/tbody/tr["+i+"]/td["+j+"]")).getText();
						String regionData = driver.findElement("xpath","//table[@class='dashboardDetails']/tbody/tr["+i+"]/td["+j+"]").getText();
						System.out.println("The regionData name for :"+i+":row &"+j+":-"+regionData);
						if(j==3 || j==4){
							String startDate = cf.dateTimeConversion(reportData[i][j-1]);
							System.out.println("The startDate name for :"+startDate);
							if(regionData.trim().equals(startDate)){
								System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j-1]);
								System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col matches" );
								if(j==3){
									validationThree = true;
									System.out.println("The validation Three status is:"+validationThree);
								}
								else if(j==4){
									validationFour = true;
									System.out.println("The validation  Four status is:"+validationFour);
								}
							}
							else{
								System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j-1]);
								System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col not matches" );
								if(j==3){
									validationThree = false;
									System.out.println("The validation Three status is:"+validationThree);
								}
								else if(j==4){
									validationFour = false;
									System.out.println("The validation  Four status is:"+validationFour);
								}
							}
							flag = true;
						}
						if(flag == false){
						if(regionData.trim().equals(reportData[i][j-1].trim())){
							System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j-1]);
							System.out.println("The region name for :"+i+"-row &"+j+"-col matches" );
							if(j==1){
								validationOne = true;
								System.out.println("The Validation One status is:"+validationOne);
							}
							else if(j==2){
								validationTwo = true;
								System.out.println("The validation Two status is:"+validationTwo);
							}
							else if(j==5){
								validationFive = true;
								System.out.println("The validation Five status is:"+validationFive);
							}
							else if(j==6){
								validationSix = true;
								System.out.println("The validation Six status is:"+validationSix);
							}
							else if(j==7){
								validationSeven = true;
								System.out.println("The validation Seven status is:"+validationSeven);
							}
						}
						else{
							System.out.println("The reportData name for not match :"+i+"-row &"+j+":-"+reportData[i][j-1]);
							System.out.println("The region name for :"+i+"-row &"+j+"-col does not matches" );
							if(j==1){
								validationOne = false;
								System.out.println("The Validation One status is:"+validationOne);
							}
							else if(j==2){
								validationTwo = false;
								System.out.println("The validation Two status is:"+validationTwo);
							}
							else if(j==5){
								validationFive = false;
								System.out.println("The validation Five status is:"+validationFive);
							}
							else if(j==6){
								validationSix = false;
								System.out.println("The validation Six status is:"+validationSix);
							}
							else if(j==7){
								validationSeven = false;
								System.out.println("The validation Seven status is:"+validationSeven);
							}
						}
						}
						
					}
					
				}
				if(validationOne && validationTwo && validationThree && validationFour && validationFive && validationSix && validationSeven){
					rd.setStatus(1);
				}
				else{
					rd.setStatus(2);
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			rd.setStatus(3);
			e.printStackTrace();
		}
		return rd.getStatus();
	}
	
	//Helps to validate the downloaded hold report info with JDT inetrace info
	public int holdReportValidation(String strLocValue){
		boolean validationOne = false;
		boolean validationTwo = false;
		boolean validationThree = false;
		boolean validationFour = false;
		boolean validationFive = false;
		boolean validationSix = false;
		boolean validationSeven = false;
		boolean validationEight = false;
		CommonFunctions cf = new CommonFunctions(driver);
		try {
			//List<WebElement> dataRows = mywebDriver.findElements(By.xpath(strLocValue));
			List<WebElement> dataRows = driver.findElements("xpath",strLocValue);
			//int dataCols = mywebDriver.findElements(By.xpath("//table[@class='dashboardDetails']/thead/tr/th")).size();
			int dataCols = driver.findElements("xpath","//table[@class='dashboardDetails']/thead/tr/th").size();
			System.out.println("The hardcode table rows:"+dataRows.size());
			System.out.println("The hardcode table cols:"+dataCols);
			if(dataRows.size() >= 1){
				String[][] reportData = cf.fetchDataFromExcel("D:/JobDirect/Resources/Downloads/OrderReport.xls", "OrderReport");
				for(int i=1;i<2;i++){
					for(int j=1; j<=dataCols; j++){
						//String regionData = mywebDriver.findElement(By.xpath("//table[@class='dashboardDetails']/tbody/tr["+i+"]/td["+j+"]")).getText();
						String regionData = driver.findElement("xpath","//table[@class='dashboardDetails']/tbody/tr["+i+"]/td["+j+"]").getText();
						System.out.println("The interace name for :"+i+"-row &"+j+":-"+regionData);
						if(j==1 || j==4 || j==5){
							if(j==1){
								String startDate = cf.dateTimeConversion(reportData[i][j]);
								System.out.println("The startDate name for :"+startDate);
								if(regionData.trim().equals(startDate)){
									System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j]);
									System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col matches" );
									validationOne = true;
									System.out.println("The Validation One status is:"+validationOne);
								}
								else{
									System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j]);
									System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col not matches" );
									validationOne = false;
									System.out.println("The Validation One status is:"+validationOne);
								}
							}
							
							if(j==4){
								if(regionData.trim().equals(reportData[i][j+1])){
									System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j+1]);
									System.out.println("The interface product and report product name for :"+i+"-row &"+j+"-col matches" );
									validationFour = true;
									System.out.println("The validation  Four status is:"+validationFour);
								}
								else{
									System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j+1]);
									System.out.println("The interface product name product for :"+i+"-row &"+j+"-col not matches" );
									validationFour = false;
									System.out.println("The validation  Four status is:"+validationFour);
								}
							}
							
							if(j==5){
								if(regionData.trim().equals(reportData[i][j-1])){
									System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j-1]);
									System.out.println("The interface lineitem and report product name for :"+i+"-row &"+j+"-col matches" );
									validationFive = true;
									System.out.println("The validation Five status is:"+validationFive);
								}
								else{
									System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j-1]);
									System.out.println("The interface lineitem name product for :"+i+"-row &"+j+"-col not matches" );
									validationFive = false;
									System.out.println("The validation Five status is:"+validationFive);
								}
							}
						}
						if(j==2 || j==3 || j==6 || j==7 || j==8){
							if(regionData.trim().equals(reportData[i][j].trim())){
								System.out.println("The reportData name for :"+i+"-row &"+j+-1+":-"+reportData[i][j]);
								System.out.println("The region name for :"+i+"-row &"+j+"-col matches" );
								if(j==2){
									validationTwo = true;
									System.out.println("The validation Two status is:"+validationTwo);
								}
								else if(j==3){
									validationThree = true;
									System.out.println("The validation Three status is:"+validationThree);
								}
								else if(j==6){
									validationSix = true;
									System.out.println("The validation Six status is:"+validationSix);
								}
								else if(j==7){
									validationSeven = true;
									System.out.println("The validation Seven status is:"+validationSeven);
								}
								else if(j==8){
									validationEight = true;
									System.out.println("The validation Eight status is:"+validationEight);
								}
								
								
							}
							else{
								System.out.println("The reportData name for not match :"+i+"-row &"+j+":-"+reportData[i][j]);
								System.out.println("The region name for :"+i+"-row &"+j+"-col does not matches" );
								if(j==2){
									validationTwo = false;
									System.out.println("The validation Two status is:"+validationTwo);
								}
								else if(j==3){
									validationThree = false;
									System.out.println("The validation Three status is:"+validationThree);
								}
								else if(j==6){
									validationSix = false;
									System.out.println("The validation Six status is:"+validationSix);
								}
								else if(j==7){
									validationSeven = false;
									System.out.println("The validation Seven status is:"+validationSeven);
								}
								else if(j==8){
									validationEight = false;
									System.out.println("The validation Eight status is:"+validationEight);
								}
							}
						}	
					}
					
				}
				if(validationOne && validationTwo && validationThree && validationFour && validationFive && validationSix && validationSeven && validationEight){
					rd.setStatus(1);
				}
				else{
					rd.setStatus(2);
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			rd.setStatus(3);
			e.printStackTrace();
		}
		return rd.getStatus();
	}
	
	//Helps to validate the downloaded rush report info with JDT inetrace info
	public int rushReportValidation(String strLocValue){
		boolean validationOne = false;
		boolean validationTwo = false;
		boolean validationThree = false;
		boolean validationFour = false;
		boolean validationFive = false;
		boolean validationSix = false;
		CommonFunctions cf = new CommonFunctions(driver);
		//CommonFunctions cf = new CommonFunctions(driver);
		try {
			//List<WebElement> dataRows = mywebDriver.findElements(By.xpath(strLocValue));
			List<WebElement> dataRows = driver.findElements("xpath",strLocValue);
			//int dataCols = mywebDriver.findElements(By.xpath("//table[@class='dashboardDetails']/thead/tr/th")).size();
			int dataCols = driver.findElements("xpath","//table[@class='dashboardDetails']/thead/tr/th").size();
			System.out.println("The hardcode table rows:"+dataRows.size());
			System.out.println("The hardcode table cols:"+dataCols);
			if(dataRows.size() >= 1){
				String[][] reportData = cf.fetchDataFromExcel("D:/JobDirect/Resources/Downloads/RushOrderReport.xls", "RushOrderReport");
				for(int i=1;i<2;i++){
					boolean flag = false;
					for(int j=1; j<=dataCols; j++){
						//String regionData = mywebDriver.findElement(By.xpath("//table[@class='dashboardDetails']/tbody/tr["+i+"]/td["+j+"]")).getText();
						String regionData = driver.findElement("xpath","//table[@class='dashboardDetails']/tbody/tr["+i+"]/td["+j+"]").getText();
						System.out.println("The interace name for :"+i+"-row &"+j+-1+":-"+regionData);
						if(j==1){
							String startDate = cf.dateTimeConversion(reportData[i][j]);
							System.out.println("The startDate name for :"+startDate);
							if(regionData.trim().equals(startDate)){
								System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j]);
								System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col matches" );
								validationOne = true;
								System.out.println("The Validation One status is:"+validationOne);
							}
							else{
								System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j]);
								System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col not matches" );
								validationOne = false;
								System.out.println("The Validation One status is:"+validationOne);
							}
						}
						if(j!=1){
							if(regionData.trim().equals(reportData[i][j].trim())){
								System.out.println("The reportData name for :"+i+"-row &"+j+-1+":-"+reportData[i][j]);
								System.out.println("The region name for :"+i+"-row &"+j+"-col matches" );
								if(j==2){
									validationTwo = true;
									System.out.println("The validation Two status is:"+validationTwo);
								}
								else if(j==3){
									validationThree = true;
									System.out.println("The validation Three status is:"+validationThree);
								}
								else if(j==4){
									validationFour = true;
									System.out.println("The validation  Four status is:"+validationFour);
								}
								else if(j==5){
									validationFive = true;
									System.out.println("The validation Five status is:"+validationFive);
								}
								else if(j==6){
									validationSix = true;
									System.out.println("The validation Six status is:"+validationSix);
								}
								
							}
							else{
								System.out.println("The reportData name for not match :"+i+"-row &"+j+":-"+reportData[i][j]);
								System.out.println("The region name for :"+i+"-row &"+j+"-col does not matches" );
								if(j==2){
									validationTwo = false;
									System.out.println("The validation Two status is:"+validationTwo);
								}
								else if(j==3){
									validationThree = false;
									System.out.println("The validation Three status is:"+validationThree);
								}
								else if(j==4){
									validationFour = false;
									System.out.println("The validation  Four status is:"+validationFour);
								}
								else if(j==5){
									validationFive = false;
									System.out.println("The validation Five status is:"+validationFive);
								}
								else if(j==6){
									validationSix = false;
									System.out.println("The validation Six status is:"+validationSix);
								}
							}
						}
					}
				}
				if(validationOne && validationTwo && validationThree && validationFour && validationFive && validationSix){
					rd.setStatus(1);
				}
				else{
					rd.setStatus(2);
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			rd.setStatus(3);
			e.printStackTrace();
		}
		return rd.getStatus();
	}
	
	//Helps to validate the downloaded creative report info with JDT inetrace info
	public int creativeReportValidation(){
		boolean validationOne = false;
		//boolean validationTwo = false;
		CommonFunctions cf = new CommonFunctions(driver);
		try{
			String[][] reportData = cf.fetchDataFromExcel("D:/JobDirect/Resources/Downloads/CreativeDetailReport.xls", "CreativeDetailReport");
			int totreportDataCount = cf.getTotalRow("D:/JobDirect/Resources/Downloads/CreativeDetailReport.xls", "CreativeDetailReport");
			System.out.println("The totreportDataCount:"+totreportDataCount);
			if(totreportDataCount > 1){
				String trackingNo = reportData[1][1];
				System.out.println("The tracking no:"+trackingNo);
				cf.click_element("xpath","//div[@class='navLinks']/a[text()='ACTIVE JOBS']");
				Thread.sleep(5000);
				cf.SendKeys("id", "search", trackingNo);
				cf.waitforElement("xpath","//select[@id='search_results']/optgroup[@label='Orders']");
				urnDropdownValidation("xpath","//select[@id='search_results']/optgroup[@label='Orders']/option");
				Thread.sleep(5000);
				cf.clickByAction("xpath","//select[@id='search_results']/optgroup[@label='Orders']/option[1]");
				Thread.sleep(9000);
				String lineItemName = reportData[1][4];
				System.out.println("The lineItemName attribuet name:"+lineItemName);
				//mywebDriver.findElement(By.cssSelector("a[linename='"+lineItemName+"']")).click();
				cf.scrollToElement("css","a[linename='"+lineItemName+"']");
				//String refAttribute=mywebDriver.findElement(By.cssSelector("a[linename='"+lineItemName+"']")).getAttribute("ref");
				String refAttribute=driver.findElement("css","a[linename='"+lineItemName+"']").getAttribute("ref");
				System.out.println("The ref attribuet name:"+refAttribute);
				//String assignedValue=mywebDriver.findElement(By.xpath("//div[@ref='"+refAttribute+"']/div[7]")).getText();
				String assignedValue=driver.findElement("xpath","//div[@ref='"+refAttribute+"']/div[7]").getText();
				System.out.println("The Assigned name:"+assignedValue);
				if(assignedValue.length() == 0){
					//click_element("css","a[linename='"+lineItemName+"']");
					//mywebDriver.findElement(By.xpath("//div[@ref='"+refAttribute+"']/div[7]")).click();
					driver.findElement("xpath","//div[@ref='"+refAttribute+"']/div[7]").click();
					cf.click_element("id","popup_ok");
				}
				else{
					//mywebDriver.findElement(By.xpath("//div[@ref='"+refAttribute+"']/div[7]")).click();
					driver.findElement("xpath","//div[@ref='"+refAttribute+"']/div[7]").click();
				}
				Thread.sleep(5000);
				cf.scrollToElement("css","input[value='"+lineItemName+"']");
				//String nameAttribute=mywebDriver.findElement(By.cssSelector("input[value='"+lineItemName+"']")).getAttribute("name");
				String nameAttribute=driver.findElement("css","input[value='"+lineItemName+"']").getAttribute("name");
				System.out.println("The attribuet name:"+nameAttribute);
				String lineItemNo = null;
				if(nameAttribute.length()==17){
					lineItemNo = nameAttribute.substring(9,10);
				}
				else if(nameAttribute.length()==18){
					lineItemNo = nameAttribute.substring(9,11);
				}
				else if(nameAttribute.length()==19){
					lineItemNo = nameAttribute.substring(9,12);
				}
				System.out.println("The lineItemNo name:"+lineItemNo);
				//String itemName=mywebDriver.findElement(By.xpath("//formsection[@name='lineitemBase"+lineItemNo+"']/div[1]/div[1]/div/input")).getAttribute("value");
				String itemName=driver.findElement("xpath","//formsection[@name='lineitemBase"+lineItemNo+"']/div[1]/div[1]/div/input").getAttribute("value");
				System.out.println("The lineItem name:"+itemName);
				cf.validateText("xpath", "//formsection[@class='assetDetailWrap']/div[1]/div[@ref='region']/div/div/span", reportData[1][0]);
				//String trackingNumber=mywebDriver.findElement(By.xpath("//formsection[@class='assetDetailWrap']/div[1]/div[@ref='trackno']/div/input")).getAttribute("value");
				String trackingNumber=driver.findElement("xpath","//formsection[@class='assetDetailWrap']/div[1]/div[@ref='trackno']/div/input").getAttribute("value");
				System.out.println("The trackingNo name:"+trackingNumber);
				//String orderName=mywebDriver.findElement(By.xpath("//formsection[@class='assetDetailWrap']/div[2]/div[@ref='name']/div/input")).getAttribute("value");
				String orderName=driver.findElement("xpath","//formsection[@class='assetDetailWrap']/div[2]/div[@ref='name']/div/input").getAttribute("value");
				System.out.println("The orderName name:"+orderName);
				//String advName=mywebDriver.findElement(By.xpath("//formsection[@class='assetDetailWrap']/div[2]/div[@ref='advertiser']/div/input")).getAttribute("value");
				String advName=driver.findElement("xpath","//formsection[@class='assetDetailWrap']/div[2]/div[@ref='advertiser']/div/input").getAttribute("value");
				System.out.println("The advName name:"+advName);
				//String startDate = mywebDriver.findElement(By.cssSelector("input[name='lineItem["+lineItemNo+"][startDate][date]']")).getAttribute("value");
				String startDate = driver.findElement("css","input[name='lineItem["+lineItemNo+"][startDate][date]']").getAttribute("value");
				System.out.println("The value name:"+startDate);
				//String endDate = mywebDriver.findElement(By.cssSelector("input[name='lineItem["+lineItemNo+"][endDate][date]']")).getAttribute("value");
				String endDate = driver.findElement("css","input[name='lineItem["+lineItemNo+"][endDate][date]']").getAttribute("value");
				System.out.println("The value name:"+endDate);
				
				if(lineItemName.trim().equals(itemName.trim()) && (trackingNumber.trim().equals(trackingNo.trim()))  && (orderName.trim().equals(reportData[1][2].trim())) && (advName.trim().equals(reportData[1][3].trim())) && (startDate.trim().equals(cf.dateConversion(reportData[2][6]))) && (endDate.trim().equals(cf.dateConversion(reportData[2][7])))){
					System.out.println("The lineitem,trackingNo,orderName,advName,startdate,enddate value matches");
					validationOne = true;
					System.out.println("The validation Two status is:"+validationOne);
				}
				else{
					System.out.println("The ineitem/trackingNo/orderName/advName/startdate/enddate name does not matches:");
					validationOne = false;
					System.out.println("The validation Two status is:"+validationOne);
				}
			}
			if(validationOne){
				rd.setStatus(1);
			}
			else{
				rd.setStatus(2);
			}
	    	}catch(Exception e){
	    		rd.setStatus(3);
	    		e.printStackTrace();
	    	}
		return rd.getStatus();
	}
	
	//Helps to perform dashboard operation
	public int dashBoardValidation(String type,String trackingNo,String startDate, String endDate) throws InterruptedException{
		//String regionName = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div[2]/div[1]")).getText();
		try{
			CommonFunctions cf = new CommonFunctions(driver);
			String regionName = driver.findElement("xpath",".//*[@id='dashboardDetails']/div[2]/div[1]").getText();
			if(regionName.equalsIgnoreCase("2adpro Preview")){
				if(type.equals("Hold")){
					//String holdValue = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div[2]/div[6]/a")).getText();
					String holdValue = driver.findElement("xpath",".//*[@id='dashboardDetails']/div[2]/div[6]/a").getText();
					System.out.println("hold value 1st:"+holdValue);
					cf.SendKeys("id", "search", trackingNo);
					cf.waitforElement("xpath","//select[@id='search_results']/optgroup[@label='Orders']");
					urnDropdownValidation("xpath","//select[@id='search_results']/optgroup[@label='Orders']/option");
					Thread.sleep(5000);
					cf.clickByAction("xpath","//select[@id='search_results']/optgroup[@label='Orders']/option[1]");
					Thread.sleep(9000);
					//boolean trackingNoStatus = validateText("xpath","//div[@id='activeAdDetails']/div[@class='activeAdInnerWrap']/div[@class='activeAdWrap jobdetail']/div[1]",trackingNo);
					cf.click_element("xpath",".//*[@id='activeAdDetails']/div[2]/div[2]/div[1]/div");
					Thread.sleep(2000);
					cf.clickByAction("xpath","//input[@value='Hold']");
					Thread.sleep(5000);
					cf.SendKeys("xpath", ".//*[@name='holdForm']/div[2]/textarea[@name='hold_message']", "HoldTest");
					cf.click_element("xpath",".//*[@id='hold_ordersubmit']");
					Thread.sleep(5000);
					cf.click_element("xpath","//div[@class='navLinks']/a[text()='DASHBOARD']");
					Thread.sleep(5000);
					cf.click_element("id","from");
					Thread.sleep(2000);
					cf.dataFun(startDate);
					cf.click_element("id","to");
					cf.dataFun(endDate);
					Thread.sleep(2000);
					cf.click_element("xpath","//input[@value='SHOW']");
					Thread.sleep(5000);
					//String upadtedValue = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div[2]/div[6]/a")).getText();
					String upadtedValue = driver.findElement("xpath",".//*[@id='dashboardDetails']/div[2]/div[6]/a").getText();
					System.out.println("hold value 2nd:"+upadtedValue);
					int increment = Integer.parseInt(holdValue) + 1;
					int increment2 = Integer.parseInt(upadtedValue);
					if(increment == increment2){
						System.out.println("It came pass");
					}
					else{
						System.out.println("It came fail");
					}
				}
			}
			else{
				System.out.println("The region is not 2adpropreview");
			}
		}catch(Exception e){
    		rd.setStatus(3);
    		e.printStackTrace();
    	}
		return rd.getStatus();
	}
	
	//Helps to validate total counts for 2adproprview/all region
	public int dashboardCountValidaion(String rowvalue){
		try{
			int a = 0;
			int j = 11;
			String regionName = null;
			int i = 0;
			if(rowvalue.equalsIgnoreCase("one")){
				a = 2;
				//regionName = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div["+a+"]/div[1]")).getText();
				regionName = driver.findElement("xpath",".//*[@id='dashboardDetails']/div["+a+"]/div[1]").getText();
				i = 2;
			}
			else if(rowvalue.equalsIgnoreCase("all")){
				a = 3;
				//regionName = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div["+a+"]/a/div")).getText();
				regionName = driver.findElement("xpath",".//*[@id='dashboardDetails']/div["+a+"]/a/div").getText();
				i = 1;
			}
			System.out.println("The region name is:"+regionName);
			
				String[] statusValue = new String[13];
				
				for(i=i;i<j;i++){
					if(rowvalue.equalsIgnoreCase("one")){
						//statusValue[i] = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div["+a+"]/div["+i+"]/a")).getText();
						statusValue[i] = driver.findElement("xpath",".//*[@id='dashboardDetails']/div["+a+"]/div["+i+"]/a").getText();
						if(i==10){
							j=12;
						}
					}
					else if(rowvalue.equalsIgnoreCase("all")){
						if(i==1){
							//statusValue[i] = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div["+a+"]/div["+i+"]/a[2]")).getText();
							statusValue[i] = driver.findElement("xpath",".//*[@id='dashboardDetails']/div["+a+"]/div["+i+"]/a[2]").getText();
						}
						else{
							//statusValue[i] = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div["+a+"]/div["+i+"]/a")).getText();
							statusValue[i] = driver.findElement("xpath",".//*[@id='dashboardDetails']/div["+a+"]/div["+i+"]/a").getText();
						}
					}
					System.out.println("The Amend value for 2adproPreviewregion:"+i+":"+statusValue[i]);
				}
				
				if(rowvalue.equalsIgnoreCase("one")){
					int statusTotalCount = 	Integer.parseInt(statusValue[2])+Integer.parseInt(statusValue[3])+Integer.parseInt(statusValue[4])+Integer.parseInt(statusValue[5])+Integer.parseInt(statusValue[6])+Integer.parseInt(statusValue[7])+Integer.parseInt(statusValue[8])+Integer.parseInt(statusValue[9])+Integer.parseInt(statusValue[10])+Integer.parseInt(statusValue[11]);
					System.out.println("The total Status Count value is"+statusTotalCount);
					
					//String interfaceTotalCount = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div["+a+"]/div[12]/a")).getText();
					String interfaceTotalCount = driver.findElement("xpath",".//*[@id='dashboardDetails']/div["+a+"]/div[12]/a").getText();
					System.out.println("The interface total Status Count value is"+interfaceTotalCount);
					
					if(statusTotalCount == Integer.parseInt(interfaceTotalCount)){
						System.out.println("The interface total Status Count and calculated total status count mathces");
						rd.setStatus(1);
					}
					else{
						System.out.println("The interface total Status Count and calculated total status count not mathces");
						rd.setStatus(2);
					}
					
				}
				else if(rowvalue.equalsIgnoreCase("all")){
					int statusTotalCount = 	Integer.parseInt(statusValue[1])+Integer.parseInt(statusValue[2])+Integer.parseInt(statusValue[3])+Integer.parseInt(statusValue[4])+Integer.parseInt(statusValue[5])+Integer.parseInt(statusValue[6])+Integer.parseInt(statusValue[7])+Integer.parseInt(statusValue[8])+Integer.parseInt(statusValue[9])+Integer.parseInt(statusValue[10]);
					System.out.println("The total Status Count value is"+statusTotalCount);
					
					//String interfaceTotalCount = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div["+a+"]/div[11]/a")).getText();
					String interfaceTotalCount = driver.findElement("xpath",".//*[@id='dashboardDetails']/div["+a+"]/div[11]/a").getText();
					System.out.println("The interface total Status Count value is"+interfaceTotalCount);
					
					if(statusTotalCount == Integer.parseInt(interfaceTotalCount)){
						System.out.println("The interface total Status Count and calculated total status count mathces");
						rd.setStatus(1);
					}
					else{
						System.out.println("The interface total Status Count and calculated total status count not mathces");
						rd.setStatus(2);
					}
					
				}
				
				/*String amend = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div[2]/div[2]/a")).getText();
				System.out.println("The Amend value for 2adproPreviewregion:"+amend);
				String revised = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div[2]/div[3]/a")).getText();
				System.out.println("The revised value for 2adproPreviewregion:"+revised);
				String submitted = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div[2]/div[4]/a")).getText();
				System.out.println("The submitted value for 2adproPreviewregion:"+submitted);
				String updated = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div[2]/div[5]/a")).getText();
				System.out.println("The updated value for 2adproPreviewregion:"+updated);
				String updated = mywebDriver.findElement(By.xpath(".//*[@id='dashboardDetails']/div[2]/div[6]/a")).getText();
				System.out.println("The updated value for 2adproPreviewregion:"+updated);*/
			
	    	}catch(Exception e){
	    		rd.setStatus(3);
	    		e.printStackTrace();
	    	}
		return rd.getStatus();
	}
	
	public static int getDfpColNameCount(String input,int totreportColCount, String[][] reportData){
		int j=0;
		for(int i=0;i<1;i++){
			for(j=0;j<=totreportColCount;j++){
				if(reportData[i][j].equalsIgnoreCase(input)){
					System.out.println("The dfp report col count for the input "+input+" is"+j);
					break;
				}
			}
		}
		return j;
	}
	
	public static int getDfpRowNameCount(String input,int colCount,int totreportRowCount, String[][] reportData){
		for(int i=1;i<totreportRowCount;i++){
			if(reportData[i][colCount].equalsIgnoreCase(input)){
				
			}
		}
		return totreportRowCount;
		
	}
	
	public void campaignReportValidation(){
		CommonFunctions cf = new CommonFunctions(driver);
		try{
			String[][] dfpReportData = cf.fetchDataFromExcel("D:/JobDirect/Resources/Downloads/CampaignReportDfp.xls", "Detailed");
			int totDfpReportRowCount = cf.getTotalRow("D:/JobDirect/Resources/Downloads/CampaignReportDfp.xls", "Detailed");
			System.out.println("The totDfpReportRowCount:"+totDfpReportRowCount);
			int totDfpReportColCount = cf.getColRow("D:/JobDirect/Resources/Downloads/CampaignReportDfp.xls", "Detailed");
			System.out.println("The totDfpReportColCount:"+totDfpReportColCount);
			
			String[][] proReportData = cf.fetchDataFromExcel("D:/JobDirect/Resources/Downloads/CampaignReportPro.xls", "Detailed");
			int totProReportRowCount = cf.getTotalRow("D:/JobDirect/Resources/Downloads/CampaignReportPro.xls", "Detailed");
			System.out.println("The totProReportRowCount:"+totProReportRowCount);
			int totProReportColCount = cf.getColRow("D:/JobDirect/Resources/Downloads/CampaignReportPro.xls", "Detailed");
			System.out.println("The totProReportColCount:"+totProReportColCount);
			if(totProReportRowCount > 1){
				
				
			}
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	}
	
	//helps to upload file via FTP
	public int FTPUpload(String localPath, String uploadPath) throws Exception{
		AppLogger.LOGGER.info("File upload starts");
		  ftpClient = new FTPClient();
		     try {

		         ftpClient.connect(prop.getProperty("server"));
		         ftpClient.login(prop.getProperty("ftpUsername"),prop.getProperty("ftpPassword"));
		         ftpClient.enterLocalPassiveMode();

		         ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

		        // uploads first file using an InputStream
		         File Upload = new File(basePath+localPath);
		         String UploadFileName = uploadPath;
		         InputStream inputStream = new FileInputStream(Upload);

		         System.out.println("Start uploading first file");
		         boolean ftpUpload = ftpClient.storeFile(UploadFileName, inputStream);
		         inputStream.close();
		         if (ftpUpload) {
		             System.out.println("The first is uploaded successfully.");
		             rd.setStatus(1);
		         }else{
		        	 System.out.println("The first upload is unsucessful.");
		        	 rd.setStatus(3);
		         }
		         TimeUnit.MINUTES.sleep(13);

		     } catch (IOException ex) {
		    	 AppLogger.LOGGER.info("The first upload is unsucessful" + ex.getMessage());
		         ex.printStackTrace();
		     } finally {
		         try {
		             if (ftpClient.isConnected()) {
		                 ftpClient.logout();
		                 ftpClient.disconnect();
		             }
		         } catch (IOException ex) {
		             ex.printStackTrace();
		             AppLogger.LOGGER.info("The file upload is unsuccesful." + ex.getMessage());
		         }
		     }
		     return rd.getStatus();	
		 }

}
