package me.theofrancisco;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class AppProperties {

	public String getLastSetDirectory () {
		Properties properties = new Properties();
		InputStream inputStream = null;
		
		try{
			inputStream = new FileInputStream("resources\\config.properties");
			
			//load properties file
			
			properties.load (inputStream);
			return properties.getProperty("lastSetDirectory");
		}catch (IOException e){
						System.err.println(e.getMessage());
			return null;
		}
	}
	
	public void setOpenedDirectory (String path){
		Properties properties = new Properties();
		OutputStream outputStream = null;
		
		try{
			outputStream = new FileOutputStream ("resources\\config.properties");
			properties.setProperty("lastSetDirectory", path);
			
			// save properties to project root folder
			properties.store(outputStream , null );
		}catch (IOException e){
			System.err.println(e.getMessage());			
		}
	}
}
