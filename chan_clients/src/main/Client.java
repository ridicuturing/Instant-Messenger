package main;

import java.io.IOException;

import frame.LoginFrame;
import socket.Mysocket;

public class Client {
	public static void main(String[] args) throws IOException {
		new Mysocket();
		new LoginFrame();
	}
}
