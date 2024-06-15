import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

@Builder
@Data
@AllArgsConstructor
public class EmployeeCompetencePanel extends JPanel {
    JLabel nameLabel, name2Label, levelLabel, scoreLabel, del, del1, del2, del3;
    JTextField scoreField;
    JButton cancel, submit;
    JComboBox nameField, levelField;
    EmployeeCompetencePanel() {
        Font font = Main.font;
        del = new JLabel();
        del.setPreferredSize(new Dimension(900,30));
        del1 = new JLabel();
        del1.setPreferredSize(new Dimension(900,30));
        del2 = new JLabel();
        del2.setPreferredSize(new Dimension(900,10));
        del3 = new JLabel();
        del3.setPreferredSize(new Dimension(900,10));
        nameLabel = new JLabel("Наименование: ");
        nameLabel.setFont(font);
        nameLabel.setPreferredSize(new Dimension(200, 30));
        name2Label = new JLabel();
        name2Label.setFont(font);
        name2Label.setPreferredSize(new Dimension(700,30));
        nameField = new JComboBox(new String[]{"К1.1 - Умение предоставить информацию", "К1.2 - Умение найти информацию",
                "К1.3 - Умение сохранить информацию", "К2.1 - Умение работать с сетевыми документами и облачными технологиями",
                "К2.2 - Умение создать коммуникационные интернет-каналы передачи и обмена информацией",
                "К3.1 - Обработка текста", "К3.2 - Обработка звука", "К3.3 - Обработка видео", "К3.4 - Обработка графики",
                "К3.5 - Создание мультимедийного материала", "К3.6 - Создание интерактивного материала",
                "К3.7 - Создание гипертекстового материала", "К4.1 - Безопасность конфиденциальной информации",
                "К4.2 - Цифровой этикет", "К4.3 - Компетенции в области права", "К5.1 - Стандартная установка программного обеспечения",
                "К5.2 - Подключение стандартного (периферийного) оборудования", "К5.3 - Использовании стандартного программного обеспечения",
                "К5.4 - Управление и наполнение курса в системе дистанционного и смешенного обучения",
                "К5.5 - Работа с формализованными данными"});
        nameField.setFont(font);
        nameField.setPreferredSize(new Dimension(700,30));
        levelLabel = new JLabel("Уровень: ");
        levelLabel.setFont(font);
        levelLabel.setPreferredSize(new Dimension(200, 30));
        levelField = new JComboBox(new String[]{"Слабый уровень", "Недостаточный уровень", "Пороговый (начальный) уровень",
                "Базовый (средний) уровень", "Продвинутый уровень"});
        levelField.setFont(font);
        levelField.setPreferredSize(new Dimension(700,30));
        scoreLabel = new JLabel("Балл (0-100): ");
        scoreLabel.setFont(font);
        scoreLabel.setPreferredSize(new Dimension(200, 30));
        scoreField = new JTextField();
        scoreField.setFont(font);
        scoreField.setPreferredSize(new Dimension(700,30));
        cancel = new JButton("Отмена");
        submit = new JButton("Сохранить");
        submit.setFont(font);
        cancel.setFont(font);

        nameField.setSelectedIndex(0);
        levelField.setSelectedIndex(0);
        scoreField.setText("");
        add(nameLabel);
        add(nameField);

        add(levelLabel);
        add(levelField);
        add(scoreLabel);
        add(scoreField);
        add(del);
        add(cancel);
        add(submit);
    }
}