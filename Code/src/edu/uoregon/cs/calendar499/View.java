package edu.uoregon.cs.calendar499;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.ImageIcon;
///////// VIEW MODULE \\\\\\\\\\\\\\\\\\
/*
 * This module focuses on displaying elements to the user.
 * Because of doing such, many of the lines of code are self-explanatory (e.g. "setFont" or "setColor" or "drawString" or "drawRect", etc).
 * 
 */

public class View {

	private static final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };
	private static final String[] days = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };

	private static FontMetrics titleMetric;
	private static FontMetrics bodyMetric;
	private static FontMetrics eventMetric;
	private static FontMetrics editMetric;
	private static FontMetrics daysMetric;
	private static Color currentDayColor = new Color(.95f, 0.8f, 0.8f);
	private static Color currentDayColor2 = new Color(0.5f, 0.3f, 0.3f, 0.4f);
	private static Color titleColor = new Color(0, 0, 0);
	private static Color weeksColor = new Color(0, 0, 0);
	private static Color otherMonthColor = new Color(0.7f, 0.7f, 0.7f);
	private static Color currentMonthColor = new Color(1f, 1f, 1f);
	private static Color eventBackgroundColor = new Color(0.0f, 0.4f, 0.9f, 0.3f);
	private static Color eventPrevMonthBackgroundColor = new Color(0.0f, 0.45f, 1.0f, 0.4f);
	private static Color eventCurrentDayBackgroundColor = new Color(0.2f, 0.5f, 1.0f, 0.4f);
	private static Color eventEditHintColor = new Color(0.4f, 0.4f, 0.4f, 0.6f);
	private static Color eventEditCheckBoxColor = Color.blue;
	private static Color eventEditDisabledColor = new Color(0.4f, 0.4f, 0.4f, 0.6f);
	private static Color eventEditHighlightColor = new Color(0.1f, 0.1f, 0.1f, 0.1f);
	
	private Image leftImage = new ImageIcon(this.getClass().getResource("left.png")).getImage();
	private Image leftImage1 = new ImageIcon(this.getClass().getResource("left1.png")).getImage();
	private Image upImage = new ImageIcon(this.getClass().getResource("up.png")).getImage();
	private Image xImage = new ImageIcon(this.getClass().getResource("x.png")).getImage();

	private final Point LeftArrowMonthPosition = new Point(360, 12);
	private final Point RightArrowMonthPosition = new Point(865, 12);
	// This function handles rendering all of the elements on the screen, may call two additional helper render functions for drawing events
	public void Display(Graphics2D g, Calendar c) {
		// Sets the metrics to work with this graphics element passed to this function
		// If the metrics are already set, don't both getting metrics for the fonts
		titleMetric = (titleMetric != null ? titleMetric : g.getFontMetrics(GUI.titleFont));
		daysMetric = (daysMetric != null ? daysMetric : g.getFontMetrics(GUI.daysFont));
		bodyMetric = (bodyMetric != null ? bodyMetric : g.getFontMetrics(GUI.bodyFont));
		eventMetric = (eventMetric != null ? eventMetric : g.getFontMetrics(GUI.eventFont));
		editMetric = (editMetric != null ? editMetric : g.getFontMetrics(GUI.editFont));
		// Stores the previous font, stroke and color
		Font tempFont = g.getFont();
		Stroke tempStroke = g.getStroke();
		Color tempColor = g.getColor();

		g.setFont(GUI.titleFont);
		g.setColor(titleColor);

		String title = months[c.get(Calendar.MONTH)] + ", " + c.get(Calendar.YEAR); // The currently viewed month and
																					// year

		Point titlePosition = View.centerString(title, new Rectangle(0, 10, GUI.windowSize.width, 40), titleMetric); 
		// Centers the current month/year in the center of the title headbar

		g.drawString(title, titlePosition.x, titlePosition.y); // Draw the title to the screen

		g.setFont(GUI.bodyFont);
		g.setColor(weeksColor);
		// Renders the header for the days of the week (so Su, Mo, Tu, ..., Sa)
		for (int day = 0; day < 7; day++) {
			Point dayPosition = View.centerString(days[day], new Rectangle((day * width) - 1, 59, width, 26),
					bodyMetric);
			g.drawString(days[day], dayPosition.x, dayPosition.y);
		}

		// Draws the left and right arrows
		g.drawImage(leftImage, LeftArrowMonthPosition.x, LeftArrowMonthPosition.y,
				LeftArrowMonthPosition.x + leftImage.getWidth(null),
				LeftArrowMonthPosition.y + leftImage.getHeight(null), 0, 0, leftImage.getWidth(null),
				leftImage.getHeight(null), null); // Draws the left arrow for changing the month
		g.drawImage(leftImage, RightArrowMonthPosition.x, RightArrowMonthPosition.y,
				RightArrowMonthPosition.x + leftImage.getWidth(null),
				RightArrowMonthPosition.y + leftImage.getHeight(null), leftImage.getWidth(null), 0, 0,
				leftImage.getHeight(null), null); 
		// Draws the right arrow for changing the month, because of using the left arrow image, draws from right to left (while drawing left to
		// 			right), hence flips the image

		// Draws all of the cells in the calendar, 6 weeks in a calendar month, 7 days in a calendar week.
		// This application assumes that the month will start in the 1st week (so week = 0 in this application)
		for (int week = 0; week < 6; ++week) {
			for (int day = 0; day < 7; ++day) {
				g.setStroke(GUI.boxStroke);
				g.setFont(GUI.daysFont);
				// Get the status of the current cell in accordance to its day/week combination
				int dayNumber = CalMathAbs.GetDayN(week, day, c); // Gets the day number of the day (so the 1st,
																	// 2nd,...31st)
				boolean isOtherMonth = CalMathAbs.IsNotCalMonth(week, day, c); // Gets if the cell's day is not in this
																				// month
				boolean isCurrentDay = CalMathAbs.IsCurrentDay(week, day, c); // Gets if the cell's day is the user's
																				// current date

				if (isCurrentDay) {// If it is the user's current day, fill a rectangle with the currentDayColor
					g.setColor(currentDayColor);
					g.fillRect(width * day - 1, height * week + 88, width, height); // Fills the current cell
					if (isOtherMonth) {
						g.setColor(currentDayColor2); // However, if this is in a different month (viewing January while
														// it is early February), then add a slight overlay to
														// differentiate
					}
				} else if (isOtherMonth) {
					g.setColor(otherMonthColor);
				} else {
					g.setColor(currentMonthColor);
				}

				// Draws the background of the cell
				g.fillRect(width * day - 1, height * week + 88, width, height);
				// Draws a border around the cell
				g.setColor(Color.black);
				g.drawRect(width * day - 1, height * week + 88, width, height);
				// Draw the date in the top left of the cell

				Point dayPosition = View.centerString(dayNumber + (dayNumber < 10 ? " " : ""),
						new Rectangle(width * day + 17, height * week + 84, 10, 22), bodyMetric); 
				// Center it on the top left of the cell
				g.drawString(dayNumber + "", dayPosition.x, dayPosition.y); // Draws the number

				
				
				/////////////////// Render events in month view \\\\\\\\\\\\\\\\\\\
				Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(week, day, c)); // Grab the current cell's
																							// Calendar
				ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal, false); // Grab the events for
																									// the current cell
				if (events != null && !events.isEmpty()) { // If there are events to render
					for (int i = 0; i < events.size(); i++) { // Loop over all possible events
						if (isCurrentDay) { // Change color of the background box based upon if it today, in another
											// month or in the current month
							g.setColor(eventCurrentDayBackgroundColor);
						} else if (isOtherMonth) {
							g.setColor(eventPrevMonthBackgroundColor);
						} else {
							g.setColor(eventBackgroundColor);
						}
						g.fillRect(width * day + 5, height * week + 88 + 22 + i * 24, width - 12, 20); // Fill in the
																										// event box
						g.setColor(Color.BLACK);
						g.setFont(GUI.eventFont);

						String eventTitle = chopTitle(
								displayTime(events.get(i).getTimeStart(), events.get(i).getTimeEnd()) + " "
										+ events.get(i).getTitle(),
								24); // Creates the event title, including the time and title, only 24 characters can fit
						if (i == 3 && events.size() != 4) {
							// We need to draw a box saying "+ X more..." along with a + icon for if there are more than 4 events
							// and we are on the fourth box
							g.setFont(GUI.eventItalFont);
							eventTitle = "+" + (events.size() - 3) + " more...";

							Point eventPosition = View.leftCenterString(eventTitle,
									new Rectangle(width * day + 20, height * week + 88 + 22 + i * 24, width - 12, 20),
									eventMetric); // Center the string and draw it. 
							g.drawString(eventTitle, eventPosition.x, eventPosition.y);
							break;
						}
						Point eventPosition = View.leftCenterString(eventTitle,
								new Rectangle(width * day + 7, height * week + 88 + 22 + i * 24, width - 12, 20),
								eventMetric);
						g.drawString(eventTitle, eventPosition.x, eventPosition.y);
					}
				}
			}
		}

		
		/*
		 * Now we render the popup box, if we are displaying events or an event
		 */
		if (isDisplayingEvents() || isDisplayingEvent()) {
			// Display a white box...
			// First figure out if it needs to be above or below the current day...
			// Those cells in the first 3 weeks will be below
			// Those cells in the last 3 weeks will be above

			Point eventBox = new Point(0, 0);
			Polygon popupPoly = new Polygon(); // Will use a polygon to display a "pointer" arrow to the current day being referenced.
			Calendar today = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, c)); // Get the event's date based on the event's cell
			int currentBoxHeight = getPopupBoxHeight(); // Get the popup box height
			eventBox = this.getPopupBox(); // See getPopupBox.
			if (eventGridY > 2) { // Runs when weeks 3, 4, or 5 
				// Show popup above
				popupPoly.addPoint(eventBox.x, eventBox.y); // Top left
				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y); // Top right
				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y + currentBoxHeight); // Bottom right
				
				// The arrow portion of the polygon
				popupPoly.addPoint(width * eventGridX + width / 2 + 40, eventBox.y + currentBoxHeight);
				popupPoly.addPoint(width * eventGridX + width / 2, eventBox.y + currentBoxHeight + 10);
				popupPoly.addPoint(width * eventGridX + width / 2 - 40, eventBox.y + currentBoxHeight);
				
				popupPoly.addPoint(eventBox.x, eventBox.y + currentBoxHeight); // Bottom left corner

			} else {
				// Show popup below
				popupPoly.addPoint(eventBox.x, eventBox.y); // Top left corner
				
				// The arrow portion of the polygon
				popupPoly.addPoint(width * eventGridX + width / 2 - 40, eventBox.y);
				popupPoly.addPoint(width * eventGridX + width / 2, height * (eventGridY + 1) + 95);
				popupPoly.addPoint(width * eventGridX + width / 2 + 40, eventBox.y);
				
				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y); // Top right corner
				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y + currentBoxHeight); // Bottom right corner
				popupPoly.addPoint(eventBox.x, eventBox.y + currentBoxHeight); // Bottom left corner
			}
			
			g.setColor(Color.white);
			g.fillPolygon(popupPoly); // Fill the polygon with a white background
			g.setColor(Color.black);
			g.drawPolygon(popupPoly); // Draw a border on the polygon to differentiate it from the background
			if (!isDisplayingEvent()) { // If the user is displaying only events (not event editor popup), then draw a horizontal line to separate the date and the event boxes
				g.drawLine(popupPoly.xpoints[0], popupPoly.ypoints[0] + 55, popupPoly.xpoints[0] + width + boxWidth,
						popupPoly.ypoints[0] + 55);
			}
			// Don't display this line as it gets confusing if the date is a part of the editable part of the event, which it is. A line makes it appear like it isn't.
			
			g.setFont(GUI.editFont);
			String dateString = today.get(Calendar.DATE) + " " + months[today.get(Calendar.MONTH)] + ", "
					+ today.get(Calendar.YEAR); // The current date of the event in string format.
			Point datePoint = null;

			if (!isDisplayingEvent()) { // If the user is displaying events for the day, determine where to render the string and then render the events.

				datePoint = View.centerString(dateString,
						new Rectangle(popupPoly.xpoints[0] + boxWidth / 2, popupPoly.ypoints[0] + 15, width, 30),
						editMetric);
				g.drawString(dateString, datePoint.x, datePoint.y);

				renderEvents(g, popupPoly.xpoints[0], popupPoly.ypoints[0]); //Renders the events, the 2 points are the top left x & y coordinates of the popup box.
			} else { // If the user is displaying the event editor, append the appropriate name to the dateString, render the string and the event editor
				if (eventIndex == -1) { // eventindex == -1 means no previous index, thus it is a new event
					dateString = dateString + " - New Event";
				} else { // An eventIndex other than -1 means that it is an existing event.
					dateString = dateString + " - Edit Event";
				}
				datePoint = View.centerString(dateString,
						new Rectangle(popupPoly.xpoints[0] + boxWidth / 2, popupPoly.ypoints[0] + 20, width, 30),
						editMetric);
				g.drawString(dateString, datePoint.x, datePoint.y);
				renderEvent(g, popupPoly.xpoints[0], popupPoly.ypoints[0]); // Renders the event editor, the 2 points are the top left x & y coordinates of the popup box.
			}

			g.drawImage(leftImage1, datePoint.x - 50, datePoint.y - 26, datePoint.x - 10, datePoint.y + 40 - 26, 0, 0,
					leftImage1.getWidth(null), leftImage1.getHeight(null), null); // Draw a left arrow to change the date of the popup box

			g.drawImage(leftImage1, 2 * popupPoly.xpoints[0] + boxWidth + width - datePoint.x + 10,
					datePoint.y - 20 - 6, 2 * popupPoly.xpoints[0] + boxWidth + width - datePoint.x + 10 + 40,
					datePoint.y + 20 - 6, leftImage1.getWidth(null), 0, 0, leftImage1.getHeight(null), null); // Draw a right arrow to change the date of the popup box

			g.drawImage(xImage, popupPoly.xpoints[0] + 10, popupPoly.ypoints[0] + 10,
					popupPoly.xpoints[0] + xImage.getWidth(null) + 10,
					popupPoly.ypoints[0] + xImage.getHeight(null) + 10, 0, 0, xImage.getWidth(null),
					xImage.getHeight(null), null); // Draws a 'x' that can be clicked to dismiss the popup window
		}
		// Reset the stroke, font and color
		g.setStroke(tempStroke);
		g.setFont(tempFont);
		g.setColor(tempColor);
	}

	// This function will, based upon the currently selected day on the calendar, return the top left corner position for the popup box.
	public Point getPopupBox() {
		if (!this.isDisplayingEvent() && !this.isDisplayingEvents()) {
			// If there isn't a day selected, return -1,-1
			return new Point(-1, -1);
		}
		Point adjustedPosition = null;
		int currentBoxHeight = getPopupBoxHeight(); // Get the height of the popup box.

		if (eventGridY > 2) { // If the event occurs on the 4th, 5th or 6th row, the top left corner will be above the cell 
			adjustedPosition = new Point(
					width * eventGridX - (eventGridX <= 1 ? -5 + (eventGridX) * width : boxWidth / 2)
							- (eventGridX >= 5 ? boxWidth / 2 - (6 - eventGridX) * width + 5 : 0),
					height * (eventGridY + 1) - currentBoxHeight - 55);

		} else { // If the event occurs on the 1st, 2nd or 3rd row, the top left corner will be below the cell
			adjustedPosition = new Point(
					width * eventGridX - (eventGridX <= 1 ? -5 + (eventGridX) * width : boxWidth / 2)
							- (eventGridX >= 5 ? boxWidth / 2 - (6 - eventGridX) * width + 5 : 0),
					height * (eventGridY + 1) + 105);
		}

		return adjustedPosition;
	}
	// Returns the height of the popup box
	public int getPopupBoxHeight() {
		int currentBoxHeight = boxHeight; // Standard height of the popup box
		Calendar today = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, GUI.instance.main.cal)); 
		if (!isDisplayingEvent() && GUI.instance.main.globalCalendar.grab(today,false).size() <= 3) { // If the popup box is for displaying events occuring in a day and it is less than 4, decrease the size of the box.
			currentBoxHeight = boxHeight - 55 * (4 - GUI.instance.main.globalCalendar.grab(today,false).size()); 
		}
		return currentBoxHeight;
	}
	// Attempts to "scroll down" list of available events for the day
	public void scrollDown() {
		
		if (!this.isDisplayingEvent() && this.isDisplayingEvents()) { // Assumes that one is in the day view
			Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, eventDate));
			ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal, false);
			Main.logDebug(3, events.size());
			if (events.size() >= 4) { // Determines if one can even scroll with this amount of events
				this.scrollBar = (this.scrollBar + 4 >= events.size() ? events.size() - 4 : this.scrollBar + 1); // The scroll bar is for the top of the popup, not the bottom, so we need to also include the 4 other positions.
				return; // Return so you don't execute this.scrollBar = 0
			}
		}
		this.scrollBar = 0; // If something is amiss (not enough events, not displaying, etc), just set it to 0.
	}
	// Attempts to "scroll up" list of available events for the day. Exactly the same as scrollDown so comments omitted.
	public void scrollUp() {
		if (!this.isDisplayingEvent() && this.isDisplayingEvents()) {
			Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, eventDate));
			ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal, false);
			Main.logDebug(3, events.size());
			if (events.size() >= 4) {
				this.scrollBar = (this.scrollBar <= 0 ? 0 : this.scrollBar - 1); // If it is zero, then keep it at zero, otherwise just subtract one from it.
				return; // Return so you don't execute this.scrollbar = 0
			}
		}
		this.scrollBar = 0;
	}

	
	// Attempts to determine where on the screen did the user click, and return an int representing what was clicked.
	// Mainly handling the objects with the popupbox.
	/*
	 * IDs:
	 * 			-General-
	 * 
	 * 	-1: Not clicked in popup window
	 * 	-2: Clicked the 'x' icon
	 * 	0: User clicked in the popup window, but on nothing in particular
	 * 	2: User clicked the previous day arrow
	 * 	3: User clicked the next day arrow
	 * 	
	 * 		-Day View/List events view-
	 * 
	 * 	1: User clicked the new event button
	 * 	6 + i: (i+1)th event clicked in day view
	 * 	4: User clicked on the 'scroll up' arrow
	 * 	5: User clicked on the 'scroll down' arrow
	 * 
	 * 		   -Event Editor view-
	 * 
	 * 	-3: User clicked on the title field
	 * 	-14: User clicked on the hours part of the start time field
	 * 	-4: User clicked on the minutes part of the start time field
	 * 	-15: User clicked on the hours part of the end time field
	 * 	-5: User clicked on the minutes part of the end time field 
	 * 	-6: User clicked on the checkbox for all day event
	 * 	-7: User clicked on the notes field
	 * 	-8: User clicked the save event button
	 * 	-9: User clicked the cancel/delete event button
	 */
	public int getObjectClicked(Point mousePos) {
		Point topLeft = getPopupBox();
		int boxHeight = getPopupBoxHeight();

		if (!(new Rectangle(topLeft.x, topLeft.y, boxWidth + width, boxHeight).contains(mousePos))) {
			return -1; // The mouse was not within the popup box.
		}
		Main.logDebug(3,(mousePos.x - topLeft.x) + " " + (mousePos.y - topLeft.y));
		Point newPosition = new Point(mousePos.x - topLeft.x, mousePos.y - topLeft.y); 
		// Create a new point that represents the mouse relative distance to the top left corner of the popup box
		if (new Rectangle(10, 10, 32, 32).contains(newPosition)) {
			return -2; // If the relative mouse position is over the 'x', return -2.
		}
		
		Calendar today = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, GUI.instance.main.cal));
		// Get the selected grid's date
		String dateString = "";
		
		// Determine if the user is displaying events or the event editor.
		if (!this.isDisplayingEvent() && this.isDisplayingEvents()) { // Events
			//Determine what the title would be displaying (for use after the if block)
			dateString = today.get(Calendar.DATE) + " " + months[today.get(Calendar.MONTH)] + ", "
					+ today.get(Calendar.YEAR);
			
			// Determine if the 'Add event' button was clicked
			if (new Rectangle(30, 70, 520, 45).contains(newPosition)) {
				return 1;
			}
			
			Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, eventDate));
			ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal, false);
			
			int es = events.size(); // Figure out how many 'events' are listed, and determine if any were pressed.
			for (int i = 0; i < es; i++) {
				if (i > 3) {
					break;
				}
				if (new Rectangle(30, 70 + 65 + 55 * i, 520 - (es > 4 ? 40 : 0), 45).contains(newPosition)) {
					// The reason for the inline if statement is that when there are more than 4 events for a day
					// we have to render arrows on the right, so they render 40 shorter than normal.
					return 6 + i;
				}
			}
			if (es > 4) {
				if (new Rectangle(boxWidth + width - upImage.getWidth(null) - 10, 70 + 65, upImage.getWidth(null),
						upImage.getHeight(null)).contains(newPosition)) {
					// If the user clicked on the up arrow
					return 4;
				}
				if (new Rectangle(boxWidth + width - upImage.getWidth(null) - 10, 70 + 65 + 55 * 3,
						upImage.getWidth(null), upImage.getHeight(null)).contains(newPosition)) {
					// If the user clicked on the down arrow
					return 5;
				}
			}

		} else if (this.isDisplayingEvent()) { // Editor
			
			//Determine what the title would be displaying (for use after the if block)
			dateString = today.get(Calendar.DATE) + " " + months[today.get(Calendar.MONTH)] + ", "
					+ today.get(Calendar.YEAR) + " - " + (this.eventIndex == -1 ? "New Event" : "Edit Event");

			
			if (new Rectangle(10, 65, 560, 45).contains(newPosition)) {
				// Title
				return -3;
			}
			
			if (new Rectangle(10, 120, 140, 40).contains(newPosition) && !this.eventAllDay) {
				// User clicked on the start time field, but did they click the left half or the right half of the field?
				if (newPosition.x < 75) {
					return -14;//left half
				}
				return -4;//right half
			}
			if (new Rectangle(155, 120, 145, 165 - 120).contains(newPosition) && !this.eventAllDay) {
				// User clicked on the end time field, but did they click the left half or the right half of the field?
				if (newPosition.x < (145 / 2) + 155) {
					return -15; // left half
				}
				return -5;// right half
			}
			
			if (new Rectangle(150, 305, 430 - 145, 445 - 305).contains(newPosition)) {
				return -9; //Cancel button position
			}

			if (new Rectangle(525, 130, 25, 25).contains(newPosition)) {
				return -6; // The checkbox for the all day event
			}
			if (new Rectangle(10, 175, 560, 45).contains(newPosition)) {
				return -7;  // The notes field 
			}
			if (new Rectangle(150, 230, 430 - 150, 45).contains(newPosition)) {
				return -8; // The accept button
			}
		}
		if (this.isDisplayingEvents()) {
			Point datePoint = View.centerString(dateString,
					new Rectangle(topLeft.x + boxWidth / 2, topLeft.y + 15, width, 30), editMetric);
			Main.logDebug(3,datePoint + " " + mousePos);
			
			// Determine if the user clicked on the left arrow (which is dynamically positioned by the date string)
			if (new Rectangle(datePoint.x - 45, datePoint.y - 21, 30, 30).contains(mousePos)) {
				return 2; // Day left arrow pressed
			}

			if (new Rectangle(2 * topLeft.x + boxWidth + width - datePoint.x + 15, datePoint.y - 21, 30, 30)
					.contains(mousePos)) {
				return 3; // Day right arrow pressed
			}
		}
		return 0; // User clicked the popup box, but nothing in particular was clicked
	}

	// This function will attempt to move the popup box to the previous day, changing the month if required.
	// If the month changes, it will go to the last day in the previous month (so with Feb 1, 2019, the previous day is Jan 31, 2019)
	public void moveToLeft() {
		// Determine if the popup box is at the top left of the cells, or if the popup is on the first day in the month
		// If so, we need to change the month
		if ((eventGridY == 0 && eventGridX == 0) || (CalMathAbs.IsNotCalMonth(eventGridY - (eventGridX == 0 ? 1 : 0),
				eventGridX - (eventGridX == 0 ? -6 : 1), GUI.instance.main.cal))) { 
			
			// Set the month back one, wrapping the year if needed
			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? -1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? 11 : -1));
			
			// Figure out what the maximum (last) day is for this month
			int dayN = GUI.instance.main.cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			// Set the day to this value
			GUI.instance.main.cal.set(Calendar.DATE, dayN);
			// Clone the calendar to set the eventDate pointer
			eventDate = (Calendar) GUI.instance.main.cal.clone();
			
			// Brute force finding the X and Y positions of the new popup by searching all possible cells.
			for (int week = 0; week < 6; week++) {
				for (int day = 0; day < 7; day++) {
					if (!CalMathAbs.IsNotCalMonth(week, day, GUI.instance.main.cal)
							&& CalMathAbs.GetDayN(week, day, GUI.instance.main.cal) == dayN) {
						// If it is the current month and the days match, display the popup on that day.
						eventGridX = day;
						eventGridY = week;
					}
				}
			}

		} else {
			// If not, we need to simply subtract one from the column, potentially wrapping if necessary
			eventGridY -= (eventGridX == 0 ? 1 : 0);
			eventGridX -= (eventGridX == 0 ? -6 : 1);
			eventDate = CalMathAbs.GetDayCal(eventGridY, eventGridX, GUI.instance.main.cal);
		}
	}
	// This function will attempt to move the popup box to the next day, changing the month if required.
	// If the month changes, it will go to the first day in the previous month (so with Feb 1, 2019, the previous day is Jan 31, 2019)
	public void moveToRight() {
		// Determine if the popup box is at the bottom right of the cells, or if the popup is on the last day in the month
		// If so, we need to change the month
		if ((eventGridY == 5 && eventGridX == 6) || (CalMathAbs.IsNotCalMonth(eventGridY + (eventGridX == 6 ? 1 : 0),
				eventGridX + (eventGridX == 6 ? -6 : 1), GUI.instance.main.cal))) {
			
			// Set the month forward one, wrapping the year if needed
			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? 1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? -11 : 1));
			
			int dayN = 1; // The first of any month is always 1.
			// Set the day to this value
			GUI.instance.main.cal.set(Calendar.DATE, dayN);
			
			//Clone the calendar to set the eventDate pointer
			eventDate = (Calendar) GUI.instance.main.cal.clone();
			
			// Brute force finding the X and Y positions of the new popup by searching all possible cells.
			for (int week = 0; week < 6; week++) {
				for (int day = 0; day < 7; day++) {
					if (!CalMathAbs.IsNotCalMonth(week, day, GUI.instance.main.cal)
							&& CalMathAbs.GetDayN(week, day, GUI.instance.main.cal) == dayN) {
						// If it is the current month and the days match, display the popup on that day.
						eventGridX = day;
						eventGridY = week;
					}
				}
			}

		} else {
			// If not, we need to simply add one to the column, potentially wrapping if necessary
			eventGridY += (eventGridX == 6 ? 1 : 0);
			eventGridX += (eventGridX == 6 ? -6 : 1);
			eventDate = CalMathAbs.GetDayCal(eventGridY, eventGridX, GUI.instance.main.cal);
		}
	}

	// A local function that will determine how the times should be rendered to the user
	public String displayTime(Calendar sc, Calendar ec) {
		if (ec.get(Calendar.SECOND) == 59) {// If the event is all day
			return "All day -";
		}
		if (CalMathAbs.ToHM(sc).equals(CalMathAbs.ToHM(ec))) { // If the start and end time are equal, display only one of them
			return CalMathAbs.ToHM(sc);
		}
		return CalMathAbs.ToHM(sc) + "-" + CalMathAbs.ToHM(ec); // Otherwise, separate them with a '-'
	}

	// This function handles drawing the gui elements for the day view
	public void renderEvents(Graphics2D g, int topLeftX, int topLeftY) {
		g.setColor(Color.BLACK);
		g.setFont(GUI.bodyFont);
		g.drawRect(topLeftX + 30, topLeftY + 70, boxWidth + width - 60, 45); // Draw the box for Add Event
		String addNew = "+Add Event";
		Point addPoint = View.centerString(addNew,
				new Rectangle(topLeftX + 30, topLeftY + 70, boxWidth + width - 60, 45), bodyMetric); // Sets the Add Event string in the box

		g.drawString(addNew, addPoint.x, addPoint.y);

		Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, eventDate));
		ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal, false);

		boolean renderBar = events.size() > 4; // Determine if there is enough events to warrant a scroll bar (/scroll arrows)
		for (int i = 0; i < events.size(); i++) {
			if (i > 3) {
				break;
			}
			g.drawRect(topLeftX + 30, topLeftY + 70 + 65 + 55 * i, boxWidth + width - 60 - (renderBar ? 40 : 0), 45); // Draws the boxes for existing events

			String renderString = chopTitle(
					displayTime(events.get(i + scrollBar).getTimeStart(), events.get(i + scrollBar).getTimeEnd()) + " "
							+ events.get(i + scrollBar).getTitle(),
					29);
			Point renderEvent = View.centerString(renderString, new Rectangle(topLeftX + 30,
					topLeftY + 70 + 65 + 55 * i, boxWidth + width - 60 - (renderBar ? 40 : 0), 45), bodyMetric); 

			g.drawString(renderString, renderEvent.x, renderEvent.y); // Draws a centered string in the boxes rendered for the event

		}
		// Draw arrows
		if (renderBar) {
			// The up image is misnamed, as it actually points down, hence to draw the "up arrow" you have to render
			// it top to bottom but read it from its image file from bottom to top.
			
			g.drawImage(upImage, topLeftX + boxWidth + width - upImage.getWidth(null) - 10, topLeftY + 70 + 65,
					topLeftX + boxWidth + width - 10, topLeftY + 70 + 65 + upImage.getHeight(null), 0,
					upImage.getHeight(null), upImage.getWidth(null), 0, null); // Render the up arrow
			g.drawImage(upImage, topLeftX + boxWidth + width - upImage.getWidth(null) - 10, topLeftY + 70 + 65 + 55 * 3,
					topLeftX + boxWidth + width - 10, topLeftY + 70 + 65 + upImage.getHeight(null) + 55 * 3, 0, 0,
					upImage.getWidth(null), upImage.getHeight(null), null); // Render the down arrow
		}

	}
	// This function handles drawing the gui elements for the event editor
	public void renderEvent(Graphics2D g, int topLeftX, int topLeftY) {
		g.setColor(Color.BLACK);
		g.setFont(GUI.editFont);
		
		float redElement = 1f - ((GUI.instance.frame.getFrameCount() - this.delta) / 50f); //If displaying an error, this makes the error fade over time
		if (redElement < 0 || redElement > 1) { // If the redElement is out of range [0-1], set the redBox to false
			this.redBox = false;
		}
		g.setColor((selectedField == 0 && this.redBox ? new Color(redElement, 0, 0) : Color.BLACK));
		g.drawRect(topLeftX + 10, topLeftY + 10 + 55, boxWidth + width - 20, 45); // Draws rect for the title
		
		
		g.setColor((selectedField == 4 && this.redBox ? new Color(redElement, 0, 0) : Color.BLACK));
		g.drawRect(topLeftX + 10, topLeftY + 10 + 165, boxWidth + width - 20, 45); // Draws rect for the notes section
		
		
		g.setColor((selectedField == 8 && this.redBox ? new Color(redElement, 0, 0) : Color.BLACK));
		g.drawRect(topLeftX + 150, topLeftY + 10 + 165 + 55, boxWidth + width - 300, 45); // Draws rect for 'save event' button
		
		
		g.setColor(Color.BLACK);
		g.drawRect(topLeftX + 150, topLeftY + 10 + 275 + 20, boxWidth + width - 300, 45); // Draws rect for 'delete event' button

		String dispTitle = eventTitle; // If displaying a hint text, use a more 'gray' color to cue the user that it is a hint and not actual text
		if (eventTitle.length() == 0) {
			g.setColor(eventEditHintColor);
			g.setFont(GUI.editItalFont);
			dispTitle = eventTitleHint;
		}
		
		// If the user has clicked on this field last, display a flashing cursor that can cue the user that this field can be typed in
		String dispTitleEdit = "";
		if (selectedField == 0 && (GUI.instance.frame.getFrameCount() % 50) < 25) { // Make the cursor blink every so often
			dispTitleEdit = (dispTitle.replaceAll("[\\s\\S]", " ")) + "|";
		}

		Point titlePoint = View.centerString(dispTitle,
				new Rectangle(topLeftX + 12, topLeftY + 10 + 55, boxWidth + width - 24, 45), editMetric);
		g.drawString(dispTitleEdit, titlePoint.x - 5, titlePoint.y - 2);

		g.drawString(dispTitle, titlePoint.x, titlePoint.y); // Display the title field

		g.setColor(Color.BLACK);
		g.setFont(GUI.editFont);

		// The time sections of the event editor
		Point allDay = View.centerString("All Day Event?",
				new Rectangle(topLeftX + 10 + boxWidth + width - 270, topLeftY + 10 + 110, 200, 45), editMetric);
		g.drawString("All Day Event?", allDay.x, allDay.y);

		g.drawRect(topLeftX + 10 + boxWidth + width - 65, topLeftY + 21 + 110, 25, 25);

		if (eventAllDay) {
			// Fill in the box if the user checked it
			g.setColor(eventEditCheckBoxColor);
			g.fillRect(topLeftX + 10 + boxWidth + width - 62, topLeftY + 21 + 113, 19, 19);
			
			// This is what the fields should display
			eventTime1 = "00:00";
			eventTime2 = "23:59";

			// Change how the next two boxes are rendered...
			g.setColor(eventEditDisabledColor); // Set the color for displaying the times in the next fields
			g.setFont(GUI.editItalFont);
			dispTitle = eventTitleHint;
			

		} else if (this.selectedField != 2 && this.selectedField != 3) {
			// Swap the start and end time if they are out of order (the end time occurs before the start time).
			int i1 = Integer.parseInt(eventTime1.substring(0, 2).trim());
			int i2 = Integer.parseInt(eventTime2.substring(0, 2).trim());
			if (i1 == i2) { // If the hours are equal, check the minutes and then swap if needed
				int i3 = Integer.parseInt(eventTime1.substring(3).trim());
				int i4 = Integer.parseInt(eventTime2.substring(3).trim());
				if (i4 < i3) {
					String temp = eventTime1;
					eventTime1 = eventTime2;
					eventTime2 = temp;
				}
			} else if (i2 < i1) { // If the end time's hour is earlier than the start time's hour, swap
				String temp = eventTime1;
				eventTime1 = eventTime2;
				eventTime2 = temp;
			}
			
		}
		
		// For the next section of if statements, they are to repair when the user inputs incomplete values and moves on (such as 9 instead of 09)
		if (this.selectedField == 2 && this.overField == 0) {
			// Fix the minutes field of the start time
			int i2 = Integer.parseInt(eventTime1.substring(3).trim());
			eventTime1 = eventTime1.substring(0, 3) + (i2 < 10 ? "0" : "") + i2;
		} else if (this.selectedField == 2) {
			// Fix the hours field of the start time
			int i1 = Integer.parseInt(eventTime1.substring(0, 2).trim());
			eventTime1 = (i1 < 10 ? "0" : "") + i1 + eventTime1.substring(2);
		} else if (this.selectedField == 3 && this.overField == 2) {
			// Fix the minutes field of the end time
			int i2 = Integer.parseInt(eventTime2.substring(3).trim());
			eventTime2 = eventTime2.substring(0, 3) + (i2 < 10 ? "0" : "") + i2;
		} else if (this.selectedField == 3) {
			// Fix the hours field of the end time
			int i1 = Integer.parseInt(eventTime2.substring(0, 2).trim());
			eventTime2 = (i1 < 10 ? "0" : "") + i1 + eventTime2.substring(2);
		} else if (this.selectedField != 2) {
			// Fix the minutes and hours field of the start time
			int i1 = Integer.parseInt(eventTime1.substring(0, 2).trim());
			int i2 = Integer.parseInt(eventTime1.substring(3).trim());
			eventTime1 = (i1 < 10 ? "0" : "") + i1 + ":" + (i2 < 10 ? "0" : "") + i2;
		} else if (this.selectedField != 3) {
			// Fix the minutes and hours field of the end time
			int i1 = Integer.parseInt(eventTime2.substring(0, 2).trim());
			int i2 = Integer.parseInt(eventTime2.substring(3).trim());
			eventTime2 = (i1 < 10 ? "0" : "") + i1 + ":" + (i2 < 10 ? "0" : "") + i2;
		}
		
		
		
		// Now add a "highlight" to the field that the user is editing, if they are editing the time
		g.setFont(GUI.editFont);
		g.setColor(eventEditHighlightColor);
		if (selectedField == 2) {
			if (this.overField == 0) {
				g.fillRect(topLeftX + 46, topLeftY + 10 + 110 + 10, 33, 27);
			}
			if (this.overField == 1) {
				g.fillRect(topLeftX + 46 + 39, topLeftY + 10 + 110 + 10, 33, 27);
			}
		}
		if (selectedField == 3) {
			if (this.overField == 2) {
				g.fillRect(topLeftX + 46 + 145, topLeftY + 10 + 110 + 10, 33, 27);
			}
			if (this.overField == 3) {
				
				g.fillRect(topLeftX + 46 + 39 + 145, topLeftY + 10 + 110 + 10, 33, 27);
				
			}
		}
		// Actually render the times, center them in their respective boxes.
		g.setColor(Color.BLACK);
		Point time1 = View.centerString(eventTime1,
				new Rectangle(topLeftX + 10, topLeftY + 10 + 110, (boxWidth + width - 290) / 2, 45), editMetric);
		g.drawString(eventTime1, time1.x, time1.y);
		Point time2 = View.centerString(eventTime2, new Rectangle(topLeftX + 10 + (boxWidth + width - 290) / 2,
				topLeftY + 10 + 110, (boxWidth + width - 290) / 2, 45), editMetric);
		g.drawString(eventTime2, time2.x, time2.y);

		
		// Draws the boxes, however, invalid input may turn the box red
		g.setColor((selectedField == 2 && this.redBox ? new Color(redElement, 0, 0) : Color.BLACK));
		g.drawRect(topLeftX + 10, topLeftY + 10 + 110, (boxWidth + width - 290) / 2, 45);

		g.setColor((selectedField == 3 && this.redBox ? new Color(redElement, 0, 0) : Color.BLACK));
		g.drawRect(topLeftX + 10 + (boxWidth + width - 290) / 2, topLeftY + 10 + 110, (boxWidth + width - 290) / 2, 45);
		
		
		
		// If either of the boxes are red, then the center line should be red (as they share a common center line)
		g.setColor(((selectedField == 3 || selectedField == 2) && this.redBox ? new Color(redElement, 0, 0)
				: Color.BLACK));
		g.drawLine(topLeftX + 10 + (boxWidth + width - 290) / 2, topLeftY + 10 + 110,
				topLeftX + 10 + (boxWidth + width - 290) / 2, topLeftY + 10 + 155);

		
		// Render the box around the "All Day Event?" field, not the checkbox but the whole field
		g.setColor(Color.black);
		g.drawRect(topLeftX + 10 + boxWidth + width - 275, topLeftY + 10 + 110, 255, 45);
		g.setFont(GUI.editFont);

		// Similar to the title field, just shifted down and using a different variable name. Refer to the title field up above
		String dispNotes = eventNotes;
		if (eventNotes.length() == 0) {
			g.setColor(eventEditHintColor);
			g.setFont(GUI.editItalFont);
			dispNotes = eventNotesHint;
		}

		String dispNotesEdit = "";
		if (selectedField == 4 && (GUI.instance.frame.getFrameCount() % 50) < 25) {
			dispNotesEdit = (dispNotes.replaceAll("[\\s\\S]", " ")) + "|";
		}

		Point notesPoint = View.centerString(dispNotes,
				new Rectangle(topLeftX + 12, topLeftY + 10 + 165, boxWidth + width - 24, 45), editMetric);
		g.drawString(dispNotesEdit, notesPoint.x - 5, notesPoint.y - 2);

		g.drawString(dispNotes, notesPoint.x, notesPoint.y);

		
		
		
		// Render the "save event" text
		g.setColor(Color.black);
		g.setFont(GUI.bodyFont);
		Point savePoint = View.centerString(eventSaveHint,
				new Rectangle(topLeftX + 12, topLeftY + 10 + 165 + 34 + 20, boxWidth + width - 24, 45), bodyMetric);
		g.drawString(eventSaveHint, savePoint.x, savePoint.y);

		// Render the "delete event/cancel" text 
		g.setColor(Color.RED);
		g.setFont(GUI.bodyFont);
		if (eventIndex == -1) { // Display the 'cancel' text
			Point deletePoint = View.centerString(eventAbandonHint,
					new Rectangle(topLeftX + 12, topLeftY + 10 + 275 + 20 - 2, boxWidth + width - 24, 45), bodyMetric);
			g.drawString(eventAbandonHint, deletePoint.x, deletePoint.y);
		} else { // Display the 'delete' text
			Point deletePoint = View.centerString(eventDeleteHint,
					new Rectangle(topLeftX + 12, topLeftY + 10 + 275 + 20 - 2, boxWidth + width - 24, 45), bodyMetric);
			g.drawString(eventDeleteHint, deletePoint.x, deletePoint.y);
		}

	}

	
	// A function to determine if the popup window is visible (because if isDisplayingEvent is true, then it must be the case that this function
	// returns true.
	public boolean isDisplayingEvents() {
		return isEventsShown;
	}
	// A function to determine if the popup window is displaying the event editor
	public boolean isDisplayingEvent() {
		return isEventShown;
	}
	// A helper function to determine if the popup box is displayed on this cell on the screen.
	public boolean isShowingThisEvent(int gridX, int gridY) {
		return isEventShown && eventGridX == gridX && eventGridY == gridY;
	}

	// Sets up the fields for displaying a new event
	public void createEvent() {
		eventIndex = -1; // Index should be -1 for new events
		eventTitle = "";
		
		// Be a little helpful by displaying the start and end times are the 15 minute window the user is currently in
		// 	(e.g., if it is currently 9:54 a, then it will display 9:45 and 10:00) Only exception is that
		// if the time is current >11:45p, then it will display 23:45 and 23:59.
		Calendar e = (Calendar) Calendar.getInstance().clone();
		e.set(Calendar.MINUTE, e.get(Calendar.MINUTE) - (e.get(Calendar.MINUTE) % 15));
		eventTime1 = CalMathAbs.formatCalendarTime(e);
		e.add(Calendar.MINUTE, 15);
		if (e.get(Calendar.HOUR_OF_DAY) == 0 && e.get(Calendar.MINUTE) < 15) { // Check the edge case of >11:45p
			eventTime2 = "23:59";
		} else {
			eventTime2 = CalMathAbs.formatCalendarTime(e); // Otherwise use the calculated value
		}
		this.eventOrigDate = (Calendar) eventDate.clone();
		eventNotes = "";
		eventAllDay = false; // By default, it is not an all day event
		eventEvent = new CalendarEvent("", Calendar.getInstance(), Calendar.getInstance());
	}

	// This function will show the event editor popup window for the specified event (assumes that the day view was previously shown)
	// Passing -1 is for a new event, anything else will attempt to fetch the event's details and display them.
	// Assumption: EventI should be >= 6 as this is what is returned from getObjectClicked. 
	public void showEvent(int eventI) {
		selectedField = -1; // The selected field should always start with nothing selected
		if (eventI == -1) {
			this.createEvent(); // New event
		} else {
			Main.logDebug(3, "Shown! " + this.eventIndex);
			
			this.eventIndex = eventI - 6 + scrollBar; // Get actual index, including the offset from scrollBar
			Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, eventDate)); // Get the event's date
			ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal, false); 
			this.eventDate = curCal;
			this.eventOrigDate = (Calendar) curCal.clone(); // Make a copy of the current date
			eventEvent = events.get(this.eventIndex);
			eventTitle = eventEvent.getTitle();
			eventNotes = eventEvent.getNote();
			if (eventEvent.getTimeEnd().get(Calendar.SECOND) != 59) { // Determine if the event is an all day event
				eventAllDay = false;
				eventTime1 = CalMathAbs.formatCalendarTime(eventEvent.getTimeStart()); // Set eventTime1 and 2 to HH:MM values from the start/end times
				eventTime2 = CalMathAbs.formatCalendarTime(eventEvent.getTimeEnd());
			} else {
				// It is an all day event, the rendering events will update eventTime1 and 2 with the correct values
				eventAllDay = true;
				eventTime1 = "";
				eventTime2 = "";
			}
		}
		isEventShown = true;
	}
	
	
	// Will try to save the event to the current day's list of events. Will return false if it can't be saved, true if is saved successfully.
	public boolean attemptSaving() {
		if (eventTitle != "") { // Only requirement (as the rest is autofilled)
			// Create a new calendar event object
			CalendarEvent cc = new CalendarEvent("", Calendar.getInstance(), Calendar.getInstance());
			Calendar sc = CalMathAbs.GetDayCal(eventGridY, eventGridX, this.eventDate); // Get the cell's jCalendar object
			Calendar ec = CalMathAbs.GetDayCal(eventGridY, eventGridX, this.eventDate);
			cc.setTitle(this.eventTitle); // Set the title and notes field for this event
			cc.setNote(this.eventNotes);
			// If the event is all day, we set the start and end time fields to very precise values, otherwise we simply just set the minutes and hours.
			if (this.eventAllDay) {
				ec.set(Calendar.SECOND, 59);
				ec.set(Calendar.MINUTE, 59);
				ec.set(Calendar.HOUR_OF_DAY, 23);
				sc.set(Calendar.SECOND, 0);
				sc.set(Calendar.MINUTE, 0);
				sc.set(Calendar.HOUR_OF_DAY, 0);
			} else {
				// The strings have HH:MM, so the substring is to isolate HH and MM.
				ec.set(Calendar.MINUTE, Integer.parseInt(this.eventTime2.substring(3)));
				ec.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.eventTime2.substring(0, 2))); 
				sc.set(Calendar.MINUTE, Integer.parseInt(this.eventTime1.substring(3)));
				sc.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.eventTime1.substring(0, 2)));
			}
			// Set the start and end times for this event
			cc.setTimeStart(sc);
			cc.setTimeEnd(ec);
			ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(eventDate, true); // Get the date to add the event to
			
			// Print the variables to debugger
			Main.logDebug(3,CalMathAbs.printDate(eventDate));
			Main.logDebug(3,CalMathAbs.printDate(sc));
			Main.logDebug(3,CalMathAbs.printDate(ec));
			Main.logDebug(3,CalMathAbs.printDate(eventOrigDate));
			Main.logDebug(3,eventGridX + " " + eventGridY);
			
			
			if (eventIndex == -1) {
				// Add this event to the list
				events.add(cc);
			} else {
				if (eventDate.equals(eventOrigDate)) {
					// If the date is the same, simply update the event at this position
					CalendarEvent ee = events.get(this.eventIndex);
					ee.setNote(ee.getNote());
					ee.setTitle(cc.getTitle());
					ee.setTimeEnd(cc.getTimeEnd());
					ee.setTimeStart(cc.getTimeStart());
				} else {
					// If the date is not the same, we need to delete the old one and add the new one to the new list
					ArrayList<CalendarEvent> eventsOrig = GUI.instance.main.globalCalendar.grab(this.eventOrigDate, true);
					eventsOrig.remove(this.eventIndex);
					events.add(cc);
				}

			}
			// Successfully added
			return true;
		}
		// It failed to save, so return false. (Only reason is that the title is empty)
		return false;
	}

	// A simple function that will set necessary variables to default values (e.g. -1, false, 0)
	// to allow for rendering logic to not display the event editor. Note, if the event editor was
	// opened without prior opening the day view, this will cause the system to jump straight
	// back to the month view, but if the day view was opened prior to the event editor, 
	// this will cause the event editor to close and the day view to open.
	public void hideEvent() {
		// Just set all of these values to their default (null like) values.
		isEventShown = false;
		selectedField = -1;
		eventTitle = "";
		eventEvent = null;
		this.eventIndex = -1;
		eventAllDay = false;
		eventTime1 = "";
		eventTime2 = "";
		this.delta = 0;
		this.overField = -1;
		this.prevContents = "";
		this.redBox = false;
		scrollBar = 0;
		eventNotes = "";
		selectedField = -1;
	}


	

	// A simple function that takes a col and a row and will display the event popup on that day. Setting necessary values as needed.
	public void showEvents(int gridX, int gridY) {
		eventGridX = gridX;
		eventGridY = gridY;
		isEventsShown = true;
		eventDate = CalMathAbs.GetDayCal(gridY, gridX, GUI.instance.main.cal);
		scrollBar = 0;
		selectedField = -1;

	}

	// A simple function that will set necessary variables to default values (e.g. -1, false, 0)
	// to allow for rendering logic to not display the day view. 
	public void hideEvents() {
		eventGridX = -1;
		eventGridY = -1;
		isEventsShown = false;
		eventDate = null;
		selectedField = -1;
		scrollBar = 0;
	}

	// A simple function that will, when provided with a mouse position, will return which cell was clicked in (col, row), or (-1,-1) 
	// if the click was on the titlebar. 
	public Point getGUIBox(Point mousePos) {
		if (mousePos.y <= 88) {
			return new Point(-1, -1); // Represents out of bounds.
		}
		return new Point((int) (mousePos.x - (mousePos.x % width)) / width,
				(int) ((mousePos.y - 88) - ((mousePos.y - 88) % height)) / height);
		// Take the posiition, and take the integer division (floor division) of it with the cell width and height
		// to get the cell column and row, however we subtract off 88 from y to account for the cells starting at y=88 and not y=0.
	}

	//Takes a mouse position, modifies (a copy) of it to be relative to the popup box, and determine if any of the
	// of the clickable events were clicked.
	
	// Reason why this is commented out, the reason is that we decided that allowing the user to click on an event in the month
	// view to display it would be a bad idea because to get to the day view you would need to click in an extremely small space.
	// Hence we decided that clicking a cell should bring you to the day view without checking to see if it should go straight
	// to the event view.
	/*
	private int getEventBox(Point mousePos, Point boxPos) {

		Point p = new Point(mousePos.x - (boxPos.x * width), mousePos.y - 88 - (boxPos.y * height));

		if (p.x <= 4 || p.x >= 173 || p.y < 22 || (p.y - 22) % 24 > 20 || p.y >= 116) {
			return -1;
		}

		return (int) ((p.y - 22) - ((p.y - 22) % 24)) / 24;
	}*/

	// A function for determining if the left or right arrow at the top of the
	// screen was clicked. Returns -1 for left month clicked, +1 for right month clicked, and 0 otherwise.
	public int getTitleBox(Point mousePos) {

		if (mousePos.x >= LeftArrowMonthPosition.x && mousePos.y >= LeftArrowMonthPosition.y
				&& mousePos.x <= LeftArrowMonthPosition.x + leftImage.getWidth(null)
				&& mousePos.y <= LeftArrowMonthPosition.y + leftImage.getHeight(null)) {
			return -1;
		} // Checks if the mouse position was inside of the bounds of the month left arrow position
		
		
		if (mousePos.x >= RightArrowMonthPosition.x && mousePos.y >= RightArrowMonthPosition.y
				&& mousePos.x <= RightArrowMonthPosition.x + leftImage.getWidth(null)
				&& mousePos.y <= RightArrowMonthPosition.y + leftImage.getHeight(null)) {
			return 1;
		} // Checks if the mouse position was inside of the bounds of the month right arrow position
		
		return 0; // Return 0 if neither was true.

	}

	// A helper function that will take a string and will chop it and add triple
	// dots if it is too long, returns new string with modifications.
	public static String chopTitle(String title, int length) {
		if (title.length() > length) {
			return title.substring(0, length - 3) + "...";
		}
		return title;
	}

	// A helper function that will take a string and will center it vertically and
	// horizontally in the rectangle provided, returns point to draw the string at
	// for the specified font.
	public static Point centerString(String s, Rectangle rect, FontMetrics fm) {

		return new Point(rect.x + (rect.width - fm.stringWidth(s)) / 2,
				rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent());
	}

	// A helper function that will take a string and will center it vertically in
	// the rectangle provided, still left justified, returns point to draw the
	// string at for the specified font.
	public static Point leftCenterString(String s, Rectangle rect, FontMetrics fm) {

		return new Point(rect.x, rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent());
	}

	private final String eventTitleHint = "Enter Event Name (Max 40 chars, Req.).";
	private final String eventNotesHint = "Enter Event Notes (Max 40 characters).";
	private final String eventDeleteHint = "Delete Event";
	private final String eventAbandonHint = "Cancel";
	private final String eventSaveHint = "Save Event";
	private String eventTitle = "";
	private String eventNotes = "";
	private boolean eventAllDay = false;
	private String eventTime1 = "10:00";
	private String eventTime2 = "20:00";
	private int overField = -1;
	private int typingTimeLoc = -1;
	private String prevContents = "";
	private int eventIndex = -1; // -1 if new event, >= 0 if editing an event.
	private boolean isEventsShown = false;
	private boolean isEventShown = false;
	private int eventGridX = 0;
	private int eventGridY = 0;
	private boolean redBox = false;
	private long delta = 0;
	private Calendar eventDate = CalMathAbs.ClearTime(Calendar.getInstance());
	private Calendar eventOrigDate = CalMathAbs.ClearTime(Calendar.getInstance());
	private CalendarEvent eventEvent = null;
	private int scrollBar = 0;
	private int selectedField = -1;
	private int boxHeight = 360;
	private int boxWidth = 400;
	private int height = 128;
	private int width = 181;


	/*
	 * Getters and setters for most private variables that are suppose to be changed
	 * or read by outside modules.
	 */
	public int getBoxHeight() {
		return boxHeight;
	}

	public int getBoxWidth() {
		return boxWidth;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public String getEventNotes() {
		return eventNotes;
	}

	public boolean isEventAllDay() {
		return eventAllDay;
	}

	public String getEventTime1() {
		return eventTime1;
	}

	public String getEventTime2() {
		return eventTime2;
	}

	public int getOverField() {
		return overField;
	}

	public int getTypingTimeLoc() {
		return typingTimeLoc;
	}

	public String getPrevContents() {
		return prevContents;
	}

	public int getEventIndex() {
		return eventIndex;
	}

	public boolean isEventsShown() {
		return isEventsShown;
	}

	public boolean isEventShown() {
		return isEventShown;
	}

	public int getEventGridX() {
		return eventGridX;
	}

	public int getEventGridY() {
		return eventGridY;
	}

	public boolean isRedBox() {
		return redBox;
	}

	public long getDelta() {
		return delta;
	}

	public Calendar getEventDate() {
		return eventDate;
	}

	public Calendar getEventOrigDate() {
		return eventOrigDate;
	}

	public CalendarEvent getEventEvent() {
		return eventEvent;
	}

	public int getScrollBar() {
		return scrollBar;
	}

	public int getSelectedField() {
		return selectedField;
	}

	public void setBoxHeight(int boxHeight) {
		this.boxHeight = boxHeight;
	}

	public void setBoxWidth(int boxWidth) {
		this.boxWidth = boxWidth;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public void setEventNotes(String eventNotes) {
		this.eventNotes = eventNotes;
	}

	public void setEventAllDay(boolean eventAllDay) {
		this.eventAllDay = eventAllDay;
	}

	public void setEventTime1(String eventTime1) {
		this.eventTime1 = eventTime1;
	}

	public void setEventTime2(String eventTime2) {
		this.eventTime2 = eventTime2;
	}

	public void setOverField(int overField) {
		this.overField = overField;
	}

	public void setTypingTimeLoc(int typingTimeLoc) {
		this.typingTimeLoc = typingTimeLoc;
	}

	public void setPrevContents(String prevContents) {
		this.prevContents = prevContents;
	}

	public void setEventIndex(int eventIndex) {
		this.eventIndex = eventIndex;
	}

	public void setEventsShown(boolean isEventsShown) {
		this.isEventsShown = isEventsShown;
	}

	public void setEventShown(boolean isEventShown) {
		this.isEventShown = isEventShown;
	}

	public void setEventGridX(int eventGridX) {
		this.eventGridX = eventGridX;
	}

	public void setEventGridY(int eventGridY) {
		this.eventGridY = eventGridY;
	}

	public void setRedBox(boolean redBox) {
		this.redBox = redBox;
	}

	public void setDelta(long delta) {
		this.delta = delta;
	}

	public void setEventDate(Calendar eventDate) {
		this.eventDate = eventDate;
	}

	public void setEventOrigDate(Calendar eventOrigDate) {
		this.eventOrigDate = eventOrigDate;
	}

	public void setEventEvent(CalendarEvent eventEvent) {
		this.eventEvent = eventEvent;
	}

	public void setScrollBar(int scrollBar) {
		this.scrollBar = scrollBar;
	}

	public void setSelectedField(int selectedField) {
		this.selectedField = selectedField;
	}
}
