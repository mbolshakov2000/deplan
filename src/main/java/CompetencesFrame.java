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

class CompetencesFrame extends JFrame {
    Competence competence;
    CompetencesFrame() {

        ArrayList<Competence> competences = new ArrayList<>();
        String id="";
        String abbreviation="";
        String name="";
        String description="";
        try{
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * from competence");
            while (rs.next()) {
                id = rs.getString("competence_id");
                abbreviation = rs.getString("abbreviation");
                name = rs.getString("name");
                description = rs.getString("description");
                if (description == null)
                    description = "";
                competences.add(new Competence(id, abbreviation, name, description));
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
                        "Нет таблицы competence");
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
        CompetencesPanel panel = new CompetencesPanel();
        add(panel);
        setVisible(true);
        setTitle("Компетенции");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.competencesList.setListData(competences.toArray());
        panel.competencesList.setSelectedIndex(Main.curCompetence);
        if ( Main.curCompetence != -1 ) {
            panel.competencesList.scrollRectToVisible( panel.competencesList.getCellBounds( Main.curCompetence, Main.curCompetence ) );
            String str = panel.competencesList.getSelectedValue().toString();
            int select = panel.competencesList.getSelectedIndex();
            id = competences.get(select).id;
            name = competences.get(select).name;
            abbreviation = competences.get(select).abbreviation;
            description = competences.get(select).description;
            competence = new Competence(id, abbreviation, name, description);
        }

        panel.competencesList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String str = panel.competencesList.getSelectedValue().toString();
                int select = panel.competencesList.getSelectedIndex();
                String id = competences.get(select).id;
                String name = competences.get(select).name;
                String abbreviation = competences.get(select).abbreviation;
                String description = competences.get(select).description;
                competence = new Competence(id, abbreviation, name, description);
                Main.curCompetence = select;
            }
        });

        panel.competenceCheck.addActionListener(new ActionListener() {
            @SneakyThrows
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panel,
                        "<html><h1>"+competence.abbreviation+" - "+competence.name+
                                "</h1><h2 style='width: 600px;'>"+competence.description+"</h2>");

            }
        });
    }
}
