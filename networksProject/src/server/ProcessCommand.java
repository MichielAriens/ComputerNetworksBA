package server;

public class ProcessCommand {
	String[] parts;
	public void analyseCommand(String s) {
		parts = s.split(" ");
		switch(parts[0]){
		case("GET"):
			break;
		case("HEAD"):
			break;
		case("PUT"):
			break;
		case("POST"):
			break;
		}
		
	}

}
