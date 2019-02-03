package edu.uoregon.cs.calendar499;

import java.util.Calendar;

// This module handles a lot of the Calendar operations for getting dates based on indexes
public class CalMathAbs {
	//Getting the current day of month using i and j.
	public static int GetDayN(int i, int j, Calendar copy)
	{
		//i is week, j is day, copy is the current month the calendar is viewing
		//copying calendar object 
		Calendar C = GetDayCal(i,j,copy);

		return C.get(Calendar.DAY_OF_MONTH);
	}
	
	// Returns a string of the passed jCalendar object where the hours and minutes are display like HH:MM, that is, they have leading zeros in both fields if they are less than 10.
	// (e.g. 9am 5min -> 09:05, 12pm 10 min -> 12:10.)
	public static String formatCalendarTime(Calendar c) {
		return (c.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + c.get(Calendar.HOUR_OF_DAY) + ":"
				+ (c.get(Calendar.MINUTE) < 10 ? "0" : "") + c.get(Calendar.MINUTE);
	}
	
	//Checks to see if today (the user's reported date from their Date and Time from their system) is the day in i and j of the monthh copy
	public static boolean IsCurrentDay(int i, int j, Calendar copy)
	{
		Calendar C = GetDayCal(i,j,copy);
		
		//Get the actual Calendar object for today's date
		Calendar today = Calendar.getInstance();
	
		//See if the dates are equal (ignoring the times)
		return ClearTime(C).equals(ClearTime(today)); 
	}
	
	//Determine if the day in position row & column, is in the month denoted in copy
	public static boolean IsNotCalMonth(int i, int j, Calendar copy)
	{
		int calMonth = copy.get(Calendar.MONTH); // Get the month and year from copy
		int calYear = copy.get(Calendar.YEAR);
		
		Calendar C = GetDayCal(i,j,copy); // Get the jCalendar object of the date at i,j in the calendar

		int month = C.get(Calendar.MONTH); // Get the month and year from C
		int year = C.get(Calendar.YEAR);
		
		return !(month == calMonth && year == calYear); // Determine if the months and years are equal
		
	}
	// A function that returns a "prettified" string denoting the current date and time. Used in debugging 
	public static String printDate(Calendar c) {
		
		return c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
	}
	
	// This function takes an row, column in the current calendar and determines the calendar object that goes to that date
	public static Calendar GetDayCal(int i, int j, Calendar copy)
	{
		Calendar C = ClearTime(copy);
		
		C.set(Calendar.WEEK_OF_MONTH, i + 1);			
		C.set(Calendar.DAY_OF_WEEK, j+1);
		
		return C;
	}
	
	// This function takes a jCalendar, makes a copy, strips/sets the time components to zero, and returns the new jCalendar object. Useful for using .equals with Calendars.
	public static Calendar ClearTime(Calendar C)
	{
		//making a copy of the calendar 
		Calendar copy = (Calendar) C.clone();
		
		//setting hour, minute, second, and millisecond to 0
		copy.set(Calendar.HOUR, 0);
		copy.set(Calendar.MINUTE, 0);
		copy.set(Calendar.SECOND, 0);
		copy.set(Calendar.MILLISECOND, 0);

		return copy;
	}

	// This function will take a jCalendar object, make a copy, and set the hour, minutes and seconds on the copy and return it. Useful for CalendarEvent datastructure
	public static Calendar setTime(Calendar c, int h, int m, int s) {
		Calendar copy = (Calendar) c.clone();
		copy.set(Calendar.MINUTE, m);
		copy.set(Calendar.HOUR_OF_DAY, h);
		copy.set(Calendar.SECOND, s);
		return copy;
	}
	// This function will take a jCalendar object and will create a hh:MM representation for them. (hh meaning 0-23, but no leading zero display, MM meaning 0-59, but with a leading zero displayed.)
	public static String ToHM(Calendar c) {
		return c.get(Calendar.HOUR_OF_DAY)+":" + (c.get(Calendar.MINUTE) < 10 ? "0"  : "") + c.get(Calendar.MINUTE);
	}
	
}
