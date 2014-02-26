package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class RequestHandler implements Runnable {
	
	private final Socket soc;
	private ProcessCommand proccomm;
	public RequestHandler(Socket soc) {
		proccomm = new ProcessCommand();
		this.soc = soc;
	}

	@Override
	public void run() {
		try {
			
				BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				String s = reader.readLine();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
				writer.write("Server recived: " + s);
				writer.flush();
				proccomm.analyseCommand(s);
				
				System.out.println(s);
				
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
