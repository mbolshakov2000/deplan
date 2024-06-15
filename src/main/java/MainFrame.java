import javax.swing.*;
import java.awt.event.*;

import static java.lang.Integer.parseInt;

class MainFrame extends JFrame {
    MainFrame() {
        setSize(1000, 600);
        MainPanel panel = new MainPanel();
        add(panel);
        setVisible(true);
        setTitle("Планирование развития ЦК");
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
                        MainFrame tframe = new MainFrame();
                        tframe.setJMenuBar(Main.menuBar);
                        tframe.setLocationRelativeTo(null);
                        tframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    }
                });
            }
        });

        panel.analysisButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                AlgorithmFrame answerFrame = new AlgorithmFrame();
                answerFrame.setLocationRelativeTo(null);
                answerFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });
    }
}
