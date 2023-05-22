import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class AlgorithmPanel extends JPanel {
    JLabel teachersLabel, eventsLabel, competencesLabel, del1, del2, del3, timeLabel;
    JButton startAntAlg, startAnnealingAlg, startAllAlg, startSetAlg;
    JProgressBar progressBar;
    AlgorithmPanel() {
        Font font = Main.font;
        teachersLabel = new JLabel("Преподавателей: ");
//        teachersLabel.setPreferredSize(new Dimension(150, 20));
        eventsLabel = new JLabel("Мероприятий: ");
//        eventsLabel.setPreferredSize(new Dimension(150, 20));
        competencesLabel = new JLabel("Компетенций к развитию: ");
//        competencesLabel.setPreferredSize(new Dimension(160, 20));
        timeLabel = new JLabel("Время работы: ");
        teachersLabel.setFont(font);
        eventsLabel.setFont(font);
        competencesLabel.setFont(font);
        del1 = new JLabel();
        del1.setPreferredSize(new Dimension(600,20));
        del2 = new JLabel();
        del2.setPreferredSize(new Dimension(600,20));
        del3 = new JLabel();
        del3.setPreferredSize(new Dimension(600,20));
        add(teachersLabel);
        add(del1);
        add(eventsLabel);
        add(del2);
        add(competencesLabel);
        add(del3);
        startAntAlg = new JButton("Метод муравьиных колоний");
        startAntAlg.setPreferredSize(new Dimension(250, 50));
        startAntAlg.setFont(Main.font);
        add(startAntAlg);
        startAnnealingAlg = new JButton("Метод имитации отжига");
        startAnnealingAlg.setPreferredSize(new Dimension(250, 50));
        startAnnealingAlg.setFont(Main.font);
        add(startAnnealingAlg);
        startAllAlg = new JButton("Оба алгоритма");
        startAllAlg.setPreferredSize(new Dimension(450, 50));
        startAllAlg.setFont(Main.font);
        add(startAllAlg);
        startSetAlg = new JButton(new ImageIcon(Objects.requireNonNull(AlgorithmPanel.class.getResource("tune.png"))));
        startSetAlg.setPreferredSize(new Dimension(50, 50));
        startSetAlg.setFont(Main.font);
        add(startSetAlg);
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        add(progressBar);
        timeLabel.setVisible(false);
        add(timeLabel);
    }
}