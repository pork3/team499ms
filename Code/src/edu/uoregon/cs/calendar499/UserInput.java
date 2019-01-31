package edu.uoregon.cs.calendar499;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class UserInput implements MouseListener,WindowListener{
	GUI gui;
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(!gui.eventView.handleEvent(arg0, gui)) {
			if(!gui.dayView.handleEvent(arg0, gui)) {
				if(!gui.monthView.handleEvent(arg0, gui)) {
					System.out.println("Click event not captured by monthView!");
				}else{
					//Mouse Clicked on Month View

				}
			}else{
				//Mouse Clicked on Day View

			}
		
		}else{
			//Mouse Clicked on Event View

		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if(!gui.eventView.handleEvent(arg0, gui)) {
			if(!gui.dayView.handleEvent(arg0, gui)) {
				if(!gui.monthView.handleEvent(arg0, gui)) {
					System.out.println("Click event not captured by monthView!");
				}else{
					//Mouse Entered Month View

				}
			}else{
				//Mouse Entered Day View

			}
		
		}else{
			//Mouse Entered Event View

		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if(!gui.eventView.handleEvent(arg0, gui)) {
			if(!gui.dayView.handleEvent(arg0, gui)) {
				if(!gui.monthView.handleEvent(arg0, gui)) {
					System.out.println("Click event not captured by monthView!");
				}else{
					//Mouse Exited Month View

				}
			}else{
				//Mouse Exited Day View

			}
		
		}else{
			//Mouse Exited Event View

		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if(!gui.eventView.handleEvent(arg0, gui)) {
			if(!gui.dayView.handleEvent(arg0, gui)) {
				if(!gui.monthView.handleEvent(arg0, gui)) {
					System.out.println("Click event not captured by monthView!");
				}else{
					//Mouse Pressed Month View

				}
			}else{
				//Mouse Pressed Day View

			}
		
		}else{
			//Mouse Pressed Event View

		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(!gui.eventView.handleEvent(arg0, gui)) {
			if(!gui.dayView.handleEvent(arg0, gui)) {
				if(!gui.monthView.handleEvent(arg0, gui)) {
					System.out.println("Click event not captured by monthView!");
				}else{
					//Mouse Released Month View

				}
			}else{
				//Mouse Released Day View

			}
		
		}else{
			//Mouse Released Event View

		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		if(e == WINDOW_ACTIVATED){
			System.out.println("Window Activated");
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		if(e == WINDOW_CLOSED){
			System.out.println("Window Closed");
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if(e == WINDOW_CLOSING){
			System.out.println("Window Closing");
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		if(e == WINDOW_DEACTIVATED){
			System.out.println("Window Deactivated");
		}
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		if(e == WINDOW_DEICONIFIED){
			System.out.println("Window Deiconified");
		}
	}

	@Override
	public void windowIconified(WindowEvent e) {
		if(e == WINDOW_ICONIFIED){
			System.out.println("Window Iconfinied");
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		if(e == WINDOW_OPENED){
			System.out.println("Window Opened");
		}
	}

}
