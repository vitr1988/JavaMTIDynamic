package ru.mti.edu.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.driver.OracleDriver;

public class Db {

	public static void main(String[] args) throws FileNotFoundException, SQLException {
		try (Connection conn = getConnection()){
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stat.executeUpdate("delete from dept where deptno = 1");
			stat.executeUpdate("insert into dept(deptno, dname, loc) values (1, 'IT Department', 'Moscow')");
			ResultSet rs = stat.executeQuery("select * from dept");
			rs.beforeFirst();
			File file = new File("result.txt");
			PrintWriter pw = new PrintWriter(file);
			while(rs.next()){ 
				String deptno = rs.getString("deptno");
				String dname = rs.getString("dname"),
						loc = rs.getString(3); // rs.getString("loc")
				pw.println(deptno + " | " + dname + " | " + loc);
			}
			pw.close();			
		}
	}
	
	public static Connection getConnection(){
		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
			// �� �� ����� �������� �������� ����
			OracleDriver driver = new OracleDriver();
			DriverManager.registerDriver(driver);
			
			Properties prop = new Properties();
			prop.load(new FileInputStream("src/ru/mti/edu/jdbc/dbinfo.properties"));
			return DriverManager.getConnection(prop.getProperty("db.url"), prop.getProperty("db.login"), prop.getProperty("db.password"));
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}

}
