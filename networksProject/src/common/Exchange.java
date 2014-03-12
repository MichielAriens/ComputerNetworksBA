package common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import server.ConnectionHandler;

public abstract class Exchange {
	
	public boolean image = false;
	public String initialLine; //Initial request/response line		
	public String headers = "Host: localhost:54321\nServer: Waitress 1.0\n";		
	public String body = "";
	public Exchange(String initialLine){
		this.initialLine = initialLine;
	}
	
	

	
	/**
	 * Prints an exchange to the writer of the handler passed.
	 * @param
	 */
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
				//Hijack the handler's socket's outputstream.
				InputStream input = new FileInputStream(body);
				linkStreams(input, handler.soc.getOutputStream());
				handler.writeLine("");handler.writeLine("");
			}
			handler.flush();
		}catch(IOException e){
			
		}
	}
	
	/**
	 * Writes from an inputstream into an outputstream until the inputstream is finished. 
	 * @param input
	 * @param output
	 * @throws IOException
	 */
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
