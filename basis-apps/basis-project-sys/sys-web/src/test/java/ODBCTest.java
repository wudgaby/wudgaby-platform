import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/11/13 10:00
 * @Desc :   
 */
public class ODBCTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        String url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=D:\\key.mdb";
        Connection conn = DriverManager.getConnection(url);
        Statement sta = conn.createStatement();
    }
}
