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
        ArrayList<CompetenceEvent> competences = new ArrayList<>();
        if (ev != null) {
            String event_id="";
            String id ="";
            String abbreviation = "";
            String name = "";
            String level_id="";
            String level = "";
            String result_id = "";
            String result = "";
            try {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("select dc.event_id, c.competence_id, c.abbreviation, c.name, " +
                        "dl.level_id, dl.name as \"level\", dc.result as \"result_id\", dl2.name as \"result\" " +
                        "from developed_competence dc JOIN competence c on c.competence_id = dc.competence_id " +
                        "JOIN dev_level dl on dl.level_id = dc.level_id " +
                        "JOIN dev_level dl2 on dl2.level_id = dc.result " +
                        "where dc.event_id = " + ev.id);
                while (rs.next()) {
                    event_id = rs.getString("event_id");
                    id = rs.getString("competence_id");
                    abbreviation = rs.getString("abbreviation");
                    name = rs.getString("name");
                    level_id = rs.getString("level_id");
                    level = rs.getString("level");
                    result_id = rs.getString("result_id");
                    result = rs.getString("result");
                    competences.add(new CompetenceEvent(event_id,id,abbreviation, name, level_id,
                            level, result_id, result));
                }
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 12505) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Не найдена База Данных");
                    setVisible(false);
                    dispose();
                    return;
                } else if (1017 == ex.getErrorCode()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Неверное имя пользователя или пароль");
                    setVisible(false);
                    dispose();
                    return;
                } else if (ex.getErrorCode() == 942) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Нет таблицы developed_competence или dev_level или competence");
                    setVisible(false);
                    dispose();
                    return;
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            ex.getSQLState() + '\n' + ex.getErrorCode() + '\n' + ex.getMessage());
                    setVisible(false);
                    dispose();
                    return;
                }
            } catch (StringIndexOutOfBoundsException er) {
                JOptionPane.showMessageDialog(
                        this,
                        "Измените данные БД");
                setVisible(false);
                dispose();
                return;
            }
        }

        if (ev == null)
            setSize(1000, 500);
        else
            setSize(1000, 850);
        EventDetailPanel panel = new EventDetailPanel(ev, competences);
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
                                    ", " + panel.costField.getText() +")");

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

        panel.competenceAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventCompetenceFrame ecframe = new EventCompetenceFrame(ev);
                ecframe.setLocationRelativeTo(null);
                ecframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                ecframe.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        setVisible(false);
                        dispose();
                        EventDetailFrame tframe = new EventDetailFrame(ev);
                        tframe.setLocationRelativeTo(null);
                        tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        Main.closingWindowEvent = 1;
                    }
                });
            }
        });

        panel.competenceDelete.addActionListener(new ActionListener() {
            @SneakyThrows
            public void actionPerformed(ActionEvent e) {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                Statement s = c.createStatement();
                String selectID = competences.get(panel.competenceTable.getSelectedRow()).id;
                s.executeQuery("DELETE FROM CURRENT_LEVEL WHERE event_id = "+ ev.id +" AND competence_id = " + selectID);
                setVisible(false);
                dispose();
                EventDetailFrame tframe = new EventDetailFrame(ev);
                tframe.setLocationRelativeTo(null);
                tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });
    }
}
