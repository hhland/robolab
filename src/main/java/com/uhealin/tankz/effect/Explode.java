package com.uhealin.tankz.effect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.uhealin.tankz.net.Client;
import com.uhealin.tankz.net.IStatic;


public class Explode implements IStatic{
	int x, y;
	private int[] diameters = {4, 6,7,9, 12,14,15, 18, 22,24,26,28 ,32,35,37,38,43,45, 43,40, 30, 14, 6};
	private boolean live;
	Color c,explodecolor;
	private Client client;
	int step;
	
	public Explode(int x, int y, Client client) {
		this.x = x;
		this.y = y;
		this.live=true;
		this.client=client;
		this.explodecolor=Color.GRAY;
		this.step=this.diameters.length-1;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			client.explodes.remove(this);
			return;
		}
		c = g.getColor();
		g.setColor(explodecolor);
		g.fillArc(x, y, diameters[step], diameters[step], 0,360);
		g.setColor(c);
		step--;
		if(step == 0) {
			live = false;			
		}
	}

	public void move() {
		// TODO Auto-generated method stub
		
	}

	public Rectangle getRec() {
		// TODO Auto-generated method stub
		return null;
	}
}
