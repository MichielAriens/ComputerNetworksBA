package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Receiver{
	Scanner scan;
	BasicHTTPClient basicClient;
	private ArrayList<String> requests;
	private int imageCount = 0;
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
			if(parts[0].equals("HEAD") && parts[1].equals("/connection")){
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
				int contentLengthImage=0;
				while(!currentLine.isEmpty()){
					flag = this.checkLine(currentLine);
					contentLengthImage = this.checkForLength(currentLine);
					System.out.println(currentLine);
					currentLine =reader.readLine();
				}
				if(flag==true){
					String fileType = this.getFileType();
					InputStream in = sock.getInputStream();
					OutputStream out = new FileOutputStream(imageCount+"."+fileType);
				}else{
					String bodyLine = reader.readLine();
					while(!bodyLine.isEmpty()){
						System.out.println(bodyLine);
						this.findEmbeddedObject(bodyLine);
						bodyLine=reader.readLine();
					}
				}
				
				if(parts[2].equals("HTTP/1.0")){
					
					if(requests.isEmpty()){
						basicClient.executeWithReconnect(basicClient.host+":"+sock.getPort()+"/connection", "HEAD");
					}else{
						String theRequest = requests.get(0);
						requests.remove(0);
						basicClient.executeWithReconnect(theRequest, "GET");
					}
				}else if(parts[2].equals("HTTP/1.1")){
					if(requests.isEmpty()){
						basicClient.executeWithoutReconnect(basicClient.host+":"+sock.getPort()+"/connection", "HEAD");
					}else{
						String theRequest = requests.get(0);
						requests.remove(0);
						basicClient.executeWithoutReconnect(theRequest, "GET");
					}
				}
			
		}
		
	}


	private int checkForLength(String currentLine) {
		if(currentLine.contains("Content-Length: ")){
			String[] part = currentLine.split(" ");
			return Integer.valueOf(part[1]);
		}else return 0;
	}


	private String getFileType() {
		if(basicClient.path.contains("jpg")){
			return "jpg";
		}else if(basicClient.path.contains("png")){
			return "png";
		}else if(basicClient.path.contains("bmp")){
			return "bmp";
		}else if(basicClient.path.contains("gif")){
			return "gif";
		}else return null;
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
	
	public static void linkStreams(InputStream input, OutputStream output, int amountOfBytes)
		    throws IOException
		{
			byte b = 0;
			while(amountOfBytes>0){
				b = (byte) input.read;
			}
		    byte[] buffer = new byte[1024]; // Adjust if you want
		    int bytesRead;
		    while ((bytesRead = input.read(buffer)) != -1)
		    {
		        output.write(buffer, 0, bytesRead);
		    }
		}
}
