package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

public class RequestHandler implements Runnable {
	
	private final Socket soc;
	private ProcessCommand proccomm;
	public RequestHandler(Socket soc) {
		proccomm = new ProcessCommand();
		this.soc = soc;
	}

	@Override
	public void run() {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
				reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
				
				
			
				String s = reader.readLine();
				writer.write("Server recived: " + s);
				writer.flush();
				proccomm.analyseCommand(s);
				
				int i = 1000;
				while (i > 0){
					writer.write(""+i);
					writer.newLine();
					i--;
				}
				try {
					this.wait(5000);
				} catch (InterruptedException e) {
					System.out.println("interrupted");
				}
				writer.flush();
				
				System.out.println(s);
				
				
				
			
			
		} catch (IOException e) {
			try {reader.close();} catch (IOException e1) {/*Well, screw it...*/}	
			try {writer.close();} catch (IOException e1) {/*Well, screw it...*/}
		}
	}
	
}
