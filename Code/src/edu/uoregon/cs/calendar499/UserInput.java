package edu.uoregon.cs.calendar499;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Calendar;

public class UserInput implements MouseListener, WindowListener, KeyListener {
	private GUI gui;
	// Getters and setters for this reference to GUI
	public GUI getGui() {
		return gui;
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}

	
	
	// Handles the mouse clicking on an element on the application
	@Override
	public void mousePressed(MouseEvent arg0) {
		Point p = gui.view.getGUIBox(arg0.getPoint()); // Get the column and row of the row clicked by the mouse.

		if (!gui.view.isDisplayingEvents() && !gui.view.isDisplayingEvent() && p.x != -1) { // If the user isn't displaying any events and they clicked on a cell

			if (!CalMathAbs.IsNotCalMonth(p.y, p.x, GUI.instance.main.cal)) { // If it is in the current month
				gui.view.showEvents(p.x, p.y); // Show the event
			} else {
				// Determine if the cell clicked is in the current month or the previous month by looking at the day of the cell
				int dayN = CalMathAbs.GetDayN(p.y, p.x, GUI.instance.main.cal);
				if (dayN > 15) {
					// Go to the previous month
					GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
							+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? -1 : 0));
					GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
							+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? 11 : -1));
				} else {
					// Go the next month
					GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
							+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? 1 : 0));
					GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
							+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? -11 : 1));
				}
				
				// Brute force attempting to find the day to display in the day view
				for (int week = 0; week < 6; week++) {
					for (int day = 0; day < 7; day++) {
						if (!CalMathAbs.IsNotCalMonth(week, day, GUI.instance.main.cal)
								&& CalMathAbs.GetDayN(week, day, GUI.instance.main.cal) == dayN) {
							gui.view.showEvents(day, week);
						}
					}
				}
			}
		} else if (gui.view.getTitleBox(arg0.getPoint()) == -1) { // Determine if the user clicked the "previous month" button
			gui.view.hideEvent(); // Switch to the month view by hiding the day view and event editor (cancels any modification in the event editor)
			gui.view.hideEvents();
			
			// Set the month back by 1, wrapping the year if necessary
			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? -1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? 11 : -1));
			
			
		} else if (gui.view.getTitleBox(arg0.getPoint()) == 1) {// Determine if the user clicked the "previous month" button
			gui.view.hideEvent();// Switch to the month view by hiding the day view and event editor (cancels any modification in the event editor)
			gui.view.hideEvents();
			
			// Set the month forward by 1, wrapping the year if necessary
			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? 1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? -11 : 1));
		} else { // If the user has the day view or event editor displayed, determine what they clicked here.
			int clicked = gui.view.getObjectClicked(arg0.getPoint());
			Main.logDebug(3,clicked);
			switch (clicked) { // Switch through all possible clicked locations
			case -1:
				// Clicked off the event, hide the event editor and/or day view
				if (gui.view.isDisplayingEvent()) { 
					gui.view.hideEvent();
					gui.view.hideEvents();
				} else {// If the user was in the day view, if they click on another cell, it should show that days events.
					gui.view.hideEvents();
					if (p.x != -1) { // Determine if the user clicked on a cell
						if (!CalMathAbs.IsNotCalMonth(p.y, p.x, GUI.instance.main.cal)) {
							gui.view.showEvents(p.x, p.y); // If the new cell is in the current month, just show the new events for that day
						} else {
							Main.logDebug(3,"Out of current month"); // Switch to a new month and then go to the day that was clicked by the user
							int dayN = CalMathAbs.GetDayN(p.y, p.x, GUI.instance.main.cal);
							if (dayN > 15) { // Same logic as above for determining which day to jump to.
								GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
										+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? -1 : 0));
								GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
										+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? 11 : -1));
							} else {
								GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
										+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? 1 : 0));
								GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
										+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? -11 : 1));
							}
							// Brute for determine which day is the day to show.
							for (int week = 0; week < 6; week++) {
								for (int day = 0; day < 7; day++) {
									if (!CalMathAbs.IsNotCalMonth(week, day, GUI.instance.main.cal)
											&& CalMathAbs.GetDayN(week, day, GUI.instance.main.cal) == dayN) {
										gui.view.showEvents(day, week);
									}
								}
							}
						}
					}
				}
				break;
			case -2: // The user clicked the 'x' button, hide the day view/event editor 
				gui.view.hideEvents();
				gui.view.hideEvent();
				break;
			case -3: // User clicked the title field in the event editor, select it
				gui.view.setSelectedField(0);
				break;
			case -4: // User clicked on the minutes part of the start time field, select it
				gui.view.setSelectedField(2);
				gui.view.setOverField(1);
				break;
			case -14: // User clicked on the hours part of the start time field, select it
				gui.view.setSelectedField(2);
				gui.view.setOverField(0);
				break;
			case -5: // User clicked on the minutes part of the end time field, select it
				gui.view.setSelectedField(3);
				gui.view.setOverField(3);
				break;
			case -15: // User clicked on the hours part of the end time field, select it
				gui.view.setSelectedField(3);
				gui.view.setOverField(2);
				break;
			case -6: // User clicked on the checkbox for all day event, toggle the box
				gui.view.setEventAllDay(!gui.view.isEventAllDay());
				gui.view.setSelectedField(-1);
				gui.view.setOverField(-1);
				break;
			case -7: // User clicked on the notes field in the event editor, select it
				gui.view.setSelectedField(4);
				break;
			case -8:// User clicked on the save event button, attempt to save.
				// Save..
				gui.view.setSelectedField(9); // Flashs the user on the title field if it is not filled in
				if (gui.view.attemptSaving()) { // Success, hide the event editor and display the day view
					gui.view.hideEvent();
					gui.view.setSelectedField(-1);
				} else { // Otherwise, flash the user with a red fading box to denote an error...
					gui.view.setDelta(gui.frame.getFrameCount());
					gui.view.setRedBox(true);
				}

				break;
			case -9: // User clicked on the cancel/delete event
				if (gui.view.getEventIndex() != -1) { // If the user is deleting an event
					ArrayList<CalendarEvent> eventsOrig = GUI.instance.main.globalCalendar
							.grab(gui.view.getEventOrigDate(), true); // Grab the event list
					eventsOrig.remove(gui.view.getEventIndex()); // Remove the event from the list
				}
				gui.view.hideEvent(); // Hide the event, reseting variables as necessary
				break;
			case 0: // Nothing was clicked on the popup that was interesting...
				break; 
			case 1: // User clicked on creating a new event in the day view
				gui.view.showEvent(-1);
				break;
			case 2: // User clicked on the previous day arrow
				gui.view.moveToLeft();
				break;
			case 3: // User clicked on the next day arrow
				gui.view.moveToRight();
				break;
			case 4: // User clicked on the scroll up arrow
				gui.view.scrollUp();
				break;
			case 5: // User clicked on the scroll down arrow 
				gui.view.scrollDown();
				break;
			case 6: // This catches all possible returned values from 6-9, which 
				// Correspond to clicking different listed events in the day view
				// Though because these don't have breaks they "fall through" to case 9.
			case 7:
			case 8:
			case 9:// Show the event and break
				gui.view.showEvent(clicked);
				break;
			default:
				// Debug as a critical error that something else was returned that wasn't captured!
				Main.logDebug(0,clicked);
				break;
			}
		}
	}

	// Handles the event where the user attempts to press the 'x' button on the window.
	@Override
	public void windowClosing(WindowEvent arg0) {
		GUI.instance.dispose(); // First of all, dispose the frame (close it like the user attempted to)
		GUI.instance.frame.timer.stop(); // Stop the repaint timer (to allow the frame to truely close)
		Main.instance.showWaitingWindow("Saving calendar"); // Show the waiting window with the title "Saving calendar"
		
		
		// However, because of how the java GUI's work, we need to let this function return in order for any other gui's to be responsive
		// Thus we create a new thread to handle the saving event.
		new Thread(new Runnable() {
			public void run() {
				FileIOStatus e = SaveEvents.saveCalendar(Main.instance.globalCalendar, Main.filePath); // Save the calendar
				boolean waiting = true;
				while (waiting) {
					Main.waitSafe(10); // Give the saver some time to acquire the lock
					e.lock.lock();
					waiting = e.currentStatus.equals(Status.Waiting); // Check if it is still saving

					e.lock.unlock();
				}
				Main.waitSafe(1000); // Wait an addition 1 second to prevent the "saving calendar" from opening and then closing too quickly.
				Main.instance.destroyWaitingWindow(); // Destroy the window and join this thread.
			}
		}).start();
	}

	
	
	
	
	// This contains code to detect if a key had been pressed and finally released.
	@Override
	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			// To perform a "close events/day view"
			gui.view.hideEvent();
			gui.view.hideEvents();
			break;
		case KeyEvent.VK_LEFT:
			// Move the event one day to the left...
			if(gui.view.isDisplayingEvents()) {
				gui.view.moveToLeft();
			}
			break;
		case KeyEvent.VK_HOME:
			gui.view.hideEvent(); // Switch to the month view by hiding the day view and event editor (cancels any modification in the event editor)
			gui.view.hideEvents();
			
			GUI.instance.main.cal = CalMathAbs.ClearTime(Calendar.getInstance());// Set month back to the user's current calendar date
			break;
		case KeyEvent.VK_PAGE_UP: 
			// Allows for moving the current screen one month back
			gui.view.hideEvent(); // Switch to the month view by hiding the day view and event editor (cancels any modification in the event editor)
			gui.view.hideEvents();
			
			// Set the month back by 1, wrapping the year if necessary
			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? -1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? 11 : -1));
			break;
		case KeyEvent.VK_RIGHT:
			// Move the event one day to the right
			if(gui.view.isDisplayingEvents()) {
				gui.view.moveToRight();
			}
			break;
		case KeyEvent.VK_PAGE_DOWN:
			// Allows for moving the current screen one month forward
			gui.view.hideEvent();// Switch to the month view by hiding the day view and event editor (cancels any modification in the event editor)
			gui.view.hideEvents();
			
			// Set the month forward by 1, wrapping the year if necessary
			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? 1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? -11 : 1));
			break;
		default:
			Main.logDebug(3, arg0.getKeyCode());
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		char cc = arg0.getKeyChar(); // Determine which key the user typed.
		if ((int) cc < 32 && (int) cc != 8 && (int) cc != 9) {
			Main.logDebug(3,(int) cc);
			// The user typed a control key (anything less than 32 in the ASCII int code)
			// hence ignore it.
		} else {
			switch (gui.view.getSelectedField()) { // Get which field we should attempt to type in
			case -1: // No field selected, ignore it
				break;
			case 9: // This field is when the 'save' was pressed and it failed. Ignore keypresses.
				break;
			case 0: // The title field is selected.
				if((int)cc == 9  && arg0.isShiftDown()) { // If it is a shift tab, attempt to go back to the previous field
					gui.view.setSelectedField(-1);
				}else if((int)cc == 9) { // If 9 (tab key) was pressed, select the next field
					gui.view.setSelectedField(2);
					gui.view.setOverField(0);
				}else if ((int) cc == 8) { // If 8 (backspace key) was pressed, attempt to backspace.
					if (gui.view.getEventTitle().length() > 0) { // If larger than 0, substring to remove the last character
						gui.view.setEventTitle(
								gui.view.getEventTitle().substring(0, gui.view.getEventTitle().length() - 1));
					} else { // The field is empty, flash red
						gui.view.setDelta(gui.frame.getFrameCount());
						gui.view.setRedBox(true);
					}
				} else { // If the user typed an actual key, add it to the field
					if (gui.view.getEventTitle().length() < 40) { // If the field isn't full, append the character
						gui.view.setEventTitle(gui.view.getEventTitle() + cc);
					} else { // Otherwise flash an error
						gui.view.setDelta(gui.frame.getFrameCount());
						gui.view.setRedBox(true);
					}

				}
				break;
			case 4: // The notes field is selected. Because this is exactly the same as the title field above
				// Comments have been omitted.
				if((int)cc == 9  && arg0.isShiftDown()) { // If it is a shift tab, attempt to go back to the previous field
					gui.view.setPrevContents("");
					gui.view.setOverField(3); // Set the proper subfield
					gui.view.setSelectedField(3);
				}else if((int)cc == 9) {
					gui.view.setSelectedField(-1);
				}else if ((int) cc == 8) {
					if (gui.view.getEventNotes().length() > 0) {
						gui.view.setEventNotes(
								gui.view.getEventNotes().substring(0, gui.view.getEventNotes().length() - 1));
					} else {
						gui.view.setDelta(gui.frame.getFrameCount());
						gui.view.setRedBox(true);
					}
				} else {
					if (gui.view.getEventNotes().length() < 40) {
						gui.view.setEventNotes(gui.view.getEventNotes() + cc);
					} else {
						gui.view.setDelta(gui.frame.getFrameCount());
						gui.view.setRedBox(true);
					}

				}
				break;
			case 2: // The start time field is selected
				if (((int) cc >= 48 && (int) cc <= 58) || (int) cc == 8 || (int) cc == 9) { // Only allow 0-9, tab and backspace to be used here.
					if((int)cc == 9  && arg0.isShiftDown()) { // If it is a shift tab, attempt to go back to the previous subfield
						gui.view.setPrevContents("");
						gui.view.setOverField(gui.view.getOverField() -1);
						if (gui.view.getOverField() == -1) {// If it was highlighting the hours subfield, switch to the title field.
							gui.view.setSelectedField(0);
						}
					}else if ((int) cc == 9) { // If it is a tab, go to the next subfield
						gui.view.setPrevContents("");
						gui.view.setOverField(gui.view.getOverField() + 1);
						if (gui.view.getOverField() == 2) { // If it was highlighting the minutes subfield, switch to the hours subfield of the end time field.
							gui.view.setSelectedField(3); 
						}
					} else {
						if (gui.view.getOverField() == 0) { // If we are typing in the hours subfield
							if ((int) cc == 8) { // Attempt to backspace the hours field 
								if (gui.view.getPrevContents().length() > 0) {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1));
								} else { // Flash an error if nothing to backspace
									gui.view.setDelta(gui.frame.getFrameCount());
									gui.view.setRedBox(true);
								}
							} else { // Attempt to type into the hours field
								gui.view.setPrevContents(gui.view.getPrevContents() + cc);
								if (Integer.parseInt(gui.view.getPrevContents().trim()) > 23) {
									// Coerce it to be 0-23.
									gui.view.setPrevContents("23");
								}
								if (gui.view.getPrevContents().length() == 2) { // If it is the case that we are now at a length of 2
									gui.view.setEventTime1(
											new String(new char[2 - gui.view.getPrevContents().length()])
													.replace('\0', ' ') + gui.view.getPrevContents()
													+ gui.view.getEventTime1().substring(2));
									// Append whitespace to any 'not filled in parts' of the string and go to the next subfield
									gui.view.setPrevContents("");
									gui.view.setOverField(1);
									return;
								}
							
							}
							gui.view.setEventTime1(
									new String(new char[2 - gui.view.getPrevContents().length()]).replace('\0', ' ')
											+ gui.view.getPrevContents() + gui.view.getEventTime1().substring(2)); // Ensure that any not-filled in space is filled in with a whitespace to prevent misplacing the string
						} else if (gui.view.getOverField() == 1) {// If we are typing in the minutes subfield
							if ((int) cc == 8) { // Attempt to backspace the minutes field
								if (gui.view.getPrevContents().length() > 0) {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1));
								} else {// Flash an error if nothing to backspace
									gui.view.setDelta(gui.frame.getFrameCount());
									gui.view.setRedBox(true);
								}
							} else {// Attempt to type into the minutes field
								gui.view.setPrevContents(gui.view.getPrevContents() + cc);
								if (Integer.parseInt(gui.view.getPrevContents()) > 59) {
									// Coerce it to be 0-59.
									gui.view.setPrevContents("59");
								}
								if (gui.view.getPrevContents().length() == 2) {// If it is the case that we are now at a length of 2
									gui.view.setEventTime1(gui.view.getEventTime1().substring(0, 3)
											+ new String(new char[2 - gui.view.getPrevContents().length()])
													.replace('\0', ' ')
											+ gui.view.getPrevContents());
									// Append whitespace to any 'not filled in parts' of the string and go to the next subfield
									gui.view.setPrevContents("");
									gui.view.setOverField(2);
									gui.view.setSelectedField(3);
									return;
								}
							}
							gui.view.setEventTime1(gui.view.getEventTime1().substring(0, 3)
									+ new String(new char[2 - gui.view.getPrevContents().length()]).replace('\0', ' ')
									+ gui.view.getPrevContents());// Ensure that any not-filled in space is filled in with a whitespace to prevent misplacing the string
						}

					}
				} else { // Invalid input. flash the box red.
					gui.view.setDelta(gui.frame.getFrameCount());
					gui.view.setRedBox(true);
				}
				break;
			case 3:
				if (((int) cc >= 48 && (int) cc <= 58) || (int) cc == 8 || (int) cc == 9) {// Only allow 0-9, tab and backspace to be used here.
					if((int)cc == 9  && arg0.isShiftDown()) { // If it is a shift tab, attempt to go back to the previous subfield
						gui.view.setPrevContents("");
						gui.view.setOverField(gui.view.getOverField() -1);
						if (gui.view.getOverField() == 1) {// If it was highlighting the hours subfield, switch to the start time field.
							gui.view.setSelectedField(2);
						}
					}else if ((int) cc == 9) {// If it is a tab, go to the next subfield
						gui.view.setPrevContents("");
						gui.view.setOverField(gui.view.getOverField() + (gui.view.getOverField() == 3 ? -4 : 1));
						if (gui.view.getOverField() == -1) {// If it was highlighting the minutes subfield, switch to the notes field.
							gui.view.setSelectedField(4);
						}
					} else {
						if (gui.view.getOverField() == 2) {// If we are typing in the hours subfield
							if ((int) cc == 8) {// Attempt to backspace the hours field by using substring of the
								if (gui.view.getPrevContents().length() > 0) {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1));
								} else {// Flash an error if nothing to backspace
									gui.view.setDelta(gui.frame.getFrameCount());
									gui.view.setRedBox(true);
								}
							} else {// Attempt to type into the hours field
								gui.view.setPrevContents(gui.view.getPrevContents() + cc);
								if (Integer.parseInt(gui.view.getPrevContents()) > 23) {
									// Coerce it to be 0-23.
									gui.view.setPrevContents("23");
								}
								if (gui.view.getPrevContents().length() == 2) {// If it is the case that we are now at a
																				// length of 2
									gui.view.setEventTime2(new String(new char[2 - gui.view.getPrevContents().length()])
											.replace('\0', ' ') + gui.view.getPrevContents()
											+ gui.view.getEventTime2().substring(2));
									// Append whitespace to any 'not filled in parts' of the string and go to the next subfield
									gui.view.setPrevContents("");
									gui.view.setOverField(3);
									return;
								}
							}
							gui.view.setEventTime2(
									new String(new char[2 - gui.view.getPrevContents().length()]).replace('\0', ' ')
											+ gui.view.getPrevContents() + gui.view.getEventTime2().substring(2));// Ensure that any not-filled in space is filled in with a whitespace to prevent misplacing the string
						} else if (gui.view.getOverField() == 3) {// If we are typing in the minutes subfield
							if ((int) cc == 8) {// Attempt to backspace the minutes field
								if (gui.view.getPrevContents().length() > 0) {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1));
								} else {// Flash an error if nothing to backspace
									gui.view.setDelta(gui.frame.getFrameCount());
									gui.view.setRedBox(true);
								}
							} else {// Attempt to type into the minutes field
								
								gui.view.setPrevContents(gui.view.getPrevContents() + cc);
								if (Integer.parseInt(gui.view.getPrevContents()) > 59) {
									// Coerce it to be 0-59.
									gui.view.setPrevContents("59");
								}
								if (gui.view.getPrevContents().length() == 2) {
									gui.view.setEventTime2(gui.view.getEventTime2().substring(0, 3)
											+ new String(new char[2 - gui.view.getPrevContents().length()])
													.replace('\0', ' ')
											+ gui.view.getPrevContents());
									// Append whitespace to any 'not filled in parts' of the string and go to the next subfield
									gui.view.setPrevContents("");
									gui.view.setOverField(-1);
									gui.view.setSelectedField(-1);
									return;
								}

							}
							gui.view.setEventTime2(gui.view.getEventTime2().substring(0, 3)
									+ new String(new char[2 - gui.view.getPrevContents().length()]).replace('\0', ' ')
									+ gui.view.getPrevContents());// Ensure that any not-filled in space is filled in with a whitespace to prevent misplacing the string
						}

					}
				} else {// Invalid input. flash the box red.
					gui.view.setDelta(gui.frame.getFrameCount());
					gui.view.setRedBox(true);
				}
				break;
			}
		}
	}
	
	////////// REQUIRED BUT UNUSED FUNCTIONS \\\\\\\\\\\\
	// These functions are required by using the interface that we are using, however they are unused for this application
	
	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent arg0) {}

	
}
