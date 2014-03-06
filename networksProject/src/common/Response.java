package common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;

import server.ConnectionHandler;


public class Response extends Exchange {
	
	

	public Response(String initialLine, ConnectionHandler parent) {
		super(initialLine);
	}
	
	
	
	
	/**
	 * END OF NON STATIC METHODS
	 */
	
	public static Response getResponseTo(Request request, ConnectionHandler handler){
		if (request.getHttpCommand().equals("GET")){
			return getResponseToGET(request, handler);
		}else if(request.getHttpCommand().equals("HEAD")){
			return getResponseToHEAD(request, handler);
		}else if(request.getHttpCommand().equals("POST")){
			return getResponseToPOST(request, handler);
		}else if(request.getHttpCommand().equals("PUT")){
			return getResponseToPUT(request, handler);
		}else{
			return null;
		}
	}
	
	
	private static Response getResponseToPOST(Request request, ConnectionHandler handler) {
			Response res = new Response(request.getMode() + " 200 OK", handler);
			System.out.println("      > POST: " + request.getBody());
			return res;
		}

	private static Response getResponseToHEAD(Request request, ConnectionHandler handler) {
		File file = new File(request.getPath());
		if(file.canRead()){
			Response res = new Response(request.getMode() + " 200 OK", handler);
			res.headers += "Content-Length: " + file.length() + "\n"; 
			return res;
		}else{
			return new Response(request.getMode() + " 404 Not Found", request.parent);
		}
	}




	private static Response getResponseToPUT(Request request, ConnectionHandler handler) {
		try {
			File file = new File(request.getPath());
			if(!file.isFile()){
				Response res = new Response(request.getMode() + " 200 OK", handler);
				FileOutputStream out = new FileOutputStream(request.getPath());
				InputStream in = new ByteArrayInputStream( request.getBody().getBytes( ) );
				linkStreams(in, out);
				return res;
			}else{
				return new Response(request.getMode() + " 404 Not Found", request.parent);
			}
		} catch (IOException e) {
			return new Response(request.getMode() + " 404 Not Found", request.parent);
		}
	}
	
	private static Response getResponseToGET(Request request, ConnectionHandler handler) {
		File file = new File(request.getPath());
		if(file.canRead()){
			Response res = new Response(request.getMode() + " 200 OK", handler);
			res.body = request.getPath();
			res.headers += "\nContent-Length: " + file.length() + "\n"; 
			return res;
		}else{
			return new Response(request.getMode() + " 404 Not Found", request.parent);
		}
		
	}
	
	public static void linkWriteToStream(Reader input, OutputStream output)
		    throws IOException
		{
		    byte[] buffer = new byte[1024]; // Adjust if you want
		    int bytesRead;
		    while ((bytesRead = input.read()) != -1)
		    {
		        output.write(buffer, 0, bytesRead);
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
