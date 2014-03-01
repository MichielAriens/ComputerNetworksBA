package common;

import java.io.BufferedWriter;

public abstract class Exchange {
	


	public String initialLine; //Initial response line		
	public String headers = "";			
	public String body = "";
	public String Trail = "";
	public Exchange(String initialLine){
		this.initialLine = initialLine;
	}

	
	
	public void printTo(BufferedWriter writer){
		
	}
}
