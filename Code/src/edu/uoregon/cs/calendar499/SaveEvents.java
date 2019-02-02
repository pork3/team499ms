package edu.uoregon.cs.calendar499;

import java.io.File;
import java.util.Calendar;

public class SaveEvents {

	private static void threadedSave(Calendar calendar, String fileName, FileIOStatus status) {
		
		status.currentStatus = Status.Waiting;
		status.isFromSaving = true;
		/*check to see if the file exists*/
		File dataFile = new File( fileName );
		boolean exists = dataFile.exists();
		
		JSONObject contents = convertToString(calendar);
		/*check to see if file exists*/
		if ( exists == true) {
			/*idk what to do here*/
			
		} else {
			/*file does not exists so now create*/

			try ( BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))){
			/*write contents from JSON obj to file specified in filename*/
			contents.write(writer);	
			
			/*notify caller of error*/	
			} catch (Exception e){
				status.errorCode = ErrorNumbers.LoadError;
				status.currentStatus = Status.failed;
			}
		}
		
			
		
		
	}
	
	private static JSONObject convertToString(Cal calendar) {
		/*version number*/
		String version = "1.0"

		/*creates the json obj to write*/
		JSONObject obj = new JSONObject();
		obj.put("version", version);

		/*JSON array to hold the events*/
		JSONArray eventArray = new JSONArray();

		/*iterate through hashmap get all items*/	
		for (Map.Entry<Calendar, ArrayList<CalendarEvents>> : map.entrySet()){
			Calendar key = entry.getKey();
			ArrayList<CalendarEvent> value = entry.getValue();

			/*get the info for the day month year from calendar key*/
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH); 
			int day = calendar.get(Calendar.DAY_OF_MONTH);

			/*now loop through all events associated with that key *day*/
			for (CalendarEvent ce : value){
				JSONObject eventObj = newJSONObject();
				eventObj.put("year", year);
				eventObj.put("month", month);
				eventObj.put("day", day);
				eventObj.put("start", value.getTimeStart());
				eventObj.put("end", value.getTimeEnd());
				eventObj.put("title", value.getTitle());
				eventObj.put("details", value.getDetails());
				eventArray.put(eventObj);
			}

		}
		/*now attach the event array to the original obj*/
		obj.put("events", eventArray);

		return obj;
	}
	
	public static FileIOStatus saveCalendar(Calendar calendar, String fileName) {
		/*create initial FileIOStatus to be passed around*/
		FileIOStatus event = new FileIOStatus();
		
		Thread ioJob = new Thread(new Runnable) {
			
			public void run() {
				/*begin thread with input calendar/filename and new fileioobject*/	
				threadedSave(calendar, fileName, event);
			}
		}
		/*return to caller, while thread is running*/
		return event;
	}
}

/*
//Calendar
System.out.println(Calendar.getInstance().get(Calendar.YEAR));         // 2015
System.out.println(Calendar.getInstance().get(Calendar.MONTH ) + 1);   // 9
System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)); // 29
System.out.println(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));  // 7
System.out.println(Calendar.getInstance().get(Calendar.MINUTE));       // 35
System.out.println(Calendar.getInstance().get(Calendar.SECOND));       // 32
System.out.println(Calendar.getInstance().get(Calendar.MILLISECOND));  // 481
*/