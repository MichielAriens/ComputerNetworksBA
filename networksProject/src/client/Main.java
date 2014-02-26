package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
		
		public static void main(String[] args) {
			BasicHTTPClient bhc= new BasicHTTPClient();
			Scanner scan = new Scanner(System.in);
			System.out.println("To access the server, use port 54321");
			System.out.println("The possible HTTP commands are: GET,HEAD,PUT,POST");
			String command = scan.nextLine();
			bhc.checkCommand(command);
			
			
			try {
				bhc.connect(54321);
				System.out.println("Connection made");
				System.out.println("What would you like to do?");
				String message =scan.next();
				bhc.sendMessage(message);
			} catch (UnknownHostException e) {
				System.out.println("Server not running");
			} catch (IOException e) {
				System.out.println("Server not running pls make sure there is a connection.");
			}
			
			
		}
}
