package org.awsCognito.commons;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Example {

	public static void main(String[] args) {
	
		System.out.println(System.getProperty("ADAPTOR_HOME"));
		method();
	}
	
	public static void method() {
		Path hello = new Path();
		String path = hello.getPath("project");
		try {
			PropertiesCache cache = new PropertiesCache(path);
			
			System.out.println(cache.getProperty("hi"));
			cache.setProperty("hi", "hello");
			
			cache.flush(path);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
