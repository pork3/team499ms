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
	
	
	private static final long serialVersionUID = 1L; // A recommended value to have for anything JFrame related, not used
	
	///////// STATIC FINAL VARIABLES \\\\\\\\\\\
	// Variables that are very sensitive
	private static final int titleSize = 42; // Font size for titles
	private static final int bodySize = 26; // Font size for text in the event view (event editor)
	private static final int daysSize = 18; // Font size for text in the event view (for listing events on a specific day)
	private static final int eventSize = 12; // Font size for text in the month view (events listed in day tiles)
	private static final int editSize = 22; // Font size for text in the day view (for displaying text in editing events)
	private static final String fontFamily = "Courier"; // Font style, uses a monospaced font (allows for using string length to determine 'displayability' of said string.
	public static final Dimension windowSize = new Dimension(1254,845); // The size of the application window (excluding decorations).

	
	
	//////// MODIFIABLE PUBLIC STATIC FINAL VARIABLES \\\\\\\\\\
	
	public static final String calendarName = "Y.A.C.C - version 1.0"; // Title for the application, what is displays for the title of the GUI
	public static final int boxThickness = 2; // The thickness used to draw boxes in the application 
	public static final Color backgroundColor = new Color(255,255,242); // The background Color for the application
	
	
	////// PUBLIC STATIC FINAL VARIABLES \\\\\\\\\\
	/*
	 * These variables are derived from the above variables that are allowed to be used by other modules, if they need to determine sizes of strings.
	 */
	public static final Font titleFont = new Font(fontFamily, Font.BOLD, titleSize);
	public static final Font bodyFont = new Font(fontFamily, Font.BOLD, bodySize);
	public static final Font daysFont = new Font(fontFamily, Font.BOLD, daysSize);
	public static final Font eventFont = new Font(fontFamily, Font.PLAIN, eventSize);
	public static final Font editFont = new Font(fontFamily, Font.BOLD, editSize);
	public static final Font editItalFont = new Font(fontFamily, Font.BOLD | Font.ITALIC, editSize);
	public static final Font eventItalFont = new Font(fontFamily, Font.ITALIC, eventSize);
	public static final Stroke boxStroke = new BasicStroke(boxThickness); // A Stroke to be used when drawing boxes
	
	//////// PUBLIC VARIABLES FOR REFERENCE JUMPING \\\\\\\\
	/*
	 * These variables allow for requesting information from one module to another.
	 */
	public Main main;
	public GUIFrame frame;
	public UserInput input;
	public View view;
	
	// A static instance variable so that outside modules can gain access to this module (as there shouldn't ever be more than one GUI object created in the code).
	public static GUI instance;
	
	
	// The constructor requires the Main class object, the UserInput class object and a View class object
	// This also sets up the frame so that it can accept inputs.
	public GUI(Main main2, UserInput input, View View) {
		super(calendarName); // Sets the calendar name
		
		instance = this; // Initialize the instance variable
		
		this.main = main2; // Set references. 
		this.input = input;
		this.view = View;
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // We want to intercept close requests, so tell the frame to take any actions on our behalf. We handle the closing in the UserInput
		
		frame = new GUIFrame(this); // Creates a new jpanel that will display all graphics.
		this.setFocusTraversalKeysEnabled(false); // This allows for keys like tab and enter to be registered as key presses in UserInput 
		this.add(frame); // Add the jpanel to this frame
		this.pack(); // Resize this frame to the size of the frame
		this.setLocationRelativeTo(null); // Relocate it to the center of the screen
		this.setResizable(false); // Prevents the screen from being resized in any way (can't maximize, can't drag corners)
		this.setVisible(true); // Display the screen
		this.addWindowListener(input); // Add the UserInput object to listen to window events and key events.
		this.addKeyListener(input);
		/*
		 * The reason why Mouse Listener isn't used here but instead in the frame is because if it is added here, a "offset" will be applied to the click position that is 
		 * entirely dependent on the style of the decorations of the frame. By adding it to the jpanel directly, it treats the top left as 0,0 and the bottom right as the size of the renderable screen.
		 */
		
	}
	
}

// A panel class to handle displaying the graphics to the user
class GUIFrame extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // A recommended value to have for anything JFrame related, not used
	
	
	
	
	public Timer timer; // A timer object to call a function every 25 ms, used in refreshing the screen
	public GUI gui; // A reference to the GUI class above.
	
	public GUIFrame(GUI gui) {
		this.gui = gui; 
		super.setDoubleBuffered(true); // Set the frame to be double buffered, tries to reduce tearing when redrawing to the screen
		this.setPreferredSize(GUI.windowSize); // Set the preferred,minimum and maximum size of the frame to the window size.
		this.setMaximumSize(GUI.windowSize);
		this.setMinimumSize(GUI.windowSize);
		this.addMouseListener(gui.input);
		
		timer = new Timer(25, this); // Set up and start the timer.
		timer.start();
		
	}

	// This function is where elements are drawn to the window.
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g; // Cast to graphics2D for increased functionality
		
		g2d.setColor(GUI.backgroundColor); 
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight()); // Clear the screen by painting it white
		gui.view.Display(g2d, gui.main.cal); // Call the Display function in the View object.
		
	}
	private long frameCount = 0L; // A counter for the number of frames rendered, useful for drawing cursors that flash at a specific frequency
	
	private boolean isSaving = false;
	private FileIOStatus saveStatus = null;
	
	// This function is called by timer every 25 ms, it will increment frameCount and force the panel to redraw it's contents
	@Override
	public void actionPerformed(ActionEvent arg0) {
		frameCount++;
		
		if(frameCount > 100000L) {
			frameCount = 0;
		}
		
		if(isSaving && saveStatus != null) {
			//determine if it is good.
			if(frameCount % 2 == 0) {
				saveStatus.lock.lock();
				isSaving = saveStatus.currentStatus.equals(Status.Waiting);
				saveStatus.lock.unlock();
			}
			if(!isSaving) {
				saveStatus = null;
			}
		}else if(frameCount % 40 == 0) {
			if(Main.instance.globalCalendar.getDirty()) {
				isSaving = true;
				Main.instance.globalCalendar.setDirty(false);
				Cal dc = Main.instance.globalCalendar.deepCopy();
				saveStatus = SaveEvents.saveCalendar(dc, Main.filePath);
			}
		}
		
		repaint();
	}

	// Getters and setters for frameCount
	public long getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(long frameCount) {
		this.frameCount = frameCount;
	}
}




