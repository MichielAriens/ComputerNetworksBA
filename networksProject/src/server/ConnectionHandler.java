package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.IllegalExchangeException;
import common.Request;
import common.Response;

public class ConnectionHandler implements Runnable {
	
	private final Socket soc;
	private List<Request> requests; // FIFO request queue on this ConnectionHandler.
	
	BufferedReader reader = null;
	BufferedWriter writer = null;
	
	public ConnectionHandler(Socket soc) {
		this.soc = soc;
		this.requests = new ArrayList<Request>();
	}
	
	public String readLine(){
		try {
			return this.reader.readLine();
		} catch (IOException e) {
			return "";
		}
	}

	@Override
	public void run() {
		
		try {
				reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
				
				//contains the request being worked on.
				Request activeRequest = null;
				Response response = null;
				boolean holdConnection = true;
				
				while(holdConnection){
					activeRequest = new Request(reader.readLine(),this);
					activeRequest.grow();
					if(activeRequest.getMode().equals("HTTP/1.0"))
						holdConnection = false;
					response = Response.getResponseTo(activeRequest, this);
					response.printTo(writer);
				}
			
		} catch (IOException e) {
			try {reader.close();} catch (IOException e1) {/*Well, screw it...*/}	
			try {writer.close();} catch (IOException e1) {/*Well, screw it...*/}
		} catch (IllegalExchangeException e) {
		}
	}
	
}