package client;


/**
 * 
PUT /path/to/fail.txt HTTP/1.1 			//post en put
Content-Length: 32

the stuff i want to save 

--------

GET /index.html HTTP/1.0 				//get & head



^2 lege lines

 */

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

@SuppressWarnings("unused")
public class BasicHTTPClient {
	String HTTPVersie;
	String path;
	String host;
	Socket clientSock = null;
	Receiver receiver=null;
	public BasicHTTPClient(){
		receiver = new Receiver(new Scanner(System.in),this);
	}


	public void requestNewCommand(String[] arguments) {
		arguments[1] = this.deletehttpthing(arguments[1]);
		this.connect(arguments[1], arguments[2]);
		this.HTTPVersie=arguments[3];
		try {
			receiver.sendRequest(arguments[0]+" "+path+" "+HTTPVersie, clientSock);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private String deletehttpthing(String URI) {
		String[] parts = URI.split("://");
		if(parts.length == 1){
			return parts[0];
		}else 
			return parts[1];
	}


	public void executeWithReconnect(String URI,String HTTPCommand) {
		URI = this.deletehttpthing(URI);
		String[] PortAndHost = this.splitPort(URI);
		this.connect(PortAndHost[1], PortAndHost[0]);
		try {
			receiver.sendRequest(HTTPCommand+ " "+ path+" "+HTTPVersie, clientSock);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void executeWithoutReconnect(String URI,String HTTPCommand) {
		URI = this.deletehttpthing(URI);
		String[] portAndHost = this.splitPort(URI);
		this.makePathAndHost(portAndHost[1]);
		try {
			receiver.sendRequest(HTTPCommand+ " "+ path+" "+HTTPVersie, clientSock);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private String[] splitPort(String URI) {
		String[] returnString = new String[2];
		String[] parts1 = URI.split(":");
		if(parts1.length==1){
			returnString[0] = "80";
			returnString[1] = parts1[0];
		}else if(parts1.length==2){
			String host1 = parts1[0];
			String[] parts2 = parts1[1].split("/");
			returnString[0] = parts2[0];
			for(int i=1;i<parts2.length;i++){
				returnString[1] = returnString[1]+parts2[i];
			}
		}return null;
	}


	


	private void connect(String URI,String port) {
		
		this.makePathAndHost(URI);
			try {
				clientSock = new Socket(host,Integer.parseInt(port));
			} catch (NumberFormatException e) {
				System.out.println("somethang wong with purt");
			} catch (UnknownHostException e) {
				System.out.println("dunno what this does");
			} catch (IOException e) {
				System.out.println("somethang wong with sockits");
			}
	
	}
	
	public void makePathAndHost(String URI){
		String[] parts = URI.split("/");
		String hostMaker = parts[0];
		String pathMaker="";
		for(int i=1;i<parts.length;i++){
			pathMaker = pathMaker+parts[i];
		}
		
		host= hostMaker;
		path = pathMaker;
	}
}
