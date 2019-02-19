package protocolTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

import server.Server;
import userMySqlControler.IdForIO;
import userMySqlControler.User;

public class ProtocolTool {
	/*
	 * 1:私聊（id,msg）
	 * 2:群聊（msg）
	 * 3:发送朋友列表
	 * 4:get id ;return name;
	 */
	Socket socket = null;
	BufferedReader reader = null;
	PrintWriter writer = null;
	public ProtocolTool(Socket socket) throws IOException {
		this.socket = socket;
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintWriter(socket.getOutputStream(),true);
	}
	//protocol analyzer
	public void analyzer() throws NumberFormatException, IOException, SQLException {
		String str = reader.readLine();
		Server.println(" ask for " + str);
		switch(str) {
		case "1": privateChat(); //private Chat
					break;
		case "2": groupChat();
					break;
		case "3":sendFriendsList();
					break;
		case "4":getName();
					break;
		case "5":login();
					break;
		case "6":register();
					break;
		case "7":someoneOffline();
					break;
		case "8":addFriend();
					break;		
		case "9":delFriend();
					break;	
		case"10":fileTransfer();
					break;
		}
	}
	void privateChat() throws NumberFormatException, IOException {
		int fromId = Integer.parseInt(reader.readLine());//First read from id
		int toId = Integer.parseInt(reader.readLine());//Second read to id
		String msg = reader.readLine();		//Third read msg

		if(!IdForIO.user.containsKey(String.valueOf(toId)))
			return;
//		//send to fromUser
//		writer.println("1"); //protocol order
//		writer.println(fromId);
//		writer.println(msg); //Second write msg
		//send to toUser
		PrintWriter writerNew = IdForIO.user.get(String.valueOf(toId)); //First write id
		writerNew.println("1"); //protocol order
		writerNew.println(fromId);
		writerNew.println(msg); //Second write msg
	}
	void groupChat() throws IOException {
		String id = reader.readLine();
		String msg = reader.readLine();
		for(PrintWriter w:IdForIO.user.values()) {
			w.println(2); //protocol
			w.println(new User(id).getName() + ": " + msg);
		}
	}
	void sendFriendsList() throws IOException {
		String myId = reader.readLine();   //First read
		String tmp = new User(myId).getFriends();
		if(tmp == null) {
			writer.println("over");
			return;
		}
		String friends[] = tmp.split("\\,");
		for(String id:friends ) {
			if("".equals(id))
				continue;
			writer.println(id); // First write id
			writer.println(new User(id).getName()); //Second write name
			writer.println(IdForIO.user.containsKey(id));
		}
		writer.println("over");
	}
	void getName() throws NumberFormatException, IOException {
		User user = new User(Integer.parseInt( reader.readLine() ));
		writer.println(user.getName());
		
	}
	public void newClienOnline(String id) throws IOException {
		for(PrintWriter w:IdForIO.user.values()) {
			w.println("3"); //protocol
			w.println(id);
		}
		
	}
	public void someoneOffline() throws IOException {
		String id = reader.readLine();
		for(String s:IdForIO.user.keySet()) {
			PrintWriter w = IdForIO.user.get(s);
			if(s.equals(id))
				continue;
			w.println("4"); //protocol 4
			w.println(id);
		}
		IdForIO.user.remove(id);
		Server.print("id:" + id + "offline\n");
		Server.addOnlineNumber(-1);
	}
	boolean login() throws SQLException {
		User me = null;
		String id = null;
		String password = null;
		try {
			id = reader.readLine();
			System.out.println("id:" + id + " try to login	");
			password = reader.readLine();
			me = new User(Integer.parseInt(id));
			if(me.isExist()) {
				if(me.checkPassword(password)) {
					if(IdForIO.user.containsKey(id))
						IdForIO.user.get(id).println("-1");
					Server.addOnlineNumber();
					newClienOnline(id);
					IdForIO.user.remove(id);
					IdForIO.user.put(String.valueOf(id),writer);
					Server.print("id:" + id + " connect\n");
					writer.println(true);
					writer.println(new User(Integer.parseInt(id)).getName());
					return true;
				}
				else
					writer.println("password error");
			}else 
				writer.println("id does not exist");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	void register() throws IOException {
		String name = reader.readLine();
		String password = reader.readLine();
		writer.println(new User(name,password).id);
		
		
	}
	void addFriend() throws IOException {
		String fromId = reader.readLine();	//first reader
		String toId = reader.readLine();  	//second reader
		if(!(new User(toId).isExist())) {
				writer.println(6);//protocol 1
				return;
		}
		User fromUser = new User(fromId);
		User toUser = new User(toId);
		String tmp[] = null;
		//if toUser is not fromUser's friend ,then add it
		tmp = fromUser.getFriends().split("\\,");
		boolean flag = true;
		for(String str:tmp) {
			if(str.equals(toId)) {
				flag = false;
			}
		}
		if(flag) {
			fromUser.addFriend(toId);
			if(IdForIO.user.containsKey(fromId)) {
				writer.println(5);
				writer.println(toId);//first writer
				writer.println(new User(toId).getName());//second writer
				writer.println(IdForIO.user.containsKey(toId));	//third writer
			}
		}
		//if toUser is not fromUser'friend ,then add it
		tmp = toUser.getFriends().split("\\,");
		flag = true;
		for(String str:tmp) {
			if(str.equals(fromId)) {
				flag = false;
			}
		}
		if(flag) {
			toUser.addFriend(fromId);
			//if toUser is online
			if(IdForIO.user.containsKey(toId)) {
				IdForIO.user.get(toId).println(5);
				IdForIO.user.get(toId).println(fromId);//first writer
				IdForIO.user.get(toId).println(new User(fromId).getName());//second writer
				IdForIO.user.get(toId).println(IdForIO.user.containsKey(fromId));//third writer
			}
		}
	}
	void delFriend() throws IOException {
		String fromId = reader.readLine();//first reader
		String toId = reader.readLine();//second reader
		System.out.println(fromId + "  " + toId);
		new User(fromId).delFriend(toId);
		new User(toId).delFriend(fromId);
		//delete toUser from fromUser
		Server.println("delete id:"+toId+" id:"+fromId);
		writer.println(7);
		writer.println(toId);
		//delete fromUser from toUser
		if(IdForIO.user.containsKey(toId)) {
			Server.println("delete id:"+fromId+" id:"+toId);
			IdForIO.user.get(toId).println(7);
			IdForIO.user.get(toId).println(fromId);
		}
	}
	void fileTransfer() throws IOException {
		String fromId = reader.readLine();// 1st reader
		String toId = reader.readLine();//2nd reader
		String filename = reader.readLine();//3rd reader
		long length = Long.parseLong(reader.readLine()); //4th reader
		
		PrintWriter writer1 = IdForIO.user.get(toId);
		
		writer1.println(8); //protocol 8
		
		writer1.println(fromId);
		writer1.println(filename);
		writer1.println(length);
		
		System.out.println("start  " + length);
		int i = 0;
		while(length-- > 0) {
			i = reader.read();
			writer1.write(i);
		}
		writer1.flush();
		System.out.println("over");
			
	}
}
