package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Receiver implements Runnable{
	Scanner scan;
	
	public Receiver(Scanner scan){
		this.scan = scan;
	}
	
	/*
	 * Dit kan eventueel gebruikt worden als we hiervan een thread willen maken.
	 */
	@Override
	public void run() {
		
		
	}

	public void sendRequest(String request) throws UnknownHostException, IOException{
		
		Socket sock = new Socket("localhost",54321);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		String[] parts = request.split(" ");
		if(parts[0].equals("GET") || parts[0].equals("HEAD")){
			writer.write(request);
			writer.newLine();
			writer.newLine();
			writer.newLine();
			writer.flush();
		}else if(parts[0].equals("PUT") || parts[0].equals("POST")){
			System.out.println("What's the length of your message?");
			String contentl = "";
			Boolean bool = false;
			while(bool == false){
				try{
					String contentlength = scan.nextLine();
					Integer.parseInt(contentlength);
					contentl = contentlength;
					bool=true;
				}catch(NumberFormatException e){
					System.out.println("Needs to be an int");
				}
			}
			writer.write(request);
			writer.newLine();
			writer.write("Content-Length: "+contentl);
			writer.newLine(); writer.newLine();
			System.out.println("Pls state your message:");
			writer.write(scan.nextLine()); writer.newLine(); writer.newLine();
			writer.flush();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		String currentLine = reader.readLine();
		while(!currentLine.equals("END")){
			System.out.println(currentLine);
			currentLine=reader.readLine();
		}
		sock.close();
		System.out.println("The connection has been closed");
		
	}
}
