package server;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

public class MainS {
	public static void main(String[] a){
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		BasicHTTPServer bs = new BasicHTTPServer();
		try {
			bs.connect(54321);
			bs.listen();
		} catch (Exception e) {
			System.out.println("You done goofed!");
			e.printStackTrace();
		}
		
	}

}
