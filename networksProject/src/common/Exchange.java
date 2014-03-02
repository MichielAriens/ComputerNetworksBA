package common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

public abstract class Exchange {
	


	public String initialLine; //Initial response line		
	public String headers = "";			
	public String body = "";
	public Exchange(String initialLine){
		this.initialLine = initialLine;
	}

	
	
	public void printTo(BufferedWriter writer){
		try{
			writer.write(initialLine);
			writer.newLine();
			
			BufferedReader reader = new BufferedReader(new StringReader(headers));
			String line = "";
			while((line = reader.readLine()) != null){
				writer.write(line);
				writer.newLine();
			}
			writer.newLine();
			
			reader = new BufferedReader(new StringReader(body));
			line = "";
			while((line = reader.readLine()) != null){
				writer.write(line);
				writer.newLine();
			}
			writer.newLine();
			
			writer.flush();
		}catch(IOException e){
			
		}
	}
}
