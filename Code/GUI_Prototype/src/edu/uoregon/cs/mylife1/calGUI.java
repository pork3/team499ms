package edu.uoregon.cs.mylife1;

import java.awt.Dimension;

import javax.swing.JFrame;

public class calGUI extends JFrame{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Dimension windowSize = new Dimension(800,600);
	public static final Dimension windowSize1 = new Dimension(800,650);
	public static calGUI instance;
	public calGUI() {
		super("Calendar 499");
		instance = this;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.add(new contents());
		this.pack();
		this.setLocationRelativeTo(null);
		
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		new calGUI();
	}
}
