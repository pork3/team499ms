package edu.uoregon.cs.calendar499;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

//// Loading Events Module \\\\
/*
 * This module is responsible for loading the calendar and all the events from a provided file.
 * 
 * It is to perform the loading operation on a separate thread as to prevent "blocking" the system
 * while it loads. That way other threads can continue using the application without issues.
 */


public class LoadEvents {
	// A function to be called internally to handle loading a Calendar
	private static void threadedLoad(String fileName, FileIOStatus status) {
		
		try{
			if(!Files.exists(Paths.get(fileName))){
				throw new Exception(); // Trigger the last block of this try-multi-catch block.
			}
			// Takes all lines of a file, and then joins them with a new line character
			List<String> contents = Files.readAllLines(Paths.get(fileName)); 
			String content = String.join("\n", contents);
			
			
			Cal cal = null; // Initialize (to null) the Calendar object
			try {
				cal = convertFromString(content); // Try to convert the file contents to a Calendar object
			}catch(Exception e) {
				throw new IOException(e); // Re-throw an error to be caught in the outermost try-catch loop
			}
			status.lock.lock(); // If the above succeeds, set the status and storedValue and return
			// Lock the lock to prevent race conditions
			status.storedValue = cal;
			status.currentStatus = Status.Success;
			status.errorCode = ErrorTypes.NoError;
			status.lock.unlock();// Unlock the lock to allow others to access the contents and return
		} catch (IOException e) {
			// This is a loading error	
			// Report it as a SEVERE error
			Main.logDebug(1,e.getMessage());
			status.lock.lock();// Lock the lock to prevent race conditions, set errors and return
			status.currentStatus = Status.Failed;
			status.errorCode = ErrorTypes.LoadError;
			status.lock.unlock(); 
		}catch(Exception __) {
			// This is a file not found error, hence we handle it slightly differently.
			// We purposely leave the storedValue as empty.
			status.lock.lock(); // But lock the file, set values and return
			status.currentStatus = Status.Success;
			status.errorCode = ErrorTypes.FileNotFound;
			status.lock.unlock();
		}
		
	}

	// A helper function that will look at the contents of the file and determine what version of the loading module needs to parse the data
	// This allows for backwards compatibility, note that the version is determined by the Save module referencing the Main.VERSION_NUMBER field 
	private static Cal convertFromString(String contents) throws Exception{
		Main.logDebug(3, contents); // Debug statement, info about the contents of the file
		JSONObject obj = new JSONObject(contents); // Attempt to parse it.
		
		String version = ""; // Version placeholder
		if(obj.has("version")) {
			version = obj.getString("version"); // Load the version if it exists
		}
		Cal c = new Cal(); // Create a new Calendar (cal) object to store events
		
		// Below looks at the version of this file (if it has one) and dispatches the proper handler to parse the data, will return invalid version if it is blank or not handled. 
		// Extend the below when updating what is saved in the saved data. 
		// Note: All functions below should have the side effect that they add events to the Calendar object, also that they must throw Exception.
		// Convention: All handler functions should start with "handle", then the version number where '.' is replaced with '_'. 
		switch(version) {
		case "1.0":
			handle1_0(obj, c);
			break;
		default:
			throw new Exception("Invalid Version");
		}
		// Return the Calendar (cal) object.
		return c;	
	}
	
	// Handles files in version 1.0
	private static void handle1_0(JSONObject obj, Cal c) throws Exception {
		if(!obj.has("days")) { // Determine if the file has a 'days' field, if not, the file is deemed corrupt and an error is thrown.
			throw new Exception("Missing days");
		}
		
		JSONArray calArr = obj.getJSONArray("days"); // Grab the JSONArray from the JSONObject in the field 'days'
		for(int i = 0; i < calArr.length(); i++) { // Loop over the objects in the array
			JSONObject child = calArr.getJSONObject(i);
			if(!child.has("year") || !child.has("month") || !child.has("day") || !child.has("events")) {
				continue; // If the child object is missing these required fields, skip this object (as it is malformed).
				// However it may be the case that there are other valid objects, hence we should only continue and not throw an exception
				
				// Design decision: CC, silently ignore invalid objects in 'days' field.
			}
			JSONArray childArr = child.getJSONArray("events"); // Grab all events stored during this day.
			if(childArr.length() == 0) { // If there aren't any stored events, continue to the next day.
				continue;
			}
			Calendar cal = CalMathAbs.ClearTime(Calendar.getInstance()); // Create a jCalendar object that can be used to store the date of the child JSON object
			cal.set(child.getInt("year"), child.getInt("month"), child.getInt("day")); // Set the day, month and year of the child JSON object
			ArrayList<CalendarEvent> events = c.grab(cal, true); // Instantiate the events array in the Calendar (cal) object. To store the events for this day.
			// Remember: java is pass by reference in that this is an object that is stored elsewhere, so editing this will change the values stored in the Calendar object.
			
			for(int j = 0; j < childArr.length(); j++) { // Loops over all child objects in the array
				JSONObject cchild = childArr.getJSONObject(j);
				
				if(!cchild.has("title") || !cchild.has("note") || !cchild.has("is_all_day") || !cchild.has("start_hour") || !cchild.has("end_hour") || !cchild.has("start_minute") || !cchild.has("end_minute")) {
					continue; // If the event JSON object doesn't have the required fields, skip it
					
					// Design decision: CC, silently ignore invalid objects in 'events' field.
				}
				CalendarEvent e= new CalendarEvent("", cal, cal); // Create a new CalendarEvent object
				e.setNote(cchild.getString("note")); // Set the note field
				e.setTitle(cchild.getString("title")); // Set the title field
				
				Calendar cs = CalMathAbs.ClearTime(cal); // Start time jCalendar object
				Calendar ce = CalMathAbs.ClearTime(cal); // End time jcalendar object
				
				if(cchild.getBoolean("is_all_day")) { // If the event is all day, use the 59 seconds trick to set the time as all day.
					cs = CalMathAbs.setTime(cs, 0, 0, 0);
					ce = CalMathAbs.setTime(ce, 23, 59, 59);
				}else {
					cs = CalMathAbs.setTime(cs, cchild.getInt("start_hour"), cchild.getInt("start_minute"), 0); // If it isn't all day, set the minutes and hours field for the start and end times
					ce = CalMathAbs.setTime(ce, cchild.getInt("end_hour"), cchild.getInt("end_minute"), 0);
				}
				
				e.setTimeStart(cs); // Set the start and end times in the calendar event
				e.setTimeEnd(ce);
				events.add(e); // Add it to the events array
			}
			events.sort(new Comparator<CalendarEvent>() {
				@Override
				public int compare(CalendarEvent o1, CalendarEvent o2) {
					return o1.getTimeStart().compareTo(o2.getTimeStart());
				}
			});
		}
	}
	
	
	// The only public function, creates a thread to load the data and returns a FileIOStatus object as a promise/status for the operation
	public static FileIOStatus loadCalendar(String fileName) {
		
		FileIOStatus event = new FileIOStatus();
		
		String file = fileName; // Required because fileName isn't visible inside of ioJob.
		Thread ioJob = new Thread(new Runnable() {
		
			public void run() {
				threadedLoad(file, event); // Dispatch a thread to load the events, then when it returns, this thread will join/be destroyed automatically.
			}
		});
		ioJob.start(); // Start the thread process
		return event; // Return the promise
	}
}
