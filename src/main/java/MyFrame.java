import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

class MyFrame extends JFrame {
    MyFrame() {
        setSize(1000, 600);
        MyPanel panel = new MyPanel();
        add(panel);
        setVisible(true);
        setTitle("Планирование развития ЦК сотрудников ООВО");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.dbButton.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                OracleConnectionFrame answerFrame = new OracleConnectionFrame();
                answerFrame.setLocationRelativeTo(null);
                answerFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                answerFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        setVisible(false);
                        dispose();
                        MyFrame tframe = new MyFrame();
                        tframe.setJMenuBar(Main.menuBar);
                        tframe.setLocationRelativeTo(null);
                        tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    }
                });
            }
        });

        panel.analysisButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                panel.progressBar.setVisible(true);
                panel.progressBar.setIndeterminate(true);
                PredictThread predictThread = new PredictThread();
                Thread predThread = new Thread(predictThread);
                predThread.start();
                Timer v = new Timer(10, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (predictThread.getResult()==1){
                            panel.progressBar.setVisible(false);
                            ((Timer) e.getSource()).stop();
                        }
                    }
                });
                v.start();
                AlgorithmFrame answerFrame = new AlgorithmFrame();
                answerFrame.setLocationRelativeTo(null);
                answerFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });
    }
}
