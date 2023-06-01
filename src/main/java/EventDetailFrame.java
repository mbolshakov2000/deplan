import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

class EventDetailFrame extends JFrame {
    EventDetailFrame(Event ev) {
        Main.closingWindowEvent = 0;
        setSize(1000, 500);
        EventDetailPanel panel = new EventDetailPanel(ev);
        add(panel);
        setVisible(true);
        setTitle("Режим редактирования");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel.submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel.nameField.isShowing()) {
                    if (!panel.nameField.getText().equals("") && !panel.quantityField.getText().equals("")) {
                        try {
                            int id = 1;
                            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                            Statement s = c.createStatement();
                            ResultSet rs = s.executeQuery("SELECT max(event_id) from event");
                            if (rs.next()) {
                                id = parseInt(rs.getString(1)) + 1;
                            }
                            String startDate;
                            String endDate;
                            if (panel.dateStartField.getModel().isSelected()) {
                                startDate = "TO_DATE('" + panel.dateStartField.getModel().getDay() + "-" +
                                        (panel.dateStartField.getModel().getMonth()+1) + "-" +
                                        panel.dateStartField.getModel().getYear() + "', 'DD-MM-YYYY')";
                            }
                            else {
                                startDate = "''";
                            }
                            if (panel.dateEndField.getModel().isSelected()) {
                                endDate = "TO_DATE('" + panel.dateEndField.getModel().getDay() + "-" +
                                        (panel.dateEndField.getModel().getMonth()+1) + "-" +
                                        panel.dateEndField.getModel().getYear() + "', 'DD-MM-YYYY')";
                            }
                            else{
                                endDate = "''";
                            }
                            if (panel.costField.getText().equals(""))
                                panel.costField.setText("''");
                            s.executeQuery("INSERT INTO Event VALUES (" + id+",'"+panel.nameField.getText() +
                                    "','"+panel.typeField.getText()+"','"+panel.descriptionField.getText()+ "', " +
                                    startDate+ ", " + endDate + ", " + panel.quantityField.getText() +
                                    ", " + panel.costField.getText() +",0)");

                        } catch (SQLException errrr) {
                            System.out.println("Ошибка");
                        }
                        setVisible(false);
                        dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(
                                null,
                                "Введите Наименование и Кол-во (если количество - бесконечно, укажите 100000)");
                    }
                }
                else {
                    try {
                        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                        Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                        Statement s = c.createStatement();

                        String startDate;
                        String endDate;
                        if (panel.dateStartField.getModel().isSelected()) {
                            startDate = "TO_DATE('" + panel.dateStartField.getModel().getDay() + "-" +
                                    (panel.dateStartField.getModel().getMonth()+1) + "-" +
                                    panel.dateStartField.getModel().getYear() + "', 'DD-MM-YYYY')";
                        }
                        else {
                            startDate = "''";
                        }
                        if (panel.dateEndField.getModel().isSelected()) {
                            endDate = "TO_DATE('" + panel.dateEndField.getModel().getDay() + "-" +
                                    (panel.dateEndField.getModel().getMonth()+1) + "-" +
                                    panel.dateEndField.getModel().getYear() + "', 'DD-MM-YYYY')";
                        }
                        else{
                            endDate = "''";
                        }
                        if (panel.costField.getText().equals(""))
                            panel.costField.setText("''");
                        s.executeQuery("UPDATE Event SET etype = '"+panel.typeField.getText()+
                                "', description = '"+panel.descriptionField.getText()+"', date_start = " + startDate +
                                ", date_end = " + endDate + ", quantity = " + panel.quantityField.getText() +
                                ", cost = " + panel.costField.getText() + " where event_id = "+ ev.id);
                        }
                        catch (SQLException err){
                            System.out.println("Ошибка");
                        }
                    setVisible(false);
                    dispose();
                }
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
