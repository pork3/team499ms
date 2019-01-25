package edu.uoregon.cs.calendar499;

import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;

public class FileIOStatus {
	
	public Status currentStatus = Status.Waiting;
	public int errorCode = 0;
	public Calendar storedValue = null;
	public boolean isFromSaving = false;
	public ReentrantLock lock = new ReentrantLock();
	
}
