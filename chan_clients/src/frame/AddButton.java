package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import socket.Mysocket;
import userInfo.User;

public class AddButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public AddButton() {
		this.setPreferredSize(new Dimension(40, 30));
		this.setBackground(Color.DARK_GRAY);
		this.setText("+");
		this.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 20));
		this.setFocusPainted(false);// 设置不要焦点（文字的边框）
		this.setBorder(null);
		this.setForeground(Color.WHITE);

		// 设置按下的颜色
		UIManager.put("Button.select", new Color(220, 220, 220));
//		 在Pressed里面写不顶用

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				JFrame frame = new JFrame("add friends");
				JLabel idLabel = new JLabel("friend's id");
				JTextField idField = new JTextField();
				idField.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
						int keyChar = e.getKeyChar();
						if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {
						} else {
							e.consume(); // 关键，屏蔽掉非法输入
						}
					}
				});
				JButton button = new JButton("add");
				frame.setLayout(null);
				frame.setSize(260, 166);
				frame.setDefaultCloseOperation(2);
				frame.setAlwaysOnTop(true);
				
				idLabel.setBounds(80, 16, 300, 16);
				idLabel.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 20));
				frame.add(idLabel);
				
				button.setBounds(75,70,80,33);
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Mysocket.writer.println(8);		//protocol 8
						Mysocket.writer.println(User.myId);  //first writer
						int id = Integer.parseInt(idField.getText());
						Mysocket.writer.println(id);		//second writer
//						frame.dispose();
					}
				});
				frame.add(button);
				
				idField.setBounds(25,36,190,26);
				frame.add(idField);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				setBackground(Color.DARK_GRAY);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				setBackground(new Color(192, 192, 192));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame("add friends");
		JLabel idLabel = new JLabel("friend's id");
		JTextField idField = new JTextField();
		JButton button = new JButton("add");
		frame.setLayout(null);
		frame.setSize(260, 166);
		frame.setDefaultCloseOperation(2);
		
		idLabel.setBounds(80, 16, 300, 16);
		idLabel.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 20));
		frame.add(idLabel);
		
		button.setBounds(75,70,80,33);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mysocket.writer.println(8);		//protocol 8
				Mysocket.writer.println(User.myId);  //first writer
				int id = Integer.parseInt(idField.getText());
				Mysocket.writer.println(id);		//second writer
				String name = null;
				String tmp  = null;
				try {
					name = Mysocket.reader.readLine();  //first reader
					tmp = Mysocket.reader.readLine();	//second reader
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				boolean isOnline = "true".equals(tmp);
				User.friends.add(new User(id,name,isOnline));
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
				}).start();;
			}
		});
		frame.add(button);
		
		idField.setBounds(25,36,190,26);
		frame.add(idField);
		
		frame.setVisible(true);
	}
}