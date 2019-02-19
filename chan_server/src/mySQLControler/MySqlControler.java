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
    static String PASS = "137894553";
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
//    	res = sql.executeQuery("select id,isOnline from user where id=1");
//		if(res.next())
//			res.updateString("name","666");
//		res.updateRow();
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
//    	System.out.println("1");
//        Connection conn = null;
//        Statement stmt = null;
//        try{
//        	System.out.println("1");
//            // ע�� JDBC ����
//            Class.forName("com.mysql.jdbc.Driver");
//        
//            // ������
//            System.out.println("�������ݿ�...");
//            conn = DriverManager.getConnection(DB_URL,USER,PASS);
//        
//            // ִ�в�ѯ
//            System.out.println(" ʵ����Statement��...");
//            stmt = conn.createStatement();
//            String sql;
//            sql = "SELECT id, name, url FROM websites";
//            ResultSet rs = stmt.executeQuery(sql);
//        
//            // չ����������ݿ�
//            while(rs.next()){
//                // ͨ���ֶμ���
//                int id  = rs.getInt("id");
//                String name = rs.getString("name");
//                String url = rs.getString("url");
//    
//                // �������
//                System.out.print("ID: " + id);
//                System.out.print(", վ������: " + name);
//                System.out.print(", վ�� URL: " + url);
//                System.out.print("\n");
//            }
//            // ��ɺ�ر�
//            rs.close();
//            stmt.close();
//            conn.close();
//        }catch(SQLException se){
//            // ���� JDBC ����
//            se.printStackTrace();
//        }catch(Exception e){
//            // ���� Class.forName ����
//            e.printStackTrace();
//        }finally{
//            // �ر���Դ
//            try{
//                if(stmt!=null) stmt.close();
//            }catch(SQLException se2){
//            }// ʲô������
//            try{
//                if(conn!=null) conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }
//        }
//        System.out.println("Goodbye!");
    }
}

