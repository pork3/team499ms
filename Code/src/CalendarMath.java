package magic;

import java.util.Calendar;
import java.util.Date;

public class CalendarMath 
{
	//getting the current day of month using i and j.
	public static int GetDayN(int i, int j, Calendar C)
	{
		//copying calendar object 
		Calendar copy = (Calendar) C.clone();
		
		//getting number of weeks in the month
		int numWeeks = C.get(Calendar.WEEK_OF_MONTH);
		
		if (i >= 0 || i <= numWeeks)
		{
			C.set(Calendar.WEEK_OF_MONTH, i + 1);
			
			if (j >= 0 || j <= 6)
			{
				C.set(Calendar.DAY_OF_WEEK, j+1);
			}
		}
		else
		{
			System.out.println("out of range");
		}
		
		int day = C.get(Calendar.DAY_OF_MONTH);
		
		//System.out.println("month " + C.get(Calendar.MONTH));
		        		
		return day;
	}
	
	//checks to see if today is the day in i and j
	public static boolean IsCurrentDay(int i, int j, Calendar C)
	{
		Calendar copy = (Calendar) C.clone();
		
		//storing today's date
		Calendar today = Calendar.getInstance();
		
		//getting number of weeks in the month
		int numWeeks = C.get(Calendar.WEEK_OF_MONTH);
				
		if (i >= 0 || i <= numWeeks)
		{
			C.set(Calendar.WEEK_OF_MONTH, i + 1);
			
			if (j >= 0 || j <= 6)
			{
				C.set(Calendar.DAY_OF_WEEK, j+1);
			}
		}
		else
		{
			System.out.println("out of range");
		}
		
		int day = C.get(Calendar.DAY_OF_MONTH);
		
		if(day == today.get(Calendar.DAY_OF_MONTH))
		{
			return true;
		}
		
		return false;
	}
	
	//given the month and the year in the object passed does the day in i and j land on that month
	public static boolean IsnotCalMonth(int i, int j, Calendar C)
	{
		Calendar copy = (Calendar) C.clone();
		
		int calMonth = C.get(Calendar.MONTH);
		int calYear = C.get(Calendar.YEAR);
		
		
		//getting number of weeks in the month
		int numWeeks = C.get(Calendar.WEEK_OF_MONTH);
				
		if (i >= 0 || i <= numWeeks)
		{
			C.set(Calendar.WEEK_OF_MONTH, i + 1);
			
			if (j >= 0 || j <= 6)
			{
				C.set(Calendar.DAY_OF_WEEK, j+1);
			}
		}
		else
		{
			System.out.println("out of range");
		}
		
		int month = C.get(Calendar.MONTH);
		int year = C.get(Calendar.YEAR);
		
		if(month == calMonth && year == calYear)
		{
			return true;
		}
		
		return false;
	}
	
	
	public static Calendar getDayCal(int i, int j, Calendar C)
	{
		Calendar copy = (Calendar) C.clone();
		
		//getting number of weeks in the month
		int numWeeks = C.get(Calendar.WEEK_OF_MONTH);
				
		if (i >= 0 || i <= numWeeks)
		{
			C.set(Calendar.WEEK_OF_MONTH, i + 1);
			
			if (j >= 0 || j <= 6)
			{
				C.set(Calendar.DAY_OF_WEEK, j+1);
			}
		}
		else
		{
			System.out.println("out of range");
		}
		
		
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
	
	
	
	public static void main(String[] args)
	{	
		Calendar cal = Calendar.getInstance();
		
		int currentDay = cal.get(Calendar.DAY_OF_MONTH);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		System.out.println(currentDay);
		//System.out.println(dayOfWeek);

		
		int num = GetDayN(4, 3, cal);
		
		System.out.println(num);
		
		//Returns True 
		if(IsCurrentDay(4, 3, cal))
		{
			System.out.println("True");
		}
		else
		{
			System.out.println("False");
		}
		
		//Returns False
		if(IsCurrentDay(4, 5, cal))
		{
			System.out.println("True");
		}
		else
		{
			System.out.println("False");
		}

	}
	
}


