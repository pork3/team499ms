package edu.uoregon.cs.calendar499;

import java.util.Calendar;

public class CalMathAbs {
	public static int GetDayN(int i, int j, Calendar c) {
		if(i == 0 && j < 5) {
			return 31;
		}
		return 1;
	}
	
	public static boolean IsCurrentDay(int i, int j, Calendar c) {
		if(i == 0) {
			return false;
		}
		return true;
	}
	
	public static boolean IsNotCalMonth(int i, int j, Calendar c) {
		if(i == 0 && j < 5) {
			return true;
		}
		return false;
	}
	
	public static Calendar GetDayCal(int i, int j, Calendar c) {
		return (Calendar)c.clone();
	}
	
	public static Calendar ClearTime(Calendar c) {
		Calendar copy = (Calendar) c.clone();
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
