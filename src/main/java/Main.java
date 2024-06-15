import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.util.Date;

public class Main {
    static String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    static String USER = "personnel_department";
    static String PASSWORD = "123";
    static int closingWindowEmployee = 0;
    static int closingWindowEvent = 0;
    static int curTeach = -1;
    static int curEvent = -1;
    static int curCompetence = -1;
    static int firstTime = 0;
    static JMenuBar menuBar;
    static SecretKeySpec secretKey = new SecretKeySpec("Bar12345Bar12345".getBytes(), "AES");

    static Font font = new Font("Verdana", Font.PLAIN, 15);

    public static void main(String[] args) {

        // system UI
        LookAndFeel old = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Throwable ex) {
            old = null;
        }

        if (firstTime == 0) {
            firstTime = 1;
            try (FileReader reader = new FileReader("latest.ini")) {
                Scanner sc = new Scanner(reader);
                URL = sc.nextLine();
                USER = sc.nextLine();
                Cipher cipher2 = Cipher.getInstance("AES");
                cipher2.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] decryptCode = new byte[100];
                int i = 0;
                while (sc.hasNextByte()) {
                    decryptCode[i] = sc.nextByte();
                    i++;
                }
                byte[] decryptMe = new byte[i];
                for (int k = 0; k < i; k++) {
                    decryptMe[k] = decryptCode[k];
                }
                byte[] decryptText = cipher2.doFinal(decryptMe);
                String decryptTextString = "";
                for (byte b : decryptText) {
                    decryptTextString += (char) b;
                }
                PASSWORD = decryptTextString;
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException noSuchPaddingException) {
                noSuchPaddingException.printStackTrace();
            }
        }

        // new menuBar
        menuBar = new JMenuBar();

        // new Menu
        JMenu fileMenu = new JMenu("Файл");
        fileMenu.setFont(font);

        JMenuItem newItem = new JMenuItem("Новая конфигурация");
        newItem.setFont(font);
        fileMenu.add(newItem);


        JMenuItem dbItem = new JMenuItem("Настройка подключения к Oracle");
        dbItem.setFont(font);
        fileMenu.add(dbItem);


        JMenuItem openItem = new JMenuItem("Открыть конфигурацию");
        openItem.setFont(font);
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Сохранить конфигурацию");
        saveItem.setFont(font);
        fileMenu.add(saveItem);

        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
                Date date = new Date();
                File confFile = new File("conf-"+formatter.format(date)+".ini");
                try (FileWriter writer = new FileWriter(confFile, false)) {
                    Cipher cipher = Cipher.getInstance("AES");
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                    byte[] cipherText = cipher.doFinal(PASSWORD.getBytes());
                    String cipherTextString = "";
                    for (byte b : cipherText){
                        cipherTextString += b + " ";
                    }
                    String s = URL + "\n" + USER + "\n" + cipherTextString;
                    writer.write(s);
                    writer.flush();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                }
            }
        });

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.setFont(font);
        fileMenu.add(exitItem);

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu openMenu = new JMenu("Посмотреть");
        openMenu.setFont(font);

        JMenuItem openEmployees = new JMenuItem("Сотрудники");
        openEmployees.setFont(font);
        openMenu.add(openEmployees);

        openEmployees.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.curTeach = -1;
                EmployeesFrame tframe = new EmployeesFrame();
                tframe.setLocationRelativeTo(null);
                tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });

        JMenuItem openEvents = new JMenuItem("Мероприятия");
        openEvents.setFont(font);
        openMenu.add(openEvents);

        openEvents.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventsFrame tframe = new EventsFrame();
                tframe.setLocationRelativeTo(null);
                tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });

        JMenuItem openCompetences = new JMenuItem("Компетенции");
        openCompetences.setFont(font);
        openMenu.add(openCompetences);

        openCompetences.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CompetencesFrame cframe = new CompetencesFrame();
                cframe.setLocationRelativeTo(null);
                cframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });

        JMenu analysisMenu = new JMenu("Анализ");
        analysisMenu.setFont(font);

        JMenuItem newAnalysis = new JMenuItem("Новый анализ");
        newAnalysis.setFont(font);
        analysisMenu.add(newAnalysis);

        newAnalysis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AlgorithmFrame tframe = new AlgorithmFrame();
                tframe.setLocationRelativeTo(null);
                tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });

        JMenuItem openAnalysis = new JMenuItem("Открыть последний анализ");
        openAnalysis.setFont(font);
        analysisMenu.add(openAnalysis);

        menuBar.add(fileMenu);
        menuBar.add(openMenu);
        menuBar.add(analysisMenu);

        MainFrame frame = new MainFrame();
        frame.setJMenuBar(menuBar);
        frame.setLocationRelativeTo(null);

        openAnalysis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i;
                int result;

                Workbook book = null;
                try {
                    book = new HSSFWorkbook();
                    Sheet sheet = book.createSheet("График КПК");
                    Row row0 = sheet.createRow(0);
                    Cell[] date = new Cell[24];

                    CellStyle style = book.createCellStyle();
                    org.apache.poi.ss.usermodel.Font font = book.createFont();
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

                    ArrayList<Schedule> schedule = new ArrayList<>();
                    String id_employee="";
                    String last_name="";
                    String first_name="";
                    String patronymic="";
                    String id_event="";
                    String name_event="";
                    try {
                        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                        Connection c = DriverManager.getConnection(Main.URL, Main.USER, Main.PASSWORD);
                        Statement s = c.createStatement();
                        ResultSet rs = s.executeQuery("select em.employee_id, em.last_name, em.first_name, em.patronymic, " +
                                "ev.event_id, ev.name " +
                                "from schedule_element s JOIN employee em on s.employee_id = em.employee_id " +
                                "JOIN event ev on ev.event_id = s.event_id where s.schedule_id = (select max(schedule_id) from schedule) " +
                                "order by em.employee_id ");
                        while (rs.next()) {
                            id_employee = rs.getString("employee_id");
                            last_name = rs.getString("last_name");
                            first_name = rs.getString("first_name");
                            patronymic = rs.getString("patronymic");
                            id_event = rs.getString("event_id");
                            name_event = rs.getString("name");
                            String fio_employee = last_name+" "+first_name.charAt(0)+". "+patronymic.charAt(0)+".";
                            schedule.add(new Schedule(id_employee,fio_employee, id_event, name_event));
                        }
                    } catch (SQLException ex) {
                        if (ex.getErrorCode() == 12505) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Не найдена База Данных");

                        } else if (1017 == ex.getErrorCode()) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Неверное имя пользователя или пароль");
                        } else if (ex.getErrorCode() == 942) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Нет таблицы developed_competence или dev_level или competence");
                        } else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    ex.getSQLState() + '\n' + ex.getErrorCode() + '\n' + ex.getMessage());
                        }
                    } catch (StringIndexOutOfBoundsException er) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Измените данные БД");
                    }

                    int inOneColumn = schedule.size() / 12;
                    Row[] row = new Row[inOneColumn+1];
                    for (int g = 0; g <= inOneColumn; g++){
                        row[g] = sheet.createRow(g+1);
                    }
                    for (int g = 0; g <= inOneColumn; g++) {
                        Cell[] cname = new Cell[24];
                        for (i = 0; i < 24; i++) {
                            if (i % 2 == 0 && (g*12)+i/2 < schedule.size()) {
                                cname[i] = row[g].createCell(i);
                                cname[i].setCellValue(schedule.get((g*12)+i/2).id_employee + ". " +
                                        schedule.get((g*12)+i/2).name_employee);
                            }
                            else if (i % 2 == 1){
                                cname[i] = row[g].createCell(i);
                                cname[i].setCellValue(schedule.get((g*12)+i/2).id_event + ". " +
                                        schedule.get((g*12) + i/2).name_event);
                            }
                            else break;
                        }
                    }

                    sheet.autoSizeColumn(0);
                    for (i = 0; i < schedule.size(); i++) {
                        sheet.autoSizeColumn(i + 1);
                    }

                    FileOutputStream fos = new FileOutputStream("last.xls");
                    book.write(fos);
                    book.close();
                    Desktop desktop = null;
                    if (Desktop.isDesktopSupported()) {
                        desktop = Desktop.getDesktop();
                    }
                    try {
                        desktop.open(new File("last.xls"));
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    fos.close();

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
                Date date = new Date();
                File confFile = new File("latest.ini");
                try (FileWriter writer = new FileWriter(confFile, false)) {
                    Cipher cipher = Cipher.getInstance("AES");
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                    byte[] cipherText = cipher.doFinal(PASSWORD.getBytes());
                    String cipherTextString = "";
                    for (byte b : cipherText){
                        cipherTextString += b + " ";
                    }
                    String s = URL + "\n" + USER + "\n" + cipherTextString;
                    writer.write(s);
                    writer.flush();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                }
            }
        });

        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose();
                URL = "jdbc:oracle:thin:@localhost:1521:xe";
                USER = "personnel_department";
                PASSWORD = "123";
                main(args);
            }
        });

        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Конфигурационный файл(.ini)", "ini");
                fileChooser.setFileFilter(filter);
                fileChooser.setDialogTitle("Выбор директории");
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try (FileReader reader = new FileReader(fileChooser.getSelectedFile())) {
                        Scanner sc = new Scanner(reader);
                        URL = sc.nextLine();
                        USER = sc.nextLine();
                        Cipher cipher2 = Cipher.getInstance("AES");
                        cipher2.init(Cipher.DECRYPT_MODE, secretKey);
                        byte[] decryptCode = new byte[100];
                        int i = 0;
                        while (sc.hasNextByte()){
                            decryptCode[i] = sc.nextByte();
                            i++;
                        }
                        byte[] decryptMe = new byte[i];
                        for (int k = 0; k < i; k ++){
                            decryptMe[k] = decryptCode[k];
                        }
                        byte[] decryptText = cipher2.doFinal(decryptMe);
                        String decryptTextString = "";
                        for (byte b : decryptText){
                            decryptTextString += (char) b;
                        }
                        PASSWORD = decryptTextString;
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException noSuchPaddingException) {
                        noSuchPaddingException.printStackTrace();
                    }
                }
                frame.setVisible(false);
                frame.dispose();
                main(args);
            }
        });

        dbItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OracleConnectionFrame ocframe = new OracleConnectionFrame();
                ocframe.setLocationRelativeTo(null);
                ocframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                ocframe.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        frame.setVisible(false);
                        frame.dispose();
                        main(args);
                    }
                });
            }
        });
    }
}