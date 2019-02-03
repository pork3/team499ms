package edu.uoregon.cs.calendar499;

import java.util.concurrent.locks.ReentrantLock;


// This class is for defining an object that will denote the status of saving and loading progress.
public class FileIOStatus {
	
	public Status currentStatus = Status.Waiting; // Current status of the saving/loading event
	public ErrorTypes errorCode = ErrorTypes.NoError; // Current error of the event
	public Cal storedValue = null; // A Cal object if this was executed from a loading event
	public boolean isFromSaving = false; // Is this for a saving event or a loading event
	public ReentrantLock lock = new ReentrantLock(); // A lock to prevent values from being read before they are fully set
	
}
