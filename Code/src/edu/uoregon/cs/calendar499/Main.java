package edu.uoregon.cs.calendar499;
// Generic imports and package (which must come first)

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


/////////// Main Class \\\\\\\\\\\\\\\\\
/*
 * This class is designed to be the entry point for the application. 
 * It is responsible for loading the calendar initially and setting
 * up any necessary objects for running the calendar.
 * 
 * Also contains functions for displaying the "loading/saving" popup
 * and for printing debugging information.
 */

////////////// NOTES \\\\\\\\\\\\\\\\\\\\
/*
 * Java contains an object that is used very 
 * frequently called "Calendar", this is different from
 * the program which is a calendar. To differentiate 
 * between the two, we will:
 * 		Refer to any java originating object with a "j" appended to the front of the name.
 */

public class Main {
	
	public static final String VERSION_NUMBER = "1.0"; // The current application version. 
	/*
	 * Only change the above IFF (if and only if) you have updated the format for saving calendars.
	 * Change the below for changes to the application and not the format for saving calendars.
	 */
	public static final String APPLICATION_NUMBER = "1.0.1"; // The current program version. Safe to change to reflect versions of the program.
	
	
	public static String filePath = "default.cal"; // The default path and file name for saving and loading calendars.
	public Calendar cal = CalMathAbs.ClearTime(Calendar.getInstance()); // The default jCalendar object, the default refers to the current user's date and time.
	public Cal globalCalendar; // The public Calendar object that stores all the events for the program.
	public Render render; // A G.U.I for letting the user know that the application is performing processes in the background and isn't stalling
	public static Main instance; // A variable to allow for accessing different variables without making said variables public.
	private UserInput ui; // A variable to store the user input class object.
	private GUI gui; // A variable to stroe the display module class object.
	private static boolean debugging = true; // A variable for determining if the system should be printing debug statements to std. out
	
	//Constructor, args is the arguments passed to the 
	public Main(String[] args) {
		instance = this; // Set the instance variable to this object.
		
		showWaitingWindow("Loading Calendar"); // Display the loading gui telling the user that the application is loading
		
		if(args.length != 0) {
			//assume first argument is a path to the save location...
			filePath = args[0]; // The first argument is the filePath and fileName.
			debugging = (args.length >= 2) || debugging; // If debugging is true, keep it true, otherwise only set it to true if there are 2 or more arguments to the java application.
		}
		
		FileIOStatus e = LoadEvents.loadCalendar(filePath); // Request the system to load the calendar from the specified path. Returns immediately with a promise/status object
		boolean waiting = true; // Initialize waiting variable
		while(waiting) {
			waitSafe(10); //Allow for some time for other threads to acquire a lock
			e.lock.lock(); //Attempt to acquire the lock
			waiting = e.currentStatus.equals(Status.Waiting); // Determine if the calendar has been loaded
			
			e.lock.unlock(); // Release the lock to allow for other threads to acquire it
		}
		setMessage("Starting calendar"); // Set the status of the loading gui
		waitSafe(500); // Prevents the GUI from instantly being removed, instead delay's the opening process just a bit.
		if(e.currentStatus == Status.Failed || e.storedValue == null) { 
			// The loading of the calendar failed, update the GUI, print to the debugger, and create a new Calendar object
			setMessage((e.currentStatus == Status.Failed) ? "Calendar corrupted!" : "Starting calendar");
			Main.logDebug(0, e.currentStatus);
			globalCalendar = new Cal();
		}else {
			// The calendar loaded properly, change the status on the GUI and set the Calendar object
			Main.logDebug(3,"Loaded calendar");
			globalCalendar = e.storedValue;
		}
		waitSafe(500); // Give time for the user to read the feedback
		ui =  new UserInput(); // Create the UserInput object
		gui = new GUI(this, ui, new View()); //Create the graphics object for the program. Passes the UserInput module for module communication
		ui.setGui(gui); // Set the GUI field for the UserInput, allowing for module communication
		destroyWaitingWindow(); // Destroy the loading window as it is not necessary as the actual calendar GUI has been created.
		
	}
	private static String[] levels = {"Critical", "Severe", "Warning", "Info"}; // The different levels for debugging. Used internally.
	
