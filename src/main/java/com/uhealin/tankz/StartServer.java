package com.uhealin.tankz;

import com.uhealin.tankz.net.Server;

public class StartServer {

	public static void main(String[] args) {
		Server s = new Server();
		s.loadForm();
		s.start();
	}

}
