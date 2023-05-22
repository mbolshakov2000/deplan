import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

class AlgorithmFrame extends JFrame {
    AlgorithmFrame() {

        ArrayList<Teacher> teachers = new ArrayList<>();
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
            ResultSet rs = s.executeQuery("SELECT e.employee_id, e.last_name, e.first_name, e.patronymic, p.position_name, eh.phone FROM employee e JOIN employee_history eh on eh.employee_id=e.employee_id JOIN position p on p.position_id = eh.position_id where eh.end_date is NULL");
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
                teachers.add(new Teacher(id, last_name, first_name, patronymic, position, phone));
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


        ArrayList<CompetenceTeacher> competencesTeachers = new ArrayList<>();
        String employee_id = "";
        id ="";
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
            ResultSet rs = s.executeQuery("select cl.employee_id, c.competence_id, c.abbreviation, c.name, dl.level_id, dl.name as \"level\", cl.score, cl.cl_date from current_level cl " +
                    "JOIN competence c on c.competence_id = cl.competence_id " +
                    "JOIN dev_level dl on dl.level_id = cl.level_id " +
                    "where cl.cl_date = (select max(cl2.cl_date) from current_level cl2 " +
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
                competencesTeachers.add(new CompetenceTeacher(employee_id,id,abbreviation, name, level_id, level, score, date));
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

        ArrayList<Event> events = new ArrayList<>();
        id="";
        name="";
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
                    "quantity, cost FROM event where (date_start > SYSDATE and date_end > " +
                    "SYSDATE AND quantity > 0) or (quantity > 0 and date_start is null and date_end is null)");
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

        ArrayList<CurrentLevel> currentLevels = new ArrayList<>();
        String competence_id="";
        level_id="";
        String event_id="";
        String result_id="";
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select cl.competence_id, cl.level_id as \"result_id\", cl.event_id, " +
                    "max(cl2.level_id) keep (dense_rank first order by cl2.cl_date) as \"level_id\" from current_level cl join " +
                    "current_level cl2 on cl.employee_id = cl2.employee_id and cl.competence_id = cl2.competence_id and " +
                    "cl2.cl_date < cl.cl_date where (cl.is_deleted is null or cl.is_deleted = 0) and (cl2.is_deleted is null or cl2.is_deleted = 0) " +
                    "group by cl.event_id, cl.competence_id, cl.level_id, cl.employee_id, cl.cl_date");
            while (rs.next()) {
                competence_id = rs.getString("competence_id");
                level_id = rs.getString("level_id");
                event_id = rs.getString("event_id");
                result_id = rs.getString("result_id");
                currentLevels.add(new CurrentLevel(competence_id, level_id, event_id, result_id));
            }
        } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ошибка");
                setVisible(false);
                dispose();
                return;
        }
        File file = new File("train.csv");
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Competence,Event,Level,Dest,Dest1");
            bw.newLine();
            for (int i = 0; i < currentLevels.size(); i++) {
                bw.write(currentLevels.get(i).getCompetence_id() + "," + currentLevels.get(i).getEvent_id() + "," +
                        currentLevels.get(i).getLevel_id() + "," + currentLevels.get(i).getResult_id() + "," +
                        (Integer.parseInt(currentLevels.get(i).getResult_id()) > Integer.parseInt(currentLevels.get(i).getLevel_id()) ? 1 : 0));
                bw.newLine();
            }
            bw.close();
            fw.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String line = "python src/main/resources/predict_result.py";
        CommandLine cmdLine = CommandLine.parse(line);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);
        try {
            int exitCode = executor.execute(cmdLine);
            System.out.println(outputStream.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
            Statement s = c.createStatement();
            s.executeQuery("delete from developed_competence purge");
            BufferedReader reader = new BufferedReader(new FileReader("text.txt"));
            String newLine = reader.readLine();
            while (newLine != null) {
                String[] parts = newLine.split(" ");
                s.executeQuery("INSERT INTO developed_competence VALUES (" + parts[1] + "," + parts[0] + "," + parts[2] + "," + parts[3] + ")");
                newLine = reader.readLine();
            }
            reader.close();
        }
        catch (Exception errrr) {
            errrr.printStackTrace();
        }

        ArrayList<CompetenceEvent> competencesEvents = new ArrayList<>();
        event_id="";
        id ="";
        abbreviation = "";
        name = "";
        level_id="";
        level = "";
        result_id = "";
        String result = "";
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select dc.event_id, c.competence_id, c.abbreviation, c.name, " +
                    "dl.level_id, dl.name as \"level\", dc.result as \"result_id\", dl2.name as \"result\" " +
                    "from developed_competence dc JOIN competence c on c.competence_id = dc.competence_id " +
                    "JOIN dev_level dl on dl.level_id = dc.level_id " +
                    "JOIN dev_level dl2 on dl2.level_id = dc.result ");
            while (rs.next()) {
                event_id = rs.getString("event_id");
                id = rs.getString("competence_id");
                abbreviation = rs.getString("abbreviation");
                name = rs.getString("name");
                level_id = rs.getString("level_id");
                level = rs.getString("level");
                result_id = rs.getString("result_id");
                result = rs.getString("result");
                competencesEvents.add(new CompetenceEvent(event_id,id,abbreviation, name, level_id,
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


        setSize(600, 350);
        AlgorithmPanel panel = new AlgorithmPanel();
        add(panel);
        setVisible(true);
        setTitle("Анализ");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.teachersLabel.setText("Сотрудников: " + teachers.size());
        panel.eventsLabel.setText("Мероприятий: " + events.size());
        panel.competencesLabel.setText("Компетенций: "+ competencesTeachers.size());

        panel.startAnnealingAlg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.progressBar.setVisible(true);
                panel.progressBar.setIndeterminate(true);
                panel.startAntAlg.setVisible(false);
                panel.startAnnealingAlg.setVisible(false);
                panel.startSetAlg.setVisible(false);
                panel.startAllAlg.setVisible(false);
                panel.timeLabel.setVisible(false);

                AnnealingAlgorithmThread annealingAlgorithmThread = new AnnealingAlgorithmThread(teachers, competencesTeachers, events, competencesEvents);
                Thread annealingThread = new Thread(annealingAlgorithmThread);
                annealingThread.start();
                Timer v = new Timer(10, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (annealingAlgorithmThread.getResult()>0){
                            panel.progressBar.setVisible(false);
                            panel.startAntAlg.setVisible(true);
                            panel.startAnnealingAlg.setVisible(true);
                            panel.startAllAlg.setVisible(true);
                            panel.startSetAlg.setVisible(true);
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
                                        s_id = parseInt(rs.getString(1)) + 1;
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

        panel.startAntAlg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.progressBar.setVisible(true);
                panel.progressBar.setIndeterminate(true);
                panel.startAntAlg.setVisible(false);
                panel.startSetAlg.setVisible(false);
                panel.startAnnealingAlg.setVisible(false);
                panel.startAllAlg.setVisible(false);
                panel.timeLabel.setVisible(false);

                AntAlgorithmThread antAlgorithmThread = new AntAlgorithmThread(teachers,competencesTeachers,events, competencesEvents);
                Thread annealingThread = new Thread(antAlgorithmThread);
                annealingThread.start();
                Timer v = new Timer(10, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (antAlgorithmThread.getResult()>0){
                            panel.progressBar.setVisible(false);
                            panel.startAntAlg.setVisible(true);
                            panel.startAnnealingAlg.setVisible(true);
                            panel.startAllAlg.setVisible(true);
                            panel.startSetAlg.setVisible(true);
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
                                        s_id = parseInt(rs.getString(1)) + 1;
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

        panel.startAllAlg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.progressBar.setVisible(true);
                panel.progressBar.setIndeterminate(true);
                panel.startAntAlg.setVisible(false);
                panel.startSetAlg.setVisible(false);
                panel.startAnnealingAlg.setVisible(false);
                panel.startAllAlg.setVisible(false);
                panel.timeLabel.setVisible(false);

                AnnealingAlgorithmThread annealingAlgorithmThread = new AnnealingAlgorithmThread(teachers, competencesTeachers, events, competencesEvents);
                AntAlgorithmThread antAlgorithmThread = new AntAlgorithmThread(teachers,competencesTeachers,events, competencesEvents);
                Thread annealingThread = new Thread(annealingAlgorithmThread);
                annealingThread.start();
                Thread antThread = new Thread(antAlgorithmThread);
                antThread.start();
                Timer v = new Timer(10, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (annealingAlgorithmThread.getResult()>0 && antAlgorithmThread.getResult()>0){
                            panel.progressBar.setVisible(false);
                            panel.startAntAlg.setVisible(true);
                            panel.startAnnealingAlg.setVisible(true);
                            panel.startSetAlg.setVisible(true);
                            panel.startAllAlg.setVisible(true);
                            if (annealingAlgorithmThread.getResult()>antAlgorithmThread.getResult())
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
                                        s_id = parseInt(rs.getString(1)) + 1;
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

        panel.startSetAlg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetAlgorithmFrame saframe = new SetAlgorithmFrame(teachers, competencesTeachers, events, competencesEvents, 0);
                saframe.setLocationRelativeTo(null);
                saframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });
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
