package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {

    private static final String URL  = "jdbc:oracle:thin:@//10.40.112.10:1521/dbsys.jz.jec.ac.jp";
    private static final String USER = "jz240121";
    private static final String PASS = "pass";

    public static Connection getConnection() throws Exception {
    	Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
