import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

class SetAlgorithmFrame extends JFrame {

    ArrayList<Teacher> teachers;
    ArrayList<CompetenceTeacher> competencesTeachers;
    ArrayList<Event> events;
    ArrayList<CompetenceEvent> competencesEvents;

    SetAlgorithmFrame(ArrayList<Teacher> teachers, ArrayList<CompetenceTeacher> competencesTeachers,
                      ArrayList<Event> events, ArrayList<CompetenceEvent> competencesEvents, int alg) {
        this.teachers = teachers;
        this.competencesTeachers = competencesTeachers;
        this.events = events;
        this.competencesEvents = competencesEvents;

        if (alg == 0) {
            setSize(600, 350);
        }
        if (alg == 1) {
            setSize(600, 300);
        }
        if (alg == 2) {
            setSize(600, 450);
        }
        SetAlgorithmPanel panel = new SetAlgorithmPanel(alg);
        add(panel);
        setVisible(true);
        setTitle("Настройка анализа");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.chooseAlg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
                SetAlgorithmFrame tframe = new SetAlgorithmFrame(teachers, competencesTeachers, events, competencesEvents, panel.chooseAlg.getSelectedIndex());
                tframe.setLocationRelativeTo(null);
                tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });

        if (alg == 1) {
            panel.startAnnealingAlg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AnnealingAlgorithmThread annealingAlgorithmThread = new AnnealingAlgorithmThread(teachers, competencesTeachers, events, competencesEvents);
                    try {
                        annealingAlgorithmThread.tmin = Integer.parseInt(panel.tminField.getText());
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите целое значение максимальной температуры");
                        return;
                    }
                    try {
                        annealingAlgorithmThread.tmax = Integer.parseInt(panel.tmaxField.getText());
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите целое значение минимальной температуры");
                        return;
                    }
                    try {
                        annealingAlgorithmThread.coolingRate = Double.parseDouble((panel.coolField.getText()));
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите вещественное значение изменения температуры");
                        return;
                    }

                    panel.progressBar.setVisible(true);
                    panel.progressBar.setIndeterminate(true);
                    panel.startAnnealingAlg.setVisible(false);
                    panel.timeLabel.setVisible(false);

                    Thread annealingThread = new Thread(annealingAlgorithmThread);
                    annealingThread.start();
                    Timer v = new Timer(10, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (annealingAlgorithmThread.getResult() > 0) {
                                panel.progressBar.setVisible(false);
                                panel.startAnnealingAlg.setVisible(true);
                                panel.timeLabel.setText("Время работы: " + annealingAlgorithmThread.getResult() + " ms");
                                panel.timeLabel.setVisible(true);
                                ((Timer) e.getSource()).stop();
                                saveXLS(events, teachers, annealingAlgorithmThread.getBest());

                                try {
                                    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                                    Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                                    Statement s = c.createStatement();
                                    ResultSet rs = s.executeQuery("SELECT max(schedule_id) from schedule");
                                    int s_id = 1;
                                    if (rs.next()) {
                                        try {
                                            s_id = Integer.parseInt(rs.getString(1)) + 1;
                                        }
                                        catch (Exception ex){
                                            s_id = 1;
                                        }
                                    }
                                    s.executeQuery("INSERT INTO schedule VALUES (" + s_id + "," + s_id + ",sysdate,null)");
                                    Calendar calendar = Calendar.getInstance();
                                    DateFormat formatter = new SimpleDateFormat("yyyy");
                                    ArrayList<Integer> way = annealingAlgorithmThread.getWay();
                                    for (int i = 0; i < way.size(); i++) {
                                        int monthInt = i % 12 + 1;
                                        String month;
                                        if (monthInt < 10)
                                            month = "0" + monthInt;
                                        else month = String.valueOf(monthInt);
                                        String curDate = "TO_DATE('" + "01-" + month + "-" + formatter.format(calendar.getTime()) + "', 'DD-MM-YYYY')";
                                        s.executeQuery("INSERT INTO schedule_element VALUES (" + (way.get(i)+1) + "," + (i+1) + "," + "1" + "," + curDate + "," + s_id +")");
                                    }
                                } catch (SQLException errrr) {
                                    errrr.printStackTrace();
                                }
                            }
                        }
                    });
                    v.start();
                }
            });
        }

        if (alg == 0) {
            panel.startAntAlg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    AntAlgorithmThread antAlgorithmThread = new AntAlgorithmThread(teachers, competencesTeachers, events, competencesEvents);
                    try {
                        antAlgorithmThread.numberAnt = Integer.parseInt(panel.countAntField.getText());
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите целое значение количества муравьев");
                        return;
                    }
                    try {
                        antAlgorithmThread.q = Double.parseDouble((panel.qField.getText()));
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите вещественное значение коэффициента априорной предпочтительности");
                        return;
                    }
                    try {
                        antAlgorithmThread.p = Double.parseDouble((panel.pField.getText()));
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите вещественное значение коэффициента феромона");
                        return;
                    }
                    try {
                        antAlgorithmThread.Part = Double.parseDouble((panel.partField.getText()));
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите вещественное значение изменения феромона");
                        return;
                    }

                    panel.progressBar.setVisible(true);
                    panel.progressBar.setIndeterminate(true);
                    panel.startAntAlg.setVisible(false);
                    panel.timeLabel.setVisible(false);

                    Thread annealingThread = new Thread(antAlgorithmThread);
                    annealingThread.start();
                    Timer v = new Timer(10, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (antAlgorithmThread.getResult() > 0) {
                                panel.progressBar.setVisible(false);
                                panel.startAntAlg.setVisible(true);
                                panel.timeLabel.setText("Время работы: " + antAlgorithmThread.getResult() + " ms");
                                panel.timeLabel.setVisible(true);
                                ((Timer) e.getSource()).stop();
                                saveXLS(events, teachers, antAlgorithmThread.getBest());

                                try {
                                    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                                    Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                                    Statement s = c.createStatement();
                                    ResultSet rs = s.executeQuery("SELECT max(schedule_id) from schedule");
                                    int s_id = 1;
                                    if (rs.next()) {
                                        try {
                                            s_id = Integer.parseInt(rs.getString(1)) + 1;
                                        }
                                        catch (Exception ex){
                                            s_id = 1;
                                        }
                                    }
                                    s.executeQuery("INSERT INTO schedule VALUES (" + s_id + "," + s_id + ",sysdate,null)");
                                    Calendar calendar = Calendar.getInstance();
                                    DateFormat formatter = new SimpleDateFormat("yyyy");
                                    ArrayList<Integer> way = antAlgorithmThread.getWay();
                                    for (int i = 0; i < way.size(); i++) {
                                        int monthInt = i % 12 + 1;
                                        String month;
                                        if (monthInt < 10)
                                            month = "0" + monthInt;
                                        else month = String.valueOf(monthInt);
                                        String curDate = "TO_DATE('" + "01-" + month + "-" + formatter.format(calendar.getTime()) + "', 'DD-MM-YYYY')";
                                        s.executeQuery("INSERT INTO schedule_element VALUES (" + (way.get(i)+1) + "," + (i+1) + "," + "1" + "," + curDate + "," + s_id +")");
                                    }
                                } catch (SQLException errrr) {
                                    errrr.printStackTrace();
                                }
                            }
                        }
                    });
                    v.start();
                }
            });
        }

        if (alg == 2) {
            panel.startAllAlg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AnnealingAlgorithmThread annealingAlgorithmThread = new AnnealingAlgorithmThread(teachers, competencesTeachers, events, competencesEvents);
                    AntAlgorithmThread antAlgorithmThread = new AntAlgorithmThread(teachers, competencesTeachers, events, competencesEvents);

                    try {
                        antAlgorithmThread.numberAnt = Integer.parseInt(panel.countAntField.getText());
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите целое значение количества муравьев");
                        return;
                    }
                    try {
                        antAlgorithmThread.q = Double.parseDouble((panel.qField.getText()));
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите вещественное значение коэффициента априорной предпочтительности");
                        return;
                    }
                    try {
                        antAlgorithmThread.p = Double.parseDouble((panel.pField.getText()));
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите вещественное значение коэффициента феромона");
                        return;
                    }
                    try {
                        antAlgorithmThread.Part = Double.parseDouble((panel.partField.getText()));
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите вещественное значение изменения феромона");
                        return;
                    }
                    try {
                        annealingAlgorithmThread.tmin = Integer.parseInt(panel.tminField.getText());
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите целое значение максимальной температуры");
                        return;
                    }
                    try {
                        annealingAlgorithmThread.tmax = Integer.parseInt(panel.tmaxField.getText());
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите целое значение минимальной температуры");
                        return;
                    }
                    try {
                        annealingAlgorithmThread.coolingRate = Double.parseDouble((panel.coolField.getText()));
                    }
                    catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(null,
                                "Введите вещественное значение изменения температуры");
                        return;
                    }

                    panel.progressBar.setVisible(true);
                    panel.progressBar.setIndeterminate(true);
                    panel.startAllAlg.setVisible(false);
                    panel.timeLabel.setVisible(false);

                    Thread annealingThread = new Thread(annealingAlgorithmThread);
                    annealingThread.start();
                    Thread antThread = new Thread(antAlgorithmThread);
                    antThread.start();
                    Timer v = new Timer(10, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (annealingAlgorithmThread.getResult() > 0 && antAlgorithmThread.getResult() > 0) {
                                panel.progressBar.setVisible(false);
                                panel.startAllAlg.setVisible(true);
                                if (annealingAlgorithmThread.getResult() > antAlgorithmThread.getResult())
                                    panel.timeLabel.setText("Время работы: " + annealingAlgorithmThread.getResult() + " ms");
                                else
                                    panel.timeLabel.setText("Время работы: " + antAlgorithmThread.getResult() + " ms");
                                panel.timeLabel.setVisible(true);
                                ((Timer) e.getSource()).stop();
                                if (annealingAlgorithmThread.getDistance() > antAlgorithmThread.getDistance())
                                    saveXLS(events, teachers, annealingAlgorithmThread.getBest());
                                else saveXLS(events, teachers, antAlgorithmThread.getBest());


                                try {
                                    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                                    Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                                    Statement s = c.createStatement();
                                    ResultSet rs = s.executeQuery("SELECT max(schedule_id) from schedule");
                                    int s_id = 1;
                                    if (rs.next()) {
                                        try {
                                            s_id = Integer.parseInt(rs.getString(1)) + 1;
                                        }
                                        catch (Exception ex){
                                            s_id = 1;
                                        }
                                    }
                                    s.executeQuery("INSERT INTO schedule VALUES (" + s_id + "," + s_id + ",sysdate,null)");
                                    Calendar calendar = Calendar.getInstance();
                                    DateFormat formatter = new SimpleDateFormat("yyyy");
                                    ArrayList<Integer> way;
                                    if (annealingAlgorithmThread.getDistance() > antAlgorithmThread.getDistance())
                                        way = annealingAlgorithmThread.getWay();
                                    else way = antAlgorithmThread.getWay();
                                    for (int i = 0; i < way.size(); i++) {
                                        int monthInt = i % 12 + 1;
                                        String month;
                                        if (monthInt < 10)
                                            month = "0" + monthInt;
                                        else month = String.valueOf(monthInt);
                                        String curDate = "TO_DATE('" + "01-" + month + "-" + formatter.format(calendar.getTime()) + "', 'DD-MM-YYYY')";
                                        s.executeQuery("INSERT INTO schedule_element VALUES (" + (way.get(i)+1) + "," + (i+1) + "," + "1" + "," + curDate + "," + s_id +")");
                                    }
                                } catch (SQLException errrr) {
                                    errrr.printStackTrace();
                                }
                            }
                        }
                    });
                    v.start();
                }
            });
        }
    }

    public void saveXLS (ArrayList<Event> events, ArrayList<Teacher> teachers, Ant ant){
        int i;
        int result;
        JFileChooser fileChooserXLS = new JFileChooser();
        FileNameExtensionFilter filterXLS = new FileNameExtensionFilter(
                "Файл Excel (XLS)", "xls");
        fileChooserXLS.setFileFilter(filterXLS);

        Workbook book = null;
        try {
            long startTime = System.currentTimeMillis();
            book = new HSSFWorkbook();
            Sheet sheet = book.createSheet("График КПК");
            Row row0 = sheet.createRow(0);
            Cell[] date = new Cell[24];

            CellStyle style = book.createCellStyle();
            Font font = book.createFont();
            font.setBold(true);
            style.setFont(font);

            for (i = 0; i < 23; i++) {
                date[i] = row0.createCell(i);
                date[i].setCellStyle(style);
            }

            date[0].setCellValue("Январь");
            date[2].setCellValue("Февраль");
            date[4].setCellValue("Март");
            date[6].setCellValue("Апрель");
            date[8].setCellValue("Май");
            date[10].setCellValue("Июнь");
            date[12].setCellValue("Июль");
            date[14].setCellValue("Август");
            date[16].setCellValue("Сентябрь");
            date[18].setCellValue("Октябрь");
            date[20].setCellValue("Ноябрь");
            date[22].setCellValue("Декабрь");
            int inOneColumn = ant.getCount() / 12;
            Row[] row = new Row[inOneColumn+1];
            for (int g = 0; g <= inOneColumn; g++){
                row[g] = sheet.createRow(g+1);
            }
            for (int g = 0; g <= inOneColumn; g++) {
                Cell[] cname = new Cell[24];
                for (i = 0; i < 24; i++) {
                    if (i % 2 == 0 && (g*12)+i/2 < teachers.size()) {
                        cname[i] = row[g].createCell(i);
                        cname[i].setCellValue(teachers.get((g*12)+i/2).id + ". " +
                                teachers.get((g*12)+i/2).last_name + " " +
                                teachers.get((g*12)+i/2).first_name.charAt(0) + ". " +
                                teachers.get((g*12)+i/2).patronymic.charAt(0) + ".");
                    }
                    else if (i % 2 == 1){
                        cname[i] = row[g].createCell(i);
                        cname[i].setCellValue(events.get(ant.get((g*12)+i/2)).id + ". " +
                                events.get(ant.get((g*12) + i/2)).name);
                    }
                    else break;
                }
            }

            sheet.autoSizeColumn(0);
            for (i = 0; i < ant.getCount(); i++) {
                sheet.autoSizeColumn(i + 1);
            }
            long endTime = System.currentTimeMillis();
            fileChooserXLS.setDialogTitle("Сохранение файла");
            fileChooserXLS.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooserXLS.setSelectedFile(new File(".xls"));
            result = fileChooserXLS.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                long startTime1 = System.currentTimeMillis();
                FileOutputStream fos = new FileOutputStream(fileChooserXLS.getSelectedFile());
                book.write(fos);
                book.close();
                long endTime1 = System.currentTimeMillis();
                System.out.println("save " + (endTime + endTime1 - startTime1 - startTime) + "ms");
                JOptionPane.showMessageDialog(null,
                        "Файл '" + fileChooserXLS.getSelectedFile() +
                                " ' сохранен");
                fos.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
