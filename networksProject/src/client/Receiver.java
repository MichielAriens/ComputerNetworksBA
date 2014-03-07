package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Receiver{
	Scanner scan;
	BasicHTTPClient basicClient;
	private ArrayList<String> requests;
	public Receiver(Scanner scan,BasicHTTPClient basicClient){
		this.scan = scan;
		this.basicClient=basicClient;
		this.requests = new ArrayList<String>();
		
	}
	

	public void sendRequest(String request, Socket sock) throws UnknownHostException, IOException{
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		String[] parts = request.split(" ");
		if(parts[0].equals("GET") || parts[0].equals("HEAD")){
			writer.write(request);
			writer.newLine();
			if(parts[0].equals("HEAD") && parts[1].equals("connection")){
				writer.write("Connection: close");
				writer.newLine();
			}
			
			writer.newLine();
			
			writer.flush();
		}else if(parts[0].equals("PUT") || parts[0].equals("POST")){
			System.out.println("What's the message?");
			String message="";
			Boolean bool = false;
			while(bool == false){
				
					String messageLine = scan.nextLine();
					
					if(messageLine.equals("")){
						bool=true;
						
					}else
						message = message+"\n"+messageLine;
				
			}
			writer.write(request);
			writer.newLine();
			writer.write("Content-Length: "+message.getBytes().length);
			writer.newLine(); 
			writer.write(message); writer.newLine(); writer.newLine();
			writer.flush();
		}
		if(parts[0].equals("PUT") || parts[0].equals("POST") || parts[0].equals("HEAD")){
			
			String currentLine = reader.readLine();
			while(!currentLine.isEmpty()){
				System.out.println(currentLine);
				currentLine=reader.readLine();
			}
		
		}else if(parts[0].equals("GET")){
			
				String currentLine = reader.readLine();
				boolean flag = false;
				while(!currentLine.isEmpty()){
					flag = this.checkLine(currentLine);
					System.out.println(currentLine);
					currentLine =reader.readLine();
				}
				if(flag==true){
					
				}else{
					String bodyLine = reader.readLine();
					while(bodyLine!=null){
						System.out.println(bodyLine);
						this.findEmbeddedObject(bodyLine);
						bodyLine=reader.readLine();
					}
				}
				
				if(parts[2].equals("HTTP/1.0")){
					
					if(requests.isEmpty()){
						basicClient.executeWithReconnect(basicClient.host+":"+sock.getPort()+"/connection", "HEAD");
					}else{
						basicClient.executeWithReconnect(requests.get(0), "GET");
					}
				}else if(parts[2].equals("HTTP/1.1")){
					if(requests.isEmpty()){
						basicClient.executeWithoutReconnect(basicClient.host+":"+sock.getPort()+"/connection", "HEAD");
					}else{
						basicClient.executeWithoutReconnect(requests.get(0), "GET");
					}
				}
			
		}
		
	}


	private void findEmbeddedObject(String bodyLine) {
		if(bodyLine.contains("src=\"")){
			String[] parts =bodyLine.split("src=\"");
			
			String[] link = parts[1].split("\"");
			requests.add(link[0]);
		}else if(bodyLine.contains("href=\"")){
			String[] parts =bodyLine.split("href=\"");
			String[] link = parts[1].split("\"");
			requests.add(link[0]);
		}
		
	}


	private boolean checkLine(String currentLine) {
		return currentLine.contains("image");
		
	}
}
