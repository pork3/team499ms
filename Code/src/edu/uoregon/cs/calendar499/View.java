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

public class View {

	private static final String[] months = { "January", "Feburary", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };
	private static final String[] days = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };

	private static FontMetrics titleMetric;
	private static FontMetrics bodyMetric;
	private static FontMetrics eventMetric;
	private static FontMetrics editMetric;
	private static FontMetrics daysMetric;
	private static Color currentDayColor = new Color(0.9f, 0.9f, 1.0f);

	// private Image plusImage = new
	// ImageIcon(this.getClass().getResource("plus.png")).getImage();
	private Image leftImage = new ImageIcon(this.getClass().getResource("left.png")).getImage();
	private Image leftImage1 = new ImageIcon(this.getClass().getResource("left1.png")).getImage();
	private Image upImage = new ImageIcon(this.getClass().getResource("up.png")).getImage();
	private Image xImage = new ImageIcon(this.getClass().getResource("x.png")).getImage();

	public void Display(Graphics2D g, Calendar c) {
		titleMetric = (titleMetric != null ? titleMetric : g.getFontMetrics(GUI.titleFont));
		daysMetric = (daysMetric != null ? daysMetric : g.getFontMetrics(GUI.daysFont));
		bodyMetric = (bodyMetric != null ? bodyMetric : g.getFontMetrics(GUI.bodyFont));
		eventMetric = (eventMetric != null ? eventMetric : g.getFontMetrics(GUI.eventFont));
		editMetric = (editMetric != null ? editMetric : g.getFontMetrics(GUI.editFont));
		Font tempFont = g.getFont();
		Stroke tempStroke = g.getStroke();
		g.setFont(GUI.titleFont);

		g.setColor(Color.BLACK);

		String title = months[c.get(Calendar.MONTH)] + ", " + c.get(Calendar.YEAR);

		Point titlePosition = View.centerString(title, new Rectangle(0, 10, GUI.windowSize.width, 40), titleMetric);

		g.drawString(title, titlePosition.x, titlePosition.y);

		g.setFont(GUI.bodyFont);
		for (int day = 0; day < 7; day++) {
			Point dayPosition = View.centerString(days[day], new Rectangle((day * width) - 1, 59, width, 26),
					bodyMetric);
			g.drawString(days[day], dayPosition.x, dayPosition.y);
		}

		// Left and right arrow...
		g.drawImage(leftImage, 35 + 326, 11, 75 + 326, 51, 0, 0, leftImage.getWidth(null), leftImage.getHeight(null),
				null);
		g.drawImage(leftImage, GUI.instance.getWidth() - 405, 11, GUI.instance.getWidth() - 365, 51,
				leftImage.getWidth(null), 0, 0, leftImage.getHeight(null), null);

		for (int week = 0; week < 6; ++week) {
			for (int day = 0; day < 7; ++day) {
				// GUI.instance.dayView.Display(g, CalMathAbs.GetDayCal(week, day, c));
				g.setStroke(GUI.boxStroke);
				g.setFont(GUI.daysFont);
				int dayNumber = CalMathAbs.GetDayN(week, day, c);
				boolean isOtherMonth = CalMathAbs.IsNotCalMonth(week, day, c);
				boolean isCurrentDay = CalMathAbs.IsCurrentDay(week, day, c);

				if (isCurrentDay) {
					// Display in blue
					g.setColor(currentDayColor);

				} else if (isOtherMonth) {
					// Display the day with grey background
					g.setColor(Color.LIGHT_GRAY);
				} else {
					// Display the day normally
					g.setColor(Color.white);
				}
				// draw the background of the day
				g.fillRect(width * day - 1, height * week + 88, width, height);
				// draw the box around the day
				g.setColor(Color.black);
				g.drawRect(width * day - 1, height * week + 88, width, height);
				// draw the date in the top left
				Point dayPosition = View.centerString(dayNumber + (dayNumber < 10 ? " " : ""),
						new Rectangle(width * day + 17, height * week + 84, 10, 22), bodyMetric);
				g.drawString(dayNumber + "", dayPosition.x, dayPosition.y);

				Color eventBackgroundColor = new Color(0.0f, 0.4f, 0.9f, 0.3f);
				Color eventPrevMonthBackgroundColor = new Color(0.0f, 0.45f, 1.0f, 0.4f);
				Color eventCurrentDayBackgroundColor = new Color(0.2f, 0.5f, 1.0f, 0.4f);

				// Draw events here!
				Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(week, day, c));
				ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal);
				// System.out.println(curCal);
				if (events != null && !events.isEmpty()) {
					for (int i = 0; i < events.size(); i++) {
						if (isCurrentDay) {
							g.setColor(eventCurrentDayBackgroundColor);
						} else if (isOtherMonth) {
							g.setColor(eventPrevMonthBackgroundColor);
						} else {
							g.setColor(eventBackgroundColor);
						}
						g.fillRect(width * day + 5, height * week + 88 + 22 + i * 24, width - 12, 20);
						g.setColor(Color.BLACK);
						g.setFont(GUI.eventFont);

						String eventTitle = chopTitle(
								CalMathAbs.ToHM(events.get(i).getTimeStart()) + "-"
										+ CalMathAbs.ToHM(events.get(i).getTimeEnd()) + " " + events.get(i).getTitle(),
								24);
						if (i == 3 && events.size() != 4) {
							// We need to draw a box saying "+ X more..." along with a + icon...
							g.setFont(GUI.eventItalFont);
							eventTitle = "+" + (events.size() - 3) + " more...";

							Point eventPosition = View.leftCenterString(eventTitle,
									new Rectangle(width * day + 20, height * week + 88 + 22 + i * 24, width - 12, 20),
									eventMetric);
							g.drawString(eventTitle, eventPosition.x, eventPosition.y);
							break;
						}
						Point eventPosition = View.leftCenterString(eventTitle,
								new Rectangle(width * day + 7, height * week + 88 + 22 + i * 24, width - 12, 20),
								eventMetric);
						g.drawString(eventTitle, eventPosition.x, eventPosition.y);
					}
				}
				// 23 - 11 = 12 chars titles...
			}
		}

		if (isDisplayingEvents() || isDisplayingEvent()) {
			// Display a white box...
			// First figure out if it needs to be above or below the current day...
			// Those in the first 3 weeks will be below
			// Those in the last 3 weeks will be above

			Point eventBox = new Point(0, 0);
			Polygon popupPoly = new Polygon();
			Calendar today = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, c));
			int currentBoxHeight = boxHeight;
			if (!isDisplayingEvent() && GUI.instance.main.globalCalendar.grab(today).size() <= 3) {
				currentBoxHeight = boxHeight - 55 * (4 - GUI.instance.main.globalCalendar.grab(today).size());
			}
			eventBox = this.getPopupBox();
			if (eventGridY > 2) {
				// Show above...
				popupPoly.addPoint(eventBox.x, eventBox.y); // Top left
				// Arrow

				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y); // Top right
				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y + currentBoxHeight); // Bottom right
				popupPoly.addPoint(width * eventGridX + width / 2 + 40, eventBox.y + currentBoxHeight);

				popupPoly.addPoint(width * eventGridX + width / 2, eventBox.y + currentBoxHeight + 10);
				popupPoly.addPoint(width * eventGridX + width / 2 - 40, eventBox.y + currentBoxHeight);
				popupPoly.addPoint(eventBox.x, eventBox.y + currentBoxHeight); // Bottom left

			} else {
				// Show below...
				popupPoly.addPoint(eventBox.x, eventBox.y); // Top left
				// Arrow
				popupPoly.addPoint(width * eventGridX + width / 2 - 40, eventBox.y);
				popupPoly.addPoint(width * eventGridX + width / 2, height * (eventGridY + 1) + 95);
				popupPoly.addPoint(width * eventGridX + width / 2 + 40, eventBox.y);
				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y); // Top right
				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y + currentBoxHeight); // Bottom right
				popupPoly.addPoint(eventBox.x, eventBox.y + currentBoxHeight); // Bottom left
			}
			g.setColor(Color.white);
			g.fillPolygon(popupPoly);
			g.setColor(Color.black);
			g.drawPolygon(popupPoly);
			g.drawLine(popupPoly.xpoints[0], popupPoly.ypoints[0] + 55, popupPoly.xpoints[0] + width + boxWidth,
					popupPoly.ypoints[0] + 55);
			g.setFont(GUI.editFont);

			String dateString = today.get(Calendar.DATE) + " " + months[today.get(Calendar.MONTH)] + ", "
					+ today.get(Calendar.YEAR);
			Point datePoint = null;

			if (!isDisplayingEvent()) {
				// set up how the thing is rendered.

				datePoint = View.centerString(dateString,
						new Rectangle(popupPoly.xpoints[0] + boxWidth / 2, popupPoly.ypoints[0] + 15, width, 30),
						editMetric);
				g.drawString(dateString, datePoint.x, datePoint.y);

				renderEvents(g, popupPoly.xpoints[0], popupPoly.ypoints[0]);
			} else {
				if (eventIndex == -1) {
					dateString = dateString + " - New Event";
				} else {
					dateString = dateString + " - Edit Event";
				}
				datePoint = View.centerString(dateString,
						new Rectangle(popupPoly.xpoints[0] + boxWidth / 2, popupPoly.ypoints[0] + 15, width, 30),
						editMetric);
				g.drawString(dateString, datePoint.x, datePoint.y);
				renderEvent(g, popupPoly.xpoints[0], popupPoly.ypoints[0]);
			}

			g.drawImage(leftImage1, datePoint.x - 50, datePoint.y - 26, datePoint.x - 10, datePoint.y + 40 - 26, 0, 0,
					leftImage1.getWidth(null), leftImage1.getHeight(null), null);

			g.drawImage(leftImage1, 2 * popupPoly.xpoints[0] + boxWidth + width - datePoint.x + 10,
					datePoint.y - 20 - 6, 2 * popupPoly.xpoints[0] + boxWidth + width - datePoint.x + 10 + 40,
					datePoint.y + 20 - 6, leftImage1.getWidth(null), 0, 0, leftImage1.getHeight(null), null);

			g.drawImage(xImage, popupPoly.xpoints[0] + 10, popupPoly.ypoints[0] + 10,
					popupPoly.xpoints[0] + xImage.getWidth(null) + 10,
					popupPoly.ypoints[0] + xImage.getHeight(null) + 10, 0, 0, xImage.getWidth(null),
					xImage.getHeight(null), null);
		}

		g.setStroke(tempStroke);
		g.setFont(tempFont);
	}

	public Point getPopupBox() {
		if(!this.isDisplayingEvent() && !this.isDisplayingEvents()) {
			return new Point(-1,-1);
		}
		Point adjustedPosition = null;
		int currentBoxHeight = getPopupBoxHeight();
		
		if(eventGridY > 2) {
			adjustedPosition = new Point(
					width * eventGridX - (eventGridX <= 1 ? -5 + (eventGridX) * width : boxWidth / 2)
							- (eventGridX >= 5 ? boxWidth / 2 - (6 - eventGridX) * width + 5 : 0),
					height * (eventGridY + 1) - currentBoxHeight - 55);

		}else {
			adjustedPosition = new Point(
					width * eventGridX - (eventGridX <= 1 ? -5 + (eventGridX) * width : boxWidth / 2)
							- (eventGridX >= 5 ? boxWidth / 2 - (6 - eventGridX) * width + 5 : 0),
					height * (eventGridY + 1) + 105);
		}
		
		
		return adjustedPosition;
	}
	
	public int getPopupBoxHeight() {
		int currentBoxHeight = boxHeight;
		Calendar today = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, GUI.instance.main.cal));
		if (!isDisplayingEvent() && GUI.instance.main.globalCalendar.grab(today).size() <= 3) {
			currentBoxHeight = boxHeight - 55 * (4 - GUI.instance.main.globalCalendar.grab(today).size());
		}
		return currentBoxHeight;
	}
	
	public int getObjectClicked(Point mousePos) {
		Point topLeft = getPopupBox();
		int boxHeight = getPopupBoxHeight();
		
		if(!(new Rectangle(topLeft.x, topLeft.y, boxWidth + width, boxHeight).contains(mousePos))) {
			return -1;
		}
		System.out.println((mousePos.x - topLeft.x) + " " + (mousePos.y - topLeft.y));
		Point newPosition = new Point(mousePos.x - topLeft.x, mousePos.y - topLeft.y);
		if(new Rectangle(10, 10, 32, 32).contains(newPosition)) {
			return -2;
		}
		if(new Rectangle(30,70,520, 45).contains(newPosition)) {
			return 1;
		}
		Calendar today = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, GUI.instance.main.cal));
		String dateString = today.get(Calendar.DATE) + " " + months[today.get(Calendar.MONTH)] + ", "
				+ today.get(Calendar.YEAR);
		Point datePoint = View.centerString(dateString,
				new Rectangle(topLeft.x + boxWidth / 2, topLeft.y + 15, width, 30),
				editMetric);
		System.out.println(datePoint + " " + mousePos);
		if(new Rectangle(datePoint.x - 45, datePoint.y - 21, 30, 30).contains(mousePos)) {
			return 2;
		}

		if(new Rectangle(2 * topLeft.x + boxWidth + width - datePoint.x + 15, datePoint.y - 21, 30, 30).contains(mousePos)) {
			return 3;
		}
		return 0;
	}
	
	public void moveToLeft() {
		
		if((eventGridY == 0 && eventGridX == 0) || (CalMathAbs.IsNotCalMonth(eventGridY, eventGridX, GUI.instance.main.cal))) {
			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR) + (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? -1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH) + (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? 11 : -1));
			
			
		}else {
			eventGridY -= (eventGridX == 0 ? 1 : 0);
			eventGridX -= (eventGridX == 0 ? -6 : 1);
		}
	}

	public int boxHeight = 360;
	public int boxWidth = 400;
	public int height = 128;
	public int width = 181;

	public void renderEvents(Graphics2D g, int topLeftX, int topLeftY) {
		// Draw a square to add a new event...
		g.setColor(Color.BLACK);
		g.setFont(GUI.bodyFont);
		g.drawRect(topLeftX + 30, topLeftY + 70, boxWidth + width - 60, 45);
		String addNew = "+Add Event";
		Point addPoint = View.centerString(addNew,
				new Rectangle(topLeftX + 30, topLeftY + 70, boxWidth + width - 60, 45), bodyMetric);

		g.drawString(addNew, addPoint.x, addPoint.y);

		Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, eventDate));
		ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal);

		boolean renderBar = events.size() > 4;
		for (int i = 0; i < events.size(); i++) {
			if (i > 3) {
				break;
			}
			g.drawRect(topLeftX + 30, topLeftY + 70 + 65 + 55 * i, boxWidth + width - 60 - (renderBar ? 40 : 0), 45);

			// Render events!
			String renderString = chopTitle(CalMathAbs.ToHM(events.get(i + scrollBar).getTimeStart()) + "-"
					+ CalMathAbs.ToHM(events.get(i + scrollBar).getTimeEnd()) + " "
					+ events.get(i + scrollBar).getTitle(), 29);
			Point renderEvent = View.centerString(renderString, new Rectangle(topLeftX + 30,
					topLeftY + 70 + 65 + 55 * i, boxWidth + width - 60 - (renderBar ? 40 : 0), 45), bodyMetric);

			g.drawString(renderString, renderEvent.x, renderEvent.y);

		}
		// Draw arrows
		if (renderBar) {
			g.drawImage(upImage, topLeftX + boxWidth + width - upImage.getWidth(null) - 10, topLeftY + 70 + 65,
					topLeftX + boxWidth + width - 10, topLeftY + 70 + 65 + upImage.getHeight(null), 0,
					upImage.getHeight(null), upImage.getWidth(null), 0, null);
			g.drawImage(upImage, topLeftX + boxWidth + width - upImage.getWidth(null) - 10, topLeftY + 70 + 65 + 55 * 3,
					topLeftX + boxWidth + width - 10, topLeftY + 70 + 65 + upImage.getHeight(null) + 55 * 3, 0, 0,
					upImage.getWidth(null), upImage.getHeight(null), null);
		}

	}

	public void renderEvent(Graphics2D g, int topLeftX, int topLeftY) {
		g.setColor(Color.BLACK);

		g.setFont(GUI.editFont);

		g.drawRect(topLeftX + 10, topLeftY + 10 + 55, boxWidth + width - 20, 45);

		g.drawRect(topLeftX + 10, topLeftY + 10 + 165, boxWidth + width - 20, 45);

		g.drawRect(topLeftX + 150, topLeftY + 10 + 165 + 55, boxWidth + width - 300, 45);

		g.drawRect(topLeftX + 150, topLeftY + 10 + 275 + 20, boxWidth + width - 300, 45);

		String dispTitle = eventTitle;
		if (eventTitle.length() == 0) {
			g.setColor(new Color(0.4f, 0.4f, 0.4f, 0.6f));
			g.setFont(GUI.editItalFont);
			dispTitle = eventTitleHint;
		}
		String dispTitleEdit = "";
		if (selectedField == 0 && (GUI.instance.frame.frameCount % 50) < 25) {
			dispTitleEdit = (dispTitle.replaceAll("[\\s\\S]", " ")) + "|";
		}

		Point titlePoint = View.centerString(dispTitle,
				new Rectangle(topLeftX + 12, topLeftY + 10 + 55, boxWidth + width - 24, 45), editMetric);
		g.drawString(dispTitleEdit, titlePoint.x - 5, titlePoint.y - 2);

		g.drawString(dispTitle, titlePoint.x, titlePoint.y);

		g.setColor(Color.BLACK);
		g.setFont(GUI.editFont);

		// Time

		g.drawRect(topLeftX + 10, topLeftY + 10 + 110, boxWidth + width - 290, 45);

		g.drawRect(topLeftX + 10 + boxWidth + width - 275, topLeftY + 10 + 110, 255, 45);
		Point allDay = View.centerString("All Day Event?",
				new Rectangle(topLeftX + 10 + boxWidth + width - 270, topLeftY + 10 + 110, 200, 45), editMetric);
		g.drawString("All Day Event?", allDay.x, allDay.y);

		g.drawRect(topLeftX + 10 + boxWidth + width - 65, topLeftY + 21 + 110, 25, 25);

		if (eventAllDay) {
			g.setColor(Color.blue);
			g.fillRect(topLeftX + 10 + boxWidth + width - 62, topLeftY + 21 + 113, 19, 19);
			eventTime1 = "00:00";
			eventTime2 = "23:59";

			// Change how the next two boxes are rendered...
			g.setColor(new Color(0.4f, 0.4f, 0.4f, 0.6f));
			g.setFont(GUI.editItalFont);
			dispTitle = eventTitleHint;

		}

		g.setFont(GUI.editFont);

		String startTSEdit = eventTime1;
		if (!eventAllDay && selectedField == 2 && (GUI.instance.frame.frameCount % 50) < 25) {
			startTSEdit = (startTSEdit.replaceAll("[\\s\\S]", " ")) + "|";
			Point startTSPoint = View.centerString(startTSEdit,
					new Rectangle(topLeftX + 13, topLeftY + 10 + 110 - 2, (boxWidth + width - 290) / 2, 45),
					editMetric);
			g.drawString(startTSEdit, startTSPoint.x, startTSPoint.y);

		}

		String endTSEdit = eventTime2;
		if (!eventAllDay && selectedField == 3 && (GUI.instance.frame.frameCount % 50) < 25) {
			endTSEdit = (endTSEdit.replaceAll("[\\s\\S]", " ")) + "|";
			Point endTSPoint = View.centerString(endTSEdit, new Rectangle(topLeftX + 13 + (boxWidth + width - 290) / 2,
					topLeftY + 10 + 110 - 2, (boxWidth + width - 290) / 2, 45), editMetric);
			g.drawString(endTSEdit, endTSPoint.x, endTSPoint.y);
		}

		Point time1 = View.centerString(eventTime1,
				new Rectangle(topLeftX + 10, topLeftY + 10 + 110, (boxWidth + width - 290) / 2, 45), editMetric);
		g.drawString(eventTime1, time1.x, time1.y);
		Point time2 = View.centerString(eventTime2, new Rectangle(topLeftX + 10 + (boxWidth + width - 290) / 2,
				topLeftY + 10 + 110, (boxWidth + width - 290) / 2, 45), editMetric);
		g.drawString(eventTime2, time2.x, time2.y);

		g.setColor(Color.black);
		g.drawLine(topLeftX + 10 + (boxWidth + width - 290) / 2, topLeftY + 10 + 110,
				topLeftX + 10 + (boxWidth + width - 290) / 2, topLeftY + 10 + 155);

		g.setFont(GUI.editFont);

		// Notes

		String dispNotes = eventNotes;
		if (eventNotes.length() == 0) {
			g.setColor(new Color(0.4f, 0.4f, 0.4f, 0.6f));
			g.setFont(GUI.editItalFont);
			dispNotes = eventNotesHint;
		}

		String dispNotesEdit = "";
		if (selectedField == 4 && (GUI.instance.frame.frameCount % 50) < 25) {
			dispNotesEdit = (dispNotes.replaceAll("[\\s\\S]", " ")) + "|";
		}

		Point notesPoint = View.centerString(dispNotes,
				new Rectangle(topLeftX + 12, topLeftY + 10 + 165, boxWidth + width - 24, 45), editMetric);
		g.drawString(dispNotesEdit, notesPoint.x - 5, notesPoint.y - 2);

		g.drawString(dispNotes, notesPoint.x, notesPoint.y);

		g.setColor(Color.black);
		g.setFont(GUI.bodyFont);
		Point savePoint = View.centerString(eventSaveHint,
				new Rectangle(topLeftX + 12, topLeftY + 10 + 165 + 34 + 20, boxWidth + width - 24, 45), bodyMetric);
		g.drawString(eventSaveHint, savePoint.x, savePoint.y);

		// Delete
		g.setColor(Color.RED);
		g.setFont(GUI.bodyFont);
		if (eventIndex == -1) {
			Point deletePoint = View.centerString(eventAbandonHint,
					new Rectangle(topLeftX + 12, topLeftY + 10 + 275 + 20 - 2, boxWidth + width - 24, 45), bodyMetric);
			g.drawString(eventAbandonHint, deletePoint.x, deletePoint.y);
		} else {
			Point deletePoint = View.centerString(eventDeleteHint,
					new Rectangle(topLeftX + 12, topLeftY + 10 + 275 + 20 - 2, boxWidth + width - 24, 45), bodyMetric);
			g.drawString(eventDeleteHint, deletePoint.x, deletePoint.y);
		}

	}

	public final String eventTitleHint = "Enter Event Name (Max 40 characters).";
	public final String eventNotesHint = "Enter Event Notes (Max 40 characters).";
	public final String eventLocationHint = "Location.";
	public final String eventDeleteHint = "Delete Event";
	public final String eventAbandonHint = "Cancel";
	public final String eventTime1Hint = "Start Time";
	public final String eventSaveHint = "Save Event";
	public final String eventTime2Hint = "End Time";
	public String eventTitle = "";
	public String eventNotes = "";
	public String eventLocation = "";
	public boolean eventAllDay = false;
	public String eventTime1 = "10:00";
	public String eventTime2 = "20:00";

	public int eventIndex = -1; // -1 if new event, >= 0 if editing an event.
	public boolean isEventsShown = false;
	public boolean isEventShown = false;
	public int eventGridX = 0;
	public int eventGridY = 0;
	public Calendar eventDate = CalMathAbs.ClearTime(Calendar.getInstance());
	public CalendarEvent eventEvent = null;
	public int scrollBar = 0;
	public int selectedField = -1;
	/*
	 * Selected field: -1 is nothing is selected, should be set when user clicks on
	 * window but not on a field 0 is title field is selected. 1 is time field was
	 * clicked (and hence should show the time menu) 2 is that the user clicked the
	 * start time field 3 is that the user clicked the end time field 4 is that the
	 * user clicked the note field 5 is that the user clicked the location field.
	 */

	public boolean isDisplayingEvents() {
		return isEventsShown;
	}

	public boolean isDisplayingEvent() {
		return isEventShown;
	}

	public boolean isShowingThisEvent(int gridX, int gridY) {
		return isEventShown && eventGridX == gridX && eventGridY == gridY;
	}

	// Assumes that isEventsShown is true.
	public void createEvent() {
		// Creates a new event
		eventIndex = -1;
		eventTitle = "";

	}

	public void showEvent(int eventIndex) {

	}

	public void showEvents(int gridX, int gridY) {
		eventGridX = gridX;
		eventGridY = gridY;
		isEventsShown = true;
		eventDate = CalMathAbs.GetDayCal(gridX, gridY, GUI.instance.main.cal);
		scrollBar = 0;

	}
	public void hideEvents() {
		eventGridX = -1;
		eventGridY = -1;
		isEventsShown = false;
		eventDate = null;
		scrollBar = 0;
		
	}


	public Point getGUIBox(Point mousePos) {
		if (mousePos.y <= 88) {
			return new Point(-1, -1); // Represents out of bounds.
		}
		return new Point((int) (mousePos.x - (mousePos.x % width)) / width,
				(int) ((mousePos.y - 88) - ((mousePos.y - 88) % height)) / height);
	}

	public int getEventBox(Point mousePos, Point boxPos) {

		Point p = new Point(mousePos.x - (boxPos.x * width), mousePos.y - 88 - (boxPos.y * height));

		if (p.x <= 4 || p.x >= 173 || p.y < 22 || (p.y - 22) % 24 > 20 || p.y >= 116) {
			return -1;
		}

		return (int) ((p.y - 22) - ((p.y - 22) % 24)) / 24;
	}

	public int getTitleBox(Point mousePos) {

		if (mousePos.x >= 360 && mousePos.y >= 12 && mousePos.x <= 400 && mousePos.y <= 50) {
			return -1;
		}
		if (mousePos.x >= 865 && mousePos.y >= 12 && mousePos.x <= 905 && mousePos.y <= 50) {
			return 1;
		}
		return 0;

	}

	public static String chopTitle(String title, int length) {
		if (title.length() > length) {
			return title.substring(0, length - 3) + "...";
		}
		return title;
	}

	public static Point centerString(String s, Rectangle rect, FontMetrics fm) {

		return new Point(rect.x + (rect.width - fm.stringWidth(s)) / 2,
				rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent());
	}

	public static Point leftCenterString(String s, Rectangle rect, FontMetrics fm) {

		return new Point(rect.x, rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent());
	}

	
}
