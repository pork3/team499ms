package edu.uoregon.cs.calendar499;

import java.util.Calendar;

public class CalMathAbs {
	//getting the current day of month using i and j.
	public static int GetDayN(int i, int j, Calendar copy)
	{
		//copying calendar object 
		Calendar C = GetDayCal(i,j,copy);


		return C.get(Calendar.DAY_OF_MONTH);
	}
	
	//checks to see if today is the day in i and j
	public static boolean IsCurrentDay(int i, int j, Calendar copy)
	{
		Calendar C = GetDayCal(i,j,copy);
		
		//Get the actual Calendar object for today's date
		Calendar today = Calendar.getInstance();
	
		
	
		return C.get(Calendar.YEAR) == today.get(Calendar.YEAR) && C.get(Calendar.MONTH) == today.get(Calendar.MONTH) && C.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
	}
	
	//given the month and the year in the object passed does the day in i and j land on that month
	public static boolean IsNotCalMonth(int i, int j, Calendar copy)
	{
		int calMonth = copy.get(Calendar.MONTH);
		int calYear = copy.get(Calendar.YEAR);
		
		Calendar C = GetDayCal(i,j,copy);

		int month = C.get(Calendar.MONTH);
		int year = C.get(Calendar.YEAR);
		
		return !(month == calMonth && year == calYear);
		
	}
	
	
	public static Calendar GetDayCal(int i, int j, Calendar copy)
	{
		Calendar C = ClearTime(copy);
		
		C.set(Calendar.WEEK_OF_MONTH, i + 1);			
		C.set(Calendar.DAY_OF_WEEK, j+1);
		
		return C;
	}
	
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

	public static String ToHM(Calendar c) {
		// TODO Auto-generated method stub
		return c.get(Calendar.HOUR_OF_DAY)+":" + (c.get(Calendar.MINUTE) < 10 ? "0"  : "") + c.get(Calendar.MINUTE);
	}
	
}
