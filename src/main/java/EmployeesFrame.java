import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class EmployeesFrame extends JFrame {
    Employee teach;
    EmployeesFrame() {

        ArrayList<Employee> employees = new ArrayList<>();
        String id="";
        String last_name="";
        String first_name="";
        String patronymic="";
        String position="";
        String phone="";
        try{
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT e.employee_id, e.last_name, e.first_name, e.patronymic, p.position_name, eh.phone FROM employee e JOIN employee_history eh on eh.employee_id=e.employee_id JOIN position p on p.position_id = eh.position_id where eh.end_date is NULL and (e.is_deleted is null or e.is_deleted = 0) order by e.last_name, e.first_name, e.patronymic");
            while (rs.next()) {
                id = rs.getString("employee_id");
                last_name = rs.getString("last_name");
                first_name = rs.getString("first_name");
                patronymic = rs.getString("patronymic");
                position = rs.getString("position_name");
                phone = rs.getString("phone");
                if (patronymic == null)
                    patronymic = "";
                if (phone == null)
                    phone = "Не определен";
                employees.add(new Employee(id, last_name, first_name, patronymic, position, phone));
            }
        } catch (SQLException ex) {
            if (ex.getErrorCode()==12505){
                JOptionPane.showMessageDialog(
                        this,
                        "Не найдена База Данных");
                setVisible(false);
                dispose();
                return;
            }
            else if (1017 == ex.getErrorCode()){
                JOptionPane.showMessageDialog(
                        this,
                        "Неверное имя пользователя или пароль");
                setVisible(false);
                dispose();
                return;
            }
            else if (ex.getErrorCode()==942){
                JOptionPane.showMessageDialog(
                        this,
                        "Нет таблицы employee или employee_history");
                setVisible(false);
                dispose();
                return;
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getSQLState()+'\n'+ex.getErrorCode()+'\n'+ex.getMessage());
                setVisible(false);
                dispose();
                return;
            }
        }
        catch (StringIndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(
                    this,
                    "Измените данные БД");
            setVisible(false);
            dispose();
            return;
        }


        setSize(1100, 500);
        EmployeesPanel panel = new EmployeesPanel();
        add(panel);
        setVisible(true);
        setTitle("Сотрудники");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.employeesList.setListData(employees.toArray());
        panel.employeesList.setSelectedIndex(Main.curTeach);
        if ( Main.curTeach != -1 ) {
            panel.employeesList.scrollRectToVisible( panel.employeesList.getCellBounds( Main.curTeach, Main.curTeach ) );
            String str = panel.employeesList.getSelectedValue().toString();
            int select = panel.employeesList.getSelectedIndex();
            id = employees.get(select).id;
            first_name = employees.get(select).first_name;
            last_name = employees.get(select).last_name;
            patronymic = employees.get(select).patronymic;
            position = employees.get(select).position;
            phone = employees.get(select).phone;
            teach = new Employee(id, last_name, first_name, patronymic,position,phone);
        }

        panel.employeesList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String str = panel.employeesList.getSelectedValue().toString();
                int select = panel.employeesList.getSelectedIndex();
                String id = employees.get(select).id;
                String first_name = employees.get(select).first_name;
                String last_name = employees.get(select).last_name;
                String patronymic = employees.get(select).patronymic;
                String position = employees.get(select).position;
                String phone = employees.get(select).phone;
                teach = new Employee(id, last_name, first_name, patronymic,position,phone);
                Main.curTeach = select;
            }
        });

        panel.employeeAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EmployeeDetailFrame tdframe = new EmployeeDetailFrame(null);
                tdframe.setLocationRelativeTo(null);
                tdframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                tdframe.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        if (Main.closingWindowEmployee == 0) {
                            setVisible(false);
                            dispose();
                            EmployeesFrame tframe = new EmployeesFrame();
                            tframe.setLocationRelativeTo(null);
                            tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        }
                    }
                });
            }
        });

        panel.employeeChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (panel.employeesList.getSelectedIndex() != -1) {
                    EmployeeDetailFrame tdframe = new EmployeeDetailFrame(teach);
                    tdframe.setLocationRelativeTo(null);
                    tdframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    tdframe.addWindowListener(new java.awt.event.WindowAdapter() {
                        public void windowClosed(WindowEvent e) {
                            if (Main.closingWindowEmployee == 0) {
                                setVisible(false);
                                dispose();
                                EmployeesFrame tframe = new EmployeesFrame();
                                tframe.setLocationRelativeTo(null);
                                tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                            }
                        }
                    });
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Выберите сотрудника");
                }
            }
        });

        panel.employeeDelete.addActionListener(new ActionListener() {
            @SneakyThrows
            public void actionPerformed(ActionEvent e) {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                Statement s = c.createStatement();
                String select = employees.get(panel.employeesList.getSelectedIndex()).id;
                Calendar calendar = Calendar.getInstance();
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String curDate = "TO_DATE('"+formatter.format(calendar.getTime())+"', 'DD-MM-YYYY')";
                s.executeQuery("UPDATE EMPLOYEE_HISTORY SET end_date = "+curDate+" where employee_id = "+ select);
                setVisible(false);
                dispose();
                EmployeesFrame tframe = new EmployeesFrame();
                tframe.setLocationRelativeTo(null);
                tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });
    }
}
