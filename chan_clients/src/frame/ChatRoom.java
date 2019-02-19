package frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import file.Myfile;
import socket.Mysocket;
import userInfo.User;

public class ChatRoom extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextArea area = new JTextArea();//chat content
	JTextField tf  = new JTextField();//msg
	Container cc;
	public ChatRoom(User user) {
		super(user.name);
		area.setEditable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		cc = this.getContentPane();
		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new BevelBorder(BevelBorder.RAISED));
		getContentPane().add(scrollPane,BorderLayout.CENTER);
		scrollPane.setViewportView(area);
		cc.add(tf, "South");
		tf.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				area.append(User.myName + ": " + tf.getText() + "\n");
				Myfile.contentIn(String.valueOf(user.id),User.myName + ": " + tf.getText() + "\n");
				if(user.id == 666)
					sendGroupMsgListener(user.id,tf.getText(),1);
				else {
					sendMsgListener(user.id,tf.getText());
				}
			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
			}
		});
		
		setSize(400,200);
		setLocation(500, 250);
	}
	void sendMsgListener(int id,String msg) {
		Mysocket.writer.println(1); //protocol private chat
		Mysocket.writer.println(User.myId);
		Mysocket.writer.println(id);
		Mysocket.writer.println(msg);
//		area.setSelectionEnd(200);
		tf.setText("");
	}
	void sendGroupMsgListener(int id,String msg,int i) {
		Mysocket.writer.println(2); //protocol group chat
		Mysocket.writer.println(User.myId);
		Mysocket.writer.println(msg);
//		area.setSelectionEnd(200);
		tf.setText("");
	}
	public static void main(String[] args) {
		new ChatRoom(new User("asd")).setVisible(true);
	}
}

