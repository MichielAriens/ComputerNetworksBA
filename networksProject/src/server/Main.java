package server;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {
	public static void main(String[] a){
		BasicHTTPServer bs = new BasicHTTPServer();
		try {
			bs.connect(54321);
			bs.listen();
		} catch (Exception e) {
			System.out.println("You done goofed!");
		}
		
	}

}
