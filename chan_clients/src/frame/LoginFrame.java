package frame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import protocolToolForClient.ProtocolTool;
import socket.Mysocket;
import userInfo.User;


public class LoginFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x = 0,y = 0;
	public int width,height;
	Container con = getContentPane();
	int frameHeight = 100;
	int frameWedth = 250;
	Socket socket = null;
	
	//  I/O
	BufferedReader reader = null;
	PrintWriter writer = null;
	
	//userId frame
	JLabel userId = new JLabel("userId: ");
	JTextField userText = new JTextField();
	//userPassword frame
	JLabel userPassword = new JLabel("password: ");
	JPasswordField passwordText = new JPasswordField("3",7);
	//picture
	JLabel JLIcon = new JLabel("asd");
	private JPanel contentPane;
	private boolean isDraging = false;
	public LoginFrame() {
		super("sign in");
		contentPane = new JPanel();
		setUndecorated(true);
		setBounds(100, 100, 439, 269);
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
					int left = getLocation().x;
					int top = getLocation().y;
					setLocation(left + e.getX() - x, top + e.getY() - y);
				}
			}
		});
		contentPane.setLayout(null);
		add(contentPane);
		
		
		JLabel title = new JLabel("Chat Room");
		title.setForeground(Color.WHITE);
//		title.setBackground(Color.WHITE);
		title.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 50));
		title.setBounds(88, 10, 357, 80);
		contentPane.add(title);
		
		//login button
		MyButton login = new MyButton("login");
		MyButton register = new MyButton("register");
		
		//id label
		userId.setForeground(Color.WHITE);
		userId.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		userId.setBounds(37,90, 126, 27);
		contentPane.add(userId);
		
		userText.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		userText.setBounds(170, 87, 219, 35);
		userText.setBorder(null);
		userText.setColumns(10);
		userText.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {
				} else {
					e.consume(); // 关键，屏蔽掉非法输入
				}
			}
		});
		contentPane.add(userText);
		
		//password label
		userPassword.setForeground(Color.WHITE);
		userPassword.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		userPassword.setBounds(37, 135, 126, 27);
		contentPane.add(userPassword);
