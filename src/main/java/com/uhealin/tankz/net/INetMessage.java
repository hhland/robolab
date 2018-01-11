package com.uhealin.tankz.net;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface INetMessage {

	public static final int TANK_NEW_MSG=0;
	public static final int TANK_MOVE_MSG=1;
	public static final int TANK_FIRE_MSG=2;
	public static final int TANK_DEAD_MSG=3;
	public static final int CANNON_DEAD_MSG=4;
	
	public void sendMsg(DatagramSocket ds,String ip,int udpport );
	
	public void translateMsg(DataInputStream dis);
}
