package client;



public class MainC {
		
		public static void main(String[] args) {
			BasicHTTPClient bhc= new BasicHTTPClient();
			
//			System.out.println("To access the server, use port 54321");
//			System.out.println("The possible HTTP commands are: GET,HEAD,PUT,POST");
			
			bhc.requestNewCommand(args);
	
			
		}
}
