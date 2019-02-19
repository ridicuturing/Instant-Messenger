package mySQLControler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class MySqlControler {
 
    // JDBC 
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/qquser";
    
    static String USER = "root";
    static String PASS = "123";
    public static Connection con = null;
    public static Statement stmt = null;
    public static void start() {
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		System.out.println("succeed to connect mySql!");
    		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/qquser" , USER , PASS);
    		System.out.println("succeed to connect database!");
    	}catch(ClassNotFoundException e) {
    		System.out.println("maybe you forget importing the mysql jar");
    		e.printStackTrace();
    	}catch(SQLException e) {
    		System.out.println("!!!how about the user table");
    		e.printStackTrace();
    	}
    	try {
    		stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,  
                ResultSet.CONCUR_UPDATABLE);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		System.out.println("unconnected database!");
		e.printStackTrace();
	}
    }

    public static void main(String[] args) throws SQLException {
    	Statement sql = stmt;
    	ResultSet res;
    	try {
		res = sql.executeQuery("select * from user where id=1");
		while(res.next()) {
			res.updateString("name","666");
			res.updateRow();
			System.out.println(res.getString(3) + " " + res.getString("name") + " ");
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
}

