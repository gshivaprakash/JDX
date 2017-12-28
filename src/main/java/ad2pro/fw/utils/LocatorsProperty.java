package ad2pro.fw.utils;

/**
 * This class consist of getter/setter methods to set "Pass","Fail","AutomationFail" execution status
 */

public class LocatorsProperty {

	private int pass;
	private int fail;
	private int automationFail;
	
	public int getStatus() {
		return this.pass;
	}

	public void setStatus(int pass) {
		this.pass = pass;
	}
	
	public int getFailStatus() {
		return this.fail;
	}

	public void setFailStatus(int fail) {
		this.fail = fail;
	}
	
	public int getautomationStatus() {
		return this.automationFail;
	}

	public void setautomationStatus(int automationFail) {
		this.automationFail = automationFail;
	}


}

