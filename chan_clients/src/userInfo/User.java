package userInfo;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import file.Myfile;
import frame.ChatRoom;
import socket.Mysocket;

public class User   {
	public String name = null; // Initial name
	public int id = -1;
	
	
	public static int myId = 0;
	public static String myName = null;
	
	boolean isOnline = false;
	String gifString = "1";
	public static List<User> friends = new ArrayList<>();
	
	public ChatRoom chatRoom = null;
	private JLabel label = null;

	public User(String string) {
		chatRoom = new ChatRoom(this);
//		setLabel();
	}
	public User() {
		chatRoom = new ChatRoom(this);
//		setLabel();
	}
	public User(User user) {
		chatRoom = new ChatRoom(user);
//		setLabel();
	}
	public User(int id,String name,boolean isOnline) {
		this.name = name;
		this.isOnline = isOnline;
		this.id = id;
		chatRoom = new ChatRoom(this);
//		setLabel();
	}
	public static User getUser(int id) {
		for(User user:User.friends) {
			if(id == user.id)
				return user;
		}
		return null;
	}
	public JLabel getLabel(boolean isOnline2) {
		label = new JLabel("<html>" + name +"<br>id:" + id + "</html>");
		label.setOpaque(true);   //不透明
		label.setForeground(Color.WHITE);
		label.setBackground(Color.darkGray);
		if(id == 666)
			gifString = "all";
		else
			gifString = String.valueOf(id%10+1);
		ImageIcon img = new ImageIcon("./gif/" + gifString + ".gif");//创建图片对象
		label.setIcon(img);
		if(!gifString.equals("all"))
			label.setEnabled(isOnline2);
		label.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				label.setBackground(Color.WHITE);
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		//right click menu
		JPopupMenu menu = new JPopupMenu();
		JMenuItem mDel = new JMenuItem("delete"); 
		mDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mysocket.writer.println(9); //protocol 9
				Mysocket.writer.println(User.myId);//first writer	
				Mysocket.writer.println(User.this.id);//second writer
			}
		});
        menu.add(mDel); 

		JMenuItem mFile = new JMenuItem("file transfer"); 
		mFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int i = fc.showOpenDialog(fc);
				if(i ==JFileChooser.APPROVE_OPTION) {
					File selectedFile = fc.getSelectedFile();
					System.out.println(selectedFile.getPath());
					Myfile.filein(User.this.id,selectedFile);
				}
			}
		});
		menu.add(mFile);
		label.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				label.setBackground(new Color(192, 192, 192));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				label.setBackground(new Color(220, 220, 220));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				label.setBackground(Color.DARK_GRAY);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				label.setBackground(new Color(192, 192, 192));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getClickCount() == 2)
					chatRoom.setVisible(true);
				if(e.getButton() == MouseEvent.BUTTON3) {
					menu.show(label, e.getX(), e.getY());
				}
			}
			
		});
		return label;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIsOnline(boolean b) {
		isOnline = b;
	}
	public boolean getIsOnline() {
		return isOnline;
	}
	public void addFriend(User user) {
		friends.add(user);
	}
	public void removeFriend(User user) {
		friends.remove(user);
	}
	public void listFriends() {
		for(User user : friends) {
			System.out.println(user.name);
		}
	}
}


