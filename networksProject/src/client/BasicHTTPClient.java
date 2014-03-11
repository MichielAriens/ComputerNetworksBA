package client;


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * A class which takes care of splitting hosts, URI's, ports and http version. 
 * It also takes care of the connection with the server. 
 * @author Wander
 *
 */
public class BasicHTTPClient {
	
	/**
	 * A Variable holding the HTTP version used.
	 */
	String HTTPVersie;
	
	/**
	 * The file or image which we want to use for our request.
	 */
	String path;
	
	/**
	 * The host with which we want to connect and send requests to.
	 */
	String host;
	
	/**
	 * The socket the client will use to send requests.
	 */
	Socket clientSock = null;
	
	/**
	 * A variable holding the class which deals with the actual sending of the request.
	 */
	Sender sender=null;
	
	/**
	 * The constructor which initializes the sender with a scanner and itself.
	 */
	public BasicHTTPClient(){
		sender = new Sender(new Scanner(System.in),this);
	}

	/**
	 * The method used when the client is first started. It takes the arguments from the main method and 
	 * attempts to connect to a given host and port. It checks the HTTP version and then tries to send 
	 * the request through sender.
	 * @param arguments	The arguments given to the main method.
	 * @throws UnknownHostException If the host cannot be found.
	 * @throws IOException If there is a problem with the sockets.
	 */
	public void requestNewCommand(String[] arguments) {
		arguments[1] = this.deletehttpthing(arguments[1]);
		this.connect(arguments[1], arguments[2]);
		this.HTTPVersie=arguments[3];
		try {
			sender.sendRequest(arguments[0]+" "+path+" "+HTTPVersie, clientSock);
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Deletes the http:// tag from the given URI if it was present.
	 * @param URI	The URI with the possible http:// tag in front.
	 * @return	The given URI without the possible http:// tag in front of it.
	 */
	private String deletehttpthing(String URI) {
		String[] parts = URI.split("://");
		if(parts.length == 1){
			return parts[0];
		}else 
			return parts[1];
	}

	/**
	 * Gets the necessary information out of the given URI and the given HTTP command. 
	 * Then makes a new connection with the server received from the information out of the URI.
	 * After the new connection has been made, sends the request through sender.
	 * @param URI	The URI used for the next request.
	 * @param HTTPCommand	The HTTP command used for the next request.
	 * @throws UnknownHostException If the host cannot be found.
	 * @throws IOException If there is a problem with the sockets.
	 */
	public void executeWithReconnect(String URI,String HTTPCommand) {
		URI = this.deletehttpthing(URI);
		String[] PortAndHost = this.splitPort(URI);
		this.connect(PortAndHost[1], PortAndHost[0]);
		try {
			sender.sendRequest(HTTPCommand+ " "+ path+" "+HTTPVersie, clientSock);
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Gets the necessary information out of the given URI and the given HTTP command. 
	 * This method does not make a new connection but uses the clientsocket which was initialized 
	 * before. Then sends the request through sender. Important is that no new connection is made.
	 * @param URI	The URI used for the next request.
	 * @param HTTPCommand	The HTTP command used for the next request.
	 * @throws UnknownHostException If the host cannot be found.
	 * @throws IOException If there is a problem with the sockets.
	 */
	public void executeWithoutReconnect(String URI,String HTTPCommand) {
		URI = this.deletehttpthing(URI);
		String[] portAndHost = this.splitPort(URI);
		this.makePathAndHost(portAndHost[1]);
		try {
			sender.sendRequest(HTTPCommand+ " "+ path+" "+HTTPVersie, clientSock);
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	/**
	 * If the URI contains a port number, splits off the port number and returns the port and the
	 * URI without the port number.
	 * @param URI	The URI with a possible port in it.
	 * @return	An array of length 2 with the port number and the URI without the port number.
	 * 			The array is of type String.
	 * 			Returns null if the URI is not correct.
	 */
	private String[] splitPort(String URI) {
		String[] returnString = new String[2];
		String[] parts1 = URI.split(":");
		if(parts1.length==1){
			returnString[0] = "80";
			returnString[1] = parts1[0];
			return returnString;
		}else if(parts1.length==2){
			String host1 = parts1[0];
			String[] parts2 = parts1[1].split("/");
			returnString[0] = parts2[0];
			returnString[1] = host1;
			for(int i=1;i<parts2.length;i++){
				returnString[1] = returnString[1]+"/"+parts2[i];
			}
			return returnString;
		}return null;
	}


	

	/**
	 * Given the URI, creates a path and host using the method makePathAndHost.
	 * Uses the created host and given port to make a connection through sockets.
	 * @param URI	The URI containing a path and host.
	 * @param port	The port on which we would like to connect.
	 * @throws NumberFormatException If the port cannot be parsed to an integer.
	 * @throws UnknownHostException If the host cannot be found.
	 * @throws IOException If there is something wrong with the sockets.
	 */
	private void connect(String URI,String port) {
		
		this.makePathAndHost(URI);
			try {
				clientSock = new Socket(host,Integer.parseInt(port));
			} catch (NumberFormatException e) {
				System.out.println("There is something wrong with the port.");
			} catch (UnknownHostException e) {
				System.out.println("The host cannot be found.");
			} catch (IOException e) {
				System.out.println("There is something wrong with the sockets.");
			}
	
	}
	
	/**
	 * Given the URI, splits it up into a host and a path.
	 * @param URI	The URI to split into a host and a path.
	 */
	public void makePathAndHost(String URI){
		String[] parts = URI.split("/");
		String hostMaker = parts[0];
		String pathMaker="";
		for(int i=1;i<parts.length;i++){
			pathMaker = pathMaker+"/"+parts[i];
		}
		
		host= hostMaker;
		path = pathMaker;
	}
}
