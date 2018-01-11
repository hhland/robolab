package com.uhealin.tankz.net;

import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server extends Frame implements WindowListener {
	private static final int HEIGHT = 400, WIDTH = 300;
	private static int ID = 100, CLIENTUDP = 2222;
	ServerSocket ss = null;
	public TextArea taInfo = new TextArea();
	/**
	 * TCP的监听端口
	 */
	public static final int TCP_PORT = 8888;

	/**
	 * UDP的监听端口
	 */
	public static final int UDP_PORT = 6666;

	List<client> clients = new ArrayList<client>();

	// public Server()
	// {this.loadForm();}

	/**
	 * 启动服务器
	 * 
	 */

	public void loadForm() {
		this.setSize(this.HEIGHT, this.WIDTH);
		this.setLocation(400, 300);
		this.setTitle("超级坦克大战服务端");
		// this.setLayout(new GridLayout());
		// this.add(new Label("TCP端口:"));
		taInfo = new TextArea();
		taInfo.setText("超级坦克大战服务端启动完毕\n\n");
		this.add(taInfo);
		this.addWindowListener(this);
		this.setVisible(true);

	}

	public void start() {

		new Thread(new UDPBroadThread(this)).start();
		try {
			ss = new ServerSocket(TCP_PORT);

		} catch (IOException e) {
			System.out.println("error:start");
		}

		while (true) {
			Socket s = null;
			try {
				taInfo.setText(taInfo.getText() + "监听客户端连接\n");
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String IP = s.getInetAddress().getHostAddress();
				// int udpPort = dis.readInt();
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(ID++);
				dos.writeInt(CLIENTUDP);
				client c = new client(IP, CLIENTUDP++);
				clients.add(c);
				// s.close();
				taInfo.setText(taInfo.getText() + "一个客户端以连接上! 地址- "
						+ s.getInetAddress() + ":" + s.getPort()
						+ "\n分配到UDP Port:" + CLIENTUDP + "\n分配到id号:" + (ID - 1)
						+ "\n");
				taInfo.setText(taInfo.getText() + "一个客服线程启动，总客服线程数 :" + clients.size()
						+ "\n\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (s != null) {
					try {
						s.close();
						s = null;
					} catch (IOException e) {
						System.out.println("error:start");
					}
				}
			}
		}

	}

	

	private class client {
		String IP;

		int udpPort;

		public client(String IP, int udpPort) {
			this.IP = IP;
			this.udpPort = udpPort;
		}
	}

	private class UDPBroadThread implements Runnable {

		Server server;
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);

		public UDPBroadThread(Server server) {
			this.server = server;
		}

		public void run() {
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(UDP_PORT);
			} catch (SocketException e) {
				System.out.println("error:broad");
			}
			while (ds != null) {
				try {
					ds.receive(dp);
					for (int i = server.clients.size()-1; i>=0; i--) {
						client c = server.clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.IP,
								c.udpPort));
						ds.send(dp);
					}
					// System.out.println("a packet received!");
				} catch (IOException e) {
					System.out.println("error:broad");
				}
			}
		}

	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
       System.exit(0);
	}

	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.exit(0);
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
}
