package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
		
		public static void main(String[] args) {
			BasicHTTPClient bhc= new BasicHTTPClient();
			Scanner scan = new Scanner(System.in);
			System.out.println("which port?");
			int port;
			port = scan.nextInt();
			
			try {
				bhc.connect(port);
				System.out.println("Connection made");
				System.out.println("What message would you like to send?");
				String message =scan.next();
				bhc.sendMessage(message);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(true){
				
			}
			
		}
}