//		s
		passwordText = new JPasswordField();
		passwordText.setBounds(170, 132, 219, 35);
		passwordText.setBorder(null);
		passwordText.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 30));
		passwordText.setColumns(10);
		contentPane.add(passwordText);
		
	//	add(panelForPicture);
		login.setBounds(37, 205, 170, 40);
		login.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if("".equals(userText.getText())) {
					noIdInput();
				}else
				submit(userText.getText(),passwordText.getPassword());
			}
		});
		contentPane.add(login);
		
		// button
		ExitButton eb = new ExitButton();
		int windowWeith = this.getWidth();
		eb.setBounds(windowWeith - 4 - 40, 0, 40, 30);
		contentPane.add(eb);
		MinimizeButton mb = new MinimizeButton(this);
		mb.setBounds(windowWeith - 4 - 80, 0, 40, 30);
		contentPane.add(mb);
		
		register.setBounds(225, 205, 170, 40);
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				registerFrame();
			}
		});
		
		
		contentPane.add(register);
		
		passwordText.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if("".equals(userText.getText())) {
					noIdInput();
				}else
				submit(userText.getText(),passwordText.getPassword());
			}
		});
		userText.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if("".equals(userText.getText())) {
					noIdInput();
				}else
				submit(userText.getText(),passwordText.getPassword());
			}
		});
		
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	void submit(String id,char[] s) {//submit id, password and check it 
		String check = null;
		String password = new String(s);
		Mysocket.writer.println(5);
		Mysocket.writer.println(id);
		Mysocket.writer.println(password);
		try {
			check = Mysocket.reader.readLine();
			if(check.equals("true")) {
				loginSuccess(Integer.parseInt(id));
			}else if(check.equals("id does not exist")) {
				idNotExist();
			}else if(check.equals("password error")) {
				passwordError();
			}	
		} catch (IOException e) {
			System.out.println("submit error!");
			e.printStackTrace();
		}
		
	}
	void noIdInput() {
		JDialog j = new JDialog(this,"error");
		JLabel jl = new JLabel("  please input your id");
		j.add(jl);
		j.setSize(100,80);
		j.setLocationRelativeTo(null);
		j.setVisible(true);
	}
	void loginSuccess(int id) throws NumberFormatException, IOException{
		User.myName = Mysocket.reader.readLine();
		User.myId = id;
		System.out.println("id:" + id);
		new FriendsFrame(id);
		dispose();
		
		new Thread(new Runnable() {
			public void run() {
				try {
					while(true)
						new ProtocolTool().analyzer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	void idNotExist() {
		JDialog j = new JDialog(this,"error");
		JLabel jl = new JLabel("  id does not exist");
		j.add(jl);
		j.setSize(100,80);
		j.setLocationRelativeTo(null);
		j.setVisible(true);
	}
	void passwordError() {
		JDialog j = new JDialog(this,"error");
		JLabel jl = new JLabel("  password error");
		j.add(jl);
		j.setSize(100,80);
		j.setLocationRelativeTo(null);
		j.setVisible(true);
	}
	void register(String name,String password) throws IOException {
		
		String id = null;
		Mysocket.writer.println(6); //protocol 6
		Mysocket.writer.println(name);
		Mysocket.writer.println(password);
		id = Mysocket.reader.readLine();
		
			
		
		//pop up to display new id
		JFrame jd = new JFrame();
		jd.setUndecorated(true);
		jd.setSize(300, 145);
		contentPane = new JPanel();
		jd.add(contentPane);
		contentPane.setLayout(null);
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
					int left = jd.getLocation().x;
					int top = jd.getLocation().y;
					jd.setLocation(left + e.getX() - x, top + e.getY() - y);
				}
			}
		});
		
		//id
		JLabel title = new JLabel("id: " + id);
		if(id.equals("-1"))
			title.setText("ERROR!");
		title.setForeground(Color.WHITE);
//				title.setBackground(Color.WHITE);
		title.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 50));
		title.setBounds(20,10,357, 80);
		contentPane.add(title);
		
		// button
		ExitButton eb = new ExitButton();
		int windowWeith = jd.getWidth();
		int windowHeight = jd.getHeight();
		eb.setBounds(windowWeith - 4 - 40, 0, 40, 30);
		contentPane.add(eb);
		
		MinimizeButton mb = new MinimizeButton(jd);
		mb.setBounds(windowWeith - 4 - 80, 0, 40, 30);
		contentPane.add(mb);
		
		MyButton confirm = new MyButton("confirm");
		confirm.setBounds(windowWeith/2-58,windowHeight-50,100,30);
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
				new LoginFrame();
		}
		});
		contentPane.add(confirm);
		
		jd.setLocationRelativeTo(null);
		jd.setVisible(true);
	}
	
	JPasswordField passwordField = null;
	void registerFrame() {		
		JFrame j = new JFrame("rigister");
		j.setUndecorated(true);
		j.setSize(439, 236);
		contentPane = new JPanel();
		j.add(contentPane);
		contentPane.setLayout(null);
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
					int left = j.getLocation().x;
					int top = j.getLocation().y;
					j.setLocation(left + e.getX() - x, top + e.getY() - y);
				}
			}
		});
		//title
		JLabel title = new JLabel("Regiser");
		title.setForeground(Color.WHITE);
//		title.setBackground(Color.WHITE);
		title.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 50));
		title.setBounds(133,0,357, 80);
		contentPane.add(title);
		
		
		//name
		JLabel nameLabel = new JLabel("name:");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		nameLabel.setBounds(10,80, 156, 35);
		contentPane.add(nameLabel);
		
		JTextField nameField = new JTextField();
		nameField.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		nameField.setBounds(166, 80, 219, 35);
		nameField.setBorder(null);
		nameField.setColumns(10);
		contentPane.add(nameField);
		//password
		JLabel passwordLabel = new JLabel("password:");
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		passwordLabel.setBounds(10,128, 156, 35);
		contentPane.add(passwordLabel);
		
		
		passwordField = new JPasswordField();
		passwordField.setBounds(166, 128, 219, 35);
		passwordField.setBorder(null);
		passwordField.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 30));
		passwordField.setColumns(10);
		contentPane.add(passwordField);
		

		
		// button
		ExitButton eb = new ExitButton();
		int windowWeith = j.getWidth();
		eb.setBounds(windowWeith - 4 - 40, 0, 40, 30);
		contentPane.add(eb);
		
		MinimizeButton mb = new MinimizeButton(j);
		mb.setBounds(windowWeith - 4 - 80, 0, 40, 30);
		contentPane.add(mb);
		
		MyButton confirm = new MyButton("confirm");
		confirm.setBounds(166,188,100,30);
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					j.dispose();
					register(nameField.getText(),String.valueOf(passwordField.getPassword()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(confirm);
		
		j.setLocationRelativeTo(null);
		j.setVisible(true);
	}
	
}