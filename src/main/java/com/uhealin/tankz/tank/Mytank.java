package com.uhealin.tankz.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.ImageProducer;

import com.uhealin.tankz.EDirections;
import com.uhealin.tankz.net.Client;
import com.uhealin.tankz.net.TankFireMsg;
import com.uhealin.tankz.net.TankMoveMsg;
import com.uhealin.tankz.net.TankNewMsg;


public class Mytank extends Tank{

	
	 private boolean Btnup,Btndown,Btnleft,Btnright;

//���캯��
	public Mytank(int x, int y, Client client, boolean stand) {
		super(x, y, client, stand);
	    this.tankcolor=Color.WHITE;
	    this.ptcolor=Color.BLUE;
	    this.stand=true;
	    this.tankId=0;
		// TODO Auto-generated constructor stub
	}
	
	
	
	public void Move()
	{
		this.move();
		this.setDirection();
	}

	public void Draw(Graphics g)
	{
        this.draw(g);
        this.Move();
	}
	
	@Override
	

	public void setDirection()
	{    EDirections olddir=dir;
		 if(Btnup&&!Btndown&&!Btnleft&&!Btnright) dir=ptdir=EDirections.N;
		   else if(Btnup&&!Btndown&&!Btnleft&&Btnright) dir=ptdir=EDirections.NE;
		   else if(!Btnup&&!Btndown&&!Btnleft&&Btnright) dir=ptdir=EDirections.E;
		   else if(!Btnup&&Btndown&&!Btnleft&&Btnright) dir=ptdir=EDirections.SE;
		   else if(!Btnup&&Btndown&&!Btnleft&&!Btnright) dir=ptdir=EDirections.S;
		   else if(!Btnup&&Btndown&&Btnleft&&!Btnright) dir=ptdir=EDirections.SW;
		   else if(!Btnup&&!Btndown&&Btnleft&&!Btnright) dir=ptdir=EDirections.W;
		   else if(Btnup&&!Btndown&&Btnleft&&!Btnright) dir=ptdir=EDirections.NW;
		   else if(!Btnup&&!Btndown&&!Btnleft&&!Btnright)  dir=EDirections.STOP;
		 if(this.tankId!=0&&olddir!=dir)
			this.client.nethelper.send(new TankMoveMsg(tankId, x, y, dir, ptdir));
	}
	
	public void KeyPress(KeyEvent e)
	{
		
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_RIGHT:Btnright=true;break;
		case KeyEvent.VK_LEFT:Btnleft=true;break;
		case KeyEvent.VK_UP:Btnup=true;break;
		case KeyEvent.VK_DOWN:Btndown=true;break;
		//case KeyEvent.VK_CONTROL:fire();break;
		case KeyEvent.VK_SPACE:client.mytank.setLive(true);break;
		//case KeyEvent.VK_A:aroundfire();break;
		case KeyEvent.VK_P:client.emeytanks.add(new EmeyTank(client.WIDTH>>1,client.HEIGHT>>1,
				client,false));break;
		//case KeyEvent.VK_N:
			//if(this.tankId==0)
			//client.nethelper.connect("127.0.0.1", 8888);
        }
	}
	
	public void KeyRelease(KeyEvent e)
	{
			
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_RIGHT:Btnright=false;break;
			case KeyEvent.VK_LEFT:Btnleft=false;break;
			case KeyEvent.VK_UP:Btnup=false;break;
			case KeyEvent.VK_DOWN:Btndown=false;break;
			case KeyEvent.VK_CONTROL:
				this.fire(ptdir);
				if(this.tankId!=0)
					this.client.nethelper.send(new TankFireMsg(this.tankId,this.ptdir));
				break;
			case KeyEvent.VK_SPACE:
				client.mytank.setLive(true);
				if(this.tankId!=0)
					this.client.nethelper.send(new TankNewMsg(this.client.mytank));
				break;
			//case KeyEvent.VK_A:aroundfire();break;
	        }
	}
	

	

}
