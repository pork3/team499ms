package edu.uoregon.cs.calendar499;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;


// This class object is used to store events and contains a single function useful in creating events
public class Cal {
	
	// This stores a hashmap (indexed with a jCalendar (date) object) that holds lists of CalendarEvents;
	public HashMap<Calendar, ArrayList<CalendarEvent>> days = new HashMap<Calendar, ArrayList<CalendarEvent>>();
	 
	
	
	
	
	// This function, when provided with a jCalendar, will attempt to grab said day's events, however if the key doesn't exist in the container above, we create a new list and entry for it and return it.
	// The usingArray boolean lets this function know if it should allocate a new empty arrow in the hashmap (for adding events) or if it should simply attempt to get the value or default to an empty list (for just reading events).
	// This prevents adding a lot of "empty" calendar events. Speeding up other operations.
	public ArrayList<CalendarEvent> grab(Calendar day, boolean usingArray){
		if(!days.containsKey(day)) {
			if(!usingArray) {
				return new ArrayList<CalendarEvent>();
			}
			days.put(day, new ArrayList<CalendarEvent>());
		}
		return days.get(day);
	}
	
	// This function will return a deep copy of this object, that can be safely used in saving without worrying about concurrent modification errors
	// This function should be called by a thread that may be doing operations as to prevent it from making modifications while it copies. 
	public Cal deepCopy() {
		Cal c = new Cal();
		Set<Calendar> cals = days.keySet();
		for(Calendar ca : cals) { // Go over each key
			ArrayList<CalendarEvent> events = days.get(ca);
			if(events.size() != 0) {
				ArrayList<CalendarEvent> ev = c.grab(ca, true);
				for(CalendarEvent e : events) { // Go over all events in the array
					ev.add((CalendarEvent)e.clone());
				}
			}
		}
		return c;
	}
}
