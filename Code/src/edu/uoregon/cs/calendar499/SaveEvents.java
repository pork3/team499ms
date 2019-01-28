package edu.uoregon.cs.calendar499;

import java.io.File;
import java.util.Calendar;

public class SaveEvents {
	private static void threadedSave(Calendar calendar, String fileName, FileIOStatus status) {
		
		status.lock.lock();
		status.currentStatus = Status.Waiting;
		status.isFromSaving = true;
		File dataFile = new File( fileName );
		boolean exists = dataFile.exists();
		
		String contents = convertToString(calendar);
		
		if ( exists == true) {
			
			
		} else {
			/*create a new file*/
		}
		
		
	}
	
	private static String convertToString(Calendar calendar) {
		
		CalendarEvent ce = Cal.years.get(calendar);
		int year = calendar.getInstance().get(calendar.YEAR);
		int month = calendar.getInstance().get(calendar.MONTH);
		int day = calendar.getInstance().get(calendar.DAY_OF_MONTH);
		
		int startHour = ce.gettimeStart.getInstance().get(calendar.HOUR_OF_DAY);
		int startMin = ce.gettimeStart.getInstance().get(calendar.MINUTE);
		
		int endHour = ce.gettimeEnd.getInstance().get(calendar.HOUR_OF_DAY);
		int endMin = ce.gettimeEnd.getInstance().get(calendar.MINUTE);
		
		String title = ce.getTitle();
		String note = ce.getNote();
		
		
		
	}
	
	public static FileIOStatus saveCalendar(Calendar calendar, String fileName) {
		
		FileIOStatus event = new FileIOStatus();
		/*check if file exists*/
		
		Thread ioJob = new Thread(new Runnable) {
		
			public void run() {
				/*check if file exists*/
				threadedSave(calendar, fileName, event);
			}
		}
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