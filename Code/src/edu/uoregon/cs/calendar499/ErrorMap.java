package edu.uoregon.cs.calendar499;

import java.util.HashMap;

public class ErrorMap {
	
	private HashMap<ErrorNumbers, String> er = new HashMap<ErrorNumbers, String>();
	
	ErrorMap(){	
		InitMap(); 
	}	
	
	public String GetCustomError(String message) {
		
		if( er.containsKey(ErrorNumbers.CustomError)) {
			/*only have one error message check to see*/
			if (er.get(ErrorNumbers.CustomError) == message) {
				return message;
			}
			er.remove(ErrorNumbers.CustomError);
		}
		er.put(ErrorNumbers.CustomError, message);
		return message;
	}
	
	public String GetError(ErrorNumbers en) {
		
		String error = er.get(en);
		return error;
		
	}
	
	private void InitMap() {
		er.put(ErrorNumbers.OutOfRange,  "Date given was out of range");
		er.put(ErrorNumbers.DateError , "Date given was incorrect");
		er.put(ErrorNumbers.FileFormat, "Database not in correct format");
		er.put(ErrorNumbers.FileRead ,"Unable to read database file");
		er.put(ErrorNumbers.FileNotFound  , "Unable to locate database file");
		er.put(ErrorNumbers.LoadError , "Unable to load database file");
		
	}
}

