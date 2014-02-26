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

public class BasicHTTPClient {
	
	Socket sock = null;
	
	public void connect(int port) throws UnknownHostException, IOException{
		sock = new Socket("localhost",port);
	}

	public void sendMessage(String message) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		writer.write(message);
		writer.newLine();
		writer.flush();
	}
	

}
