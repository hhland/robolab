package com.uhealin.tankz.net;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import com.uhealin.tankz.EDirections;



public class TankFireMsg implements INetMessage {
    int Type=INetMessage.TANK_FIRE_MSG;
	Client client;
	int tankId;
	EDirections ptdir;
	public TankFireMsg(Client client)
	{
		this.client=client;
	}
	
	public TankFireMsg(int tankId,EDirections ptdir)
	{
		this.tankId=tankId;
		this.ptdir=ptdir;
	}
	
	public void sendMsg(DatagramSocket ds, String ip, int udpport) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		DataOutputStream dos=new DataOutputStream(baos);
        try {
			dos.writeInt(this.Type);
			dos.writeInt(this.tankId);
			dos.writeInt(ptdir.ordinal());
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
        int tankid;
		try {
			tankid = dis.readInt();
			  if(tankid==client.mytank.tankId)
		        	return;
			  EDirections ptdir=EDirections.values()[dis.readInt()];
			  boolean exist=false;
			  for(int i=client.nettanks.size()-1;i>=0;i--)
			  {
				  if(client.nettanks.get(i).tankId==tankid)
					  client.nettanks.get(i).fire(ptdir);
				  exist=true;
				  break;
			  }
				  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
        
		
	}

}
