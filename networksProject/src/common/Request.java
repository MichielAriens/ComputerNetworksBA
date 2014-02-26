package common;

import java.util.HashMap;

public class Request {
	
	
	public static final int HTTP1_1 = 1;
	public static final int HTTP1_0 = 0;
	

	private final String httpCommand;
	private final String Path;
	private final String Mode;
	
	
	public Request(String fullRequestString) {
		String[] parts = fullRequestString.split(" ");
		
	}

	private String headers(){
		return null;
	}

}
