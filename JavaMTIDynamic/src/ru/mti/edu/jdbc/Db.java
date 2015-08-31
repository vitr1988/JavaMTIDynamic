package ru.mti.edu.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.driver.OracleDriver;

public class Db {

	public static void main(String[] args) throws FileNotFoundException, SQLException {
		try (Connection conn = getConnection()){
			DatabaseMetaData dbmd = conn.getMetaData();
			
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//			stat.executeUpdate("delete from dept where deptno = 1");
			PreparedStatement prepStat = conn.prepareStatement("delete from dept where deptno = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//			prepStat.setString(1, "1 or 1 = 1");
			prepStat.setInt(1, 1);
			// select * from dept where deptno = 1 or 1 = 1 - SQL Injection
			prepStat.execute();
//			System.out.println(prepStat.getUpdateCount());
			stat.executeUpdate("insert into dept(deptno, dname, loc) values (1, 'IT Department', 'Moscow')");
			ResultSet rs = stat.executeQuery("select * from dept");
			ResultSetMetaData metaData = rs.getMetaData();
			
			rs.beforeFirst();
			File file = new File("result.txt");
			PrintWriter pw = new PrintWriter(file);
			while(rs.next()){ 
				int deptno = rs.getInt("deptno");
				String dname = rs.getString("dname"),
						loc = rs.getString(3); // rs.getString("loc")
				if (deptno == 1){
					rs.updateString("loc", "Saint-Peterburg");
					rs.updateRow();
				}
				pw.println(deptno + " | " + dname + " | " + rs.getString(3));
			}
			
//			CallableStatement call = conn.prepareCall("{call raise_application_error(?, ?)}");
//			call.setInt(1, -20002);
//			call.setString(2, "Проверка вызова хранимой процедурыvgfgsf!");
//			call.execute();
			
			pw.close();
			stat.close();
			prepStat.close();
		}
	}
	
	public static Connection getConnection() {
		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
			// �� �� ����� �������� �������� ����
			OracleDriver driver = new OracleDriver();
			DriverManager.registerDriver(driver);
			
//			DataSource dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/orcl");
//			dataSource.getConnection();
			
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
