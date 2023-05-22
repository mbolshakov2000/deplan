import javax.swing.*;
import java.awt.*;

public class SetAlgorithmPanel extends JPanel {
    JLabel algLabel, countAntLabel, qLabel, pLabel, partLabel, tmaxLabel, tminLabel, coolLabel, del1, del2, del3, timeLabel;
    JTextField countAntField, qField, pField, partField, tmaxField, tminField, coolField;
    JButton startAntAlg, startAnnealingAlg, startAllAlg;
    JProgressBar progressBar;
    JComboBox chooseAlg;
    SetAlgorithmPanel(int alg) {
        Font font = Main.font;
        del1 = new JLabel();
        del1.setPreferredSize(new Dimension(600,20));
        del2 = new JLabel();
        del2.setPreferredSize(new Dimension(600,1));
        del3 = new JLabel();
        del3.setPreferredSize(new Dimension(600,20));
        algLabel = new JLabel("Алгоритм: ");
        algLabel.setFont(font);
        add(algLabel);
        chooseAlg = new JComboBox(new String[]{"Метод муравьиных колоний", "Метод имитации отжига", "Оба метода"});
        chooseAlg.setFont(font);
        chooseAlg.setSelectedIndex(alg);
        add(chooseAlg);
        add(del1);
        timeLabel = new JLabel("Время работы: ");

        if (alg == 0 || alg == 2){
            countAntLabel = new JLabel("Количество муравьев: ");
            countAntLabel.setFont(font);
            add(countAntLabel);
            countAntField = new JTextField();
            countAntField.setFont(font);
            countAntField.setPreferredSize(new Dimension(200,30));
            add(countAntField);
            qLabel = new JLabel("Коэффициент априорной предпочтительности: ");
            qLabel.setFont(font);
            add(qLabel);
            qField = new JTextField();
            qField.setFont(font);
            qField.setPreferredSize(new Dimension(100,30));
            add(qField);
            pLabel = new JLabel("Коэффициент феромона: ");
            pLabel.setFont(font);
            add(pLabel);
            pField = new JTextField();
            pField.setFont(font);
            pField.setPreferredSize(new Dimension(200,30));
            add(pField);
            partLabel = new JLabel("Изменение феромона(0.95, 0.8): ");
            partLabel.setFont(font);
            add(partLabel);
            partField = new JTextField();
            partField.setFont(font);
            partField.setPreferredSize(new Dimension(200,30));
            add(partField);
        }
        if (alg == 1 || alg == 2){
            tmaxLabel = new JLabel("Максимальная температура: ");
            tmaxLabel.setFont(font);
            add(tmaxLabel);
            tmaxField = new JTextField();
            tmaxField.setFont(font);
            tmaxField.setPreferredSize(new Dimension(200,30));
            add(tmaxField);
            tminLabel = new JLabel("Минимальная температура: ");
            tminLabel.setFont(font);
            add(tminLabel);
            tminField = new JTextField();
            tminField.setFont(font);
            tminField.setPreferredSize(new Dimension(250,30));
            add(tminField);
            coolLabel = new JLabel("Охлаждение: ");
            coolLabel.setFont(font);
            add(coolLabel);
            coolField = new JTextField();
            coolField.setFont(font);
            coolField.setPreferredSize(new Dimension(300,30));
            add(coolField);
        }
        if (alg == 0) {
            startAntAlg = new JButton("Начать");
            startAntAlg.setPreferredSize(new Dimension(250, 50));
            startAntAlg.setFont(Main.font);
            add(startAntAlg);
        }
        if (alg == 1) {
            startAnnealingAlg = new JButton("Начать");
            startAnnealingAlg.setPreferredSize(new Dimension(250, 50));
            startAnnealingAlg.setFont(Main.font);
            add(startAnnealingAlg);
        }
        if (alg == 2) {
            startAllAlg = new JButton("Начать");
            startAllAlg.setPreferredSize(new Dimension(250, 50));
            startAllAlg.setFont(Main.font);
            add(startAllAlg);
        }

        add(del2);
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        add(progressBar);
        timeLabel.setVisible(false);
        add(timeLabel);
    }
}