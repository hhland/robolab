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
import com.uhealin.tankz.tank.NetTank;



public class TankNewMsg implements INetMessage {

	
	int Type=INetMessage.TANK_NEW_MSG;
	Client client;
	Mytank mytank;
	
	public TankNewMsg(Client client)
	{
		this.client=client;
	}
	
	public TankNewMsg(Mytank mytank)
	{
		this.mytank=mytank;
	}
	
	public void sendMsg(DatagramSocket ds, String IP, int udpPort) {
		// TODO Auto-generated method stub
         ByteArrayOutputStream baos=new ByteArrayOutputStream();
         DataOutputStream dos=new DataOutputStream(baos);
         try {
            dos.writeInt(Type);
            dos.writeInt(mytank.tankId);
			dos.writeInt(mytank.x);
			dos.writeInt(mytank.y);
			dos.writeInt(mytank.dir.ordinal());
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
        	  boolean exist=false;
        	 for(int i=client.nettanks.size()-1;i>=0;i--)
             {
             	if(client.nettanks.get(i).tankId==id)
             		exist=true;
             }
        	 if(!exist)
        	 {
        		 client.nethelper.send(new TankNewMsg(client.mytank));
        		 NetTank nt=new NetTank(x,y,client,true);
        		 nt.tankId=id;
        		 client.nettanks.add(nt);
        		 System.out.println("nettankadd");
        	 }
             
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
      
       
	}

}
