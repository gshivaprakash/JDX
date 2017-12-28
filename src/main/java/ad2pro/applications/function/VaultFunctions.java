package ad2pro.applications.function;

import java.io.IOException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ad2pro.fw.utils.CommonFunctions;
import ad2pro.fw.utils.Driver;
import ad2pro.fw.utils.LocatorsProperty;
import ad2pro.fw.utils.PropertiesFileParser;
import ad2pro.fw.utils.SeleniumHelper;

/**
 * This class consist of needed operations that are related to vault application
 */

public class VaultFunctions {
	
	private Driver driver;
	//static WebDriver mywebDriver;
	CommonFunctions cf = new CommonFunctions(driver);
	SeleniumHelper shelp=new SeleniumHelper(driver);
	public static FTPClient ftpClient = null;
	private static String basePath = System.getProperty("user.dir");
	PropertiesFileParser prop = new PropertiesFileParser();
	LocatorsProperty rd = new LocatorsProperty();
	
	public VaultFunctions(Driver driver)//constructor
	{
		this.driver = driver;
	}
	
	//Helps to validate assetlist/assetupload report info with vault interace info
	public int vaultListandUplaodReportValidation(String strLocValue, String Filepath, String SheetName){
		boolean sucess = false;
		boolean validationTwo = false;
		boolean validationThree = false;
		boolean validationFour = false;
		boolean validationFive = false;
		boolean validationSix = false;
		boolean validationSeven = false;
		boolean validationEight = false;
		boolean validationNine = false;
		try {
			//List<WebElement> dataRows = mywebDriver.findElements(By.xpath(strLocValue));
			List<WebElement> dataRows = driver.findElements("xpath",strLocValue);
			//int dataCols = mywebDriver.findElements(By.xpath("//div[@class='produtTable']/div[@class='produtTableRow head']/div")).size();
			int dataCols = driver.findElements("xpath","//div[@class='produtTable']/div[@class='produtTableRow head']/div").size();
			System.out.println("The hardcode table rows:"+dataRows.size());
			System.out.println("The hardcode table cols:"+dataCols);
			if(dataRows.size() >= 1){
				String[][] reportData = cf.fetchDataFromExcel(Filepath, SheetName);
				for(int i=1;i<3;i++){
					boolean flag = false;
					for(int j=2; j<=dataCols; j++){
						//String regionData = mywebDriver.findElement(By.xpath("//div[@class='produtTable']/div[@class='produtTableRow']["+i+"]/div["+j+"]")).getText();
						String regionData = driver.findElement("xpath","//div[@class='produtTable']/div[@class='produtTableRow']["+i+"]/div["+j+"]").getText();
						System.out.println("The interace name for :"+i+"-row & "+j+"-col"+":-"+regionData);
						if(j==8){
							String startDate = cf.dateTimeSecConversion(reportData[i][j-1]);
							System.out.println("The startDate name for :"+startDate);
							if(regionData.trim().equals(startDate)){
								//System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j-1]);
								System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col matches" );
								validationEight = true;
								System.out.println("The Validation One status is:"+validationEight);
							}
							else{
								//System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j-1]);
								System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col not matches" );
								validationEight = false;
								System.out.println("The Validation One status is:"+validationEight);
							}
						}
						if(j!=8){
							if(regionData.trim().equals(reportData[i][j-1].trim())){
								System.out.println("The reportData name for :"+i+"-row &"+j+-1+":-"+reportData[i][j-1]);
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
								else if(j==7){
									validationSeven = true;
									System.out.println("The validation Seven status is:"+validationSeven);
								}
								else if(j==9){
									validationNine = true;
									System.out.println("The validation Nine status is:"+validationNine);
								}
							}
							else{
								System.out.println("The reportData name for not match :"+i+"-row &"+j+"-col"+":-"+reportData[i][j-1]);
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
								else if(j==7){
									validationSeven = false;
									System.out.println("The validation Seven status is:"+validationSeven);
								}
								else if(j==9){
									validationNine = false;
									System.out.println("The validation Nine status is:"+validationNine);	
								}
							}
						}
					}
				}
				if(validationTwo && validationThree && validationFour && validationFive && validationSix && validationSeven && validationEight && validationNine){
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
	
	//Helps to validate assetDownloaded/assetnotDownloaded report info with vault interace info
	public int vaultDownloadandNotDownloadReportValidation(String strLocValue, String Filepath, String SheetName){
		boolean validationTwo = false;
		boolean validationThree = false;
		boolean validationFour = false;
		boolean validationFive = false;
		boolean validationSix = false;
		boolean validationSeven = false;
		boolean validationEight = false;
		boolean validationNine = false;
		try {
			//List<WebElement> dataRows = mywebDriver.findElements(By.xpath(strLocValue));
			List<WebElement> dataRows = driver.findElements("xpath",strLocValue);
			//int dataCols = mywebDriver.findElements(By.xpath("//div[@class='produtTable']/div[@class='produtTableRow head']/div")).size();
			int dataCols = driver.findElements("xpath","//div[@class='produtTable']/div[@class='produtTableRow head']/div").size();
			System.out.println("The hardcode table rows:"+dataRows.size());
			System.out.println("The hardcode table cols:"+dataCols);
			if(dataRows.size() >= 1){
				String[][] reportData = cf.fetchDataFromExcel(Filepath, SheetName);
				for(int i=1;i<2;i++){
					boolean flag = false;
					for(int j=2; j<=dataCols; j++){
						//String regionData = mywebDriver.findElement(By.xpath("//div[@class='produtTable']/div[@class='produtTableRow']["+i+"]/div["+j+"]")).getText();
						String regionData = driver.findElement("xpath","//div[@class='produtTable']/div[@class='produtTableRow']["+i+"]/div["+j+"]").getText();
						System.out.println("The interace name for :"+i+"-row & "+j+"-col"+":-"+regionData);
						if(j==7){
							String startDate = cf.dateTimeSecConversion(reportData[i][j-1]);
							System.out.println("The startDate name for :"+startDate);
							if(regionData.trim().equals(startDate)){
								//System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j-1]);
								System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col matches" );
								validationSeven = true;
								System.out.println("The Validation Seven status is:"+validationSeven);
							}
							else{
								//System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j-1]);
								System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col not matches" );
								validationSeven = false;
								System.out.println("The Validation Seven status is:"+validationSeven);
							}
						}
						if(j!=7){
							if(regionData.trim().equals(reportData[i][j-1].trim())){
								System.out.println("The reportData name for :"+i+"-row &"+j+-1+":-"+reportData[i][j-1]);
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
								else if(j==8){
									validationEight = true;
									System.out.println("The validation Eight status is:"+validationEight);
								}
								else if(j==9){
									validationNine = true;
									System.out.println("The validation Nine status is:"+validationNine);
								}
							}
							else{
								System.out.println("The reportData name for not match :"+i+"-row &"+j+"-col"+":-"+reportData[i][j-1]);
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
								else if(j==8){
									validationEight = false;
									System.out.println("The validation Eight status is:"+validationEight);
								}
								else if(j==9){
									validationNine = false;
									System.out.println("The validation Nine status is:"+validationNine);	
								}
							}
						}
					}
				}
				if(validationTwo && validationThree && validationFour && validationFive && validationSix && validationSeven && validationEight && validationNine){
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
	
	//Helps to validate assetviewed list report info with vault interace info
	public int vaultAssetViewedReportValidation(String strLocValue, String Filepath, String SheetName){
		boolean validationTwo = false;
		boolean validationThree = false;
		boolean validationFour = false;
		boolean validationFive = false;
		boolean validationSix = false;
		boolean validationSeven = false;
		try {
			//List<WebElement> dataRows = mywebDriver.findElements(By.xpath(strLocValue));
			List<WebElement> dataRows = driver.findElements("xpath",strLocValue);
			//int dataCols = mywebDriver.findElements(By.xpath("//div[@class='produtTable']/div[@class='produtTableRow head']/div")).size();
			int dataCols = driver.findElements("xpath","//div[@class='produtTable']/div[@class='produtTableRow head']/div").size();
			System.out.println("The hardcode table rows:"+dataRows.size());
			System.out.println("The hardcode table cols:"+dataCols);
			if(dataRows.size() >= 1){
				String[][] reportData = cf.fetchDataFromExcel(Filepath, SheetName);
				for(int i=1;i<2;i++){
					boolean flag = false;
					for(int j=2; j<=dataCols; j++){
						//String regionData = mywebDriver.findElement(By.xpath("//div[@class='produtTable']/div[@class='produtTableRow']["+i+"]/div["+j+"]")).getText();
						String regionData = driver.findElement("xpath","//div[@class='produtTable']/div[@class='produtTableRow']["+i+"]/div["+j+"]").getText();
						System.out.println("The interace name for :"+i+"-row & "+j+"-col"+":-"+regionData);
						if(j==7){
							String startDate = cf.dateTimeSecConversion(reportData[i][j-1]);
							System.out.println("The startDate name for :"+startDate);
							if(regionData.trim().equals(startDate)){
								//System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j-1]);
								System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col matches" );
								validationSeven = true;
								System.out.println("The Validation Seven status is:"+validationSeven);
							}
							else{
								//System.out.println("The reportData name for :"+i+"-row &"+j+":-"+reportData[i][j-1]);
								System.out.println("The interface startdate and report startdate name for :"+i+"-row &"+j+"-col not matches" );
								validationSeven = false;
								System.out.println("The Validation Seven status is:"+validationSeven);
							}
						}
						if(j!=7){
							if(regionData.trim().equals(reportData[i][j-1].trim())){
								System.out.println("The reportData name for :"+i+"-row &"+j+-1+":-"+reportData[i][j-1]);
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
								System.out.println("The reportData name for not match :"+i+"-row &"+j+"-col"+":-"+reportData[i][j-1]);
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
				if(validationTwo && validationThree && validationFour && validationFive && validationSix && validationSeven){
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
	
	//Helps to validate wish list functionality
	static String autofirstId;
	static String diningfirstId;
	public int vaultWhishListValidation(String catogeryType){
		try{
			if(catogeryType.equalsIgnoreCase("auto")){
				//autofirstId = mywebDriver.findElement(By.xpath("//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[1]")).getAttribute("data-asset-id");
				autofirstId = driver.findElement("xpath","//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[1]").getAttribute("data-asset-id");
				System.out.println("The autofirstId is:"+autofirstId);
				rd.setStatus(1);
			}
			else if(catogeryType.equalsIgnoreCase("Dining")){
				//diningfirstId = mywebDriver.findElement(By.xpath("//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[1]")).getAttribute("data-asset-id");
				diningfirstId = driver.findElement("xpath","//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[1]").getAttribute("data-asset-id");
				System.out.println("The diningfirstId is:"+diningfirstId);
				rd.setStatus(1);
			}
			else if(catogeryType.equalsIgnoreCase("WhishList")){
				//String wishListFirstAssetID = mywebDriver.findElement(By.xpath("//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[1]")).getAttribute("data-asset-id");
				String wishListFirstAssetID = driver.findElement("xpath","//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[1]").getAttribute("data-asset-id");
				System.out.println("The wishListFirstAssetID is:"+wishListFirstAssetID);
				
				//String wishListSecondAssetID = mywebDriver.findElement(By.xpath("//div[@class='opt_1_wrap newproductwrap']/div[1]/div[2]/div[@class='productThumbIcons']/span[1]")).getAttribute("data-asset-id");
				String wishListSecondAssetID = driver.findElement("xpath","//div[@class='opt_1_wrap newproductwrap']/div[1]/div[2]/div[@class='productThumbIcons']/span[1]").getAttribute("data-asset-id");
				System.out.println("The wishListSecondAssetID is:"+wishListSecondAssetID);
				
				if(wishListFirstAssetID.equals(diningfirstId) && wishListSecondAssetID.equals(autofirstId)){
					int validationOne = cf.validateTag("xpath","//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[@class='light_box added']");
					int validationTwo = cf.validateTag("xpath","//div[@class='opt_1_wrap newproductwrap']/div[1]/div[2]/div[@class='productThumbIcons']/span[@class='light_box added']");
					if(validationOne == 1 && validationTwo == 1){
						rd.setStatus(1);
					}else{
						rd.setStatus(2);
					}
				}
				else{
					rd.setStatus(2);
				}
			}
			else if(catogeryType.equalsIgnoreCase("DiningRemoveWhishList")){
				//String diningRemovedFirstAssetID = mywebDriver.findElement(By.xpath("//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[1]")).getAttribute("data-asset-id");
				String diningRemovedFirstAssetID = driver.findElement("xpath","//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[1]").getAttribute("data-asset-id");
				System.out.println("The diningRemovedFirstAssetID is:"+diningRemovedFirstAssetID);
				if(diningRemovedFirstAssetID.equals(diningfirstId)){
					int validationThree = cf.validateTag("xpath","//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[@class='light_box add']");
					if(validationThree == 1){
						rd.setStatus(1);
					}else{
						rd.setStatus(2);
					}
				}else{
					rd.setStatus(2);
				}
			}
			else if(catogeryType.equalsIgnoreCase("AutoRemoveWhishList")){
				//String autoRemovedFirstAssetID = mywebDriver.findElement(By.xpath("//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[1]")).getAttribute("data-asset-id");
				String autoRemovedFirstAssetID = driver.findElement("xpath","//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[1]").getAttribute("data-asset-id");
				System.out.println("The autoRemovedFirstAssetID is:"+autoRemovedFirstAssetID);
				if(autoRemovedFirstAssetID.equals(autofirstId)){
					int validationFour = cf.validateTag("xpath","//div[@class='opt_1_wrap newproductwrap']/div[1]/div[1]/div[@class='productThumbIcons']/span[@class='light_box add']");
					if(validationFour == 1){
						rd.setStatus(1);
					}else{
						rd.setStatus(2);
					}
				}else{
					rd.setStatus(2);
				}
			}
		}catch(Exception e){
			rd.setStatus(3);
			e.printStackTrace();
		}
		return rd.getStatus();
		
	}
	
	/*public boolean batchUpdate(int assetCount) throws IOException{
		boolean sucess = false;
		try{
			Click_element("linkText","ASSETS");
		}catch(Exception e){
			
		}
		return sucess;
	}*/


}

