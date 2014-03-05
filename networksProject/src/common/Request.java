package common;

import java.io.BufferedReader;
import java.util.Date;
import java.util.HashMap;

import server.BasicHTTPServer;
import server.ConnectionHandler;

public class Request extends Exchange{

	public final ConnectionHandler parent;
	
	private boolean grown = false;
	
	private String httpCommand; 	
	private String path;			
	private String mode;			
	private String headers;			
	private String body;			
	
	
	public Request(String initialLine, ConnectionHandler connectionHandler) throws IllegalExchangeException {
		super(initialLine);
		this.parent = connectionHandler;
		String[] parts = initialLine.split(" ");
		if(parts.length != 3){
			throw new IllegalExchangeException();
		}
		this.httpCommand = parts[0];
		this.path = parts[1];
		this.mode = parts[2];
		
		if(path.startsWith("/")){
			path = path.substring(1);
		}
	}
	

	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}



	public String getMode() {
		return mode;
	}



	public void setMode(String mode) {
		this.mode = mode;
	}



	public String getHeaders() {
		return headers;
	}



	public void setHeaders(String headers) {
		this.headers = headers;
	}



	public String getBody() {
		return body;
	}



	public void setBody(String body) {
		this.body = body;
	}



	public String getHttpCommand() {
		return httpCommand;
	}



	public void setHttpCommand(String com){
		this.httpCommand = com;
	}

	private String headers(){
		return null;
	}



	/**
	 * 
	 */
	public void grow() {
		int emptyLines = 0;
		String nextLine;
		//Read headers.
		while(emptyLines < 1){
			nextLine = parent.readLine();
			if(nextLine.equals("")){
				emptyLines++;
			}else{
				headers += "\n" + nextLine;
			}
		}while(emptyLines < 2 && this.hasBody()){
			nextLine = parent.readLine();
			if(nextLine.equals("")){
				emptyLines++;
			}else{
				body += "\n" + nextLine;
			}
		}
		
		
	}


	private boolean hasBody() {
		return (this.httpCommand.equals("POST") || this.httpCommand.equals("PUT"));
	}

}
