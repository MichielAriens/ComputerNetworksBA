package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Receiver{
	Scanner scan;
	BasicHTTPClient basicClient;
	public Receiver(Scanner scan,BasicHTTPClient basicClient){
		this.scan = scan;
		this.basicClient=basicClient;
		
	}
	

	public void sendRequest(String request, Socket sock) throws UnknownHostException, IOException{
		
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		String[] parts = request.split(" ");
		if(parts[0].equals("GET") || parts[0].equals("HEAD")){
			writer.write(request);
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
			writer.newLine(); writer.newLine();
			writer.write(message); writer.newLine();
			writer.flush();
		}
		if(parts[0].equals("PUT") || parts[0].equals("POST") || parts[0].equals("HEAD")){
			BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String currentLine = reader.readLine();
			while(!currentLine.isEmpty()){
				System.out.println(currentLine);
				currentLine=reader.readLine();
			}
		sock.close();
		}
		
	}
}
