package client;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class BasicHTTPClient {
	
	Socket sock = null;
	
	public void connect(int port) throws UnknownHostException, IOException{
		sock = new Socket("localhost",port);
	}

	public void sendMessage(String message) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		writer.write(message);
		writer.flush();
		
		
	}
	

}
