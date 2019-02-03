package edu.uoregon.cs.calendar499;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Calendar;

public class UserInput implements MouseListener, WindowListener, KeyListener {
	private GUI gui;

	public GUI getGui() {
		return gui;
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}

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
		} else if (gui.view.getTitleBox(arg0.getPoint()) == -1) {
			gui.view.hideEvent();
			gui.view.hideEvents();
			
			GUI.instance.main.cal.set(Calendar.YEAR, GUI.instance.main.cal.get(Calendar.YEAR)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? -1 : 0));
			GUI.instance.main.cal.set(Calendar.MONTH, GUI.instance.main.cal.get(Calendar.MONTH)
					+ (GUI.instance.main.cal.get(Calendar.MONTH) == 0 ? 11 : -1));
		} else if (gui.view.getTitleBox(arg0.getPoint()) == 1) {
			gui.view.hideEvent();
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
				gui.view.setSelectedField(0);
				break;
			case -4:
				gui.view.setSelectedField(2);
				gui.view.setOverField(1);
				break;
			case -14:
				gui.view.setSelectedField(2);
				gui.view.setOverField(0);
				break;
			case -5:
				gui.view.setSelectedField(3);
				gui.view.setOverField(3);
				break;
			case -15:
				gui.view.setSelectedField(3);
				gui.view.setOverField(2);
				break;
			case -6:
				gui.view.setEventAllDay(!gui.view.isEventAllDay());
				gui.view.setSelectedField(-1);
				gui.view.setOverField(-1);
				break;
			case -7:
				gui.view.setSelectedField(4);
				break;
			case -8:
				// Save..
				gui.view.setSelectedField(8);
				if (gui.view.attemptSaving()) {
					gui.view.hideEvent();
					gui.view.setSelectedField(-1);
				} else {
					gui.view.setDelta(gui.frame.getFrameCount());
					gui.view.setRedBox(true);
				}

				break;
			case -9:
				if (gui.view.getEventIndex() != -1) {
					// actually have to delete it.
					ArrayList<CalendarEvent> eventsOrig = GUI.instance.main.globalCalendar
							.grab(gui.view.getEventOrigDate());
					eventsOrig.remove(gui.view.getEventIndex());
				}
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
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		GUI.instance.dispose();
		GUI.instance.frame.timer.stop();
		Main.instance.showWaitingWindow("Saving calendar");
		/*
		 * 
		 * /* Handle errors....
		 */
		// None the less...exit*/
		new Thread(new Runnable() {
			public void run() {
				FileIOStatus e = SaveEvents.saveCalendar(Main.instance.globalCalendar, Main.filePath);
				boolean waiting = true;
				while (waiting) {
					Main.waitSafe(10);
					e.lock.lock();
					waiting = e.currentStatus.equals(Status.Waiting);

					e.lock.unlock();
				}
				Main.waitSafe(1000);
				Main.instance.destroyWaitingWindow();
			}
		}).start();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}
	/*
	 * Escape
	 * Left
	 * Right
	 * PageUp
	 * PageDown
	 * 
	 */
	private boolean[] keysDown = new boolean[5]; 
	@Override
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			//Leave the 
			keysDown[0] = true;
			break;
		case KeyEvent.VK_LEFT:
			keysDown[1] = true;
			break;
		case KeyEvent.VK_PAGE_UP:
			// Only allow for changing months via keypress when in the 
			keysDown[3] = true;
			break;
		case KeyEvent.VK_RIGHT:
			keysDown[2] = true;
			break;
		case KeyEvent.VK_PAGE_DOWN:
			keysDown[4] = true;
			break;
		default:
			Main.logDebug(3, arg0.getKeyCode());
			break;
		}
	}

	
	
	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		char cc = arg0.getKeyChar();
		if ((int) cc < 32 && (int) cc != 8 && (int) cc != 9) {
			System.out.println((int) cc);
			// ignore?
		} else {
			switch (gui.view.getSelectedField()) {
			case -1:
				break;
			case 0:
				if((int)cc == 9) {
					gui.view.setSelectedField(2);
					gui.view.setOverField(0);
				}else if ((int) cc == 8) {
					if (gui.view.getEventTitle().length() > 0) {
						gui.view.setEventTitle(
								gui.view.getEventTitle().substring(0, gui.view.getEventTitle().length() - 1));
					} else {
						gui.view.setDelta(gui.frame.getFrameCount());
						gui.view.setRedBox(true);
					}
				} else {
					if (gui.view.getEventTitle().length() < 40) {
						gui.view.setEventTitle(gui.view.getEventTitle() + cc);
					} else {
						gui.view.setDelta(gui.frame.getFrameCount());
						gui.view.setRedBox(true);
					}

				}
				break;
			case 4:
				if((int)cc == 9) {
					gui.view.setSelectedField(-1);
				}else if ((int) cc == 8) {
					if (gui.view.getEventNotes().length() > 0) {
						gui.view.setEventNotes(
								gui.view.getEventNotes().substring(0, gui.view.getEventNotes().length() - 1));
					} else {
						gui.view.setDelta(gui.frame.getFrameCount());
						gui.view.setRedBox(true);
					}
				} else {
					if (gui.view.getEventNotes().length() < 40) {
						gui.view.setEventNotes(gui.view.getEventNotes() + cc);
					} else {
						gui.view.setDelta(gui.frame.getFrameCount());
						gui.view.setRedBox(true);
					}

				}
				break;
			case 2:
				if (((int) cc >= 48 && (int) cc <= 58) || (int) cc == 8 || (int) cc == 9) {
					if ((int) cc == 9) {
						gui.view.setPrevContents("");
						gui.view.setOverField(gui.view.getOverField() + 1);
						if (gui.view.getOverField() == 2) {
							gui.view.setSelectedField(3);
						}
					} else {
						if (gui.view.getOverField() == 0) {
							if ((int) cc == 8) {
								if (gui.view.getPrevContents().length() > 0) {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1));
								} else {
									gui.view.setDelta(gui.frame.getFrameCount());
									gui.view.setRedBox(true);
								}
							} else {
								if (gui.view.getPrevContents().length() < 2) {
									gui.view.setPrevContents(gui.view.getPrevContents() + cc);
									if (Integer.parseInt(gui.view.getPrevContents()) > 23) {
										// Coerse it
										gui.view.setPrevContents("23");
									}
									if (gui.view.getPrevContents().length() == 2) {
										gui.view.setEventTime1(
												new String(new char[2 - gui.view.getPrevContents().length()])
														.replace('\0', ' ') + gui.view.getPrevContents()
														+ gui.view.getEventTime1().substring(2));
										gui.view.setPrevContents("");
										gui.view.setOverField(1);
										return;
									}
								} else if (gui.view.getPrevContents().charAt(0) == '0') {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1) + cc);
									if (Integer.parseInt(gui.view.getPrevContents()) > 23) {
										// Coerse it
										gui.view.setPrevContents("23");
									}
									if (gui.view.getPrevContents().length() == 2) {
										gui.view.setEventTime1(
												new String(new char[2 - gui.view.getPrevContents().length()])
														.replace('\0', ' ') + gui.view.getPrevContents()
														+ gui.view.getEventTime1().substring(2));
										gui.view.setPrevContents("");
										gui.view.setOverField(1);
										return;
									}
								} else {
									gui.view.setPrevContents("");
									gui.view.setOverField(1);
									return;
								}
							}
							gui.view.setEventTime1(
									new String(new char[2 - gui.view.getPrevContents().length()]).replace('\0', ' ')
											+ gui.view.getPrevContents() + gui.view.getEventTime1().substring(2));
						} else if (gui.view.getOverField() == 1) {
							if ((int) cc == 8) {
								if (gui.view.getPrevContents().length() > 0) {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1));
								} else {
									gui.view.setDelta(gui.frame.getFrameCount());
									gui.view.setRedBox(true);
								}
							} else {
								if (gui.view.getPrevContents().length() < 2) {
									gui.view.setPrevContents(gui.view.getPrevContents() + cc);
									if (Integer.parseInt(gui.view.getPrevContents()) > 59) {
										// Coerse it
										gui.view.setPrevContents("59");
									}
									if (gui.view.getPrevContents().length() == 2) {
										gui.view.setEventTime1(gui.view.getEventTime1().substring(0, 3)
												+ new String(new char[2 - gui.view.getPrevContents().length()])
														.replace('\0', ' ')
												+ gui.view.getPrevContents());
										gui.view.setPrevContents("");
										gui.view.setOverField(2);
										gui.view.setSelectedField(3);
										return;
									}
								} else if (gui.view.getPrevContents().charAt(0) == '0') {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1) + cc);
									if (Integer.parseInt(gui.view.getPrevContents()) > 59) {
										// Coerse it
										gui.view.setPrevContents("59");
									}
									if (gui.view.getPrevContents().length() == 2) {
										gui.view.setEventTime1(gui.view.getEventTime1().substring(0, 3)
												+ new String(new char[2 - gui.view.getPrevContents().length()])
														.replace('\0', ' ')
												+ gui.view.getPrevContents());
										gui.view.setPrevContents("");
										gui.view.setOverField(2);
										gui.view.setSelectedField(3);
										return;
									}
								} else {
									gui.view.setPrevContents("");
									gui.view.setOverField(2);
									gui.view.setSelectedField(3);
									return;
								}
							}
							gui.view.setEventTime1(gui.view.getEventTime1().substring(0, 3)
									+ new String(new char[2 - gui.view.getPrevContents().length()]).replace('\0', ' ')
									+ gui.view.getPrevContents());
						}

					}
				} else {
					gui.view.setDelta(gui.frame.getFrameCount());
					gui.view.setRedBox(true);
				}
				break;
			case 3:
				if (((int) cc >= 48 && (int) cc <= 58) || (int) cc == 8 || (int) cc == 9) {
					if ((int) cc == 9) {
						gui.view.setPrevContents("");
						gui.view.setOverField(gui.view.getOverField() + (gui.view.getOverField() == 3 ? -4 : 1));
						if (gui.view.getOverField() == -1) {
							gui.view.setSelectedField(4);
						}
					} else {
						if (gui.view.getOverField() == 2) {
							if ((int) cc == 8) {
								if (gui.view.getPrevContents().length() > 0) {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1));
								} else {
									gui.view.setDelta(gui.frame.getFrameCount());
									gui.view.setRedBox(true);
								}
							} else {
								if (gui.view.getPrevContents().length() < 2) {
									gui.view.setPrevContents(gui.view.getPrevContents() + cc);
									if (Integer.parseInt(gui.view.getPrevContents()) > 23) {
										// Coerse it
										gui.view.setPrevContents("23");
									}
									if (gui.view.getPrevContents().length() == 2) {
										gui.view.setEventTime2(
												new String(new char[2 - gui.view.getPrevContents().length()])
														.replace('\0', ' ') + gui.view.getPrevContents()
														+ gui.view.getEventTime2().substring(2));
										gui.view.setPrevContents("");
										gui.view.setOverField(3);
										return;
									}
								} else if (gui.view.getPrevContents().charAt(0) == '0') {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1) + cc);
									if (Integer.parseInt(gui.view.getPrevContents()) > 23) {
										// Coerse it
										gui.view.setPrevContents("23");
									}
									if (gui.view.getPrevContents().length() == 2) {
										gui.view.setEventTime2(
												new String(new char[2 - gui.view.getPrevContents().length()])
														.replace('\0', ' ') + gui.view.getPrevContents()
														+ gui.view.getEventTime2().substring(2));
										gui.view.setPrevContents("");
										gui.view.setOverField(3);
										return;
									}
								} else {
									gui.view.setPrevContents("");
									gui.view.setOverField(3);
									return;
								}
							}
							gui.view.setEventTime2(
									new String(new char[2 - gui.view.getPrevContents().length()]).replace('\0', ' ')
											+ gui.view.getPrevContents() + gui.view.getEventTime2().substring(2));
						} else if (gui.view.getOverField() == 3) {
							if ((int) cc == 8) {
								if (gui.view.getPrevContents().length() > 0) {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1));
								} else {
									gui.view.setDelta(gui.frame.getFrameCount());
									gui.view.setRedBox(true);
								}
							} else {
								if (gui.view.getPrevContents().length() < 2) {
									gui.view.setPrevContents(gui.view.getPrevContents() + cc);
									if (Integer.parseInt(gui.view.getPrevContents()) > 59) {
										// Coerse it
										gui.view.setPrevContents("59");
									}
									if (gui.view.getPrevContents().length() == 2) {
										gui.view.setEventTime2(gui.view.getEventTime2().substring(0, 3)
												+ new String(new char[2 - gui.view.getPrevContents().length()])
														.replace('\0', ' ')
												+ gui.view.getPrevContents());
										gui.view.setPrevContents("");
										gui.view.setOverField(-1);
										gui.view.setSelectedField(-1);
										return;
									}
								} else if (gui.view.getPrevContents().charAt(0) == '0') {
									gui.view.setPrevContents(gui.view.getPrevContents().substring(1) + cc);
									if (Integer.parseInt(gui.view.getPrevContents()) > 59) {
										// Coerse it
										gui.view.setPrevContents("59");
									}
									if (gui.view.getPrevContents().length() == 2) {
										gui.view.setEventTime2(gui.view.getEventTime2().substring(0, 3)
												+ new String(new char[2 - gui.view.getPrevContents().length()])
														.replace('\0', ' ')
												+ gui.view.getPrevContents());
										gui.view.setPrevContents("");
										gui.view.setOverField(-1);
										gui.view.setSelectedField(-1);
										return;
									}
								} else {
									gui.view.setPrevContents("");
									gui.view.setOverField(-1);
									gui.view.setSelectedField(-1);
									return;
								}
							}
							gui.view.setEventTime2(gui.view.getEventTime2().substring(0, 3)
									+ new String(new char[2 - gui.view.getPrevContents().length()]).replace('\0', ' ')
									+ gui.view.getPrevContents());
						}

					}
				} else {
					gui.view.setDelta(gui.frame.getFrameCount());
					gui.view.setRedBox(true);
				}
				break;
			}
		}
	}

}
