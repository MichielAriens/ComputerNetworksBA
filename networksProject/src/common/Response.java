package common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import server.ConnectionHandler;


public class Response extends Exchange {
	
	//All supported image formats: Supported means we'll send a "Content-type: image" header.
	public static final List<String> IMAGEFORMATS;

	static{
		String[] arr = {"jpg","bmp","png","gif"};
		IMAGEFORMATS = Arrays.asList(arr);
	}
	
	
	private ConnectionHandler parent;
	
	/**
	 * Basic contructor
	 * @param initialLine
	 * @param parent
	 */
	public Response(String initialLine, ConnectionHandler parent) {
		super(initialLine);
		this.parent = parent;
	}
	
	
	
	
	/**
	 * END OF NON STATIC METHODS
	 */
	
	/**
	 * Given a request this method calls the correct method to handle the creation of the response object.
	 * @param request
	 * @param handler
	 * @return
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
	
	/**
	 * Given a POST request, this method actuates the request and creates an appropriate response object 
	 * 
	 * POST request input is printed to the commandline. 
	 * @param request	a POST request
	 * @param handler	a connectionHandler
	 * @return			appropriate response object
	 */
	private static Response getResponseToPOST(Request request, ConnectionHandler handler) {
			Response res = new Response(request.getMode() + " 200 OK", handler);
			System.out.println("      > POST: " + request.getBody());
			return res;
		}

	/**
	 * Given a HEAD request, this method actuates the request and creates an appropriate response object 
	 * 
	 * POST request input is printed to the commandline. 
	 * @param request	a HEAD request
	 * @param handler	a connectionHandler
	 * @return			appropriate response object
	 */
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



	/**
	 * Given a PUT request, this method actuates the request and creates an appropriate response object 
	 * 
	 * The body of the PUT requst is saved in a file marked by path if possible. 403 Forbidden is returned if permissions are lacking.
	 * 
	 * POST request input is printed to the commandline. 
	 * @param request	a PUT request
	 * @param handler	a connectionHandler
	 * @return			appropriate response object
	 */
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
				return new Response(request.getMode() + " 403 Forbidden", request.parent);
			}
		} catch (IOException e) {
			return new Response(request.getMode() + " 500 Server Error", request.parent);
		}
	}
	
	/**
	 * Given a GET request, this method actuates the request and creates an appropriate response object 
	 * 
	 * The content length is acquired from the filesystem and incorporated in the headers.
	 * The body of the response is a link to the file to load and is read in the Exchange printTo(...) method
	 * 404 Not Found is returned if the file does not exist or can't be read.
	 * 
	 * POST request input is printed to the commandline. 
	 * @param request	a PUT request
	 * @param handler	a connectionHandler
	 * @return			appropriate response object
	 */
	private static Response getResponseToGET(Request request, ConnectionHandler handler) {
		File file = new File(request.getPath());
		if(file.canRead()){
			Response res = new Response(request.getMode() + " 200 OK", handler);
			res.body = request.getPath();
			long contentLength = file.length();
			res.headers += "Content-Length: " + contentLength + "\n";
			if(isImage(request.getPath())){
				res.headers += "Content-Type: image";
			}
			return res;
		}else{
			return new Response(request.getMode() + " 404 Not Found", request.parent);
		}
		
	}
	
	/**
	 * Checks whether the "Content-Type: image" header is appropriate. 
	 * @param path
	 * @return	true <=> IMAGEFORMATS contains the extention of path.
	 */
	private static boolean isImage(String path) {
		for(String type : IMAGEFORMATS){
			if(path.endsWith(type)){
				return true;
			}
		}
		return false;
	}

}
