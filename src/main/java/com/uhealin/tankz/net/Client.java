package com.uhealin.tankz.net;

import java.util.*;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.uhealin.tankz.effect.Cannon;
import com.uhealin.tankz.effect.Explode;
import com.uhealin.tankz.tank.EmeyTank;
import com.uhealin.tankz.tank.Mytank;
import com.uhealin.tankz.tank.NetTank;





public class Client extends Frame implements WindowListener,KeyListener{

	//���ô��ڵĿ�͸ߣ��Զ�����ʽ��ʾ
	public static final int WIDTH=800,HEIGHT=600; 
	//
	Image offscreenimage=null;
	Graphics offsetg;
	Color color;
	public NetHelper nethelper=null;
	ConnDialog dialog=new ConnDialog();
//����һ��mytank���󣬱�ʾ�ҷ���̹��
	public Mytank mytank=new Mytank(400,400,this,true);
    public List<Cannon> cannons=new ArrayList<Cannon>();
    public List<EmeyTank> emeytanks=new ArrayList<EmeyTank>();
    public List<Explode> explodes=new ArrayList<Explode>();
    public List<NetTank> nettanks=new ArrayList<NetTank>();
   
  
//���캯��
	public Client()
	{
		//loadForm();
		offscreenimage=this.createImage(WIDTH, HEIGHT);
		nethelper=new NetHelper(this);
		
		
	}
//��������
	public void loadForm()
	{
		this.setTitle("MyTank.Net beta 1.5");
		this.setSize(WIDTH, HEIGHT);
		this.setVisible(true);
		this.addWindowListener(this);
		this.addKeyListener(this);
		this.setResizable(false);
		this.setBackground(Color.BLACK);
		this.setVisible(true);
		//for(int i=0;i<20;i++)
		//      emeytanks.add(new EmeyTank(20*i,10,this,false));
		new drawThread().start();
	}
   
//�滭��Ϸ
	@Override
	public void paint(Graphics g)
	{
		color=g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("mytankId: "+mytank.tankId,15,110);
		g.drawString("emeytanks: "+emeytanks.size(),15,50);
		g.drawString("nettanks: "+nettanks.size(),15,65);
		g.drawString("cannons: "+cannons.size(),15,80);
		g.drawString("explodes: "+explodes.size(),15,95);
		g.setColor(color);
			mytank.draw(g);
		for(int i=cannons.size()-1 ;i>=0;i--)
			cannons.get(i).draw(g);
		for(int i=emeytanks.size()-1;i>=0;i--)
			emeytanks.get(i).draw(g);
		for(int i=nettanks.size()-1;i>=0;i--)
			nettanks.get(i).draw(g);
		for(int i=explodes.size()-1;i>=0;i--)
			explodes.get(i).draw(g);
        
		
	}
	
	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		//g=this.getGraphics();
		//if(offscreenimage==null)
			
		offsetg=offscreenimage.getGraphics();
		color=offsetg.getColor();
		offsetg.setColor(Color.BLACK);
		offsetg.fillRect(0, 0, WIDTH, HEIGHT);
		offsetg.setColor(color);
		print(offsetg);
		g.drawImage(offscreenimage, 0, 0, null);
		
	
	}
	
//�ػ�
	
	class drawThread extends Thread
	{   
	    
	    
		public void run()
		{
			while(true)
			{repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
		}
	}
	
	public class ConnDialog extends Dialog {
		Button btnConnect = new Button("ȷ��");
        TextField tfIP = new TextField("127.0.0.1", 12);
        TextField tfPort = new TextField("" + Server.TCP_PORT, 4);

		public ConnDialog() {
			super(Client.this, true);
			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfIP);
			this.add(new Label("Port:"));
			this.add(tfPort);
			this.add(btnConnect);
			this.setLocation(300, 300);
			this.pack();
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}
			});
			btnConnect.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					String IP = tfIP.getText().trim();
					int port = Integer.parseInt(tfPort.getText().trim());
					nethelper.connect(IP, port);
					setVisible(false);
				}

			});
		}

	}
	
//����Ϊ��д�ӿڵķ���
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
	}
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		this.dispose();
	}
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_N)
			dialog.setVisible(true);
		else
		mytank.KeyPress(e);
	}
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		mytank.KeyRelease(e);
	}
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
