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

	private static final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };
	private static final String[] days = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };

	private static FontMetrics titleMetric;
	private static FontMetrics bodyMetric;
	private static FontMetrics eventMetric;
	private static FontMetrics editMetric;
	private static FontMetrics daysMetric;
	private static Color currentDayColor = new Color(0.9f, 0.9f, 1.0f);
	private static Color currentDayColor2 = new Color(0.3f, 0.3f, 0.3f, 0.2f);
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
					g.fillRect(width * day - 1, height * week + 88, width, height);
					if (isOtherMonth) {
						g.setColor(currentDayColor2);
					}
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
								displayTime(events.get(i).getTimeStart(), events.get(i).getTimeEnd()) + " "
										+ events.get(i).getTitle(),
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
			if (!isDisplayingEvent()) {
				g.drawLine(popupPoly.xpoints[0], popupPoly.ypoints[0] + 55, popupPoly.xpoints[0] + width + boxWidth,
						popupPoly.ypoints[0] + 55);
			}
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
						new Rectangle(popupPoly.xpoints[0] + boxWidth / 2, popupPoly.ypoints[0] + 20, width, 30),
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
		if (!this.isDisplayingEvent() && !this.isDisplayingEvents()) {
			return new Point(-1, -1);
		}
		Point adjustedPosition = null;
		int currentBoxHeight = getPopupBoxHeight();

		if (eventGridY > 2) {
			adjustedPosition = new Point(
					width * eventGridX - (eventGridX <= 1 ? -5 + (eventGridX) * width : boxWidth / 2)
							- (eventGridX >= 5 ? boxWidth / 2 - (6 - eventGridX) * width + 5 : 0),
					height * (eventGridY + 1) - currentBoxHeight - 55);

		} else {
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

	public void scrollDown() {

		if (!this.isDisplayingEvent() && this.isDisplayingEvents()) {
			Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, eventDate));
			ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal);
			System.out.println(events.size());
			if (events.size() >= 4) {
				this.scrollBar = (this.scrollBar + 4 >= events.size() ? events.size() - 4 : this.scrollBar + 1);
				return;
			}
		}
		this.scrollBar = 0;
	}

	public void scrollUp() {
		if (!this.isDisplayingEvent() && this.isDisplayingEvents()) {
			Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, eventDate));
			ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal);
			System.out.println(events.size());
			if (events.size() >= 4) {
				this.scrollBar = (this.scrollBar <= 0 ? 0 : this.scrollBar - 1);
				return;
			}
		}
		this.scrollBar = 0;
	}

	public int getObjectClicked(Point mousePos) {
		Point topLeft = getPopupBox();
		int boxHeight = getPopupBoxHeight();

		if (!(new Rectangle(topLeft.x, topLeft.y, boxWidth + width, boxHeight).contains(mousePos))) {
			return -1;
		}
		System.out.println((mousePos.x - topLeft.x) + " " + (mousePos.y - topLeft.y));
		Point newPosition = new Point(mousePos.x - topLeft.x, mousePos.y - topLeft.y);
		if (new Rectangle(10, 10, 32, 32).contains(newPosition)) {
			return -2;
		}
		Calendar today = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, GUI.instance.main.cal));
		String dateString = "";
		if (!this.isDisplayingEvent() && this.isDisplayingEvents()) {
			dateString = today.get(Calendar.DATE) + " " + months[today.get(Calendar.MONTH)] + ", "
					+ today.get(Calendar.YEAR);
			if (new Rectangle(30, 70, 520, 45).contains(newPosition)) {
				return 1;
			}
			Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, eventDate));
			ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal);
			int es = events.size();
			for (int i = 0; i < es; i++) {
				if (i > 3) {
					break;
				}
				if (new Rectangle(30, 70 + 65 + 55 * i, 520 - (es > 4 ? 40 : 0), 45).contains(newPosition)) {
					return 6 + i;
				}
			}
			if (es > 4) {
				if (new Rectangle(boxWidth + width - upImage.getWidth(null) - 10, 70 + 65, upImage.getWidth(null),
						upImage.getHeight(null)).contains(newPosition)) {
					return 4;
				}
				if (new Rectangle(boxWidth + width - upImage.getWidth(null) - 10, 70 + 65 + 55 * 3,
						upImage.getWidth(null), upImage.getHeight(null)).contains(newPosition)) {
					return 5;
				}
			}

		} else if (this.isDisplayingEvent()) {
			// Handles main event things o-0
			dateString = today.get(Calendar.DATE) + " " + months[today.get(Calendar.MONTH)] + ", "
					+ today.get(Calendar.YEAR) + " - " + (this.eventIndex == -1 ? "New Event" : "Edit Event");

			if (new Rectangle(10, 65, 560, 45).contains(newPosition)) {
				// Title
				return -3;
			}
			if (new Rectangle(10, 120, 140, 40).contains(newPosition) && !this.eventAllDay) {
				if (newPosition.x < 75) {
					return -14;
				}
				return -4;
			}
			if (new Rectangle(155, 120, 145, 165 - 120).contains(newPosition) && !this.eventAllDay) {
				if (newPosition.x < (145 / 2) + 155) {
					return -15;
				}
				return -5;
			}
			if (new Rectangle(150, 305, 430 - 145, 445 - 305).contains(newPosition)) {
				return -9;
			}

			if (new Rectangle(525, 130, 25, 25).contains(newPosition)) {
				return -6;
			}
			if (new Rectangle(10, 175, 560, 45).contains(newPosition)) {
				return -7;
			}
			if (new Rectangle(150, 230, 430 - 150, 45).contains(newPosition)) {
				return -8;
			}
		}
		if (this.isDisplayingEvents()) {
			Point datePoint = View.centerString(dateString,
					new Rectangle(topLeft.x + boxWidth / 2, topLeft.y + 15, width, 30), editMetric);
			System.out.println(datePoint + " " + mousePos);
			if (new Rectangle(datePoint.x - 45, datePoint.y - 21, 30, 30).contains(mousePos)) {
				return 2;
			}

			if (new Rectangle(2 * topLeft.x + boxWidth + width - datePoint.x + 15, datePoint.y - 21, 30, 30)
					.contains(mousePos)) {
				return 3;
			}
		}
		return 0;
	}

	public void moveToLeft() {

		if ((eventGridY == 0 && eventGridX == 0) || (CalMathAbs.IsNotCalMonth(eventGridY - (eventGridX == 0 ? 1 : 0),
				eventGridX - (eventGridX == 0 ? -6 : 1), GUI.instance.main.cal))) {

			int dayN = CalMathAbs.GetDayN(eventGridY - (eventGridX == 0 ? 1 : 0),
					eventGridX - (eventGridX == 0 ? -6 : 1), GUI.instance.main.cal);

			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? -1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? 11 : -1));
			GUI.instance.main.cal.set(Calendar.DATE, dayN);
			eventDate = (Calendar) GUI.instance.main.cal.clone();
			for (int week = 0; week < 6; week++) {
				for (int day = 0; day < 7; day++) {
					if (!CalMathAbs.IsNotCalMonth(week, day, GUI.instance.main.cal)
							&& CalMathAbs.GetDayN(week, day, GUI.instance.main.cal) == dayN) {
						eventGridX = day;
						eventGridY = week;
					}
				}
			}

		} else {
			eventGridY -= (eventGridX == 0 ? 1 : 0);
			eventGridX -= (eventGridX == 0 ? -6 : 1);
			eventDate = CalMathAbs.GetDayCal(eventGridY, eventGridX, GUI.instance.main.cal);
		}
	}

	public void moveToRight() {
		if ((eventGridY == 5 && eventGridX == 6) || (CalMathAbs.IsNotCalMonth(eventGridY + (eventGridX == 6 ? 1 : 0),
				eventGridX + (eventGridX == 6 ? -6 : 1), GUI.instance.main.cal))) {
			int dayN = CalMathAbs.GetDayN(eventGridY + (eventGridX == 6 ? 1 : 0),
					eventGridX + (eventGridX == 6 ? -6 : 1), GUI.instance.main.cal);

			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? 1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? -11 : 1));
			GUI.instance.main.cal.set(Calendar.DATE, dayN);
			eventDate = (Calendar) GUI.instance.main.cal.clone();
			for (int week = 0; week < 6; week++) {
				for (int day = 0; day < 7; day++) {
					if (!CalMathAbs.IsNotCalMonth(week, day, GUI.instance.main.cal)
							&& CalMathAbs.GetDayN(week, day, GUI.instance.main.cal) == dayN) {
						eventGridX = day;
						eventGridY = week;
					}
				}
			}

		} else {
			eventGridY += (eventGridX == 6 ? 1 : 0);
			eventGridX += (eventGridX == 6 ? -6 : 1);
			eventDate = CalMathAbs.GetDayCal(eventGridY, eventGridX, GUI.instance.main.cal);
		}
	}

	public int boxHeight = 360;
	public int boxWidth = 400;
	public int height = 128;
	public int width = 181;

	public String displayTime(Calendar sc, Calendar ec) {
		if (ec.get(Calendar.SECOND) == 59) {
			return "All day -";
		}
		if (CalMathAbs.ToHM(sc).equals(CalMathAbs.ToHM(ec))) {
			return CalMathAbs.ToHM(sc);
		}
		return CalMathAbs.ToHM(sc) + "-" + CalMathAbs.ToHM(ec);
	}

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
			String renderString = chopTitle(
					displayTime(events.get(i + scrollBar).getTimeStart(), events.get(i + scrollBar).getTimeEnd()) + " "
							+ events.get(i + scrollBar).getTitle(),
					29);
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
		float redElement = 1f - ((GUI.instance.frame.getFrameCount() - this.delta) / 50f);
		if (redElement < 0 || redElement > 1) {
			this.redBox = false;
		}
		g.setColor((selectedField == 0 && this.redBox ? new Color(redElement, 0, 0) : Color.BLACK));

		g.drawRect(topLeftX + 10, topLeftY + 10 + 55, boxWidth + width - 20, 45);
		g.setColor((selectedField == 4 && this.redBox ? new Color(redElement, 0, 0) : Color.BLACK));
		g.drawRect(topLeftX + 10, topLeftY + 10 + 165, boxWidth + width - 20, 45);
		g.setColor((selectedField == 8 && this.redBox ? new Color(redElement, 0, 0) : Color.BLACK));
		g.drawRect(topLeftX + 150, topLeftY + 10 + 165 + 55, boxWidth + width - 300, 45);
		g.setColor(Color.BLACK);
		g.drawRect(topLeftX + 150, topLeftY + 10 + 275 + 20, boxWidth + width - 300, 45);

		String dispTitle = eventTitle;
		if (eventTitle.length() == 0) {
			g.setColor(new Color(0.4f, 0.4f, 0.4f, 0.6f));
			g.setFont(GUI.editItalFont);
			dispTitle = eventTitleHint;
		}
		String dispTitleEdit = "";
		if (selectedField == 0 && (GUI.instance.frame.getFrameCount() % 50) < 25) {
			dispTitleEdit = (dispTitle.replaceAll("[\\s\\S]", " ")) + "|";
		}

		Point titlePoint = View.centerString(dispTitle,
				new Rectangle(topLeftX + 12, topLeftY + 10 + 55, boxWidth + width - 24, 45), editMetric);
		g.drawString(dispTitleEdit, titlePoint.x - 5, titlePoint.y - 2);

		g.drawString(dispTitle, titlePoint.x, titlePoint.y);

		g.setColor(Color.BLACK);
		g.setFont(GUI.editFont);

		// Time

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

		} else if (this.selectedField != 2 && this.selectedField != 3) {

			int i1 = Integer.parseInt(eventTime1.substring(0, 2).trim());
			int i2 = Integer.parseInt(eventTime2.substring(0, 2).trim());
			if (i1 == i2) {
				int i3 = Integer.parseInt(eventTime1.substring(3).trim());
				int i4 = Integer.parseInt(eventTime2.substring(3).trim());
				if (i4 < i3) {
					String temp = eventTime1;
					eventTime1 = eventTime2;
					eventTime2 = temp;
				}
			} else if (i2 < i1) {
				String temp = eventTime1;
				eventTime1 = eventTime2;
				eventTime2 = temp;
			}

		}
		if (this.selectedField == 2 && this.overField == 0) {
			// Fix i2...
			int i2 = Integer.parseInt(eventTime1.substring(3).trim());
			eventTime1 = eventTime1.substring(0, 3) + (i2 < 10 ? "0" : "") + i2;
		} else if (this.selectedField == 2) {
			// Fix i1
			int i1 = Integer.parseInt(eventTime1.substring(0, 2).trim());
			eventTime1 = (i1 < 10 ? "0" : "") + i1 + eventTime1.substring(2);
		} else if (this.selectedField == 3 && this.overField == 2) {
			int i2 = Integer.parseInt(eventTime2.substring(3).trim());
			eventTime2 = eventTime2.substring(0, 3) + (i2 < 10 ? "0" : "") + i2;
		} else if (this.selectedField == 3) {
			int i1 = Integer.parseInt(eventTime2.substring(0, 2).trim());
			eventTime2 = (i1 < 10 ? "0" : "") + i1 + eventTime2.substring(2);
		} else if (this.selectedField != 2) {
			int i1 = Integer.parseInt(eventTime1.substring(0, 2).trim());
			int i2 = Integer.parseInt(eventTime1.substring(3).trim());
			eventTime1 = (i1 < 10 ? "0" : "") + i1 + ":" + (i2 < 10 ? "0" : "") + i2;
		} else if (this.selectedField != 3) {
			int i1 = Integer.parseInt(eventTime2.substring(0, 2).trim());
			int i2 = Integer.parseInt(eventTime2.substring(3).trim());
			eventTime2 = (i1 < 10 ? "0" : "") + i1 + ":" + (i2 < 10 ? "0" : "") + i2;
		}
		g.setFont(GUI.editFont);

		if (selectedField == 2) {
			if (this.overField == 0) {
				g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.1f));
				g.fillRect(topLeftX + 46, topLeftY + 10 + 110 + 10, 33, 27);
				g.setColor(Color.BLACK);
			}
			if (this.overField == 1) {
				g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.1f));
				g.fillRect(topLeftX + 46 + 39, topLeftY + 10 + 110 + 10, 33, 27);
				g.setColor(Color.BLACK);
			}
		}
		if (selectedField == 3) {
			if (this.overField == 2) {
				g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.1f));
				g.fillRect(topLeftX + 46 + 145, topLeftY + 10 + 110 + 10, 33, 27);
				g.setColor(Color.BLACK);
			}
			if (this.overField == 3) {
				g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.1f));
				g.fillRect(topLeftX + 46 + 39 + 145, topLeftY + 10 + 110 + 10, 33, 27);
				g.setColor(Color.BLACK);
			}
		}

		Point time1 = View.centerString(eventTime1,
				new Rectangle(topLeftX + 10, topLeftY + 10 + 110, (boxWidth + width - 290) / 2, 45), editMetric);
		g.drawString(eventTime1, time1.x, time1.y);
		Point time2 = View.centerString(eventTime2, new Rectangle(topLeftX + 10 + (boxWidth + width - 290) / 2,
				topLeftY + 10 + 110, (boxWidth + width - 290) / 2, 45), editMetric);
		g.drawString(eventTime2, time2.x, time2.y);

		g.setColor((selectedField == 2 && this.redBox ? new Color(redElement, 0, 0) : Color.BLACK));
		g.drawRect(topLeftX + 10, topLeftY + 10 + 110, (boxWidth + width - 290) / 2, 45);

		g.setColor((selectedField == 3 && this.redBox ? new Color(redElement, 0, 0) : Color.BLACK));
		g.drawRect(topLeftX + 10 + (boxWidth + width - 290) / 2, topLeftY + 10 + 110, (boxWidth + width - 290) / 2, 45);

		g.setColor(((selectedField == 3 || selectedField == 2) && this.redBox ? new Color(redElement, 0, 0)
				: Color.BLACK));
		g.drawLine(topLeftX + 10 + (boxWidth + width - 290) / 2, topLeftY + 10 + 110,
				topLeftX + 10 + (boxWidth + width - 290) / 2, topLeftY + 10 + 155);

		g.setColor(Color.black);
		g.drawRect(topLeftX + 10 + boxWidth + width - 275, topLeftY + 10 + 110, 255, 45);

		g.setFont(GUI.editFont);

		// Notes

		String dispNotes = eventNotes;
		if (eventNotes.length() == 0) {
			g.setColor(new Color(0.4f, 0.4f, 0.4f, 0.6f));
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

		Calendar e = (Calendar) Calendar.getInstance().clone();
		e.set(Calendar.MINUTE, e.get(Calendar.MINUTE) - (e.get(Calendar.MINUTE) % 15));
		eventTime1 = formatCalendarTime(e);
		e.add(Calendar.MINUTE, 15);
		if (e.get(Calendar.HOUR_OF_DAY) == 0 && e.get(Calendar.MINUTE) < 15) {
			eventTime2 = "23:59";
		} else {
			eventTime2 = formatCalendarTime(e);
		}
		this.eventOrigDate = (Calendar) eventDate.clone();
		eventNotes = "";
		eventAllDay = false;
		eventEvent = new CalendarEvent("", Calendar.getInstance(), Calendar.getInstance());

	}

	public void showEvent(int eventI) {
		selectedField = -1;
		if (eventI == -1) {
			this.createEvent();
		} else {
			System.out.println("Shown! " + this.eventIndex);
			this.eventIndex = eventI - 6 + scrollBar;
			Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(eventGridY, eventGridX, eventDate));
			ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(curCal);
			this.eventDate = curCal;
			this.eventOrigDate = (Calendar) curCal.clone();
			eventEvent = events.get(this.eventIndex);
			eventTitle = eventEvent.getTitle();
			eventNotes = eventEvent.getNote();
			if (eventEvent.getTimeEnd().get(Calendar.SECOND) != 59) {
				eventAllDay = false;
				eventTime1 = formatCalendarTime(eventEvent.getTimeStart());
				eventTime2 = formatCalendarTime(eventEvent.getTimeEnd());
			} else {
				eventAllDay = true;
				eventTime1 = "";
				eventTime2 = "";
			}
		}
		isEventShown = true;
	}

	public boolean attemptSaving() {
		if (eventTitle != "") {
			// basically all we need to check for
			CalendarEvent cc = new CalendarEvent("", Calendar.getInstance(), Calendar.getInstance());
			Calendar sc = CalMathAbs.GetDayCal(eventGridY, eventGridX, this.eventDate);
			Calendar ec = CalMathAbs.GetDayCal(eventGridY, eventGridX, this.eventDate);
			cc.setTitle(this.eventTitle);
			cc.setNote(this.eventNotes);

			if (this.eventAllDay) {
				ec.set(Calendar.SECOND, 59);
				ec.set(Calendar.MINUTE, 59);
				ec.set(Calendar.HOUR_OF_DAY, 23);
				sc.set(Calendar.SECOND, 0);
				sc.set(Calendar.MINUTE, 0);
				sc.set(Calendar.HOUR_OF_DAY, 0);
			} else {
				ec.set(Calendar.MINUTE, Integer.parseInt(this.eventTime2.substring(3)));
				ec.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.eventTime2.substring(0, 2)));
				sc.set(Calendar.MINUTE, Integer.parseInt(this.eventTime1.substring(3)));
				sc.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.eventTime1.substring(0, 2)));
			}
			cc.setTimeStart(sc);
			cc.setTimeEnd(ec);
			ArrayList<CalendarEvent> events = GUI.instance.main.globalCalendar.grab(eventDate);
			CalMathAbs.printDate(eventDate);
			CalMathAbs.printDate(sc);
			CalMathAbs.printDate(ec);
			CalMathAbs.printDate(eventOrigDate);
			System.out.println(eventGridX + " " + eventGridY);
			if (eventIndex == -1) {
				// Create new
				events.add(cc);
			} else {

				if (eventDate.equals(eventOrigDate)) {
					CalendarEvent ee = events.get(this.eventIndex);
					ee.setNote(ee.getNote());
					ee.setTitle(cc.getTitle());
					ee.setTimeEnd(cc.getTimeEnd());
					ee.setTimeStart(cc.getTimeStart());
				} else {
					// Basically we need to modify the original location to have a different thing
					// :D
					ArrayList<CalendarEvent> eventsOrig = GUI.instance.main.globalCalendar.grab(this.eventOrigDate);
					eventsOrig.remove(this.eventIndex);
					events.add(cc);
				}

			}

			return true;
		}
		// Skip...failed to close...maybe show a boiler plate exception...
		return false;
	}

	public void hideEvent() {
		// Always attempt saving first, except when calcenlling
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

	public String formatCalendarTime(Calendar c) {
		return (c.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + c.get(Calendar.HOUR_OF_DAY) + ":"
				+ (c.get(Calendar.MINUTE) < 10 ? "0" : "") + c.get(Calendar.MINUTE);
	}

	public void showEvents(int gridX, int gridY) {
		eventGridX = gridX;
		eventGridY = gridY;
		isEventsShown = true;
		eventDate = CalMathAbs.GetDayCal(gridY, gridX, GUI.instance.main.cal);
		scrollBar = 0;
		selectedField = -1;

	}

	public void hideEvents() {
		eventGridX = -1;
		eventGridY = -1;
		isEventsShown = false;
		eventDate = null;
		selectedField = -1;
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

	private final String eventTitleHint = "Enter Event Name (Max 40 characters).";
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
