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
import java.util.Scanner;

public class BasicHTTPClient {
	Scanner scan;
	
	public BasicHTTPClient(){
		scan = new Scanner(System.in);
	}
	
	public void checkCommand(String command) {
		String[] parts = command.split(" ");
		if(parts.length!=4){
			System.out.println("Sorry, that is not a valid command!");
			this.requestNewCommand();
			return;
		}
		if(parts[2].equals("54321")){
			System.out.println("You can only connect on port 54321(if you want to connect on our server)");
			this.requestNewCommand();
			return;
		}
		if(!(parts[0].equals("GET") || parts[0].equals("POST") || parts[0].equals("HEAD") || parts[0].equals("PUT"))){
			System.out.println("You need to use one of these commands : HEAD,GET,PUT,POST");
			this.requestNewCommand();
			return;
		}
		if(!(parts[3].equals("HTTP/1.0") || parts[3].equals("HTTP/1.1"))){
			System.out.println("The requested HTTP version is not valid, or wrongly typed, use: HTTP/1.0 or HTTP/1.1");
			this.requestNewCommand();
			return;
		}
		if(parts[3].equals("HTTP/1.0")){
			Receiver receiver = new Receiver(scan);
			try {
				receiver.sendRequest(command);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.requestNewCommand();
			
		}
		
	}

	public void requestNewCommand() {
		String command = scan.nextLine();
		this.checkCommand(command);
		
	}
	

}
