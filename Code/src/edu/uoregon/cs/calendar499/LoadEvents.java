package edu.uoregon.cs.calendar499;

import java.io.BufferedReader;
import java.util.Calendar;
import org.json.JSONObject;

public class LoadEvents {
	
	private static void threadedLoad(String fileName, FileIOStatus status) {
		
		status.currentStatus = Status.Waiting;
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
		
		Cal convertedValue = convertFromString(read);
		if (convertedValue == null) {
			status.errorCode = ErrorNumbers.LoadError;
			status.currentStatus = Status.Failed;
		}
		/*aquire lock to update Cal value stored in IOStatus object*/
		status.lock.lock();
		status.storedValue = convertedValue;
		status.lock.unlock(); 
		
	}

	
	private static Cal convertFromString(String contents) throws Exception {
		
		/*temp variables to store parsed data in*/
		String version, start,end,details,title;
		int year,month,day;
		boolean allday;
		/*create new cal object to return to caller*/
		Cal fileCal = new Cal();
		try {				
				JSONObject obj = new JSONObject(contents);
				/*check version, in future updates handler different versions differently*/
				if( obj.has("version")){
					version = obj.getString("version");
				}
				/*get event array from json*/
				if( obj.has("events")){
					/*created JSON array per documentation */
					JSONArray eventarray = obj.getJSONArray("events");
					int len = eventarray.length();

					/*take the event array as a JSON object and parse*/
					for( int i=0; i < len; i++){
						JSONObject deets = eventarray.getJSONObject(i);
						/*create objects to store the parsed data held within the JSON*/
						CalendarEvent ce = new CalendarEvent(null,null,null);
						Calendar jCalendar = new Calendar();

						/*validate year present*/
						if(deets.has("year")){
							year = deets.getInt("year");
							jCalendar.set(Calendar.YEAR, year);
						}
						/*validate month present*/
						if(deets.has("month")){
							month = deets.getInt("month");
							jCalendar.set(Calendar.MONTH, month);
						}
						/*validate day present*/
						if(deets.has("day")){
							day = deets.getInt("day");
							jCalendar.set(Calendar.DAY_OF_YEAR, day);
						}
						/*validate start time*/
						if(deets.has("start")){
							Calendar cstart = jCalendar.clone();
							start = deets.getString("start");
							/*split the string stored in JSON as a string array
							expect string in format HH:mm*/
							String[] thetime = start.split(":");
							Integer hour = Integer.parseInt(thetime[0]);
							Integer min = Integer.parseInt(thetime[1]);
							/*start time is indicated by a sec field*/	 
							cstart.set(Calendar.HOUR_OF_DAY, hour);
							cstart.set(Calendar.MINUTE, min);
							//cstart.set(Calendar.)
							ce.setTimeStart(cstart);
						}
						/*validate end time*/
						if(deets.has("end")){
							Calendar cend = jCalendar.clone();
							end = deets.getString("end");
							String[] thetime = start.split(":");
							Integer hour = Integer.parseInt(thetime[0]);
							Integer min = Integer.parseInt(thetime[1]);
							cend.set(Calendar.HOUR_OF_DAY, hour);
							cend.set(Calendar.MINUTE, min);
							
							ce.setTimeEnd(cend);
						}
						if(deets.has("details")){
							details = deets.getString("details");
							ce.setNote(details);
						}
						if(deets.has("title")){
							title = deets.getString("title");
							ce.setTitle(title);					
						}

						if(deets.has("allday")){
							allday = deets.getBoolean("allday");
						}

					/*now update the cal object*/
					fileCal.put(jCalendar, ce);	

					}

				}

			
		} Catch (Exception e){
			return null;
		}
		return fileCal;
			
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
