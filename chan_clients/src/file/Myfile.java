package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import socket.Mysocket;
import userInfo.User;

public class Myfile {
	//private chat content
	public static void contentIn(String withId,String msg) {
		String filename = "d:/" + User.myId + "and" + withId + ".txt";
		File file = new File(filename);
		try {
			if(!file.exists())
				file.createNewFile();
			FileWriter fin = new FileWriter(file,true);
			fin.write(msg);			
			fin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//chat room content
	public static void contentIn(String msg) {
		String filename = "d:/" + User.myId + "and666" + ".txt";
		File file = new File(filename);
		try {
			if(!file.exists())
				file.createNewFile();
			System.out.println(msg);
			FileWriter fin = new FileWriter(file,true);
			fin.write(msg);			
			fin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void contentOut(String withId) {
		String filename = "d:/" + User.myId + "and" + withId + ".txt";
		System.out.println(filename + "out");
		File file = new File(filename);
		if(!file.exists()) {
			return;
		}
		try {
			FileReader fileReader= new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			String tmp = null;
			while((tmp = reader.readLine()) != null) {
				System.out.print(tmp+"\n");
				User.getUser(Integer.parseInt(withId)).chatRoom.area.append(tmp+"\n");
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void filein(int id,File file) {
		Mysocket.writer.println(10); //protocol 10
		Mysocket.writer.println(User.myId);
		Mysocket.writer.println(id);
		Mysocket.writer.println(file.getName());
		Mysocket.writer.println(file.length());
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			long length = file.length();
			while(length-- > 0) {
				Mysocket.writer.write(fin.read());
			}
			Mysocket.writer.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void fileout(String filename,long length) {
		File file = new File("d:/youchat/" + filename);
		System.out.println(length);
		try {
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			while(length-- > 0 ) {
				System.out.print(length);
				out.write(Mysocket.reader.read());
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
