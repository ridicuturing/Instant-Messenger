package protocolToolForClient;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import file.Myfile;
import frame.FriendsFrame;
import socket.Mysocket;
import userInfo.User;

public class ProtocolTool {
	/*
	 * 1:私聊	private Chat
	 * 2:群聊	groupChat
	 * 3.有人上线 someone Online
	 */
	public ProtocolTool(){
	}
	public void analyzer() throws IOException {
		String str = Mysocket.reader.readLine();
		System.out.println("ask for: " + str);
		switch(str) {
		
		case"-1":System.exit(0);
					break;
		case "1":privateChat(); //private Chat
					break;
		case "2":groupChat();
					break;
		case "3":newClienOnline();
					break;
		case "4":someoneOffline();
					break;
		case "5":makeNewFriend();
					break;
		case "6":idIsNotExist();
					break;
		case "7":delFriend();
					break;
		case "8":fileTransfer();
					break;			
		}
	}
	void privateChat() {
		int fromId;
		try {
			fromId = Integer.parseInt(Mysocket.reader.readLine());
			String msg = Mysocket.reader.readLine();		//Second read msg
			if(User.getUser(fromId) == null)
				return;
			User.getUser(fromId).chatRoom.area.append(User.getUser(fromId).name + ": " + msg + "\n");
			User.getUser(fromId).chatRoom.setVisible(true);
			Myfile.contentIn(String.valueOf(fromId),User.getUser(fromId).name + ": " + msg + "\n");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//First read from id
	}
	void groupChat() throws IOException {
		String msg = Mysocket.reader.readLine();//First read msg
		
		User.friends.get(0).chatRoom.area.append(msg + "\n");
		User.friends.get(0).chatRoom.setVisible(true);
		Myfile.contentIn(msg + "\n");
	}
	void newClienOnline() throws IOException {
		int id = Integer.parseInt(Mysocket.reader.readLine());
		for(User user:User.friends) {
			if(user.id == id)
				user.setIsOnline(true);
		}
		FriendsFrame.refresh();
	}
	void someoneOffline() throws NumberFormatException, IOException {
		int id = Integer.parseInt(Mysocket.reader.readLine());
		for(User user:User.friends) {
			if(user.id == id)
				user.setIsOnline(false);
		}
//		FriendsFrame.refresh();
		new Thread(new Runnable() {
			public void run() {
				try {
					FriendsFrame.refresh();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	void makeNewFriend() {
		String name = null;
		String tmp  = null;
		String id = null;
		try {
			id = Mysocket.reader.readLine(); 	//first reader
			name = Mysocket.reader.readLine();  //second reader
			tmp = Mysocket.reader.readLine();	//third reader
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean isOnline = "true".equals(tmp);
		User.friends.add(new User(Integer.parseInt(id),name,isOnline));
		new Thread(new Runnable() {
			public void run() {
				try {
					FriendsFrame.refresh();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	void delFriend() throws IOException {
		String id = Mysocket.reader.readLine();
		User tmp = null;
		for(User user:User.friends) {
			if(Integer.parseInt(id) == user.id) {
				tmp = user;
			}
		}
		User.friends.remove(tmp);
		new Thread(new Runnable() {
			public void run() {
				try {
					FriendsFrame.refresh();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	void idIsNotExist() {
		JFrame f = new JFrame();
		f.setSize(200, 150);
		JLabel label = new JLabel("   the id is not exist");
		label.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 19));
		label.setBounds(0,0,200,200);
		f.add(label);
		f.setDefaultCloseOperation(2);
		f.setAlwaysOnTop(true);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		
	}
	static void fileTransfer() throws IOException {
		String id = Mysocket.reader.readLine();
		String filename = Mysocket.reader.readLine();
		long length = Long.parseLong(Mysocket.reader.readLine());
		
		JFrame j = new JFrame();
		JLabel l1 = new JLabel(" id:" + id + " send you a file");
		JLabel l2 = new JLabel(" D:\\youchat\\" + filename);
		JLabel l3 = new JLabel("    sending");
		l1.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 22));
		l2.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 22));
		l3.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 22));
		j.setLayout(new GridLayout(3, 1));
		j.setDefaultCloseOperation(2);
		j.add(l1);
		j.add(l2);
		j.add(l3);
		j.pack();
		j.setSize(322, 222);
		j.setLocationRelativeTo(null);
		j.setVisible(true);
		
		Myfile.fileout(filename,length);
		l3.setText("        success");
		
	}
	public static void main(String[] args) throws Exception {
		fileTransfer();
	}
}
