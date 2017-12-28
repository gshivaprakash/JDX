package ad2pro.java.common.reporting;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import org.testng.Reporter;
import ad2pro.fw.utils.SeleniumHelper;
import ad2pro.fcn.tests.*;

/**
 * Class which is used to generate the HTML template report
 */
public class TemplateGenerator {

	HashMap<String,String> TestCaseExecutionDetails=new HashMap<String,String>();
	HashMap<String,String> testCasesDetails=new HashMap<String,String>();
	HashMap<String,List<String>> hMapResultDetails=new HashMap<String,List<String>>();

	private int total;
	private int passed;
	private int failed;
	private int skipped;
	private String ln = "\n";

	/**
	 * Constructor which is used to create the template generator object
	 */

	public TemplateGenerator(HashMap<String,List<String>> hMapResultDetails) {
	
		this.hMapResultDetails=hMapResultDetails;
	}

	/**
	 * Method to build the template. Takes the count of total , passed , failed
	 * cases as parameters
	 * 
	 * @param Total
	 * @param Passed
	 * @param Failed
	 */

	public void buildTemplate(int Total, int Passed, int Failed) 
	{

		total = Total;
		passed = Passed;
		failed = Failed;
		skipped = total - (passed+failed);
	
		String resultsType = "Regreesion Test Results";
		String strChart = passed + "," + failed + "," + skipped;
		String detailFileName = "";
		String chartDimensions = "";
		String chartMaxHeight = "";
		String arrCaseID = "";
		String arrCaseTitle = "";
		String arrCaseResult = "";

		if (total < 10) {
			chartDimensions = "0|5|10";
			chartMaxHeight = "10";
		} else if ((total >= 10) && (total < 20)) {
			chartDimensions = "0|5|10|15|20";
			chartMaxHeight = "20";
		} else if ((total >= 20) && (total < 50)) {
			chartDimensions = "0|10|20|30|40|50";
			chartMaxHeight = "50";
		} else if ((total >= 50) && (total < 100)) {
			chartDimensions = "0|20|40|60|80|100";
			chartMaxHeight = "100";
		} else if ((total >= 100) && (total < 200)) {
			chartDimensions = "0|40|80|120|160|200";
			chartMaxHeight = "200";
		} else if ((total >= 200) && (total < 300)) {
			chartDimensions = "0|50|100|150|200|250|300";
			chartMaxHeight = "300";
		} else if ((total >= 300) && (total < 400)) {
			chartDimensions = "0|80|160|240|320|400";
			chartMaxHeight = "400";
		} else if ((total >= 400) && (total < 500)) {
			chartDimensions = "0|100|200|300|400|500";
			chartMaxHeight = "500";
		} else if ((total >= 500) && (total < 800)) {
			chartDimensions = "0|160|320|480|640|800";
			chartMaxHeight = "800";
		} else if ((total >= 800) && (total < 1000)) {
			chartDimensions = "0|200|400|600|800|1000";
			chartMaxHeight = "1000";
		} else {
			System.out.println("Error: Invalid Chart Scale");
		}

		try {
			detailFileName=GlobalVars.testHTMLResultPath;
			System.out.println(detailFileName);
			
			writeDetailFile(detailFileName,resultsType,strChart, chartDimensions,
					chartMaxHeight, arrCaseID, arrCaseTitle, arrCaseResult);
		} catch (Exception e) {
            System.out.println("Error ::"+e.getMessage());
		}
	}

	/**
	 * Method used to create the template headers, footers & the generic
	 * structure
	 * @throws IOException 
	 */

