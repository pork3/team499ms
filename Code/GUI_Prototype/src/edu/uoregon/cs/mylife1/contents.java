package edu.uoregon.cs.mylife1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class contents extends JPanel implements ActionListener, MouseListener{

	
	
	private Timer t;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int titleSize = 42;
	private static final int bodySize = 16;
	private static final int daysSize = 18;
	private static final String fontFamily = "TimesRoman";
	
	private static final Font titleFont = new Font(fontFamily, Font.BOLD, titleSize);
	private static final Font bodyFont = new Font(fontFamily, Font.BOLD, bodySize);
	private static final Font daysFont = new Font(fontFamily, Font.PLAIN, daysSize);
	private static final int boxThickness = 2;
	private static final Stroke boxStroke = new BasicStroke(boxThickness);
	
	
	private static final String[] months = {"January", "Feburary", "March", "April","May", "June", "July", "August","September", "October","November","December"};
	private static final String[] days = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
	
	
	private static FontMetrics titleMetric;
	private static FontMetrics bodyMetric;
	
	
	private static final Color backgroundColor = new Color(255, 255, 242);
	
	private Image leftArrow;
	public Calendar cal;
	public contents(){
		super.setDoubleBuffered(true);
		this.setPreferredSize(calGUI.windowSize);
		this.setMaximumSize(calGUI.windowSize);
		this.addMouseListener(this);
		this.setMinimumSize(calGUI.windowSize);
		ImageIcon ii = new ImageIcon(this.getClass().getResource("left.png"));
		leftArrow = ii.getImage();
		cal = Calendar.getInstance();
		t = new Timer(15, this);
		t.start();
		
		calculateDatesOfMonth(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
		
		
		
	}
	
	private int numWeeks = 0;
	private int numDaysPrevious = 0;
	private int numDaysCurrent = 0;
	private int startDay = 0;
	private int endDay = 0;
	private int currentDay = 0;
	private int currentMonth = 0;
	private int currentYear = 0;
	
	public void calculateDatesOfMonth(int month, int year) {
		currentMonth = month;
		currentYear = year;
		currentDay = cal.get(Calendar.DAY_OF_MONTH);
		
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date tempDate = cal.getTime();
		startDay = cal.get(Calendar.DAY_OF_WEEK);
		numDaysCurrent = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, numDaysCurrent);
		numWeeks = cal.get(Calendar.WEEK_OF_MONTH);
		
		cal.set(Calendar.YEAR, year + (month == 0 ? -1 : 0));
		cal.set(Calendar.MONTH, (month == 0 ? 11 : month - 1 ));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		numDaysPrevious = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.YEAR, year + (month == 11 ? 1 : 0));
		cal.set(Calendar.MONTH, (month + 1) % 12);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		endDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
		cal.setTime(tempDate);
		System.out.println(numWeeks + " " + numDaysPrevious + " " + numDaysCurrent + " " + startDay + " " + endDay);
		
	}
	
	
	
	
	
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		titleMetric = (titleMetric != null ? titleMetric : g2d.getFontMetrics(titleFont));
		bodyMetric = (bodyMetric != null ? bodyMetric : g2d.getFontMetrics(bodyFont));
		g2d.setColor(backgroundColor);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		//g2d.drawImage(dude, x, y, null);
		
		paintTitle(g2d);
		
		paintDays(g2d);
		
		
		g2d.drawImage(leftArrow, 35, 19, 75, 59, 0, 0, leftArrow.getWidth(null), leftArrow.getHeight(null), null);
		
		g2d.drawImage(leftArrow, this.getWidth() - 75, 19, this.getWidth() - 35, 59, leftArrow.getWidth(null), 0, 0, leftArrow.getHeight(null), null);
		
	}
	private long counter = 0L;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//Animation
		counter++;
		if(counter >= 500) {
			counter = 0L;
			if(Calendar.getInstance().get(Calendar.MONTH) == currentMonth  && Calendar.getInstance().get(Calendar.YEAR) == currentYear) {
				cal = Calendar.getInstance();
			}
		}
		repaint();
	}
	
	public Point centerString(String s, Rectangle rect, FontMetrics fm) {
		return new Point(rect.x + (rect.width - fm.stringWidth(s)) / 2, rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent());
	}
	
	
	public void paintTitle(Graphics2D g) {
		Font temp = g.getFont();
		g.setFont(titleFont);
		g.setColor(Color.BLACK);
		String title = months[cal.get(Calendar.MONTH)] + ", " + cal.get(Calendar.YEAR);
		Point titlePosition = this.centerString(title, new Rectangle(200, 10, 400, 40), titleMetric);
		g.drawString(title, titlePosition.x, titlePosition.y);
		
		g.setFont(bodyFont);
		for(int day = 0; day < 7; day++) {
			Point dayPosition = this.centerString(days[day], new Rectangle(day * 116 - 1, 67, 116, 20), bodyMetric);
			g.drawString(days[day], dayPosition.x, dayPosition.y);
		}
		
		g.setFont(temp);
	}
	
	public void paintDays(Graphics2D g) {
		Stroke tempStroke = g.getStroke(); 
		Font tempFont = g.getFont();
		g.setStroke(boxStroke);
		g.setFont(daysFont);
		for(int week = 0; week < 6; ++week) {
			for(int day = 0; day < 7; ++day) {
				//First draw the box...
				
				if(week == 0 && day < startDay - 1) {
					//Previous month
					g.setColor(Color.gray);
					g.fillRect(116*day - 1, 87 * week + 88, 116, 87);
					
					//Makes call to previous month get events
					g.setColor(Color.black);
					Point dayPosition = this.centerString((numDaysPrevious - startDay + day + 2) + "", new Rectangle(116*day - 1, 87 * week + 88, 20, 22), bodyMetric);
					g.drawString((numDaysPrevious - startDay + day + 2) + "", dayPosition.x, dayPosition.y);
				}else if(day + (week*7) - startDay + 2 > numDaysCurrent) {
					g.setColor(Color.gray);
					g.fillRect(116*day - 1, 87 * week + 88, 116, 87);
					
					//Makes call to previous month get events
					g.setColor(Color.black);
					Point dayPosition = this.centerString((day + (week * 7) - startDay + 2 - numDaysCurrent) + "", new Rectangle(116*day - 1, 87 * week + 88, 20, 22), bodyMetric);
					g.drawString((day + (week * 7) - startDay + 2 - numDaysCurrent) + "", dayPosition.x, dayPosition.y);
				}else {
					g.setColor(Color.white);
					g.fillRect(116*day - 1, 87 * week + 88, 116, 87);
					if(Calendar.getInstance().get(Calendar.MONTH) == currentMonth  && Calendar.getInstance().get(Calendar.YEAR) == currentYear && day + (week * 7) - startDay + 2 == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)  ) {
						g.setColor(new Color(0.8f, 0.8f, 1.0f));
						g.fillRect(116*day - 1, 87 * week + 88, 116, 87);
					}
					g.setColor(Color.black);
					Point dayPosition = this.centerString((day + (week * 7) - startDay + 2) + "", new Rectangle(116*day - 1, 87 * week + 88, 20, 22), bodyMetric);
					g.drawString((day + (week * 7) - startDay + 2) + "", dayPosition.x, dayPosition.y);
					
				}
				
				g.drawRect(116 * day -1, 87 * week + 88, 116, 87);
			}
		}
		g.setStroke(tempStroke);
		g.setFont(tempFont);
	}



	private static final Rectangle leftArrowRect = new Rectangle(35, 19, 40, 40);
	private static final Rectangle rightArrowRect = new Rectangle(calGUI.windowSize.width - 65, 19, 40, 40);

	@Override
	public void mouseClicked(MouseEvent e) {
		if(leftArrowRect.contains(e.getPoint())) {
			currentYear = (currentMonth == 0 ? -1 : 0) + currentYear;
			currentMonth = (currentMonth == 0 ? 11 : currentMonth - 1);
			calculateDatesOfMonth(currentMonth, currentYear);
		}else if(rightArrowRect.contains(e.getPoint())) {
			currentYear = (currentMonth == 11 ? 1 : 0) + currentYear;
			currentMonth = (currentMonth == 11 ? 0 : currentMonth + 1);
			calculateDatesOfMonth(currentMonth, currentYear);
			
		}
	}






	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}






	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}
