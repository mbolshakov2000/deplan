import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

class OracleConnectionFrame extends JFrame {
    String curURL, curUser, curPassword;
    OracleConnectionFrame() {
        setSize(600, 250);
        OracleConnectionPanel panel = new OracleConnectionPanel();
        add(panel);
        setVisible(true);
        setTitle("Подключение к Oracle");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        panel.def.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.URLField.setText("jdbc:oracle:thin:@localhost:1521:xe");
                panel.userField.setText("personnel_department");
                panel.passwordField.setText("123");
            }
        });
        panel.check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                curURL = panel.URLField.getText();
                curUser = panel.userField.getText();
                curPassword = panel.passwordField.getText();
                panel.del.setVisible(false);
                panel.progressBar.setVisible(true);
                panel.success.setVisible(false);
                panel.fail.setVisible(false);

                OracleConnectionThread t= new OracleConnectionThread(curURL,curUser,curPassword);
                Thread t1 = new Thread(t);
                t1.start();
                Timer v = new Timer(10, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (t.getResult()!=0){
                            panel.progressBar.setVisible(false);
                            panel.success.setVisible(false);
                            panel.fail.setVisible(false);
                            if (t.getResult()==1)
                                panel.success.setVisible(true);
                            if (t.getResult()==2)
                                panel.fail.setVisible(true);
                            ((Timer) e.getSource()).stop();
                        }
                    }
                });
                v.start();
            }
        });
        panel.submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.URL = panel.URLField.getText();
                Main.USER = panel.userField.getText();
                Main.PASSWORD = panel.passwordField.getText();
                setVisible(false);
                dispose();
            }
        });
    }
}
