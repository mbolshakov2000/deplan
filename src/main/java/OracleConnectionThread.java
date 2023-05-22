import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class OracleConnectionThread implements Runnable {

    int result = 0;
    String URL;
    String user;
    String password;

    OracleConnectionThread(String URL, String user, String password){
        this.URL = URL;
        this.user = user;
        this.password = password;
    }

    public void run(){
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection c = DriverManager.getConnection(URL,user,password);
            result = 1;
        } catch (SQLException ex) {
            result = 2;
        }
    }

    public int getResult(){
        return result;
    }
}