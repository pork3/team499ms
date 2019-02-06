package edu.uoregon.cs.calendar499;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

//// Saving Events Module \\\\
/*
 * This module is responsible for saving the calendar and all the events to a provided file.
 * 
 * It is to perform the saving operation on a separate thread as to prevent "blocking" the system
 * while it loads. That way other threads can continue using the application without issues.
 */


public class SaveEvents {
	// A function to be called internally to handle saving a Calendar
	private static void threadedSave(Cal calendar, String fileName, FileIOStatus status) {
		JSONObject savedEvents = convertToJSON(calendar); // Creates a JSONObject from the Calendar

		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName + ".tmp"))) {
			// Creates a writer object to write the JSONObject to a file
			savedEvents.write(writer); // Performs the writing action, may throw error
			
			Files.deleteIfExists(Paths.get(fileName)); // Delete the actual file if it exists
			Files.move(Paths.get(fileName + ".tmp"), Paths.get(fileName)); // Rename the temporary file to the actual file name
			status.lock.lock(); // If the above succeeds, set the status and say it succeeded.
			// Lock the lock to prevent race conditions
			status.currentStatus = Status.Success;
			status.errorCode = ErrorTypes.NoError;
			status.lock.unlock(); // Unlock the lock and return
		} catch (IOException e) {
			// Otherwise with an error, acquire the lock and set the error status and return.
			status.lock.lock();
			status.currentStatus = Status.Failed;
			status.errorCode = ErrorTypes.SaveError;
			status.lock.unlock();
		}
	}

	// Converts the Calendar object to a JSONObject
	private static JSONObject convertToJSON(Cal calendar) {
		JSONObject obj = new JSONObject(); // Create a base JSONObject

		Set<Calendar> days = calendar.days.keySet(); // Gets all unique dates (1/10/19 and 1/11/19 are unique but dates at different times aren't) stored in the calendar
		JSONArray calendararray = new JSONArray(); // Creates a JSONArray object
		for (Calendar cal : days) {
			ArrayList<CalendarEvent> events = calendar.grab(cal, false); // Get the events for this particular day in the calendar
			if(events.size() == 0) { //Prevent saving empty calendar days...saves on size of storage
				continue;
			}
			JSONObject child = new JSONObject(); // Creates a new JSON Object to store the details about the calendar day
			child.put("year", cal.get(Calendar.YEAR)); // Save the year, month and day of the key
			child.put("month", cal.get(Calendar.MONTH));
			child.put("day", cal.get(Calendar.DAY_OF_MONTH));
			JSONArray childarray = new JSONArray(); // Creates another JSONArray object to store all events that occur on this day
			for (CalendarEvent event : events) {
				JSONObject cchild = new JSONObject(); // Create a JSON object to store the event 
				cchild.put("title", event.getTitle());  // Store the title, note and start/end times (hours and minutes only)
				cchild.put("note", event.getNote());
				cchild.put("is_all_day", event.getTimeEnd().get(Calendar.SECOND) == 59); // This current version uses the fact that if the end time has a non-zero seconds field (namely equal to 59), then it is all day.
				cchild.put("start_hour", event.getTimeStart().get(Calendar.HOUR_OF_DAY));
				cchild.put("end_hour", event.getTimeEnd().get(Calendar.HOUR_OF_DAY));
				cchild.put("start_minute", event.getTimeStart().get(Calendar.MINUTE));
				cchild.put("end_minute", event.getTimeEnd().get(Calendar.MINUTE));
				childarray.put(cchild); // Add the event to the JSON array
			}
			child.put("events", childarray); // Add the array to the JSON object
			calendararray.put(child); // Add this JSON object to the outer most json array
		}
		obj.put("version", Main.APPLICATION_NUMBER); // Finally put the version number and the "dates" JSON objects/arrays into the outermost JSON object
		obj.put("days", calendararray);
		return obj;
	}
	
	// A public function to save a Calendar object into a file, returns immediately with a FileIOStatus object
	public static FileIOStatus saveCalendar(Cal calendar, String fileName) {
		/* create initial FileIOStatus to be passed around */
		FileIOStatus event = new FileIOStatus();
		event.isFromSaving = true; //This field lets the caller know that the event is from the saving function and should treat it slightly differently.
		new Thread(new Runnable() {
			public void run() {
				/* begin thread with input calendar/filename and new fileioobject */
				threadedSave(calendar, fileName, event);
			}
		}).start();
		/* return to caller, while thread is running */
		return event;
	}
}
