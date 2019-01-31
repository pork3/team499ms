package edu.uoregon.cs.calendar499;

import java.io.BufferedReader;
import java.util.Calendar;
import org.json.JSONObject;

public class LoadEvents {
	
	
	private static void threadedLoad(String fileName, FileIOStatus status) {
		
		status.currentStatus = Status.Waiting;
		
		status.lock.lock();
		try {
			/*check to see if file is good*/
			File dataFile = new File( fileName );
			
		} catch ( FileNotFoundException FNF ) {
			status.errorCode = ErrorNumbers.FileNotFound;
			status.currentStatus = Status.Failed;
			return;
		} 		
		/*load the conents into a string to be parsed*/
		String loadedContents;
		
		FileReader fileRead = new FileReader(fileName);
		BufferedReader fileBuffer = new BufferedReader(fileRead);
		
		StringBuilder sb = new StringBuilder();
		String line;
		
		while((line = fileBuffer.readLine())!= null ) {
			sb.append(line);
		}
		br.close();
		String read = sb.toString();	

		/*now that we have it parse the file*/
		status.storedValue = convertFromString(read);
		if (status.storedValue == null) {
			status.errorCode = ErrorNumbers.LoadError;
			status.currentStatus = Status.Failed;
		}
		status.lock.unlock(); 
		
	}

	
	private static Calendar convertFromString(String contents) throws Exception {
		
		String version, start,end,details;
		int year,month,day;
		boolean allday;
		
		try {				
				JSONObject obj = new JSONObject(contents);

				if( obj.has("version")){
					version = obj.getString("version");
				}
				/*get event array from json*/
				if( obj.has("events")){

					JSONArray eventarray = obj.getJSONArray("events");
					int len = eventarray.length();

					/*take the event array as a JSON object and parse*/
					for( int i=0; i < len; i++){
						JSONObject deets = eventarray.getJSONObject(i);

						if(deets.has("year")){
							year = deets.getInt("year");
						}
						if(deets.has("month")){
							year = deets.getInt("month");
						}
						if(deets.has("day")){
							day = deets.getInt("day");
						}
						if(deets.has("start")){
							start = deets.getString("start");
						}
						if(deets.has("end")){
							end = deets.getString("end");
						}
						if(deets.has("allday")){
							allday = deets.getBoolean("allday");
						}
					}
				}
				
			}
		} Catch (Exception e){
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
