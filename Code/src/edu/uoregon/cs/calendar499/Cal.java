package edu.uoregon.cs.calendar499;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Cal {
	
	public HashMap<Calendar, ArrayList<CalendarEvent>> days = new HashMap<Calendar, ArrayList<CalendarEvent>>();
	
	public Calendar cal = Calendar.getInstance();
	private GUI gui;
	private UserInput ui;
	public Cal() {
		ArrayList<CalendarEvent> ce = new ArrayList<CalendarEvent>();
		Calendar temp =CalMathAbs.ClearTime(cal); 
		Calendar temp1 =CalMathAbs.ClearTime(cal);
		Calendar temp2 =CalMathAbs.ClearTime(cal);
		temp1.set(Calendar.HOUR_OF_DAY, 11);

		temp2.set(Calendar.HOUR_OF_DAY, 13);
		
		days.put(temp, ce);
		
		ce.add(new CalendarEvent("Meeting in DES1", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES2", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES3", temp1, temp2));
		/*ce.add(new CalendarEvent("Meeting in DES4", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES5", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES6", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES7", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES8", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES9", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES10", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES11", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES12", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES13", temp1, temp2));
		ce.add(new CalendarEvent("Meeting in DES14", temp1, temp2));*/
		
		ui = new UserInput();
		gui = new GUI(this, ui,new View(), new View(), new View());
		gui.monthView.eventEvent = ce.get(0);
		gui.monthView.isEventShown = true;
		
	}
	 
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Cal();
	}
}
