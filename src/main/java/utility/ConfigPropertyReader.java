package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigPropertyReader {
	public static Properties getPropertyObjects() throws IOException {
		
		InputStream fis = new FileInputStream("path of config.properties file");
		
		Properties prop = new Properties();
		
		prop.load(fis);
		
		return prop;
	}
	
	public static String getUrl() throws IOException {
		
		return getPropertyObjects().getProperty("dburl");
		
	}

	public static String getUsername() throws IOException {
		
		return getPropertyObjects().getProperty("username");
		
	}

	public static String getPassword() throws IOException {
	
		return getPropertyObjects().getProperty("password");
	
}

}
