package ad2pro.java.common.reporting;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


/**
 * This is used to write the data into an Excel sheet
 */


public class ExportTestResults {

	private static SimpleDateFormat strReportFileName = new SimpleDateFormat("MMddyy_HHmmss");
	HSSFWorkbook wb = new HSSFWorkbook();

	String fileName = null;
	String htmlFileName = null;
	

	FileOutputStream fileOut = null;
	InputStream inputStream = null;

	Calendar cal = Calendar.getInstance();
	public final String dateTime1 = "EEE MMM dd hh:mm:ss z yyyy";
	Properties props = new Properties();
	public String testResultPath;
	public String testHTMLResultPath;
	int headings = 0;

	/**
	 * This method is to write the data into Excel Sheet(Test Results Header)
	 * 
	 * @param result
	 */
	public void exportExcelHeader(String strModCode) {
		try {

			if (headings == 0) {

				String timeStamp = strReportFileName.format(new Date());

			    fileName = GlobalVars.strTestType+"_"+strModCode+"_Test Results_"+timeStamp+".xls";
				htmlFileName =GlobalVars.strTestType+"_"+strModCode+"_Test Results"+timeStamp+".html";
				testResultPath = "./Reports";
				CoreUtils.createDir(testResultPath);
				
				
				try{
					
					testResultPath=testResultPath+"//"+strModCode;
					CoreUtils.createDir(testResultPath);
					
					File f = new File(testResultPath);
					if(!f.exists())
						f.mkdir();
					
					
					
		
				}catch(Exception e){
					System.out.println("Unable to create DIR for Testresults Application"+e.getMessage());
				}
				
				
				testHTMLResultPath = testResultPath+ "//" + htmlFileName;
				testResultPath = testResultPath + "//"+ fileName;
				
				
				
				GlobalVars.testHTMLResultPath=testHTMLResultPath;
				GlobalVars.TestResultsPath=testResultPath;
				System.out.println("OUT FILE : " + testResultPath);

				wb = new HSSFWorkbook();
				wb.createSheet("Test Result");
				fileOut = new FileOutputStream(testResultPath);
				System.out.println("Test Result file is created");
				HSSFSheet sheet = wb.getSheetAt(0);
				sheet.setAutobreaks(false);

				HSSFRow row = sheet.createRow(0);
				HSSFFont font = wb.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				HSSFCellStyle cellStyle = wb.createCellStyle();
				cellStyle.setFont(font);

				// Setting Headings in Test Results file
				row.createCell(0).setCellStyle(cellStyle);
				row.createCell(1).setCellStyle(cellStyle);
				row.createCell(2).setCellStyle(cellStyle);
				row.createCell(3).setCellStyle(cellStyle);
				row.createCell(4).setCellStyle(cellStyle);
				row.createCell(5).setCellStyle(cellStyle);
				row.createCell(6).setCellStyle(cellStyle);
				row.createCell(7).setCellStyle(cellStyle);

				row.createCell(0).setCellValue("SNo");
				row.createCell(1).setCellValue("Test Case ID");
				row.createCell(2).setCellValue("Test Case Title");
				row.createCell(3).setCellValue("Result(P/F)");
				row.createCell(4).setCellValue("Error Message");
				row.createCell(5).setCellValue("Screenshot Path");
				row.createCell(6).setCellValue("Time Stamp");
				row.createCell(7).setCellValue("Time Taken (in Seconds)");
				
				headings = 1;
			}
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			System.out.println(" Unable to create headers in Excel report"+e.getMessage());
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This is used to write the test case results into an Excel sheet after
	 * completing the each test case execution
	 * 
	 * @throws IOException
	 */
	public void exportExcelRows(List<String> result) {
		try {
			
			
		
			testResultPath=GlobalVars.TestResultsPath;
			
			System.out.println("testResultPath = " + testResultPath);
			
		
			
			inputStream = new FileInputStream(testResultPath);
			POIFSFileSystem fs = new POIFSFileSystem(inputStream);
			fileOut = new FileOutputStream(testResultPath);
			HSSFWorkbook wb = new HSSFWorkbook(fs);

			HSSFSheet sheet = wb.getSheetAt(0);
			sheet.setAutobreaks(false);
			System.out.println("result.size() = " + result.size());
			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();
			System.out.println("No of rows in sheet=" + rows);
			/*for (int ii = 0; ii < result.size(); ii++)
				System.out.println("Result = " + result.get(ii));
*/
			HSSFRow row = sheet.createRow(rows);
			
			//result.remove(5);
			
			for (int i = 0; i <=7; i++) {
				sheet.setColumnWidth(0, (short) (256 * 5));
				sheet.setColumnWidth(1, (short) (256 * 25));
				sheet.setColumnWidth(2, (short) (256 * 30));
				sheet.setColumnWidth(3, (short) (256 * 30));
				sheet.setColumnWidth(4, (short) (256 * 28));
				sheet.setColumnWidth(5, (short) (256 * 28));
				sheet.setColumnWidth(6, (short) (256 * 28));
				
				
					HSSFCell cell = row.createCell(i);
					System.out.println("..in export.." + result.get(i).toString());
					HSSFRichTextString str = new HSSFRichTextString(result.get(i).toString());
					cell.setCellValue(str);
				
				
				
			}
		
				
			wb.write(fileOut);
			fileOut.close();
		} catch(Exception e){
			System.out.println("Exception occured while experting results list to excel "+e.getMessage());
			System.out.println(e.getMessage());
		} 
	}

	/**
	 * This is used to write the Test Summary into an Test Results Excel Sheet
	 * after completion of total test cases execution.
	 */

	public void exportTestSummary(int total,int passed,int failed) {
		try {
			
			testResultPath=GlobalVars.TestResultsPath;
			inputStream = new FileInputStream(testResultPath);
			POIFSFileSystem fs = new POIFSFileSystem(inputStream);
			fileOut = new FileOutputStream(testResultPath);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			sheet.setAutobreaks(false);
			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();
			for (int ii = 0; ii <=4; ii++) {

				HSSFRow row = sheet.createRow(rows + ii + 1);
				HSSFCell cell;
				HSSFRichTextString str;

				switch (ii) {
				case 0:
					cell = row.createCell(1);
					cell.setCellValue("Browser Tested");
					cell = row.createCell(2);
					str = new HSSFRichTextString(BaseTest.browserType);
					cell.setCellValue(str);
					break;
				case 1:
					cell = row.createCell(1);
					cell.setCellValue("Total Cases Executed");
					cell = row.createCell(2);
					str = new HSSFRichTextString(Integer.toString(total));
					cell.setCellValue(str);
					break;
				case 2:
					cell = row.createCell(1);
					cell.setCellValue("Total Cases Passed");
					cell = row.createCell(2);
					str = new HSSFRichTextString(Integer.toString(passed));
					cell.setCellValue(str);
					break;
				case 3:
					cell = row.createCell(1);
					cell.setCellValue("Total Cases Failed");
					cell = row.createCell(2);
					str = new HSSFRichTextString(Integer.toString(failed));
					cell.setCellValue(str);
					break;
				case 4:
					cell = row.createCell(1);
					cell.setCellValue("Total Cases Skipped");
					cell = row.createCell(2);
					str = new HSSFRichTextString(Integer.toString((total-(passed+failed))));
					cell.setCellValue(str);
					break;
				}
			}

			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			System.out.println("Exception while exporting the testresults Summary");
			System.out.println(e.getMessage());
		} finally {
			try {
				fileOut.close();
			} catch (Exception e) {
				System.out.println("Exception while exporting the testresults Summary");
				System.out.println(e.getMessage());
			}
		}
	}

	
	

}
