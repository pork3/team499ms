package edu.uoregon.cs.calendar499;

import java.io.BufferedReader;
<<<<<<< Updated upstream
=======
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
>>>>>>> Stashed changes
import java.util.Calendar;
import org.json.JSONObject;

import org.json.JSONObject;

public class LoadEvents {
	
	
	private static void threadedLoad(String fileName, FileIOStatus status) {
		
		status.currentStatus = Status.Waiting;
		
		
		File dataFile = new File( fileName );
		String loadedContents;
		
		try {
			/*check to see if file is good*/
			FileReader fileRead = new FileReader(fileName);
			
		
			/*load the conents into a string to be parsed*/
		
			
			BufferedReader fileBuffer = new BufferedReader(fileRead);
			
			StringBuilder sb = new StringBuilder();
			String line;
			
			while((line = fileBuffer.readLine())!= null ) {
				sb.append(line);
			}
			String read = sb.toString();	

			fileBuffer.close();
			
			
			
			
			/*now that we have it parse the file*/
			try{
				status.storedValue = convertFromString(read);
				status.lock.lock();
				if (status.storedValue == null) {
				
					status.errorCode = ErrorNumbers.LoadError;
					status.currentStatus = Status.Failed;
					
				}
			}catch(Exception e){
				status.errorCode = ErrorNumbers.FileNotFound;
				status.currentStatus = Status.Failed;
			}finally{
				status.lock.unlock(); 
			}
		} catch ( IOException FNF ) {
			status.lock.lock(); 
			status.errorCode = ErrorNumbers.FileNotFound;
			status.currentStatus = Status.Failed;
			status.lock.unlock(); 
			return;
		} 		
		
		
	}

	
	private static Calendar convertFromString(String contents) throws Exception {
		 
		StringBuilder sb = new StringBuilder();
		String version, start,end,details;
		int year,month,day;
		boolean allday;
		
		try {				
			JSONObject obj = new JSONObject(contents);
			/*check version*/
			if( obj.has( "version" ) ){
				version = obj.getString("version");
			}
			/*year*/
			if ( obj.has( "year" )){
				year = obj.getInt("year");
			}
			/*month*/
			if ( obj.has( "month" )){
				month = obj.getInt("month");
			}
			/*day*/
			if ( obj.has( "day" )) {
				day = obj.getInt("day");
			}
			/*event*/
			if (obj.has( "event ")) {
				JSONObject eventobj = new JSONObject(obj);
				start = eventobj.getString("start");
				end = eventobj.getString("end");
				allday = eventobj.getBoolean("allday");
				if ( eventobj.has("details")) {
					details = eventobj.getString("details");
				}			
			}
<<<<<<< Updated upstream
		} Catch (Exception e){
=======
		} catch (Exception e){
>>>>>>> Stashed changes
			return null;
		}
			
	}
	
	public static FileIOStatus loadCalendar(String fileName) {
		
		FileIOStatus event = new FileIOStatus();
		/*check if file exists*/
		
		Thread ioJob = new Thread(new Runnable) {
		
			public void run() {
				/*check if file exists*/
				threadedLoad(filename, event);
				
			}
		}
		return event;
	}
}
