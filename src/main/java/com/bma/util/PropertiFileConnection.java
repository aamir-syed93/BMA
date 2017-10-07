package com.bma.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiFileConnection {
	
	
	public Properties getPropValues() throws IOException {
		
		Properties prop = new Properties();
		String propFileName = "mesageConfig.properties";
 
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
 
 
	
		return prop;
	}
	
	
	

}
