import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class EventCompetenceFrame extends JFrame {
    EventCompetenceFrame(Event ev) {
        setSize(1000, 250);
        EventCompetencePanel panel = new EventCompetencePanel();
        add(panel);
        setVisible(true);
        setTitle("Режим редактирования");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel.submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    try {
                        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                        Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                        Statement s = c.createStatement();
                        // if есть с текущей датой то update, else insert
                        s.executeQuery("INSERT INTO developed_competence VALUES (" + (panel.nameField.getSelectedIndex()+1) + "," + ev.id + ","+(panel.levelField.getSelectedIndex()+1)+","+(panel.resultField.getSelectedIndex()+1)+")");
                    } catch (SQLException errrr) {
                        try{
                            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                            Statement s = c.createStatement();
                            s.executeQuery("UPDATE developed_competence SET level_id = "+(panel.levelField.getSelectedIndex()+1)+", result = "+(panel.resultField.getSelectedIndex()+1)+" where event_id = "+ ev.id + " AND competence_id = " + (panel.nameField.getSelectedIndex()+1));
                        }
                        catch (SQLException err){
                            System.out.println("Ошибка");
                        }
                    }
                    setVisible(false);
                    dispose();
                }
        });

        panel.cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
    }
}
