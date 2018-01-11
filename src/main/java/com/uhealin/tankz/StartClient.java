package com.uhealin.tankz;

import com.uhealin.tankz.net.Client;
import com.uhealin.tankz.net.Server;

public class StartClient {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
            
            if(args.length==0){
               new Client().loadForm();
            }
            
            String cmd=args[0];
            
              
            if("server".equals(cmd)){
                Server s = new Server();
		s.loadForm();
		s.start();
            }else if("client".equals(cmd)){
              new Client().loadForm();
            }
        
	}

}