	// This function is used to capture debugging statement, and only when debugging (the debugging flag is set) will they print to standard out, along with time and level of severity
	public static void logDebug(int level, Object o) {
		if(debugging)
		{
			System.out.println("["+(level >= levels.length || level < 0 ? levels[0] : levels[level])+" "+CalMathAbs.printDate(Calendar.getInstance()) + "]: " + o.toString());
		}
	}
	
	// This function will halt the current thread for a certain amount of time, however it will capture any possible errors by doing so, hence it is a safe wait method.
	public static void waitSafe(long millis) {
		try {
			Thread.sleep(millis);
		}catch(Exception __) {}
	}
	public void showWaitingWindow(String status) {
		render = new Render();
		render.setVisible(true);
		render.setMessage(status);
	}
	public void setMessage(String status) {
		render.setMessage(status);
	}
	public void destroyWaitingWindow() {
		render.getBody().timer.stop();
		render.dispose();
	}
	
	
	//// REQUIRED FUNCTION \\\\
	/*
	 * This function is required to run any of the program, as java uses this function as a starting point for java programs. 
	 * 
	 * Only purpose is to call the constructor of Main, but be extended to do more.
	 */
	public static void main(String[] args) {
		new Main(args);

	}

}

// A jFrame/jGUI to display that the application is doing processes in background and that it isn't hanging. 
class Render extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // A recommended value to have for anything JFrame related, not used

	
	// A public function that sets the message displayed on the loading screen.
	public void setMessage(String message) {
		body.setMessage(message);
	}
	
	// A public function that allows access to the internal drawing frame of this GUI
	public RenderBody getBody() {
		return body;
	}
	private RenderBody body; 
	
	// Constructor that sets up the waiting window
	public Render() {
		this.setUndecorated(true); //Don't render the window's title, window controls, or outline, basically an undecorated window
		body = new RenderBody(); // Create the body of this loading window
		this.add(body); // Add the body of the loading window to this frame
		this.pack(); // Resize this frame to fit the body
		this.setLocationRelativeTo(null); // Center this frame on the screen
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Set the close operation to be used when this frame is closed by the user (in this case, do nothing, ignore the request)
	}

}

