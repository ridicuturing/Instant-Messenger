package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Mysocket {
	static public Socket socket = null;
	static public PrintWriter writer = null;
	static public BufferedReader reader = null;
	public Mysocket() {
		connect();
	}
	static Socket getMySocket() {
		if(socket == null)
			System.out.println("connect error!");
		return socket;
	}
	void connect() {
		try {
			socket = new Socket("127.0.0.1",6666);
			writer = new PrintWriter(socket.getOutputStream(),true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch(Exception e) {
			System.out.println("fail to connect server!");
		}
	}
}
