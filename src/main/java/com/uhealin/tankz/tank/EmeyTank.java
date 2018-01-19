package com.uhealin.tankz.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.uhealin.tankz.EDirections;
import com.uhealin.tankz.net.Client;



//�з�̹����
public class EmeyTank extends Tank {

	int step;      //����ƶ�����
	Random r;      //��������� 
	EDirections[] dirs;     //��������

//���췽��
	EmeyTank(int x, int y, Client client, boolean stand) {
		super(x, y, client, stand);
		r = new Random();
		this.tankcolor = Color.GREEN;
		this.ptcolor = Color.BLACK;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		this.Move();
	}

	public void Move() {
		this.setDirection();
		this.move();

	}

	public void setDirection() {
		if (step == 0) {
			step = r.nextInt(12) + 3;
			dirs = EDirections.values();
			dir = dirs[r.nextInt(dirs.length)];
			if (dir != EDirections.STOP)
				ptdir = dir;
		} else
			step--;
		if (r.nextInt(40) > 25)
			this.fire(ptdir);

	}

}
