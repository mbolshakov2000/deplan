import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import java.sql.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class MyPanel extends JPanel {
    JLabel openLabel, teachersLabel, coursesLabel, competencesLabel, dbLabel, analysisLabel, del1, del2, del3, del4, del5,
    openButton, teachersButton, coursesButton, competencesButton, dbButton, analysisButton, del6, del7, del8, del9, del10, del11;
    JFileChooser fileChooser;

    MyPanel() {
        del1 = new JLabel();
        del1.setPreferredSize(new Dimension(1200,80));
        del2 = new JLabel();
        del2.setPreferredSize(new Dimension(150, 20));
        del3 = new JLabel();
        del3.setPreferredSize(new Dimension(250, 20));
        del4 = new JLabel();
        del4.setPreferredSize(new Dimension(250, 20));
        del5 = new JLabel();
        del5.setPreferredSize(new Dimension(150, 20));
        del6 = new JLabel();
        del6.setPreferredSize(new Dimension(30, 20));
        del7 = new JLabel();
        del7.setPreferredSize(new Dimension(110, 20));
        del8 = new JLabel();
        del8.setPreferredSize(new Dimension(80,20));
        del9 = new JLabel();
        del9.setPreferredSize(new Dimension(1,20));
        del10 = new JLabel();
        del10.setPreferredSize(new Dimension(1200,20));
        del11 = new JLabel();
        del11.setPreferredSize(new Dimension(1200,1));
        Font font = Main.font;
        openLabel = new JLabel("Открыть конфигурацию");
        openLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        teachersLabel = new JLabel("Список сотрудников");
        teachersLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        competencesLabel = new JLabel("Список компетенций");
        competencesLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        coursesLabel = new JLabel("Список курсов");
        coursesLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection c = DriverManager.getConnection(Main.URL,Main.USER,Main.PASSWORD);
            dbLabel = new JLabel("Подключение к базе данных установлено");
            dbLabel.setPreferredSize(new Dimension(900,30));
            dbLabel.setFont(font);
            dbLabel.setForeground(new Color(0, 161, 40));
        } catch (SQLException ex) {
            dbLabel = new JLabel("Подключение к базе данных не установлено");
            dbLabel.setPreferredSize(new Dimension(900,30));
            dbLabel.setFont(font);
            dbLabel.setForeground(Color.RED);
        }
        analysisLabel = new JLabel("Выполнить анализ");
        analysisLabel.setFont(new Font("Verdana", Font.PLAIN, 20));

        try {
            BufferedImage myPicture3 = ImageIO.read(Objects.requireNonNull(AlgorithmPanel.class.getResource("database.png")));
            dbButton = new JLabel(new ImageIcon(myPicture3));
            add(dbButton);
            add(dbLabel);
            dbButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        }
        catch(IOException t) {
            System.out.println(t);
        }
        add(del1);

        if (Objects.equals(dbLabel.getText(), "Подключение к базе данных не установлено")) {
            try {
                BufferedImage myPicture5 = ImageIO.read(Objects.requireNonNull(AlgorithmPanel.class.getResource("folder-open-outline.png")));
                openButton = new JLabel(new ImageIcon(myPicture5));
                add(openButton);
                add(openLabel);
                openButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                openButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
                        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                "Конфигурационный файл(.ini)", "ini");
                        fileChooser.setFileFilter(filter);
                        fileChooser.setDialogTitle("Выбор директории");
                        int result = fileChooser.showOpenDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            try (FileReader reader = new FileReader(fileChooser.getSelectedFile())) {
                                Scanner sc = new Scanner(reader);
                                Main.URL = sc.nextLine();
                                Main.USER = sc.nextLine();
                                Cipher cipher2 = Cipher.getInstance("AES");
                                cipher2.init(Cipher.DECRYPT_MODE, Main.secretKey);
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
                                Main.PASSWORD = decryptTextString;
                            } catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException noSuchPaddingException) {
                                noSuchPaddingException.printStackTrace();
                            }
                        }
                    }
                });
            } catch (IOException t) {
                System.out.println(t);
            }
        }
        else {
            add(del2);
            try {
                BufferedImage myPicture1 = ImageIO.read(Objects.requireNonNull(AlgorithmPanel.class.getResource("calendar-search.png")));
                coursesButton = new JLabel(new ImageIcon(myPicture1));
                add(coursesButton);
                add(del3);
                coursesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                coursesButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        EventsFrame answerFrame = new EventsFrame();
                        answerFrame.setLocationRelativeTo(null);
                        answerFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    }
                });
            } catch (IOException t) {
                System.out.println(t);
            }

            try {
                BufferedImage myPicture4 = ImageIO.read(Objects.requireNonNull(AlgorithmPanel.class.getResource("file-document-outline.png")));
                competencesButton = new JLabel(new ImageIcon(myPicture4));
                add(competencesButton);
                add(del4);
                competencesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                competencesButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        CompetencesFrame answerFrame = new CompetencesFrame();
                        answerFrame.setLocationRelativeTo(null);
                        answerFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    }
                });
            } catch (IOException t) {
                System.out.println(t);
            }


            try {
                BufferedImage myPicture6 = ImageIO.read(Objects.requireNonNull(AlgorithmPanel.class.getResource("human-queue.png")));
                teachersButton = new JLabel(new ImageIcon(myPicture6));
                add(teachersButton);
                add(del5);
                teachersButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                teachersButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        TeachersFrame answerFrame = new TeachersFrame();
                        answerFrame.setLocationRelativeTo(null);
                        answerFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    }
                });
            } catch (IOException t) {
                System.out.println(t);
            }

            add(del6);
            add(coursesLabel);
            add(del7);
            add(competencesLabel);
            add(del8);
            add(teachersLabel);
            add(del9);
            add(del10);

            try {
                BufferedImage myPicture2 = ImageIO.read(Objects.requireNonNull(AlgorithmPanel.class.getResource("calendar-start.png")));
                analysisButton = new JLabel(new ImageIcon(myPicture2));
                add(analysisButton);
                add(del11);
                add(analysisLabel);
                analysisButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                analysisButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        AlgorithmFrame answerFrame = new AlgorithmFrame();
                        answerFrame.setLocationRelativeTo(null);
                        answerFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    }
                });
            } catch (IOException t) {
                System.out.println(t);
            }
        }
    }
}