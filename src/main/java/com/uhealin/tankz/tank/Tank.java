package com.uhealin.tankz.tank;
import com.uhealin.tankz.EDirections;
import com.uhealin.tankz.effect.Cannon;
import com.uhealin.tankz.net.Client;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


import com.uhealin.tankz.net.IDdynamic;





public class Tank implements IDdynamic
{
	//����̹�˵�ID��
	 public int tankId;
	//����̹�˵��ƶ��ٶ�
	protected static final int XSPEED=3,YSPEED=3;
    //����̹�˵Ĵ�С
	protected static final int TANK_WIDTH=30,TANK_HEIGHT=30;
	//����̹�˵�����
	public int x,y;
	protected int cannonx,cannony;
    //����һ��Client����ʾ̹�����ڵĳ���
	protected Client client;
    //����̹�˵�����stand��trueΪ�ҷ���falseΪ�з�
	protected boolean stand;
   
	//����̹�˵�״̬life,trueΪ����,falseΪ����
	public boolean alive;
	
    //��������Directionö�ٱ������ֱ��ʾ̹�˵��˶��������Ͳ�ķ���
    public EDirections dir,ptdir;
    //����̹�˵ķ�������ֱ�Ϊ���������ĸ�����ֵ��trueΪ���µ�״̬��falseΪδ����
    Color c,tankcolor,ptcolor;
    Cannon cannon;
   
    
    Tank(int x,int y,Client client,boolean stand)
	{
		
		this.x=x;
		this.y=y;
		this.client=client;
		this.stand=stand;
		this.alive=true;
		this.dir= EDirections.STOP;
		this.ptdir= EDirections.N;
		
	}
    
    public void setDirection()
    {}
    
    public void move() {
		// TODO Auto-generated method stub
		if(dir==EDirections.N)
		{y-=YSPEED;}
		else if(dir==EDirections.NE)
		{x+=XSPEED;y-=YSPEED;}
		else if(dir==EDirections.E)
		{x+=XSPEED;}
		else if(dir==EDirections.SE)
		{x+=XSPEED;y+=YSPEED;}
		else if(dir==EDirections.S)
		{y+=YSPEED;}
		else if(dir==EDirections.SW)
		{x-=XSPEED;y+=YSPEED;}
		else if(dir==EDirections.W)
		{x-=XSPEED;}
		else if(dir==EDirections.NW)
		{x-=XSPEED;y-=YSPEED;}
		
		switch(dir)
		{
		case N:y-=YSPEED;break;
		case NE:x+=XSPEED;y-=YSPEED;break;
		case E:x+=XSPEED;break;
		case SE:x+=XSPEED;y+=YSPEED;break;
		case S:y+=YSPEED;break;
		case SW:x-=XSPEED;y+=YSPEED;break;
		case W:x-=XSPEED;break;
		case NW:x-=XSPEED;y-=YSPEED;break;
		case STOP:break;
		}
		if(x < (TANK_WIDTH>>1)) x =(TANK_WIDTH)>>1;
		if(y < (TANK_HEIGHT>>1)) y = (TANK_HEIGHT)>>1;
		if(x + (TANK_WIDTH>>1)> Client.WIDTH) x =Client.WIDTH - (TANK_WIDTH>>1);
		if(y + (TANK_HEIGHT>>1)> Client.HEIGHT) y =Client.HEIGHT - (TANK_HEIGHT>>1);
				
	}
	
	public Cannon fire(EDirections ptdir)
	{
		if(this.alive==false)
			return  null;
		cannonx = this.x + (TANK_WIDTH>>1) - (Cannon.CANNON_WIDTH>>1);
		cannony = this.y + (TANK_HEIGHT>>1) -(Cannon.CANNON_HEIGHT>>1);
		cannon = new Cannon(cannonx, cannony, this.client, this.stand, ptdir,this);
		client.cannons.add(cannon);
		return cannon;
	}

	public void draw(Graphics g) {
		if(!alive) {
			if(!stand) {
				client.emeytanks.remove(this);
				}
			return;
		}
		c = g.getColor();
		g.setColor(tankcolor);
		g.fillOval(x, y, TANK_WIDTH, TANK_WIDTH);
		g.setColor(ptcolor);
		switch(ptdir) {
		case W:	
			g.drawLine(x + (TANK_WIDTH>>1), y + (TANK_HEIGHT>>1), x, y + (TANK_HEIGHT>>1));
			break;
		case NW:
			g.drawLine(x + (TANK_WIDTH>>1), y + (TANK_HEIGHT>>1), x, y);
			break;
		case N:
			g.drawLine(x + (TANK_WIDTH>>1), y + (TANK_HEIGHT>>1), x + (TANK_WIDTH>>1), y);
			break;
		case NE:
			g.drawLine(x + (TANK_WIDTH>>1), y + (TANK_HEIGHT>>1), x + TANK_WIDTH, y);
			break;
		case E:
			g.drawLine(x + (TANK_WIDTH>>1), y + (TANK_HEIGHT>>1), x + TANK_WIDTH, y + (TANK_HEIGHT>>1));
			break;
		case SE:
			g.drawLine(x + (TANK_WIDTH>>1), y + (TANK_HEIGHT>>1), x + TANK_WIDTH, y + TANK_HEIGHT);
			break;
		case S:
			g.drawLine(x +(TANK_WIDTH>>1), y + (TANK_HEIGHT>>1), x + (TANK_WIDTH>>1), y + TANK_HEIGHT);
			break;
		case SW:
			g.drawLine(x + (TANK_WIDTH>>1), y + (TANK_HEIGHT>>1), x, y + TANK_HEIGHT);
			break;
		}
		g.setColor(c);
		move();
	}
	
	public boolean isLive() {
		// TODO Auto-generated method stub
		return this.alive;
	}

	public void setLive(boolean b) {
		// TODO Auto-generated method stub
		this.alive=b;
	}

	public boolean getStand() {
		// TODO Auto-generated method stub
		return this.stand;
	}
	
	public Rectangle getRec() {
		return new Rectangle(x, y, TANK_WIDTH, TANK_HEIGHT);
	}




   
}
