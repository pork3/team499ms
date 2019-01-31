package edu.uoregon.cs.calendar499;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class UserInput implements MouseListener,WindowListener{
	GUI gui;
	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Point p = gui.view.getGUIBox(arg0.getPoint());
		
		System.out.println(p + " " + arg0.getPoint() +" " +  gui.view.getEventBox(arg0.getPoint(), p) + " " + gui.view.getTitleBox(arg0.getPoint()));
		if(!gui.view.isDisplayingEvents() && !gui.view.isDisplayingEvent() && p.x != -1) {
			gui.view.showEvents(p.x, p.y);
		}else {
			int clicked = gui.view.getObjectClicked(arg0.getPoint());
			switch(clicked) {
			case -1:
				gui.view.hideEvents();
				if(p.x != -1) {
					gui.view.showEvents(p.x,p.y);
				}
				break;
			case -2:
				gui.view.hideEvents();
				break;
			case 1:
				break;
			case 2:
				gui.view.moveToLeft();
				break;
			case 3:
				break;
			default:
				System.out.println(clicked);
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