	public void writeDetailFile(String name,String resultsType, String strChart, String chartDimensions,
			String chartMaxHeight, String arrCaseID, String arrCaseTitle,
			String arrCaseResult) throws IOException {

		
		//String tModuleName=CoreUtils.fGetNodeText(GlobalVars.locOfCtrlPrms+"MVUK_GlobModuleNames.xml","Module_BLK01",GlobalVars.strModCode);
		
		String appName="JDTraffic";
		String file = name;

		Date d = new Date();
		try {
			d = new SimpleDateFormat("MMddyy_HHmmss").parse("Mon Dec 01 08:36:08 IST 2014");
		} catch (Exception e) {
		}

		
		 String scrshotBuild="file:///";
		 System.out.println("Build Summary Screenshot Path ::"+scrshotBuild);
		 
		String headContent = "<html> "
				+ ln
				+ " <head>"
				+ ln
				+ " <style>"
				+ ln
				+ "	td.header {"
				+ ln
				+ " background-color:#bed744;border-top:0px solid #333333;border-bottom:1px dashed #000000;"
				+ ln
				+ "	}"
				+ " td.testDetails { "
				+ ln
				+ " background-color:#9a9c9e;border-top:5px solid #bed744;border-bottom:1px dashed #000000;"
				+ ln
				+ "	}"
				+ ln
				+ " span.testDetails {"
				+ ln
				+ " font-size:12px;font-weight:bold;color:#fff;line-height:200%;font-family:verdana;text-decoration:none;"
				+ ln
				+ "}"
				+ ln
				+ "td.execDetails { "
				+ ln
				+ " background-color:#bed744;border-top:5px solid #bed744;border-bottom:0px dashed #000000;"
				+ ln
				+ "}"
				+ ln
				+ " span.execDetails {"
				+ ln
				+ " font-size:12px;font-weight:bold;color:#fff;line-height:200%;font-family:verdana;text-decoration:none;"
				+ ln
				+ "}"
				+ ln
				+ "span.pass { "
				+ ln
				+ " font-size: 14px;font-weight:bold;line-height:100%;color:#00FF00;font-family:arial; "
				+ ln
				+ "	}"
				+ ln
				+ " span.fail { "
				+ ln
				+ " font-size: 14px;font-weight:bold;color:#FF0000;line-height:100%;font-family:arial; "
				+ ln
				+ " } "
				+ ln
				+ " span.skip { "
				+ ln
				+ " font-size: 14px;font-weight:bold;color:#0000FF;line-height:100%;font-family:arial; "
				+ ln
				+ " } "
				+ ln
				+ " span.title { "
				+ " font-size: 14px;font-weight:normal;color:#000000;line-height:100%;font-family:arial; "
				+ ln
				+ " } "
				+ ln
				+ " td.reqDetails { "
				+ ln
				+ " font-size:12px;font-weight:bold;color:#000000;line-height:100%;font-family:verdana;text-decoration:none; "
				+ ln
				+ " } "
				+ ln
				+ " td.reqData {  "
				+ ln
				+ " font-size:12px;color:#000000;line-height:100%;font-family:verdana;text-decoration:none; "
				+ ln
				+ " } "
				+ " tr.execution {  "
				+ ln
				+ " background-color:#555555; "
				+ ln
				+ " } "
				+ ln
				+"table#t01 tr:nth-child(even) {background-color: #eee;}table#t01 tr:nth-child(odd) {background-color:#fff;}table#t01 th	{background-color: black;color: white;}"
				+ " </style> "
				+ ln
				+ "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
				+ "<script type=\"text/javascript\">"
				+"google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
				+"google.setOnLoadCallback(drawChart);"
				+"function drawChart() {"
				+"var data = google.visualization.arrayToDataTable(["
				+"['Task', 'Test Cases'],"
				+"['Passed',"+passed+"],"
				+"['Failed',"+failed+"],"
				+"['Skipped',"+skipped+"],"
        		+"]);"
        		+"var options = {"
        		+"title: 'JD Regression Execution Details',"
        		+"pieSliceText: 'label',"
        		+"is3D: true,"
        		+"};"
        		+"var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));"
        		+"chart.draw(data, options);"
        		+"}"
        		+"</script></head> "
				+ ln
				+ "<body leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\" bgcolor='#FFFFFF'>";

		String header = "<div id=\"header\" > "
				+ ln
				+ " <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">"
				+ ln
				+ " <tr> "
				+ ln
				+ "<td align=\"left\" valign=\"middle\" class=\"header\"> "
				+ ln
				+ "<img id=\"editableImg1\""
				+ "src=\""
				+ GlobalVars.clientLogo
				+ "\" height=\"75px\" BORDER=\"0\" align=\"center\">"
				+ ln
				+ "</td>"
				+ ln
				+ "<td align=\"left\""
				+ " valign=\"middle\" class=\"header\">"
				+ ln
				+ "<span style=\"font-size:14px;font-weight:bold;color:#000000;line-"
				+ "height:200%;font-family:verdana;text-decoration:none;\">"
				+ ln
				+ "AUTOMATION TEST RESULTS"
				+ ln
				+ "</span>"
				+ ln
				+ "</td>"
				+ ln
				+ "<td align=\"\" valign=\"middle\" style=\"background-color:#3399FF;border-top:0px solid #00000;border-bottom:"
				+ "1px dashed #000000;\">"
				+ ln
				+ " <span style=\"font-size:15px;font-weight:bold;color:#000000;line-height:100%;font-family:verdana;"
				+ "text-decoration:none;\">" + ln + "</span>" + ln + "</td>"
				+ ln + " </tr>" + ln + "</table>" + ln + "</div>";

		String testDetails = "<div id=\"testDetails\">"
				+ ln
				+ "<table width=\"100%\" cellpadding=\"3\" cellspacing=\"0\"> "
				+ ln
				+ "<tr> "
				+ ln
				+ " <td align=\"left\" valign=\"middle\" class=\"testDetails\"> "
				+ ln + "<span  class=\"testDetails\">" + ln + " Date & Time : "
				+ d.toString() + ln + "</span>" + ln + "</td>" + ln
				+ "<td align=\"left\" "
				+ "valign=\"middle\" class=\"testDetails\" colspan=2> " + ln
				+ "<span  class=\"testDetails\"> " + ln
				+ "Application : <font color=\"#FFFFFF\">"+GlobalVars.strApplicationName
				+ " </font> " + ln + " </span>" + ln + " </td> " + ln
				+ "<td align=\"left\" "
				+ "valign=\"middle\" class=\"testDetails\" colspan=2> " + ln
				+ "<span  class=\"testDetails\"> " + ln
				+ "Module Name : <font color=\"#FFFFFF\">"+appName
				+ " </font> " + ln + " </span>" + ln + " </td> " + ln
				+ "<td align=\"left\" valign=\"middle\" class=\"testDetails\">"
				+ ln + "<span  class=\"testDetails\">" + ln + "Test Type :<font color=\"#FFFFFF\"> "
				+ GlobalVars.strTestType + ln + " </font></span> " + ln + " </td> " + ln
				+ " </tr>" + ln + " </table> " + ln + "</div>";

		String execDetails = "<div id=\"execDetails\"> "
				+ ln
				+ "<table width=\"100%\" cellpadding=\"3\" cellspacing=\"0\"> "
				+ ln
				+ "  <tr> "
				+ ln
				+ "<td align=\"left\" valign=\"middle\" class=\"execDetails\">"
				+ ln
				+ "<span class=\"execDetails\">"
				+ ln
				+ "Test Cases Executed : "
				+ total
				+ "</span>"
				+ ln
				+ "</td>"
				+ ln
				+ "	<td align=\"left\" valign=\"middle\" class=\"execDetails\">"
				+ ln + "<span class=\"execDetails\">" + ln + "Passed : "
				+ passed + "</span>" + ln + "</td>" + ln
				+ "<td align=\"left\" valign=\"middle\" class=\"execDetails\">"
				+ ln + "<span class=\"execDetails\">" + ln + "Failed :"
				+ failed + "</span>" + ln + "</td>" + ln
				+ "<td align=\"left\" valign=\"middle\" class=\"execDetails\">"
				+ ln + "<span class=\"execDetails\">" + ln + "Skipped : "
				+ skipped + "</span>" + ln + "</td>" + ln
				+ "<td align=\"left\" valign=\"middle\" class=\"execDetails\">"
				+ ln + "<span class=\"execDetails\">" + ln + "Browser: "
				+ BaseTest.browserType + "</span>" + ln + "</td>" + ln + "</tr>" + ln
				+ "</table>" + ln + "</div> <br/>";

		String graph = "<div id=\"graph\"  style=\"padding-left:10px\" > "
				+ ln
				+ "<table width=\"100%\" cellpadding=\"0\"  border=\"0\" cellspacing=\"0\" bgcolor='#FFFFFF'> "
				+ ln
				+ "<tr valign=\"center\"> "
				+ ln
				+ "<td bgcolor=\"#FFFFFF\" >"
				+ ln
				+ "<IMG id=\"graph\" style=\"width: 500px; height: 300px;\" SRC=\"http://chart.apis.google.com/chart?"
				+ "cht=bvg&chs=350x175&chd=t:"
				+ strChart
				+ "&chds=0,"
				+ chartMaxHeight
				+ "&chxt=x,y&chxs=0,000000,12|1,000000,12&chco=00FF00|FF0000|0000FF|FFFF00&chbh=50,0,20&"
				+ "chxl=0:|Passed|Failed|Skipped|1:|" + chartDimensions
				+ "&chg=25,16.667,2,5&chtt=Total+Test+Cases+=+" + total
				+ "&chts=000000,15\" BORDER=\"0\" align=\"left\">" + ln
				+"</td><td><div id=\"piechart_3d\" style=\"width: 500px; height: 300px;\" ></div>"
				+ "</td>" + ln + "</tr>" + ln + "</table>" + ln + "</div>" + ln
				+ "<br/> ";

		
		String buildSummary="<div id=\"buildSummary\" style=\"padding-left:15px\"><span style=\"font-size: 15px;font-weight:bold;color:#000000;font-family:arial;\">Build Summary Details ::</span><p/>"
				+ln
				+"<table id=\"t01\" style=\"width:100%\" border=\"5\"><tr style=\"text-align:center\"><th>Environment</th><th>Application URL's</th><th>Version</th><th>Build No</th></tr><tr style=\"text-align:center\"><td>"+GlobalVars.strEnv+"</td><td><a href=\""+BaseTest.appURL+"\"  target=\"_blank\">"+BaseTest.appURL+"</td><td>"+GlobalVars.strVersion+"</td><td>"+GlobalVars.strBuild+"</td></tr></table> </div>";

		
		String testCasesExecutionDetails = "<div id=\"testcaseDetails\" style=\"padding-left:15px\">"
				+ ln
				+ " <p> "
				+ "<span style=\"font-size: 15px;font-weight:bold;color:#000000;font-family:arial;\">"+GlobalVars.strApplicationName+" Test Cases Execution Details ::</span><p/>"
				+ ln+"<table id=\"t01\" style=\"width:100%\" border=\"5\"><tr  class=\"execution\"><th>SNo</th><th>Module Name</th><th>Test Case Name</th><th>Result</th><th>Error Message</th><th>Screenshot</th><th>Time Stamp</th><th>Time Taken (in Seconds)</th></tr>";

	/*	String ReporterLogs="<div id=\"ReporterLogs\" style=\"padding-left:15px\"><span style=\"font-size: 15px;font-weight:bold;color:#000000;font-family:arial;\">ReporterLogs ::</span><p/>"
				+ln
				+"<table id=\"t01\" style=\"width:100%\" border=\"5\"><tr style=\"text-align:center\"><th>CurrentTestResult</th></tr><tr style=\"text-align:center\"><td><br>"+Reporter.getOutput()+"</br></td></tr></table> </div>";
*/
		
		
		
		
		//</th><th>Output Reports</th></tr><tr style=\"text-align:center\"><td>"+Reporter.getCurrentTestResult()+"</td><td><a href=\""+Reporter.getOutput()+"</td></tr></table> </div>";
		
		
		
		
		
		
		
		try {
			
			System.out.println("Hash Map Size ::"+hMapResultDetails.size());
		     for(int row=1;row<=hMapResultDetails.size();row++)
		     {
		    	 List<String> resultDetails=hMapResultDetails.get("Row"+row);
		    	 
		    	 resultDetails.add("test");
		    	 String SNo=resultDetails.get(0);
		    	 String tcId=resultDetails.get(1);
		    	 String tcTitle=resultDetails.get(2);
		    	 String tcResult=resultDetails.get(3);
		    	 String tcErrorMes=resultDetails.get(4);
		    	 String screenshotPath="file:///"+resultDetails.get(5);
		    	 String tcTimeStamp=resultDetails.get(6);
		    	 String tcTimeTaken=resultDetails.get(7);
		    	
		    	 System.out.println("Screenshot    Path ::"+screenshotPath);
		    	 
		    	 if(tcResult.equals("Pass"))
		    		 testCasesExecutionDetails=testCasesExecutionDetails+"<tr><td>"+SNo+"</td><td>"+tcId+"</td><td>"+tcTitle+"</td><td><span class=\"pass\">"+tcResult+"</span></td><td>&nbsp</td><td>&nbsp</td><td>"+tcTimeStamp+"</td><td>"+tcTimeTaken+"</td></tr>";
		    	 else if(tcResult.equals("Fail"))
		    	 	 testCasesExecutionDetails=testCasesExecutionDetails+"<tr><td>"+SNo+"</td><td>"+tcId+"</td><td>"+tcTitle+"</td><td><span class=\"fail\">"+tcResult+"</span></td><td>"+tcErrorMes+"</td><td><a href=\""+screenshotPath+"\"  target=\"_blank\">Click Here</a></td><td>"+tcTimeStamp+"</td><td>"+tcTimeTaken+"</td></tr>";
		    	 else
		    		 testCasesExecutionDetails=testCasesExecutionDetails+"<tr><td>"+SNo+"</td><td>"+tcId+"</td><td>"+tcTitle+"</td><td><span class=\"skip\">"+tcResult+"</span></td><td>&nbsp</td><td>&nbsp</td><td>"+tcTimeStamp+"</td><td>"+tcTimeTaken+"</td></tr>";
		     }
		     
			
		
		} catch (Exception e) {
			System.out.println("Error message "+e.getMessage());
		}

		

		testCasesExecutionDetails=testCasesExecutionDetails+"</table></div>";
		BufferedWriter out = null;
		
		try {
			out = new BufferedWriter(new FileWriter(file));
			out.write(headContent);
			out.write(header);
			out.write(testDetails);
			out.write(execDetails);
			out.write(graph);
			out.write(buildSummary);
			out.write(testCasesExecutionDetails);
			//out.write(ReporterLogs);
			out.write("");
			out.write("</body>" + ln + "</html>");
		
		} catch (Exception e) {
			System.err.println(" Exception while generating html report");
			System.out.println(e.getMessage());
		}finally{
			try{
				out.close();
			}catch(Exception e){}
		}
	}

}