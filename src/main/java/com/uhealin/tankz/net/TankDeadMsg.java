package com.uhealin.tankz.net;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;




public class TankDeadMsg implements INetMessage{

	int Type=INetMessage.TANK_DEAD_MSG;
	Client client;
	int tankId;
	
	public TankDeadMsg(Client client)
	{
		this.client=client;
	}
	
	public TankDeadMsg(int tankId)
	{
		this.tankId=tankId;
	}
	

	public void sendMsg(DatagramSocket ds, String ip, int udpport) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		DataOutputStream dos=new DataOutputStream(baos);
		try {
			dos.writeInt(Type);
			dos.writeInt(tankId);
			byte[] bytes=baos.toByteArray();
		    DatagramPacket dp=new DatagramPacket(bytes,bytes.length,new InetSocketAddress(ip,udpport));
		    ds.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void translateMsg(DataInputStream dis) {
		// TODO Auto-generated method stub
		 try {
			int id=dis.readInt();
			if(this.client.mytank.tankId==id)
				return;
			for(int i=this.client.nettanks.size()-1;i>=0;i--)
			{
				if(id==this.client.nettanks.get(i).tankId)
					this.client.nettanks.remove(i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
