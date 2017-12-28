package ad2pro.fw.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class helps to return the config.properties file info based on the key value you pass
 */

public class PropertiesFileParser {
	public String getProperty(String key) throws IOException {
		Properties prop = new Properties();
		InputStream input = null;

		String filename = System.getProperty("user.dir")+"/config.properties";
		input = new FileInputStream(filename);
		//System.out.println(input);
		prop.load(input);
		return prop.getProperty(key);
	}
}
