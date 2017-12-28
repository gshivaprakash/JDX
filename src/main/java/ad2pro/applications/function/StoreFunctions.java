package ad2pro.applications.function;

import org.apache.commons.net.ftp.FTPClient;
import org.openqa.selenium.By;

import ad2pro.fw.utils.CommonFunctions;
import ad2pro.fw.utils.Driver;
import ad2pro.fw.utils.PropertiesFileParser;
import ad2pro.fw.utils.SeleniumHelper;

/**
 * This class consist of needed operations that are related to Store application
 */
public class StoreFunctions {
	
	private Driver driver;
	public static FTPClient ftpClient = null;
	private static String basePath = System.getProperty("user.dir");
	PropertiesFileParser prop = new PropertiesFileParser();
	
	public StoreFunctions(Driver driver)//constructor
	{
		this.driver = driver;
	}
	
	//helps to validate brief page page info
	public void adProBriefValidation(String categoriesType, String prodName){
		CommonFunctions cf = new CommonFunctions(driver);
		try{
			String productName = null;
			//String prodVer;
			String itemsCount = null;
			String subTotal = null;	
			cf.click_element("xpath","//a[@title='Categories']");
			int identifyNameStatus = cf.validateText("xpath", "//h2[text()='Identity']", "IDENTITY");
			if(categoriesType.equalsIgnoreCase("identity")){
				cf.click_element("xpath","//h2[text()='Identity']");
				int businessNameStatus = cf.validateText("xpath", "//h3[text()='Business Card']", "Business Card");
				if(prodName.equalsIgnoreCase("logoDesign")){
					int itemNoStatus = cf.validateText("xpath", "//li[text()='0 ITEM']", "0 ITEM");
					cf.click_element("xpath",".//*[@id='sub-categories']/div/div[1]/div[2]/div/ol/li[1]/div/div[2]/p/a");
					int changeditemNoStatus = cf.validateText("xpath", "//li[text()='1 item']", "1 item");
					//productName = mywebDriver.findElement(By.xpath("//h3[text()='logo design']")).getText();
					productName = driver.findElement("xpath","//h3[text()='logo design']").getText();
					System.out.println("The interface product name is:"+productName);
					//prodVer = mywebDriver.findElement(By.xpath("//input[@name='quant[101]']")).getAttribute("value");
					//itemsCount = mywebDriver.findElement(By.xpath("//li[contains(text(),'item')]")).getText();
					itemsCount = driver.findElement("xpath","//li[contains(text(),'item')]").getText();
					System.out.println("The interface itemsCount name is:"+itemsCount);
					//subTotal  = mywebDriver.findElement(By.xpath("//span[@class='subtotal']")).getText();
					subTotal  = driver.findElement("xpath","//span[@class='subtotal']").getText();
					System.out.println("The interface subTotal name is:"+subTotal);
					Thread.sleep(9000);
					System.out.println("It is going to click checkout:");
					cf.click_element("xpath","//a[text()='CHECK OUT']");
					int cartNameStatus = cf.validateText("xpath", "//h3[text()='ITEMS IN THE CART']", "ITEMS IN THE CART");
				}
			}
				//String cartproductName = mywebDriver.findElement(By.xpath("//table[@class='table cart-table data-th-table']/tbody/tr[1]/td[1]/h3")).getText();
				String cartproductName = driver.findElement("xpath","//table[@class='table cart-table data-th-table']/tbody/tr[1]/td[1]/h3").getText();
				System.out.println("The interface cartproductName name is:"+cartproductName);
				//String cartitemsCount = mywebDriver.findElement(By.xpath("//div[contains(text(),'item')]")).getText();
				String cartitemsCount = driver.findElement("xpath","//div[contains(text(),'item')]").getText();
				System.out.println("The interface cartitemsCount name is:"+cartitemsCount);
				//String cartsubTotal = mywebDriver.findElement(By.xpath("//table[@class='table cart-table data-th-table']/tfoot/tr/td/div[@class='cart-stotal']/span[2]")).getText();
				String cartsubTotal = driver.findElement("xpath","//table[@class='table cart-table data-th-table']/tfoot/tr/td/div[@class='cart-stotal']/span[2]").getText();
				System.out.println("The interface cartsubTotal name is:"+cartsubTotal);
				if(productName.trim().equalsIgnoreCase(cartproductName.trim()) && itemsCount.trim().equalsIgnoreCase(cartitemsCount.trim()) && subTotal.trim().equalsIgnoreCase(cartsubTotal.trim())){
					System.out.println("The productName, itemCount and cartsubtotal in the product page and cart page matches");
				}
				else{
					System.out.println("The productName, itemCount and cartsubtotal in the product page and cart page not matches");
				}
				cf.click_element("xpath","//a[text()='Go to Brief']");
				int projInfoStatus = cf.validateText("xpath", "//h2[text()='PROJECT INFORMATION']", "PROJECT INFORMATION");
				//String firstProdCategory = mywebDriver.findElement(By.id("pcategory")).getAttribute("value");
				String firstProdCategory = driver.findElement("id","pcategory").getAttribute("value");
				System.out.println("The interface firstProdCategory name is:"+firstProdCategory);
				//String firstProdName = mywebDriver.findElement(By.id("pproduct")).getAttribute("value");
				String firstProdName = driver.findElement("id","pproduct").getAttribute("value");
				System.out.println("The interface firstProdName name is:"+firstProdName);
				//String firstProdver = mywebDriver.findElement(By.id("pproduct")).getAttribute("value");
				if(categoriesType.trim().equalsIgnoreCase(firstProdCategory.trim()) && productName.trim().equalsIgnoreCase(firstProdName.trim())){
					System.out.println("The categoriesType, prodName from input mathces with projectInfo categoriesType & prodName matches");
				}
				else{
					System.out.println("The categoriesType, prodName from input mathces with projectInfo categoriesType & prodName not matches");
				}	
		}catch(Exception e){
    		e.printStackTrace();
    	}
		
	}
	
	//static String JobNo;
	public void adProGetJobNo(int row, int col,String filepath, String sheet){
		CommonFunctions cf = new CommonFunctions(driver);
		try{
			
			//String JobNo = mywebDriver.findElement(By.xpath(".//*[@id='accordion1']/tbody/tr[1]/td[1]/a")).getText();
			String JobNo = driver.findElement("xpath",".//*[@id='accordion1']/tbody/tr[1]/td[1]/a").getText();
			System.out.println("The job no:"+JobNo);
			cf.updateExcelSheet(row, col, filepath, sheet, JobNo.trim());
		}catch(Exception e){
    		e.printStackTrace();
    	}
	}

}
