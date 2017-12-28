package ad2pro.fw.testListeners;

import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
/**
 * This class helps perform TestListener functions and pass the status in the logger file
 */
public class TestListener implements ITestListener
{
	
	public void onFinish(ITestContext result)
	{
		Set<ITestResult> failedTests = result.getFailedTests().getAllResults();
		for (ITestResult temp : failedTests) 
		{
			ITestNGMethod method = temp.getMethod();
			if (result.getFailedTests().getResults(method).size() > 1) 
			{
				failedTests.remove(temp);
			} 
			else 
			{
				if (result.getPassedTests().getResults(method).size() > 0) 
				{
					failedTests.remove(temp);
				}
			}
		}
	}

	public void onStart(ITestContext result)
	{
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) 
	{
		
	}

	public void onTestFailure(ITestResult result) 
	{
		System.out.println("***** Error "+result.getName()+" test has failed *****");
    	String methodName=result.getName().toString().trim();
   
	}

	public void onTestSkipped(ITestResult result) 
	{
		
	}

	public void onTestStart(ITestResult result) 
	{
		
	}

	public void onTestSuccess(ITestResult result) 
	{
		
	}

}
