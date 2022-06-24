package org.awsCognito.commons;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesCache {

	private final Properties configProp = new Properties();

	public PropertiesCache(String path) throws FileNotFoundException {
		// Private constructor to restrict new instances
		InputStream in = new FileInputStream(path);
		System.out.println("Reading all properties from the file");
		try {
			configProp.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	public String getProperty(String key) {
		return configProp.getProperty(key);
	}

	public Set<String> getAllPropertyNames() {
		return configProp.stringPropertyNames();
	}

	public boolean containsKey(String key) {
		return configProp.containsKey(key);
	}

	public void setProperty(String key, String value) {
		configProp.setProperty(key, value);
	}

	public void flush(String path) throws FileNotFoundException, IOException {
//		String path = this.getClass().getClassLoader().getResource("application.properties").getPath().substring(1).replace("/", "//");
		try (final OutputStream outputstream = new FileOutputStream(path);) {
			System.out.println(path);
			configProp.store(outputstream, "File Updated");
			outputstream.close();
		}
	}
}
