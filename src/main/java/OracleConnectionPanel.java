import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Builder
@Data
@AllArgsConstructor
public class OracleConnectionPanel extends JPanel {
    JLabel URLLabel, userLabel, passwordLabel, success, fail, del;
    JTextField URLField, userField, passwordField;
    JButton cancel, submit, check, def;
    JProgressBar progressBar;
    OracleConnectionPanel() {
        Font font = Main.font;
        del = new JLabel();
        del.setPreferredSize(new Dimension(600,30));
        success = new JLabel("Подключение удачно");
        success.setPreferredSize(new Dimension(500,30));
        success.setFont(font);
        success.setForeground(new Color(0, 161, 40));
        fail = new JLabel("Подключение неудачно");
        fail.setPreferredSize(new Dimension(500,30));
        fail.setFont(font);
        fail.setForeground(Color.RED);
        URLLabel = new JLabel("URL: ");
        URLLabel.setFont(font);
        URLLabel.setPreferredSize(new Dimension(100, 30));
        URLField = new JTextField();
        URLField.setText(Main.URL);
        URLField.setFont(font);
        URLField.setPreferredSize(new Dimension(400,30));
        userLabel = new JLabel("Username: ");
        userLabel.setFont(font);
        userLabel.setPreferredSize(new Dimension(100, 30));
        userField = new JTextField();
        userField.setText(Main.USER);
        userField.setFont(font);
        userField.setPreferredSize(new Dimension(400,30));
        passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(font);
        passwordLabel.setPreferredSize(new Dimension(100, 30));
        passwordField = new JTextField();
        passwordField.setText(Main.PASSWORD);
        passwordField.setFont(font);
        passwordField.setPreferredSize(new Dimension(400,30));
        cancel = new JButton("Отмена");
        def = new JButton("Сбросить");
        check = new JButton("Проверить");
        submit = new JButton("Сохранить");
        check.setFont(font);
        def.setFont(font);
        submit.setFont(font);
        cancel.setFont(font);
        add(URLLabel);
        add(URLField);
        add(userLabel);
        add(userField);
        add(passwordLabel);
        add(passwordField);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(600,30));
        progressBar.setVisible(false);
        success.setVisible(false);
        fail.setVisible(false);
        add(del);
        add(progressBar);
        add(success);
        add(fail);
        add(cancel);
        add(def);
        add(check);
        add(submit);
    }
}