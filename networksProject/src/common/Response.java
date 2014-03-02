package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
		}else{
			return null;
		}
	}


	private static Response getResponseToGET(Request request, ConnectionHandler handler) {
		Response response = null;
		File file = new File(request.getPath());
		try{
			String contents = "";
			String additional = "";
			BufferedReader reader = new BufferedReader(new FileReader(file));
			boolean done = false;
			while(additional != null){
				additional = reader.readLine();
				contents += "\n" + additional;
			}
			response = new Response(request.getMode() + " 200 OK", handler);
			response.body = contents;
			
				
		}
		catch(FileNotFoundException e){
			response = new Response(request.getMode() + " 404 Not Found", request.parent);
		} catch (IOException e) {
			response = new Response(request.getMode() + " 404 Not Found", request.parent);
		}
		return response;
	}

}
