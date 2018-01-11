package com.uhealin.tankz.net;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface IDdynamic {

	 
	public void move();
	
	public void setDirection();
	
	public void draw(Graphics g);
	
	public Rectangle getRec();
	
	public boolean isLive();
	
	public void setLive(boolean b);
}
