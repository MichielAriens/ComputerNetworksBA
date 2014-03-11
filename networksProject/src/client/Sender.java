package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Sender{
	Scanner scan;
	BasicHTTPClient basicClient;
	private ArrayList<String> requests;
	private int imageCount = 0;
	public Sender(Scanner scan,BasicHTTPClient basicClient){
		this.scan = scan;
		this.basicClient=basicClient;
		this.requests = new ArrayList<String>();
		
	}
	

	public void sendRequest(String request, Socket sock) throws UnknownHostException, IOException{
		//InputStream input = sock.getInputStream();
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
					contentLengthImage = contentLengthImage+this.checkForLength(currentLine);
					System.out.println(currentLine);
					currentLine =reader.readLine();
				}
				/**
				 * Working here
				 */
				if(flag == true){
					String fileType = this.getFileType();
					String fileName = "temp/" + imageCount+"." + fileType;
					System.out.println("Saving file to: " + System.getProperty("user.dir") + "/" + fileName);
					FileWriter fw = new FileWriter(fileName);
					char[] buffer = new char[contentLengthImage];
					int totalRead = 0;
					sock.setSoTimeout(5000);
					try{
						while(totalRead < contentLengthImage){
							totalRead += reader.read(buffer, totalRead, contentLengthImage - (totalRead));
						}
					}catch(SocketTimeoutException e){
						//done
					}
					String data = String.valueOf(buffer);
					data = String.valueOf(data.subSequence(0, data.length()));
					fw.write(data);
					fw.flush(); fw.close();
					System.out.println("done");
					
					
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
	
	public static void linkStreams(Reader input, Writer output, int amount)
		    throws IOException{
	    char[] buffer = new char[1024];
	    int bytesLeft = amount;
	    int bytesRead;
	    while (bytesLeft > 0){
	    	bytesRead = input.read(buffer,0,Math.min(bytesLeft, 1024));
	    	bytesLeft -= bytesRead;
	        output.write(buffer, 0, bytesRead);
	    }
	}
	
}
