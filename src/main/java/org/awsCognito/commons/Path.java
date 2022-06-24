package org.awsCognito.commons;

public class Path {

	public String getPath(String fileName) {
		String path = this.getClass().getClassLoader().getResource("application.properties").getPath().substring(1)
				.replace("/target/classes/application.properties", "/").replace("/", "//")
				+ "src//main//resources//"+ fileName +".properties";
		System.out.println(path);
		
		return path;
	}

}
