package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;

public class RequestHandler implements Runnable {
	
	private final Socket soc;
	
	public RequestHandler(Socket soc) {
		this.soc = soc;
	}

	@Override
	public void run() {
		try {
			InputStream inStream = soc.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
			String s = reader.readLine();
			DataOutputStream 
			
			
			System.out.println(s);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
