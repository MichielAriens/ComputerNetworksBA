package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class BasicHTTPServer {

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
			RequestHandler h = new RequestHandler(soc);
			Thread t = new Thread(h);
			t.start();
			
			
		}
	}
	
	
	
	
}
