package com.uhealin.tankz.net;


import com.uhealin.tankz.net.INetMessage;
import com.uhealin.tankz.net.TankNewMsg;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;




public class NetHelper {
	Client client;

	private int udpPort;

	String IP; // server IP

	DatagramSocket ds = null;
	
	
	

	public NetHelper(Client client) {
		this.client = client;

	}
	
	
	public void connect(String IP, int port) {

		this.IP = IP;

		Socket s = null;
		try {
			s = new Socket(IP, port);
			/*DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);*/
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			this.udpPort=dis.readInt();
			client.mytank.tankId= id;
			System.out.println("Connected to server! and server give me a ID:"
					+ id);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		TankNewMsg msg = new TankNewMsg(client.mytank);
		send(msg);

		new Thread(new UDPRecvThread()).start();
	}

	
	/**
	 * ������Ϣ
	 * @param msg �����͵���Ϣ
	 */
	public void send(INetMessage msg) {
		msg.sendMsg(ds, IP, Server.UDP_PORT);
	}

	private class UDPRecvThread implements Runnable {

		byte[] buf = new byte[1024];

		public void run() {

			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					parse(dp);
					//System.out.println("a packet received from server!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp
					.getLength());
			DataInputStream dis = new DataInputStream(bais);
			int msgType = 0;
			try {
				msgType = dis.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			INetMessage msg = null;
			switch (msgType) {
			case INetMessage.TANK_NEW_MSG:
				msg = new TankNewMsg(NetHelper.this.client);
				msg.translateMsg(dis);
				break;
			case INetMessage.TANK_MOVE_MSG:
				msg = new TankMoveMsg(NetHelper.this.client);
				msg.translateMsg(dis);
				break;
		   case INetMessage.TANK_FIRE_MSG:
				msg = new TankFireMsg(NetHelper.this.client);
				msg.translateMsg(dis);
				break;
			case INetMessage.TANK_DEAD_MSG:
				msg = new TankDeadMsg(NetHelper.this.client);
				msg.translateMsg(dis);
				break;
			}

		}

	}
	
}
