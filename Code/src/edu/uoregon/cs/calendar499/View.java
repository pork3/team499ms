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

	
	private static final String[] months = {"January", "Feburary", "March", "April","May", "June", "July", "August","September", "October","November","December"};
	private static final String[] days = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
	
	
	private static FontMetrics titleMetric;
	private static FontMetrics bodyMetric;
	private static FontMetrics eventMetric;
	
	private static Color currentDayColor = new Color(0.9f,0.9f,1.0f);
	
	private Image plusImage = new ImageIcon(this.getClass().getResource("plus.png")).getImage();
	
	
	public void Display(Graphics2D g, Calendar c) {
		titleMetric = (titleMetric != null ? titleMetric : g.getFontMetrics(GUI.titleFont));
		bodyMetric = (bodyMetric != null ? bodyMetric : g.getFontMetrics(GUI.bodyFont));
		eventMetric = (eventMetric != null ? eventMetric : g.getFontMetrics(GUI.eventFont));
		Font tempFont = g.getFont();
		Stroke tempStroke = g.getStroke();
		g.setFont(GUI.titleFont);
		int height = 128;
		int width = 181;
		
		g.setColor(Color.BLACK);
		
		String title = months[c.get(Calendar.MONTH)] + ", " + c.get(Calendar.YEAR);
		
		Point titlePosition = View.centerString(title, new Rectangle(0, 10,GUI.windowSize.width, 40), titleMetric);
		
		g.drawString(title, titlePosition.x, titlePosition.y);
		
		g.setFont(GUI.bodyFont);
		for(int day = 0; day < 7; day++) {
			Point dayPosition = View.centerString(days[day], new Rectangle((day * width) -1, 59, width, 26), bodyMetric);
			g.drawString(days[day],  dayPosition.x, dayPosition.y);
		}
		
		for(int week = 0; week < 6; ++week) {
			for(int day = 0; day < 7; ++day) {
				//GUI.instance.dayView.Display(g, CalMathAbs.GetDayCal(week, day, c));
				g.setStroke(GUI.boxStroke);
				g.setFont(GUI.daysFont);
				int dayNumber = CalMathAbs.GetDayN(week, day, c);
				boolean isOtherMonth = CalMathAbs.IsNotCalMonth(week, day, c);
				boolean isCurrentDay = CalMathAbs.IsCurrentDay(week, day, c);
				
				if(isCurrentDay) {
					//Display in blue
					g.setColor(currentDayColor);
					
				}else if(isOtherMonth) {
					// Display the day with grey background
					g.setColor(Color.LIGHT_GRAY);
				}else {
					// Display the day normally
					g.setColor(Color.white);
				}
				//draw the background of the day
				g.fillRect(width*day - 1,	height*week + 88, width, height);
				//draw the box around the day
				g.setColor(Color.black);
				g.drawRect(width * day - 1, height * week + 88, width, height);
				//draw the date in the top left
				Point dayPosition = View.centerString(dayNumber+(dayNumber < 10 ? " " : ""), new Rectangle(width * day + 17, height * week + 84, 10, 22), bodyMetric);
				g.drawString(dayNumber + "" , dayPosition.x, dayPosition.y);
				
				Color eventBackgroundColor = new Color(0.0f,0.4f,0.9f,0.3f);
				Color eventPrevMonthBackgroundColor = new Color(0.0f,0.45f,1.0f,0.4f);
				Color eventCurrentDayBackgroundColor = new Color(0.2f,0.5f,1.0f,0.4f);
				
				// Draw events here!
				Calendar curCal = CalMathAbs.ClearTime(CalMathAbs.GetDayCal(week, day, c));
				ArrayList<CalendarEvent> events = GUI.instance.main.days.get(curCal);
				//System.out.println(curCal);
				if(events != null && !events.isEmpty()) {
					for(int i = 0; i < events.size(); i++) {
						if(isCurrentDay) {
							g.setColor(eventCurrentDayBackgroundColor);
						}else if(isOtherMonth) {
							g.setColor(eventPrevMonthBackgroundColor);
						}else {
							g.setColor(eventBackgroundColor);
						}
						g.fillRect(width * day + 5, height * week + 88 + 22 + i * 24, width - 12, 20);
						g.setColor(Color.BLACK);
						g.setFont(GUI.eventFont);
						
						String eventTitle = chopTitle(CalMathAbs.ToHM(events.get(i).getTimeStart())+"-" + CalMathAbs.ToHM(events.get(i).getTimeEnd()) + " " + events.get(i).getTitle());
						if(i == 3 && events.size() != 4) {
							//We need to draw a box saying "+ X more..." along with a + icon...
							g.setFont(GUI.eventItalFont);
							eventTitle = "+"+  (events.size() - 3)+ " more...";
							
							Point eventPosition = View.leftCenterString(eventTitle, new Rectangle(width * day + 20, height * week + 88 + 22 + i*24, width - 12, 20), eventMetric);
							g.drawString(eventTitle, eventPosition.x, eventPosition.y);
							break;
						}
						Point eventPosition = View.leftCenterString(eventTitle, new Rectangle(width * day + 7, height * week + 88 + 22 + i * 24, width - 12, 20), eventMetric);
						g.drawString(eventTitle, eventPosition.x, eventPosition.y);
					}
				}
				//23 - 11 = 12 chars titles...
			}
		}
		
		
		if(isDisplayingEvent()) {
			//Display a white box...
			//First figure out if it needs to be above or below the current day...
			//Those in the first 3 weeks will be below
			//Those in the last 3 weeks will be above
			eventGridX = 6; //OVERRIDE
			eventGridY = 3; //OVERRIDE
			int boxHeight = 360;
			int boxWidth = 400;
			Point eventBox = new Point(0,0);
			Polygon popupPoly = new Polygon(); 
			if(eventGridY > 2) {
				//Show above...
				eventBox = new Point(width * eventGridX - (eventGridX <= 1 ? -5 + (eventGridX)* width : boxWidth / 2) - (eventGridX >= 5 ? boxWidth/2  - (6 - eventGridX) * width+ 5 : 0), height * (eventGridY+1) - boxHeight - 55);
				
				popupPoly.addPoint(eventBox.x, eventBox.y); //Top left
				//Arrow
				
				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y); //Top right
				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y+ boxHeight); //Bottom right
				popupPoly.addPoint(width * eventGridX + width/2 + 40, eventBox.y + boxHeight);
				
				popupPoly.addPoint(width * eventGridX + width/2, eventBox.y+ boxHeight + 10);
				popupPoly.addPoint(width * eventGridX + width/2 - 40, eventBox.y + boxHeight);
				popupPoly.addPoint(eventBox.x, eventBox.y+ boxHeight); //Bottom left
				
			}else {
				//Show below...
				eventBox = new Point(width * eventGridX - (eventGridX <= 1 ? -5 + (eventGridX)* width: boxWidth/2) - (eventGridX >= 5 ? boxWidth/2 - (6 - eventGridX) * width + 5: 0), height * (eventGridY+1) + 105);
				popupPoly.addPoint(eventBox.x, eventBox.y); //Top left
				//Arrow
				popupPoly.addPoint(width * eventGridX + width/2 - 40, eventBox.y);
				popupPoly.addPoint(width * eventGridX + width/2, height * (eventGridY+1) + 95);
				popupPoly.addPoint(width * eventGridX + width/2 + 40, eventBox.y);
				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y); //Top right
				popupPoly.addPoint(eventBox.x + boxWidth + width, eventBox.y+ boxHeight); //Bottom right
				popupPoly.addPoint(eventBox.x, eventBox.y+ boxHeight); //Bottom left
				
				
			}
			
			g.setColor(Color.white);
			g.fillPolygon(popupPoly);
			g.setColor(Color.black);
			g.drawPolygon(popupPoly);
		}
		
		
		
		g.setStroke(tempStroke);
		g.setFont(tempFont);
	}
	
	public boolean isEventShown = true;
	public int eventGridX = 0;
	public int eventGridY = 0;
	public Calendar eventDate = CalMathAbs.ClearTime(Calendar.getInstance());
	
	
	public boolean isDisplayingEvent() {
		return isEventShown;
	}
	
	public boolean isShowingThisEvent(int gridX, int gridY) {
		return isEventShown && eventGridX == gridX && eventGridY == gridY;
	}
	
	public void showEvent(int gridX, int gridY) {
		eventGridX = gridX;
		eventGridY = gridY;
		isEventShown = true;
		eventDate = CalMathAbs.GetDayCal(gridX, gridY, GUI.instance.main.cal);
	}
	
	
	public static String chopTitle(String title) {
		if(title.length() > 24) {
			return title.substring(0, 21) + "...";
		}
		return title;
	}
	
	
	
	public static Point centerString(String s, Rectangle rect, FontMetrics fm) {
		
		return new Point(rect.x + (rect.width - fm.stringWidth(s)) / 2, rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent());
	}
	

	public static Point leftCenterString(String s, Rectangle rect, FontMetrics fm) {
		
		return new Point(rect.x, rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent());
	}
	
}
