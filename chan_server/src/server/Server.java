package server;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import protocolTool.ProtocolTool;
import userMySqlControler.User;


public class Server{
	public static boolean isRunning = false;
	ServerSocket ss = null;
	static JTextArea area = new JTextArea();
	static JTextField field = new JTextField(); 
	static int OnlineNumber = 0;
	public Server() {
		frame();
		try {
			ss = new ServerSocket(6666);
			isRunning = true;
			System.out.println("Server start!");
			while(isRunning) {
				Socket s = ss.accept();
				Client client = new Client(s);
				new Thread(client).start();
				
			}
		}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }
	}
	void frame() {
		JFrame jf = new JFrame("server");
//		JScrollPane 
//		jf.setLayout(null);
		area.setEditable(false);
		field.setEditable(false);
		field.setText("Online number: " + OnlineNumber);
		field.setBackground(Color.DARK_GRAY);
		field.setForeground(Color.white);
		field.setSize(200,30);
		area.setBackground(Color.DARK_GRAY);
		area.setForeground(Color.white);
		JScrollPane js = new JScrollPane(area);
		js.setBounds(0,30,200, 370);
		jf.add(field,"North");
		jf.add(js);
		jf.pack();
		jf.setSize(200,400);
//		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setAlwaysOnTop(true);
	}
	public static void addOnlineNumber() {
		OnlineNumber++;
		field.setText("Online number: " + OnlineNumber);
	}
	public static void addOnlineNumber(int s) {
		OnlineNumber += s;
		field.setText("Online number: " + OnlineNumber);
	}
	public static void print(String str) {
		area.append(str);
	}
	public static void println(String str) {
		area.append(str+"\n");
	}
	
}

class Client implements Runnable {
	String id = null;
	boolean isOnline = false;
	Socket socket = null;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	BufferedReader reader = null;
	PrintWriter writer = null;
	
	User me = null;
	public Client(Socket s) {
		socket = s;
		try {
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run() {
		try {
//			while(!check());
			while(true)
				new ProtocolTool(socket).analyzer();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}

