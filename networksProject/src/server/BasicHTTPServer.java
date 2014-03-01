package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class BasicHTTPServer {
	
	//Max waiting time to complete a request.
	public static final int TIMEOUT = 5000;

	private ServerSocket acceptorSocket;
	
	public BasicHTTPServer(){
		this.acceptorSocket = null;
	}
	
	public void connect(int port) throws UnknownHostException, IOException{
		acceptorSocket = new ServerSocket(port);
	}
	
	/**
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
