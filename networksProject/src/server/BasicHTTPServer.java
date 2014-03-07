package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class BasicHTTPServer {

	//the socket that listens for requests.
	private ServerSocket acceptorSocket;
	
	/**
	 * Constructor
	 */
	public BasicHTTPServer(){
		this.acceptorSocket = null;
	}
	
	/**
	 * Set the servers port.
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void connect(int port) throws UnknownHostException, IOException{
		acceptorSocket = new ServerSocket(port);
	}
	
	/**
	 * Listens for new connections. Spawns new threads as necessary to handle the requests.
	 * @throws IOException 
	 * 
	 */
	public void listen() throws IOException{
		while(true){
			Socket soc = acceptorSocket.accept();
			ConnectionHandler h = new ConnectionHandler(soc);
			Thread t = new Thread(h);
			t.start();
			
			
		}
	}
	
	
	
	
}
