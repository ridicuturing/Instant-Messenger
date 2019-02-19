package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import file.Myfile;
import socket.Mysocket;
import userInfo.User;

public class FriendsFrame extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	User me = new User();
	static int frameHeight = 600;
	static int frameWedth = 250;
	private static int x = 0,y = 0;
	private static boolean isDraging = false;
	static JPanel contentPane = null;
	public static JFrame f = new JFrame(); 
	static JScrollPane friendsListSrollPane = null;
	static JPanel pane = null;
	public FriendsFrame(int id) throws IOException {
		me.id = id;
		//protocol
		Mysocket.writer.println("4");
		Mysocket.writer.println(String.valueOf(id));
		me.name = Mysocket.reader.readLine();
		f.setUndecorated(true);
		
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(frameHeight,frameWedth));
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isDraging = true;
				x = e.getX();
				y = e.getY();
			}
			public void mouseReleased(MouseEvent e) {
				isDraging = false;
			}
		});
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDraging) {
					int left = f.getLocation().x;
					int top = f.getLocation().y;
					f.setLocation(left + e.getX() - x, top + e.getY() - y);
				}
			}
		});
		contentPane.setLayout(null);
		f.add(contentPane);
		
		// button
		ExitButton eb = new ExitButton(1);
		eb.setBounds(frameWedth- 4 - 40, 0, 40, 30);
		contentPane.add(eb);
		MinimizeButton mb = new MinimizeButton(f);
		mb.setBounds(frameWedth- 4 - 80, 0, 40, 30);
		contentPane.add(mb);
		AddButton add = new AddButton();
		add.setBounds(frameWedth-40, 50, 40, 30);
		contentPane.add(add);
		
		//id
		JLabel idLabel = new JLabel("id:" + id);
		idLabel.setForeground(Color.WHITE);
		idLabel.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		idLabel.setBounds(10,10,frameWedth/2,27);
		contentPane.add(idLabel);
		
		//name
		JLabel nameLabel = new JLabel(me.name);
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 35));
		nameLabel.setBounds(10,40,frameWedth/2,40);
		contentPane.add(nameLabel);
		
		//add friend
		
		
		//me
//		JPanel JPme = new JPanel(new GridLayout(0,1));
//		JPme.add(new JButton(me.name));
//		add(JPme);
		getFriendsList();//protocol
		pane = JPfriendList();
		friendsListSrollPane =  new JScrollPane(pane);
		friendsListSrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); 
		friendsListSrollPane.setBounds(10,100,230,480);
		friendsListSrollPane.setBorder(BorderFactory.createEmptyBorder(1,0,0,0));
		contentPane.add(friendsListSrollPane);
		f.setSize(frameWedth, frameHeight);
		
		f.setLocationRelativeTo(null);
		f.setAlwaysOnTop(true);
		f.setVisible(true);
	}
	
//	static JList<Object> friendsList = null;
	public static void refresh() throws NumberFormatException, IOException {
		contentPane.remove(friendsListSrollPane);
		friendsListSrollPane =  new JScrollPane(JPfriendList());
		friendsListSrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); 
		friendsListSrollPane.setBounds(10,100,230,480);
//		friendsListSrollPane.repaint();
		friendsListSrollPane.setVisible(true);
		contentPane.add(friendsListSrollPane);
		

		
	}
	public static JPanel JPfriendList() throws NumberFormatException, IOException {
		JPanel pane = new JPanel();
		pane.setBackground(Color.DARK_GRAY);
		pane.setLayout(new GridLayout(50,1));
		for(User user:User.friends) {
			pane.add(user.getLabel(user.getIsOnline()));
			Myfile.contentOut(String.valueOf(user.id));
		}
		pane.repaint(10);
//		js.getVerticalScrollBar().setUI(new ScrollBarUI()); 
//		js.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);// 不显示水平滚动条；
//		js.addMouseListener(new MouseAdapter() {
//			public void mousePressed(MouseEvent e) {
//				isDraging = true;
//				x = e.getX();
//				y = e.getY();
//			}
//			public void mouseReleased(MouseEvent e) {
//				isDraging = false;
//			}
//		});
//		js.addMouseMotionListener(new MouseMotionAdapter() {
//			public void mouseDragged(MouseEvent e) {
//				if (isDraging) {
//					int left = f.getLocation().x;
//					int top = f.getLocation().y;
//					f.setLocation(left + e.getX() - x, top + e.getY() - y);
//				}
//			}
//		});
		return pane;
	}
	void getFriendsList() throws NumberFormatException, IOException {
		int id = 0;
		String strId = null;
		Mysocket.writer.println("3");
		Mysocket.writer.println(String.valueOf(me.id));
		while(true) {
			strId =Mysocket.reader.readLine();
			if(strId.equals("over"))
				break;
			id = Integer.parseInt(strId);
			String name = Mysocket.reader.readLine();
			
			String isOnline = Mysocket.reader.readLine();
			User user = new User(id,name,isOnline.equals("true"));
			User.friends.add(user);
		}
	}
	public void run() {
		try {
			refresh();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
		new Mysocket();
		new FriendsFrame(2);
	}
	
}