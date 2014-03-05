package common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

import server.ConnectionHandler;

public abstract class Exchange {
	
	public String initialLine; //Initial response line		
	public String headers = "Host: localhost:54321";			
	public String body = "";
	public Exchange(String initialLine){
		this.initialLine = initialLine;
	}
	
	

	
	
	public void printTo(ConnectionHandler handler){
		try{
			handler.writeLine(initialLine);
			
			BufferedReader reader = new BufferedReader(new StringReader(headers));
			String line = "";
			while((line = reader.readLine()) != null){
				handler.writeLine(line);
			}
			handler.writeLine("");
			
			reader = new BufferedReader(new StringReader(body));
			line = "";
			while((line = reader.readLine()) != null){
				handler.writeLine(line);
			}
			if(body != ""){
				handler.writeLine("");
			}
			handler.flush();
		}catch(IOException e){
			
		}
	}
}
