package com.sonly.adaboost;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Description: to read file or write file 
 * @author: Sonly
 * Company: HUST 
 * @date: 2017年5月13日上午8:51:30
 */
public class FileOperator {

	/**
	 * read file by line and save to List<String>
	 * @param path
	 * @return List<String[]>
	 * @throws IOException
	 */
	public static List<String[]> readFile(String path) throws IOException {
		List<String[]> lines = new ArrayList<>();
		File file = new File(path);
		
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = new String();
			while((line = reader.readLine()) != null) {
				lines.add(line.split("\\s+"));
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		return lines;	
	}
	
	/**
	 * write to file
	 * @param strings
	 * @param path
	 * @throws IOException
	 */
	public static void writeFile(List<String> strings, String path) throws IOException {
		File file = new File(path);
		
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
		BufferedWriter writer = new BufferedWriter(outputStreamWriter);
		
		for(String string : strings) {
			writer.write(string);
			writer.newLine();
		}
		writer.close();
	}
}
