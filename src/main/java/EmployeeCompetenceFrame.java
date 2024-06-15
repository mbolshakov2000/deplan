import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

class EmployeeCompetenceFrame extends JFrame {
    EmployeeCompetenceFrame(Employee t) {
        setSize(1000, 250);
        EmployeeCompetencePanel panel = new EmployeeCompetencePanel();
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
                        Calendar calendar = Calendar.getInstance();
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String curDate = "TO_DATE('"+formatter.format(calendar.getTime())+"', 'DD-MM-YYYY')";
                        // if есть с текущей датой то update, else insert
                        s.executeQuery("INSERT INTO current_level VALUES (" + t.id + ","+(panel.nameField.getSelectedIndex()+1)+","+(panel.levelField.getSelectedIndex()+1)+","+panel.scoreField.getText()+","+curDate+")");
                    } catch (SQLException errrr) {
                        try{
                            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                            Statement s = c.createStatement();
                            Calendar calendar = Calendar.getInstance();
                            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            String curDate = "TO_DATE('"+formatter.format(calendar.getTime())+"', 'DD-MM-YYYY')";
                            s.executeQuery("UPDATE current_level SET level_id = "+(panel.levelField.getSelectedIndex()+1)+", score = "+panel.scoreField.getText()+" where employee_id = "+ t.id + " AND cl_date = "+curDate + " AND competence_id = " + (panel.nameField.getSelectedIndex()+1));
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
