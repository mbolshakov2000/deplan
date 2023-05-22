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
import static java.lang.Thread.sleep;

class TeacherDetailFrame extends JFrame {
    TeacherDetailFrame(Teacher t) {
        Main.closingWindowTeacher = 0;
        ArrayList<CompetenceTeacher> competenceTeachers = new ArrayList<>();
        if (t != null) {
            String employee_id="";
            String id ="";
            String abbreviation = "";
            String name = "";
            String level_id="";
            String level = "";
            String score = "";
            String date = "";
            try {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("select cl.employee_id, c.competence_id, c.abbreviation, c.name, " +
                        "dl.level_id, dl.name as \"level\", cl.score, cl.cl_date from current_level cl " +
                        "JOIN competence c on c.competence_id = cl.competence_id " +
                        "JOIN dev_level dl on dl.level_id = cl.level_id " +
                        "where cl.employee_id = " + t.id + " and " +
                        "cl.cl_date = (select max(cl2.cl_date) from current_level cl2 " +
                        "where cl.competence_id = cl2.competence_id and cl.employee_id = cl2.employee_id)");
                while (rs.next()) {
                    employee_id = rs.getString("employee_id");
                    id = rs.getString("competence_id");
                    abbreviation = rs.getString("abbreviation");
                    name = rs.getString("name");
                    level_id = rs.getString("level_id");
                    level = rs.getString("level");
                    score = rs.getString("score");
                    date = rs.getString("cl_date");
                    date = date.substring(0, date.indexOf(" 00:00:00"));
//                if (patronymic == null)
//                    patronymic = "";
//                if (phone == null)
//                    phone = "Не определен";
                    competenceTeachers.add(new CompetenceTeacher(employee_id,id,abbreviation, name, level_id, level, score, date));
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
                            "Нет таблицы current_level или dev_level или competence");
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
            } catch (StringIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Измените данные БД");
                setVisible(false);
                dispose();
                return;
            }
        }

        if (t == null)
            setSize(1000, 350);
        else
            setSize(1000, 620);
        TeacherDetailPanel panel = new TeacherDetailPanel(t, competenceTeachers);
        add(panel);
        setVisible(true);
        setTitle("Режим редактирования");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

//        panel.competenceTable.setListData(teachers.toArray());

        panel.submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel.lastNameField.isShowing()) {
                    if (!panel.lastNameField.getText().equals("") && !panel.firstNameField.getText().equals("") && panel.positionField.getSelectedIndex() != -1) {
                        try {
                            int id = 1;
                            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                            Statement s = c.createStatement();
                            ResultSet rs = s.executeQuery("SELECT max(employee_id) from employee");
                            if (rs.next()) {
                                id = parseInt(rs.getString(1)) + 1;
                            }
                            s.executeQuery("INSERT INTO EMPLOYEE VALUES (" + id+",'"+panel.lastNameField.getText()+"','"+panel.firstNameField.getText()+"','"+panel.patronymicField.getText()+"')");
                            Calendar calendar = Calendar.getInstance();
                            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            String curDate = "TO_DATE('"+formatter.format(calendar.getTime())+"', 'DD-MM-YYYY')";
                            s.executeQuery("INSERT INTO EMPLOYEE_HISTORY VALUES (" + id + ","+curDate+",'"+panel.phoneField.getText()+"','',"+panel.positionField.getSelectedIndex()+")");
                        } catch (SQLException errrr) {
                            System.out.println("Ошибка");
                        }
                        setVisible(false);
                        dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(
                                null,
                                "Введите ФИО");
                    }
                }
                else {
                    try {
                        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                        Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                        Statement s = c.createStatement();
                        Calendar calendar = Calendar.getInstance();
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String curDate = "TO_DATE('"+formatter.format(calendar.getTime())+"', 'DD-MM-YYYY')";
                        s.executeQuery("INSERT INTO EMPLOYEE_HISTORY VALUES (" + t.id + ","+curDate+",'"+panel.phoneField.getText()+"','',"+panel.positionField.getSelectedIndex()+")");
                    } catch (SQLException errrr) {
                        try{
                            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                            Statement s = c.createStatement();
                            Calendar calendar = Calendar.getInstance();
                            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            String curDate = "TO_DATE('"+formatter.format(calendar.getTime())+"', 'DD-MM-YYYY')";
                            s.executeQuery("UPDATE EMPLOYEE_HISTORY SET phone = '"+panel.phoneField.getText()+"', position_id = "+panel.positionField.getSelectedIndex()+" where employee_id = "+ t.id + " AND start_date = "+curDate);
                        }
                        catch (SQLException err){
                            System.out.println("Ошибка");
                        }
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
                TeacherCompetenceFrame tcframe = new TeacherCompetenceFrame(t);
                tcframe.setLocationRelativeTo(null);
                tcframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                tcframe.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        setVisible(false);
                        dispose();
                        TeacherDetailFrame tframe = new TeacherDetailFrame(t);
                        tframe.setLocationRelativeTo(null);
                        tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        Main.closingWindowTeacher = 1;
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
                String selectID = competenceTeachers.get(panel.competenceTable.getSelectedRow()).id;
                String selectDate = "TO_DATE('"+ competenceTeachers.get(panel.competenceTable.getSelectedRow()).date+"', 'YYYY-MM-DD')";
                s.executeQuery("DELETE FROM CURRENT_LEVEL WHERE cl_date = "+selectDate+" AND employee_id = "+ t.id +" AND competence_id = " + selectID);
                setVisible(false);
                dispose();
                TeacherDetailFrame tframe = new TeacherDetailFrame(t);
                tframe.setLocationRelativeTo(null);
                tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });
    }
}
