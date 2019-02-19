package main;

import mySQLControler.MySqlControler;
import server.Server;
//port:6666
public class Main {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception{

		MySqlControler.start();
		Server s = new Server();
	}
}
