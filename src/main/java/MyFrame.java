import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

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
    }
}
