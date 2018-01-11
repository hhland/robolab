package com.uhealin.tankz.effect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.uhealin.tankz.EDirections;
import com.uhealin.tankz.net.Client;
import com.uhealin.tankz.net.IStatic;
import com.uhealin.tankz.net.TankDeadMsg;
import com.uhealin.tankz.tank.Tank;



//�ڵ���
public class Cannon implements IStatic {

	private static final int XSPEED = 25, YSPEED = 25;   //�ڵ����ƶ��ٶ�
	public static final int CANNON_WIDTH = 10, CANNON_HEIGHT = 10;  //�ڵ��Ĵ�С
	private int x, y;         //�ڵ���λ������
	private Client client;    //�ڵ���������Ϸ����
	private EDirections ptdir;   //����
	private Color c, cannoncolor;   //�ڵ���ɫ
	private boolean stand, alive;   //�ڵ�������������ת̬
	public Tank tank;        //������ڵ���̹��

	//���캯��
	public Cannon(int x, int y, Client client, boolean stand, EDirections ptdir,
			Tank tank) {
		this.x = x;
		this.y = y;
		this.ptdir = ptdir;
		this.client = client;
		this.alive = true;
		this.stand = stand;
		this.cannoncolor = Color.YELLOW;
		this.tank = tank;
	}

	//�滭�ڵ�
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		c = g.getColor();
		g.setColor(cannoncolor);
		g.fillArc(x, y, CANNON_WIDTH, CANNON_HEIGHT, 0, 360);
		g.setColor(c);
		move();
		hitTank();
	}

    //�ƶ��ڵ�
	public void move() {
		if (ptdir == EDirections.N)
			y -= YSPEED;
		else if (ptdir == EDirections.NE) {
			x += XSPEED;
			y -= YSPEED;
		} else if (ptdir == EDirections.E) {
			x += XSPEED;
		} else if (ptdir == EDirections.SE) {
			x += XSPEED;
			y += YSPEED;
		} else if (ptdir == EDirections.S) {
			y += YSPEED;
		} else if (ptdir == EDirections.SW) {
			x -= XSPEED;
			y += YSPEED;
		} else if (ptdir == EDirections.W) {
			x -= XSPEED;
		} else if (ptdir == EDirections.NW) {
			x -= XSPEED;
			y -= YSPEED;
		}
		if (x < 0 || y < 0 || x > client.WIDTH || y > client.HEIGHT)
			client.cannons.remove(this);
	}

	//���̹��
	public void hitTank() {
		if (this.stand) {
			for (int i = this.client.emeytanks.size() - 1; i >= 0; i--) {
				if (this.getRec().intersects(
						this.client.emeytanks.get(i).getRec())) {
					{
						this.client.cannons.remove(this);
						this.client.explodes
								.add(new Explode(x, y, this.client));
						this.client.emeytanks.remove(i);
					}
				}
			}
			for (int i = this.client.nettanks.size() - 1; i >= 0; i--)
				if (this.getRec().intersects(
						this.client.nettanks.get(i).getRec())) {
					this.client.cannons.remove(this);
					this.client.explodes.add(new Explode(x, y, this.client));
				}
			if (this.tank != this.client.mytank && this.client.mytank.alive
					&& this.getRec().intersects(this.client.mytank.getRec())) {
				this.client.cannons.remove(this);
				this.client.explodes.add(new Explode(x, y, this.client));
				this.client.mytank.setLive(false);
				this.client.nethelper.send(new TankDeadMsg(
						this.client.mytank.tankId));
			}

		}

		else if (!this.stand
				&& this.getRec().intersects(this.client.mytank.getRec())) {
			this.client.explodes.add(new Explode(x, y, this.client));
			this.client.mytank.setLive(false);

		}
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	//��ȡ�ڵ����
	public Rectangle getRec() {
		return new Rectangle(x, y, CANNON_WIDTH, CANNON_HEIGHT);
	}
}
