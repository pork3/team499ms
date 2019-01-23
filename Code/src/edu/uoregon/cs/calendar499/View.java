package edu.uoregon.cs.calendar499;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.Calendar;

public abstract class View {

	
	
	
	public abstract void Display(Graphics2D g, Calendar month);
	
	public abstract boolean handleEvent(MouseEvent e);
	
	
	
	
}
