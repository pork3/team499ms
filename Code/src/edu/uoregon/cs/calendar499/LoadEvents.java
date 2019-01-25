package edu.uoregon.cs.calendar499;

import java.io.BufferedReader;
import java.util.Calendar;

public class LoadEvents {
	
	
	private static void threadedLoad(String fileName, FileIOStatus status) {
		
		status.currentStatus = Status.Waiting;
		status.lock.lock();
		
		try {
			/*check to see if file is good*/
			File dataFile = new File( fileName );
			
		} catch ( FileNotFoundException FNF ) {
			status.errorCode = 2;
			status.currentStatus = Status.Failed;
			return;
		} 		
		/*load the conents into a string to be parsed*/
		String loadedContents;
		
		FileReader fileRead = new FileReader(fileName);
		BufferedReader fileBuffer = new BufferedReader(fileRead);
		
		StringBuilder sb = new StringBuilder();
		String line;
		
		while((line = fileBuffer.readLine())!= null ) {
			sb.append(line);
		}
		br.close();
		String read = sb.toString();	
		
		/*now that we have it parse the file*/
		convertFromString(read);
		
	}

	
	private static Calendar convertFromString(String contents) {
		 System.out.println(contents);

		
	}
	
	public static FileIOStatus loadCalendar(String fileName) {
		
		FileIOStatus event = new FileIOStatus();
		/*check if file exists*/
		threadedLoad(fileName, event);
		
		
		return null;
	}
}
