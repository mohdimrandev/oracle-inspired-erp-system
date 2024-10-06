package erp.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
 private static final String DB_URL = "jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:6543/postgres";
 private static final String DB_USER = "postgres.yqnthxdixawyqgoscrbh";
 private static final String DB_PASSWORD = "yyj4Vzkl3ZWSjOgN";

 public static Connection getConnection() throws SQLException {
     return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
 } 
}
