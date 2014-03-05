package common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Scanner;

import server.ConnectionHandler;

public abstract class Exchange {
	
	public boolean image = false;
	public String initialLine; //Initial response line		
	public String headers = "Host: localhost:54321\nServer: Waitress 1.0\n";		
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
			
			if(body != ""){
				handler.writer.flush();
				InputStream input = new FileInputStream(body);
				linkStreams(input, handler.soc.getOutputStream());
				handler.writeLine("");
			}
			handler.flush();
		}catch(IOException e){
			
		}
	}
	
	public static void linkStreams(InputStream input, OutputStream output)
		    throws IOException
		{
		    byte[] buffer = new byte[1024]; // Adjust if you want
		    int bytesRead;
		    while ((bytesRead = input.read(buffer)) != -1)
		    {
		        output.write(buffer, 0, bytesRead);
		    }
		}
}
