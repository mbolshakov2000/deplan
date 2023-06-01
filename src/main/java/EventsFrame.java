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

class EventsFrame extends JFrame {
    Event event;
    EventsFrame() {
        ArrayList<Event> events = new ArrayList<>();
        String id="";
        String name="";
        String type="";
        String description="";
        String date_start="";
        String date_end="";
        String quantity="";
        String cost="";
        try{
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT event_id, name, etype, description, date_start, date_end, " +
                    "quantity, cost FROM event where (is_deleted is null or is_deleted = 0) and ((date_start > SYSDATE and date_end > " +
                    "SYSDATE AND quantity > 0) or (quantity > 0 and date_start is null and date_end is null))");
            while (rs.next()) {
                id = rs.getString("event_id");
                name = rs.getString("name");
                type = rs.getString("etype");
                description = rs.getString("description");
                date_start = rs.getString("date_start");
                date_end = rs.getString("date_end");
                quantity = rs.getString("quantity");
                cost = rs.getString("cost");
                events.add(new Event(id, name, type, description, date_start, date_end, quantity, cost));
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
                        "Нет таблицы event");
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
        EventsPanel panel = new EventsPanel();
        add(panel);
        setVisible(true);
        setTitle("Мероприятия");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.eventsList.setListData(events.toArray());
        panel.eventsList.setSelectedIndex(Main.curEvent);
        if ( Main.curEvent != -1 ) {
            panel.eventsList.scrollRectToVisible( panel.eventsList.getCellBounds( Main.curEvent, Main.curEvent ) );
            String str = panel.eventsList.getSelectedValue().toString();
            int select = panel.eventsList.getSelectedIndex();
            id = events.get(select).id;
            name = events.get(select).name;
            type = events.get(select).type;
            description = events.get(select).description;
            date_start = events.get(select).date_start;
            date_end = events.get(select).date_end;
            quantity = events.get(select).quantity;
            cost = events.get(select).cost;
            event = new Event(id, name, type, description,date_start,date_end,quantity,cost);
        }

        panel.eventsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String str = panel.eventsList.getSelectedValue().toString();
                int select = panel.eventsList.getSelectedIndex();
                String id = events.get(select).id;
                String name = events.get(select).name;
                String type = events.get(select).type;
                String description = events.get(select).description;
                String date_start = events.get(select).date_start;
                String date_end = events.get(select).date_end;
                String quantity = events.get(select).quantity;
                String cost = events.get(select).cost;
                event = new Event(id, name, type, description,date_start,date_end,quantity,cost);
                Main.curEvent = select;
            }
        });

        panel.eventAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventDetailFrame tdframe = new EventDetailFrame(null);
                tdframe.setLocationRelativeTo(null);
                tdframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                tdframe.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        if (Main.closingWindowTeacher == 0) {
                            setVisible(false);
                            dispose();
                            EventsFrame tframe = new EventsFrame();
                            tframe.setLocationRelativeTo(null);
                            tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        }
                    }
                });
            }
        });

        panel.eventChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (panel.eventsList.getSelectedIndex() != -1) {
                    EventDetailFrame tdframe = new EventDetailFrame(event);
                    tdframe.setLocationRelativeTo(null);
                    tdframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    tdframe.addWindowListener(new java.awt.event.WindowAdapter() {
                        public void windowClosed(WindowEvent e) {
                            if (Main.closingWindowEvent == 0) {
                                setVisible(false);
                                dispose();
                                EventsFrame eframe = new EventsFrame();
                                eframe.setLocationRelativeTo(null);
                                eframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                            }
                        }
                    });
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Выберите мероприятие");
                }
            }
        });

        panel.eventDelete.addActionListener(new ActionListener() {
            @SneakyThrows
            public void actionPerformed(ActionEvent e) {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                Statement s = c.createStatement();
                String select = events.get(panel.eventsList.getSelectedIndex()).id;
                s.executeQuery("UPDATE EVENT SET quantity = 0 where event_id = "+ select);
                setVisible(false);
                dispose();
                EventsFrame tframe = new EventsFrame();
                tframe.setLocationRelativeTo(null);
                tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });
    }
}
