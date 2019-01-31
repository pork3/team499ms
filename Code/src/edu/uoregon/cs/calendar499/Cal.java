package edu.uoregon.cs.calendar499;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Cal {
	
	public HashMap<Calendar, ArrayList<CalendarEvent>> days = new HashMap<Calendar, ArrayList<CalendarEvent>>();
	
	public Calendar cal = Calendar.getInstance();
	@SuppressWarnings("unused") //Is used!
	private GUI gui;
	private UserInput ui;
	public Cal() {
		
		
		ui = new UserInput();
		gui = new GUI(this, ui,new View());
		ui.gui = gui;
		
	}
	 
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Cal();
	}
}
