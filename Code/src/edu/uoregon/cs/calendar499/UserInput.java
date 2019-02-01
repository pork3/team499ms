package edu.uoregon.cs.calendar499;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;

public class UserInput implements MouseListener, WindowListener, KeyListener {
	GUI gui;

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Point p = gui.view.getGUIBox(arg0.getPoint());

		if (!gui.view.isDisplayingEvents() && !gui.view.isDisplayingEvent() && p.x != -1) {

			if (!CalMathAbs.IsNotCalMonth(p.y, p.x, GUI.instance.main.cal)) {
				gui.view.showEvents(p.x, p.y);
			} else {
				int dayN = CalMathAbs.GetDayN(p.y, p.x, GUI.instance.main.cal);
				if (dayN < 15) {
					GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
							+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? -1 : 0));
					GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
							+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? 11 : -1));
				} else {
					GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
							+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? 1 : 0));
					GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
							+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? -11 : 1));
				}
				for (int week = 0; week < 6; week++) {
					for (int day = 0; day < 7; day++) {
						if (!CalMathAbs.IsNotCalMonth(week, day, GUI.instance.main.cal)
								&& CalMathAbs.GetDayN(week, day, GUI.instance.main.cal) == dayN) {
							gui.view.showEvents(day, week);
						}
					}
				}
			}
		} else if (gui.view.getTitleBox(arg0.getPoint()) == -1) {
			gui.view.hideEvents();

			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? -1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? 11 : -1));
		} else if (gui.view.getTitleBox(arg0.getPoint()) == 1) {
			gui.view.hideEvents();
			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? 1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? -11 : 1));
		} else {
			int clicked = gui.view.getObjectClicked(arg0.getPoint());
			System.out.println(clicked);
			switch (clicked) {
			case -1:
				if (gui.view.isDisplayingEvent()) {
					gui.view.hideEvent();
					gui.view.hideEvents();
				} else {
					gui.view.hideEvents();
					if (p.x != -1) {
						if (!CalMathAbs.IsNotCalMonth(p.y, p.x, GUI.instance.main.cal)) {
							gui.view.showEvents(p.x, p.y);
						} else {
							System.out.println("Out of current month");
							int dayN = CalMathAbs.GetDayN(p.y, p.x, GUI.instance.main.cal);
							if (dayN > 15) {
								GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
										+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? -1 : 0));
								GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
										+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? 11 : -1));
							} else {
								GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
										+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? 1 : 0));
								GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
										+ (GUI.instance.main.cal.get(Calendar.MONTH) == 11 ? -11 : 1));
							}
							for (int week = 0; week < 6; week++) {
								for (int day = 0; day < 7; day++) {
									if (!CalMathAbs.IsNotCalMonth(week, day, GUI.instance.main.cal)
											&& CalMathAbs.GetDayN(week, day, GUI.instance.main.cal) == dayN) {
										gui.view.showEvents(day, week);
									}
								}
							}
						}
					}
				}
				break;
			case -2:
				gui.view.hideEvents();
				gui.view.hideEvent();
				break;
			case -3:
				gui.view.selectedField = 0;
				break;
			case -4:
				gui.view.selectedField = 2;
				gui.view.overField = 1;
				break;
			case -14:
				gui.view.selectedField = 2;
				gui.view.overField = 0;
				break;
			case -5:
				gui.view.selectedField = 3;
				gui.view.overField = 3;
				break;
			case -15:
				gui.view.selectedField = 3;
				gui.view.overField = 2;
				break;
			case -6:
				gui.view.eventAllDay =!gui.view.eventAllDay;
				gui.view.selectedField = -1;
				gui.view.overField = -1;
				break;
			case -7:
				gui.view.selectedField = 4;
				break;
			case -8:
				//Save..
				gui.view.selectedField = 8; 
				if(gui.view.attemptSaving()) {
					gui.view.hideEvent();
					gui.view.selectedField = -1;
				}else {
					gui.view.delta = gui.frame.frameCount;
					gui.view.redBox = true;
				}
				
				break;
			case -9:
				gui.view.hideEvent();
				break;
			case 0:
				break;
			case 1:
				gui.view.showEvent(-1);
				break;
			case 2:
				gui.view.moveToLeft();
				break;
			case 3:
				gui.view.moveToRight();
				break;
			case 4:
				// scrolled up
				gui.view.scrollUp();
				break;
			case 5:
				gui.view.scrollDown();
				// scrolled down
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				gui.view.showEvent(clicked);
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
		GUI.instance.dispose();
		GUI.instance.frame.timer.stop();

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

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		char cc = arg0.getKeyChar();
		if((int)cc < 32 && (int)cc != 8) {
			//ignore?
		}else {
			switch(gui.view.selectedField) {
			case -1:
				break;
			case 0:
				if((int)cc == 8) {
					if(gui.view.eventTitle.length() > 0) {
						gui.view.eventTitle = gui.view.eventTitle.substring(0, gui.view.eventTitle.length() - 1);
					}else {
						gui.view.delta = gui.frame.frameCount;
						gui.view.redBox = true;
					}
				}else {
					if(gui.view.eventTitle.length() < 40) {
						gui.view.eventTitle = gui.view.eventTitle + cc;
					}else {
						gui.view.delta = gui.frame.frameCount;
						gui.view.redBox = true;
					}
					
				}
				break;
			case 4:
				if((int)cc == 8) {
					if(gui.view.eventNotes.length() > 0) {
						gui.view.eventNotes = gui.view.eventNotes.substring(0, gui.view.eventNotes.length() - 1);
					}else {
						gui.view.delta = gui.frame.frameCount;
						gui.view.redBox = true;
					}
				}else {
					if(gui.view.eventNotes.length() < 40) {
						gui.view.eventNotes = gui.view.eventNotes + cc;
					}else {
						gui.view.delta = gui.frame.frameCount;
						gui.view.redBox = true;
					}
					
				}
				break;
			case 2:
				if(((int)cc >= 48 && (int)cc <= 58)  || (int)cc == 8) {
					if(gui.view.overField == 0) {
						if((int)cc == 8) {
							if(gui.view.prevContents.length() > 0) {
								gui.view.prevContents = gui.view.prevContents.substring(1);
							}else {
								gui.view.delta = gui.frame.frameCount;
								gui.view.redBox = true;
							}
						}else {
							if(gui.view.prevContents.length() < 2) {
								gui.view.prevContents = gui.view.prevContents + cc;
								if(Integer.parseInt(gui.view.prevContents) > 23) {
									//Coerse it
									gui.view.prevContents = "23";
								}
								if(gui.view.prevContents.length() == 2) {
									gui.view.eventTime1 = new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents + gui.view.eventTime1.substring(2);
									gui.view.prevContents = "";
									gui.view.overField = 1;
									return;
								}
							}else if(gui.view.prevContents.charAt(0) == '0'){
								gui.view.prevContents = gui.view.prevContents.substring(1) + cc;
								if(Integer.parseInt(gui.view.prevContents) > 23) {
									//Coerse it
									gui.view.prevContents = "23";
								}
								if(gui.view.prevContents.length() == 2) {
									gui.view.eventTime1 = new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents + gui.view.eventTime1.substring(2);
									gui.view.prevContents = "";
									gui.view.overField = 1;
									return;
								}
							}else {
								gui.view.prevContents = "";
								gui.view.overField = 1;
								return;
							}
						}
						gui.view.eventTime1 = new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents + gui.view.eventTime1.substring(2);
					}else if(gui.view.overField == 1) {
						if((int)cc == 8) {
							if(gui.view.prevContents.length() > 0) {
								gui.view.prevContents = gui.view.prevContents.substring(1);
							}else {
								gui.view.delta = gui.frame.frameCount;
								gui.view.redBox = true;
							}
						}else {
							if(gui.view.prevContents.length() < 2) {
								gui.view.prevContents = gui.view.prevContents + cc;
								if(Integer.parseInt(gui.view.prevContents) > 59) {
									//Coerse it
									gui.view.prevContents = "59";
								}
								if(gui.view.prevContents.length() == 2) {
									gui.view.eventTime1 =  gui.view.eventTime1.substring(0,3) + new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents;
									gui.view.prevContents = "";
									gui.view.overField = 2;
									gui.view.selectedField = 3;
									return;
								}
							}else if(gui.view.prevContents.charAt(0) == '0'){
								gui.view.prevContents = gui.view.prevContents.substring(1) + cc;
								if(Integer.parseInt(gui.view.prevContents) > 59) {
									//Coerse it
									gui.view.prevContents = "59";
								}
								if(gui.view.prevContents.length() == 2) {
									gui.view.eventTime1 =  gui.view.eventTime1.substring(0,3) + new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents;
									gui.view.prevContents = "";
									gui.view.overField = 2;
									gui.view.selectedField = 3;
									return;
								}
							}else {
								gui.view.prevContents = "";
								gui.view.overField = 2;
								gui.view.selectedField = 3;
								return;
							}
						}
						gui.view.eventTime1 =  gui.view.eventTime1.substring(0,3) + new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents;
					}
					
					
				}else {
					gui.view.delta = gui.frame.frameCount;
					gui.view.redBox = true;
				}
				break;
			case 3:
				if(((int)cc >= 48 && (int)cc <= 58)  || (int)cc == 8) {
					if(gui.view.overField == 2) {
						if((int)cc == 8) {
							if(gui.view.prevContents.length() > 0) {
								gui.view.prevContents = gui.view.prevContents.substring(1);
							}else {
								gui.view.delta = gui.frame.frameCount;
								gui.view.redBox = true;
							}
						}else {
							if(gui.view.prevContents.length() < 2) {
								gui.view.prevContents = gui.view.prevContents + cc;
								if(Integer.parseInt(gui.view.prevContents) > 23) {
									//Coerse it
									gui.view.prevContents = "23";
								}
								if(gui.view.prevContents.length() == 2) {
									gui.view.eventTime2 = new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents + gui.view.eventTime2.substring(2);
									gui.view.prevContents = "";
									gui.view.overField = 3;
									return;
								}
							}else if(gui.view.prevContents.charAt(0) == '0'){
								gui.view.prevContents = gui.view.prevContents.substring(1) + cc;
								if(Integer.parseInt(gui.view.prevContents) > 23) {
									//Coerse it
									gui.view.prevContents = "23";
								}
								if(gui.view.prevContents.length() == 2) {
									gui.view.eventTime2 = new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents + gui.view.eventTime2.substring(2);
									gui.view.prevContents = "";
									gui.view.overField = 3;
									return;
								}
							}else {
								gui.view.prevContents = "";
								gui.view.overField = 3;
								return;
							}
						}
						gui.view.eventTime2 = new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents + gui.view.eventTime2.substring(2);
					}else if(gui.view.overField == 3) {
						if((int)cc == 8) {
							if(gui.view.prevContents.length() > 0) {
								gui.view.prevContents = gui.view.prevContents.substring(1);
							}else {
								gui.view.delta = gui.frame.frameCount;
								gui.view.redBox = true;
							}
						}else {
							if(gui.view.prevContents.length() < 2) {
								gui.view.prevContents = gui.view.prevContents + cc;
								if(Integer.parseInt(gui.view.prevContents) > 59) {
									//Coerse it
									gui.view.prevContents = "59";
								}
								if(gui.view.prevContents.length() == 2) {
									gui.view.eventTime2 =  gui.view.eventTime2.substring(0,3) + new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents;
									gui.view.prevContents = "";
									gui.view.overField = -1;
									gui.view.selectedField = -1;
									return;
								}
							}else if(gui.view.prevContents.charAt(0) == '0'){
								gui.view.prevContents = gui.view.prevContents.substring(1) + cc;
								if(Integer.parseInt(gui.view.prevContents) > 59) {
									//Coerse it
									gui.view.prevContents = "59";
								}
								if(gui.view.prevContents.length() == 2) {
									gui.view.eventTime2 =  gui.view.eventTime2.substring(0,3) + new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents;
									gui.view.prevContents = "";
									gui.view.overField = -1;
									gui.view.selectedField = -1;
									return;
								}
							}else {
								gui.view.prevContents = "";
								gui.view.overField = -1;
								gui.view.selectedField = -1;
								return;
							}
						}
						gui.view.eventTime2 =  gui.view.eventTime2.substring(0,3) + new String(new char[2-gui.view.prevContents.length()]).replace('\0', ' ') + gui.view.prevContents;
					}
					
					
				}else {
					gui.view.delta = gui.frame.frameCount;
					gui.view.redBox = true;
				}
				break;
			}
		}
	}

}
