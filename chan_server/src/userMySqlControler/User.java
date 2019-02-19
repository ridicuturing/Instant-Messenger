package userMySqlControler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mySQLControler.MySqlControler;

public class User {

	Statement stmt = null;
	ResultSet res = null;
	Connection con = null;
//	PreparedStatement sql = null;
	public int id = -1;
	
	public User(int id){
		con = MySqlControler.con;
		stmt = MySqlControler.stmt;
		this.id = id;
	}
	public User(String id){
		con = MySqlControler.con;
		stmt = MySqlControler.stmt;
		this.id = Integer.parseInt(id);
	}
	public User(String name,String password) {
		con = MySqlControler.con;
		stmt = MySqlControler.stmt;
		try {
			System.out.println("register  name:" + name + " ,password:" + password);
			PreparedStatement prestmt = con.prepareStatement("insert into user (name,password) value(?,?)");
			prestmt.setString(1,name);
			prestmt.setString(2,password);	
			prestmt.executeUpdate();
			res = stmt.executeQuery("select * from user where name='" + name + "'");
			if(res.next())
				id = res.getInt("id");
			System.out.println("new id: " + id);
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public int userRegister(String name,String password) {
		int id = -1;
		return id;
	}
	public boolean isExist() {
		boolean tmp = false;
		try {
			res = stmt.executeQuery("select * from user where id=" + id);
			if(res.next())
				tmp = true;
			res.close();
		} catch (SQLException e) {
			System.out.println("fail to open user name!");
		}
		return tmp;
	}
	public boolean checkPassword(String password) {
		String tmp = null;
		try {
			res = stmt.executeQuery("select * from user where id=" + id);
			if(res.next())
				tmp = res.getString("password");
			res.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(password.equals(tmp))
			return true;
		else 
			return false;
	}
	public String getName() {
		String tmp = null;
		try {
			res = stmt.executeQuery("select * from user where id=" + id);
			res.next();
			tmp = res.getString("name");
			res.close();
		} catch (SQLException e) {
			System.out.println("fail to open user name!");
		}
		return tmp;
	}
	public int getId() {
		int tmp = -1;
		try {
			res = stmt.executeQuery("select * from user where id=" + id);
			res.next();
			tmp = res.getInt("id");
			res.close();
		} catch (SQLException e) {System.out.println("The id:" + id + " dose not exist!");
		}
		return tmp;
	}
	public String getPassword() {
		String tmp = null;
		try {
			res = stmt.executeQuery("select * from user where id=" + id);
			res.next();
			tmp = res.getString("password");
			res.close();
		} catch (SQLException e) {
			System.out.println("The id:" + id + " dose not exist!");
		}
		return tmp;
	}
 	public int getAge() {
		int tmp = -1;
		try {
			res = stmt.executeQuery("select * from user where id=" + id);
			res.next();
			tmp = res.getInt("age");
			res.close();
		} catch (SQLException e) {
			System.out.println("The id:" + id + " dose not exist!");
		}
		return tmp;
	}
	public String getSex() {
		String tmp = null;
		try {
			res = stmt.executeQuery("select * from user where id=" + id);
			res.next();
			tmp = res.getString("sex");
			res.close();
		} catch (SQLException e) {
			System.out.println("The id:" + id + " dose not exist!");
		}
		return tmp;
	}
	public String getFriends() {
		String tmp = null;
		try {
			res = stmt.executeQuery("select * from user where id=" + id);
			if(res.next())
				tmp = res.getString("friends");
			res.close();
		} catch (SQLException e) {
			System.out.println("The id:" + id + " dose not exist!");
		}
		return tmp;
	}
	public void addFriend(String id) {
		try {
			PreparedStatement prestmt = con.prepareStatement("update user set friends=concat(friends,?) where id=?");
			prestmt.setString(1,"," + id);
			prestmt.setInt(2,this.id);	
			prestmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void delFriend(String id) {
		String tmp = null;
		try {
			res = stmt.executeQuery("select * from user where id=" + this.id);
			if(res.next())
				tmp = res.getString("friends");
			res.close();
			tmp = tmp.replaceAll("\\,"+id , "");
			PreparedStatement prestmt = con.prepareStatement("update user set friends=? where id=?");
			prestmt.setString(1,tmp);
			prestmt.setInt(2,this.id);	
			prestmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		MySqlControler.start();
		
	}
	
}