// This class holds all the logic for drawing the loading icon
class RenderBody extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // A recommended value to have for anything JFrame related, not used

	public Timer timer; // A timer object that continually calls an ActionListener at set intervals
	private static final Dimension windowSize = new Dimension(200, 300); // The frame size of the drawable portion of the loading window
	private String message = ""; // The message to display on the loading window
	
	// A setter function for the message
	public void setMessage(String msg) {
		this.message = msg;
	}
	
	// A constructor for building the panel of the GUI
	public RenderBody() {
		super.setDoubleBuffered(true); // Double buffer to prevent tearing
		this.setPreferredSize(windowSize); // Set the size of the frame, (max, min and preferred)
		this.setMinimumSize(windowSize);
		this.setMaximumSize(windowSize);
		timer = new Timer(25, this); //Set up the timer to call the action listener (this) every 25 ms -> 40 calls per second
		timer.start(); // Start the timer
	}

	private int dx = 0; // An offset for rendering the "loading" balls, basically imagine the balls as a train, this is the "tail" of the train
	private static int numOfBalls = 3; // The number of balls to render
	private static final Stroke boxStroke = new BasicStroke(5); // The stroke (linewidth) for rendering boxes
	private static FontMetrics daysMetric; // The metric for the font rendering the message
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g; // Graphics2D has more options than just the Graphics object
		daysMetric = (daysMetric != null ? daysMetric : g.getFontMetrics(GUI.eventFont)); // Create the metric for the font if it isn't already set.
		g2d.setStroke(boxStroke); // Set the stroke for the box
		
		g2d.setColor(Color.WHITE); // Set the color to white and clear the screen
		g2d.fillRect(0, 0, windowSize.width, windowSize.height);
		
		g2d.setColor(Color.black); //Set the color to black and draw the outline for the screen (otherwise you can't differentiate windows as shadow is part of "decorations"
		g2d.drawRect(0, 0, windowSize.width-1, windowSize.height-1);
		
		g2d.setFont(GUI.eventFont); //Set the font for rendering the message string (at the bottom of the window)
		Point drawMsgStr = View.centerString(View.chopTitle(message, 30), new Rectangle(10, 270, 180, 20), daysMetric); // Centers the string for drawing onto a window
		g2d.drawString(View.chopTitle(message, 30), drawMsgStr.x, drawMsgStr.y); // Takes the required locations for a "centered" string and draws the message (chopped to 30 characters)
		for (int i = 0; i < numOfBalls; i++) {
			/*
			 * This renders the ball effect, and how it works is like this:	
			 * 		We want to render balls slowly moving across the screen, then speed off when they get to the right, then they should fly in from the left slowing down again
			 * 
			 *  	To do this we first have a left most position somewhere between -70 (left most side of the screen), and +70 (right most side of the screen)
			 *  	We shift this value by 70 (to get a value from 0 and 140), we then take the ballID, and multiply it by 20 and add it to this number
			 *  
			 *  	This spaces out the balls, but may cause them to go off the screen, so we module it by 140 to have it wrap from 140 to 0. But then we need to shift the value
			 *  	back to the regime -70 to 70, so we subtract 70.
			 *  
			 *  	We then simply ask if the ball is between -70 and -50, or if it is between 50 and 70. If so, we use the "speeding off" animation, otherwise we just render it 
			 *  	normally. Note we have to add 50 to the former, and 150 to the latter because of how calculatePos only returns the delta from these positions (-70 to -50 regime makes calculatePos returns a negative number
			 *  	hence why it gets shifted forward 50 and not back 50)
			 *  
			 *  	To trick the eyes to make it seem like to moved a lot faster than it actually did, we apply a sliding scale to linearly decrease the alpha of the dot, making it fade.
			 */
			int idx = ((dx + 70 + i * 20) % 140) - 70; 
			g2d.setColor(Color.black);
			if (idx < -50) {
				g2d.setColor(new Color(0, 0, 0, Math.max(0, 1f - ((Math.abs(idx) - 50) / ((float) delta.length)))));
				g2d.fillOval(calculatePos(idx) + 50, 250, 8, 8);
			} else if (idx >= -50 && idx <= 50) {
				g2d.fillOval(calculatePos(idx) + 100, 250, 8, 8);
			} else {
				// do stuff here...
				g2d.setColor(new Color(0, 0, 0, Math.max(0, 1f - ((idx - 50) / ((float) delta.length)))));
				g2d.fillOval(calculatePos(idx) + 150, 250, 8, 8);
			}
		}
	}

	private int[] delta = { 1, 2, 4, 6, 9, 12, 16, 20, 25, 30, 36, 42, 50 }; //A hardcoded list of positions for the animation of the dots

	// Returns the position of a dot based upon dx (the delta away from 100, the center of the screen)
	private int calculatePos(int dx) {
		int adx = Math.abs(dx);
		if (adx <= 50) { //If we are in the regime where the ball should be only slowly moving, return dx.
			return dx;
		} else {
			// Otherwise using adx as an index (hence why it is shifted 51), we figure out the delta for the rendering. Multiply it by the sign of dx to determine derection to render in.
			return (int) (delta[Math.min(delta.length - 1, adx - 51)] * Math.signum(dx)); 
		}
	}

	// A frame counter, used to determine calculations such as waiting for x frames to occur, or to only display something on every x frames.
	private long framecount = 0L;

	// This function is ran from the timer every 25 ms. It computes dx for the dots and repaints (redraws) the frame
	@Override
	public void actionPerformed(ActionEvent arg0) {
		framecount++;
		dx = dx + (dx >= 70 ? -140 : 1);
		if (framecount > 100000L) {
			framecount = 0;
		}
		repaint();
	}

}