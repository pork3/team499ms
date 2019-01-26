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
		status.storedValue = convertFromString(read);
		status.lock.unlock(); 
		
	}

	
	private static Calendar convertFromString(String contents) {
		 
		StringBuilder sb = new StringBuilder();
		int i;
		int strlength = contents.length();
		String version , year , month, day, times, description;
		
		for( i= 0; i < strlength; i++) {
			
			char s = contents.charAt(i);		
			switch(s) {
			/*version*/	
			case'v':
				while ((char c = contents.charAt(i) )!= '\n') {
					if ( c != ',') {
					sb.append(c);
					}
					i++;
				}
				version = sb.toString();
				/*clearing stringbuilder*/
				sb.setLength(0);
				break;
			/*year*/	
			case 'Y':
				i+=2;
				while ((char c = contents.charAt(i) )!= ',') {
					sb.append(c);
					i++;
				}
				version = sb.toString();
				sb.setLength(0);
				break;
			/*month*/
			case 'M':
				i+=2;
				while ((char c = contents.charAt(i) )!= ',') {
					sb.append(c);
					i++;
				}
				version = sb.toString();
				sb.setLength(0);
				break;
			/*Day*/
			case 'D':
				i+=2;
				while (( char c = contents.charAt(i))!= ',') {
					sb.append(c);
					i++;
				}
				version = sb.toString();
				sb.setLength(0);
				break;
			/*Event*/
			case 'E':
				i+=2;
				while((char c = contents.charAt(i)) !='"') {
					sb.append(c)
					i++;				
				}
				times = sb.toString();
				sb.setLength(0);
			case '"':
				while((char c = contents.charAt(i)) !='\n') {
					sb.append(c)
					i++;				
				}
				description = sb.toString();
				sb.setLength(0);
			}
			
		}
		
		
		
		
	}
	
	public static FileIOStatus loadCalendar(String fileName) {
		
		FileIOStatus event = new FileIOStatus();
		/*check if file exists*/
		
		Thread ioJob = new Thread(new Runnable) {
		
			public void run() {
				/*check if file exists*/
				threadedLoad(filename, event);
				Thread.join();
				
			}
		}
		return event;
	}
}
