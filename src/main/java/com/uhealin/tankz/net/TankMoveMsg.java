package com.uhealin.tankz.net;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.uhealin.tankz.EDirections;
import com.uhealin.tankz.tank.Mytank;


public class TankMoveMsg implements INetMessage {

	int Type=INetMessage.TANK_MOVE_MSG;
	int tankId,x,y;
	EDirections dir,ptdir;
	Client client;
	Mytank mytank;
	
	public TankMoveMsg(int tankId, int x, int y, EDirections dir,
			EDirections ptdir) {
		this.tankId=tankId;
		this.x=x;
		this.y=y;
		this.dir=dir;
		this.ptdir=ptdir;
	}
	
	public TankMoveMsg(Client client)
	{
		this.client=client;
	}

	public void sendMsg(DatagramSocket ds, String IP, int udpPort) {
		// TODO Auto-generated method stub
         ByteArrayOutputStream baos=new ByteArrayOutputStream();
         DataOutputStream dos=new DataOutputStream(baos);
         try {
            dos.writeInt(Type);
            dos.writeInt(this.tankId);
			dos.writeInt(this.x);
			dos.writeInt(this.y);
			dos.writeInt(this.dir.ordinal());
			dos.writeInt(this.ptdir.ordinal());
		} catch (IOException e) {
			e.printStackTrace();
		}
         byte[] bytes=baos.toByteArray();
         try {
			DatagramPacket dp=new DatagramPacket(bytes, bytes.length, new InetSocketAddress(IP, udpPort));
		    ds.send(dp);
         } catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e)
		{e.printStackTrace();}
	}

	public void translateMsg(DataInputStream dis) {
		// TODO Auto-generated method stub
		
        try
        {
            int id=dis.readInt();
        	if(id==client.mytank.tankId)
        		return;
        	int x=dis.readInt();
        	int y=dis.readInt();
        	EDirections dir= EDirections.values()[dis.readInt()];
        	EDirections ptdir= EDirections.values()[dis.readInt()];
        	  boolean exist=false;
        	 for(int i=client.nettanks.size()-1;i>=0;i--)
             {
             	if(client.nettanks.get(i).tankId==id)
             		{
             		client.nettanks.get(i).x=x;
             		client.nettanks.get(i).y=y;
             		client.nettanks.get(i).setDirection(dir);
             		exist=true;
             		break;
             		}
             }
             
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
      
       
	}


}
