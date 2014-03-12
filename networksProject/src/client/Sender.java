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
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is used to send a request through a socket.
 * 
 *
 */
public class Sender{
	
	/**
	 * The scanner used for reading input, that is requested for some requests.
	 */
	Scanner scan;
	
	/**
	 * A variable holding the the basicHTTPClient class, which is responsible for URI decomposition. 
	 */
	BasicHTTPClient basicClient;
	
	/**
	 * A list holding all the pending requests.
	 */
	private ArrayList<String> requests;
	
	/**
	 * A variable holding the total images past.
	 */
	private int imageCount = 0;
	
	/**
	 * Creates a Sender object and initializes the scanner, the basicHTTPClient and the request list.
	 * @param scan	The scanner for asking input.
	 * @param basicClient	The basicHTTPClient for new connections and URI decomposition.
	 */
	public Sender(Scanner scan,BasicHTTPClient basicClient){
		this.scan = scan;
		this.basicClient=basicClient;
		this.requests = new ArrayList<String>();
		
	}
	
	/**
	 * This method checks the request and splits it into a few parts. It checks the HTTPCommand and 
	 * bufferedwriter to send to the server through the given socket. Then it waits for a response
	 * from the server it is connected to. If it was a GET command, it checks for embedded objects and 
	 * depending on the HTTP version, makes a new connection or keeps the connection alive.
	 * @param request	The request to be send to the server.
	 * @param sock	The socket used for this request.
	 * @throws UnknownHostException	If the host cannot be found.
	 * @throws IOException	If there is something wrong with the sockets.
	 */
	public void sendRequest(String request, Socket sock) throws UnknownHostException, IOException{
		InputStream input = sock.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input),1);
		//Reader reader = new InputStreamReader(input);
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
					FileOutputStream fw = new FileOutputStream(fileName);
					sock.setSoTimeout(5000);					
					try{
						linkStreams(input, fw);
					}catch(SocketTimeoutException e){
						//done
					}
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

	/**
	 * If the line contains content length, returns the number after it, which is the content length.
	 * @param currentLine The line in which we look for the content length.
	 * @return The content length.
	 */
	private int checkForLength(String currentLine) {
		if(currentLine.contains("Content-Length: ")){
			String[] part = currentLine.split(" ");
			return Integer.valueOf(part[1]);
		}else return 0;
	}

	/**
	 * Returns a string of the image type.
	 * @return	The string containing the image type.
	 */
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

	/**
	 * Checks the given line for embedded objects and adds them to the request queue.
	 * @param bodyLine The line in which we check for embedded objects.
	 */
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

	/**
	 * Checks if the line contains image, if so, returns true, else returns false.
	 * @param currentLine	The line to check for the string image.
	 * @return True if the given line contains image.
	 * 			False otherwise.
	 */
	private boolean checkLine(String currentLine) {
		return currentLine.contains("image");
		
	}
	
	public static void linkStreams(InputStream input, OutputStream output)
		    throws IOException
		{
		    byte[] buffer = new byte[1024]; // Adjust if you want
		    int bytesRead;
		    while ((bytesRead = input.read(buffer)) != -1)
		    {
		        output.write(buffer, 0, bytesRead);
		    }
		}
	
	/*
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
	
	
	
	public void linkStreams(Reader input, Writer output, int amountOfBytes) throws IOException{	
		int buffer = 0;
		while(amountOfBytes > 0 && buffer != -1){
			
			buffer = input.read();
			output.write(buffer);
			amountOfBytes--;System.out.println(amountOfBytes + ": " + buffer);
		}
	}
*/
	
}
