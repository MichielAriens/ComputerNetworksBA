package common;

import java.io.File;

import server.ConnectionHandler;


public class Response extends Exchange {
	
	

	public Response(String initialLine, ConnectionHandler parent) {
		super(initialLine);
	}
	
	
	public static Response getResponseTo(Request request){
		if (request.getHttpCommand().equals("GET")){
			return getResponseToGET(request);
		}else{
			return null;
		}
	}


	private static Response getResponseToGET(Request request) {
		File file = new File(request.getPath());
		if(file.canRead()){
			
		}else{
			Response response = new Response(request.getMode() + " 404 Not Found", request.parent);
			response.headers += "\r\n\r\n";
		}
		
		
		return null;
	}

}
