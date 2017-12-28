package ad2pro.java.common.reporting;

import java.io.File;

public class CoreUtils {
	
	public static void createDir(String dirName){
		File f = new File(dirName);
		try {
			if (!f.exists()) {
				f.mkdir();
				System.out.println("Directory Created :: " +dirName);
			}
		} catch (Throwable e) {
			System.out.println("Unable to create directory  '" +dirName+"'");
		}

	}

}







