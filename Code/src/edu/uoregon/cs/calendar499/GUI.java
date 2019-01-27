package edu.uoregon.cs.calendar499;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GUI extends JFrame{
	
	
	private static final long serialVersionUID = 1L;
	public static final Dimension windowSize = new Dimension(1254,845);
	public UserInput input;
	public View monthView;
	public View dayView;
	public View eventView;
	
	
	private static final int titleSize = 42;
	private static final int bodySize = 26;
	private static final int daysSize = 18;
	private static final int eventSize = 12;
	private static final String fontFamily = "Courier";
	
	public static final Font titleFont = new Font(fontFamily, Font.BOLD, titleSize);
	public static final Font bodyFont = new Font(fontFamily, Font.BOLD, bodySize);
	public static final Font daysFont = new Font(fontFamily, Font.BOLD, daysSize);
	public static final Font eventFont = new Font(fontFamily, Font.PLAIN, eventSize);
	public static final Font eventItalFont = new Font(fontFamily, Font.ITALIC, eventSize);
	
	public static final int boxThickness = 2;
	public static final Stroke boxStroke = new BasicStroke(boxThickness);
	
	public static final Color backgroundColor = new Color(255,255,242);
	
	
	public static final String calendarName = "Calendar 499";
	
	public Cal main;
	
	
	public static GUI instance;
	public GUI(Cal c, UserInput input, View monthView, View dayView, View eventView) {
		super(calendarName);
		main = c;
		instance = this;
		this.input = input;
		this.monthView = monthView;
		this.dayView = dayView;
		this.eventView = eventView;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.add(new GUIFrame(this));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		this.addWindowListener(input);
		
		
	}
	
}


class GUIFrame extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	private Timer timer;
	public GUI gui;
	
	public GUIFrame(GUI gui) {
		this.gui = gui;
		super.setDoubleBuffered(true);
		this.setPreferredSize(GUI.windowSize);
		this.setMaximumSize(GUI.windowSize);
		this.setMinimumSize(GUI.windowSize);
		this.addMouseListener(gui.input);
		
		timer = new Timer(25, this);
		timer.start();
		
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(GUI.backgroundColor);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		gui.monthView.Display(g2d, gui.main.cal);
		
		//Month view calls day view which calls eventView
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
}




