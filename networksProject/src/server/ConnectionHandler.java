package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import common.IllegalExchangeException;
import common.Request;
import common.Response;

public class ConnectionHandler implements Runnable {
	public static final List<String> COMMANDS;
	public static final List<String> PROTOCOLS;
	
	static{
		String[] arr = {"GET","POST","HEAD","PUT"};
		COMMANDS = Arrays.asList(arr);
		String[] arr1 = {"HTTP/1.0", "HTTP/1.1"};
		PROTOCOLS = Arrays.asList(arr1);
	}
	
	
	
	public final Socket soc;
	private List<Request> requests; // FIFO request queue on this ConnectionHandler.
	
	public BufferedReader reader = null;
	public BufferedWriter writer = null;
	
	public ConnectionHandler(Socket soc) {
		System.out.println("NEW THREAD!");
		this.soc = soc;
		this.requests = new ArrayList<Request>();
	}
	
	public String readLine(){
		try {
			String s = this.reader.readLine();
			System.out.println("read  >" + s);
			
			return s;
		} catch (IOException e) {
			return "";
		}
	}
	
	public void flush(){
		try {
			this.writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeLine(String line){
		try {
			this.writer.write(line);
			this.writer.newLine();
			System.out.println("wrote >" + line);
		} catch (IOException e) {
			return;
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
					String initLine = this.readLine();
					if(wellFormed(initLine)){
						activeRequest = new Request(initLine,this);
						activeRequest.grow();
						if(activeRequest.getMode().equals("HTTP/1.0") || activeRequest.toClose())
							holdConnection = false;
						response = Response.getResponseTo(activeRequest, this);
						response.printTo(this);
					}
				}
			
		} catch (IOException e) {
			try {reader.close();} catch (IOException e1) {/*Well, screw it...*/}	
			try {writer.close();} catch (IOException e1) {/*Well, screw it...*/}
		} catch (IllegalExchangeException e) {
		}
	}

	private static boolean wellFormed(String initLine) {
		if (initLine.equals(""))
			return false;
		
		String[] parts = initLine.split(" ");
		if(parts.length != 3)
			return false;
		
		if(!COMMANDS.contains(parts[0]))
			return false;
	
		if(!PROTOCOLS.contains(parts[2]))
			return false;				
				
		return true;
	}
	
}
