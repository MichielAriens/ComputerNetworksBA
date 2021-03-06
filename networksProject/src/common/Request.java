package common;

import server.ConnectionHandler;

public class Request extends Exchange{

	public final ConnectionHandler parent;
	
	private boolean grown = false;
	
	private String httpCommand; 	
	private String path;			
	private String mode;			
	//private String headers;			
	//private String body;			
	
	
	public Request(String initialLine, ConnectionHandler connectionHandler) throws IllegalExchangeException {
		super(initialLine);
		this.headers = "";
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
	



	/**
	 * Reads in from the parent's reader to populate the header and body if required of this request.
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
				headers += nextLine + "\n";
			}
		}while(emptyLines < 2 && this.hasBody()){
			nextLine = parent.readLine();
			if(nextLine.equals("")){
				emptyLines++;
			}else{
				body += nextLine + "\n";
			}
		}
		
		
	}


	/**
	 * Checks whether this type of request is expecting a body.
	 * @return		true <=> expecting a body
	 */
	private boolean hasBody() {
		return (this.httpCommand.equals("POST") || this.httpCommand.equals("PUT"));
	}


	/**
	 * Checks whether the headers of this object contains a Connection: close directive.
	 * @return	true <=> headers contains "Connection: close" header 
	 */
	public boolean toClose() {
		return this.headers.contains("Connection: close");
	}
	
	
	/** GETTERS & SETTERS BELOW **/

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

}
