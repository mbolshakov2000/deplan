import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.*;
//import org.jdatepicker.impl.JDatePanelImpl;
//import org.jdatepicker.impl.JDatePickerImpl;
//import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

@Builder
@Data
@AllArgsConstructor
public class EventDetailPanel extends JPanel {
    JLabel nameLabel, name1Label, typeLabel, descriptionLabel, dateStartLabel, dateEndLabel, quantityLabel, costLabel, del, del1, del2, del3, delc, delb, deld1, deld2,compLabel;
    JTextField nameField, typeField, quantityField, costField;
    JTextArea descriptionField;
    JScrollPane scroll1;
    JDatePickerImpl dateStartField, dateEndField;
    JButton cancel, submit;
    JTable competenceTable;
    JButton competenceAdd, competenceChange, competenceDelete;
    EventDetailPanel(Event e) {
        Font font = Main.font;
        del = new JLabel();
        del.setPreferredSize(new Dimension(900,30));
        del1 = new JLabel();
        del1.setPreferredSize(new Dimension(900,30));
        del2 = new JLabel();
        del2.setPreferredSize(new Dimension(900,10));
        del3 = new JLabel();
        del3.setPreferredSize(new Dimension(900,10));
        delc = new JLabel();
        delc.setPreferredSize(new Dimension(700,30));
        delb = new JLabel();
        delb.setPreferredSize(new Dimension(200,40));
        deld1 = new JLabel();
        deld1.setPreferredSize(new Dimension(500,30));
        deld2 = new JLabel();
        deld2.setPreferredSize(new Dimension(500,40));
        nameLabel = new JLabel("Название: ");
        nameLabel.setFont(font);
        nameLabel.setPreferredSize(new Dimension(200, 30));
        typeLabel = new JLabel("Тип: ");
        typeLabel.setFont(font);
        typeLabel.setPreferredSize(new Dimension(200, 30));
        descriptionLabel = new JLabel("Описание: ");
        descriptionLabel.setFont(font);
        descriptionLabel.setPreferredSize(new Dimension(200, 30));
        dateStartLabel = new JLabel("Дата начала: ");
        dateStartLabel.setFont(font);
        dateStartLabel.setPreferredSize(new Dimension(200, 30));
        dateEndLabel = new JLabel("Дата окончания: ");
        dateEndLabel.setFont(font);
        dateEndLabel.setPreferredSize(new Dimension(200, 30));
        name1Label = new JLabel();
        name1Label.setFont(font);
        name1Label.setPreferredSize(new Dimension(700,30));
        quantityLabel = new JLabel("Количество: ");
        quantityLabel.setFont(font);
        quantityLabel.setPreferredSize(new Dimension(200,30));
        costLabel = new JLabel("Стоимость: ");
        costLabel.setFont(font);
        costLabel.setPreferredSize(new Dimension(200,30));
        nameField = new JTextField();
        nameField.setFont(font);
        nameField.setPreferredSize(new Dimension(700,30));
        typeField = new JTextField();
        typeField.setFont(font);
        typeField.setPreferredSize(new Dimension(700,30));
        descriptionField = new JTextArea();
        descriptionField.setFont(font);
        descriptionField.setPreferredSize(new Dimension(700,100));
        descriptionField.setLineWrap(true);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        dateStartField = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        UtilDateModel model2 = new UtilDateModel();
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p);
        dateEndField = new JDatePickerImpl(datePanel2, new DateLabelFormatter());

        quantityField = new JTextField();
        quantityField.setFont(font);
        quantityField.setPreferredSize(new Dimension(700,30));
        costField = new JTextField();
        costField.setFont(font);
        costField.setPreferredSize(new Dimension(700,30));
        cancel = new JButton("Назад");
        submit = new JButton("Сохранить");
        submit.setFont(font);
        cancel.setFont(font);

        try {
            name1Label.setText(e.name);
            typeField.setText(e.type);
            descriptionField.setText(e.description);
            if (!e.date_start.equals("")) {
                int day = Integer.parseInt(e.date_start.substring(8, 10));
                int month = Integer.parseInt(e.date_start.substring(5, 7)) - 1;
                int year = Integer.parseInt(e.date_start.substring(0, 4));
                dateStartField.getModel().setDay(day);
                dateStartField.getModel().setMonth(month);
                dateStartField.getModel().setYear(year);
                dateStartField.getModel().setSelected(true);
            }
            if (!e.date_end.equals("")) {
                int day = Integer.parseInt(e.date_end.substring(8, 10));
                int month = Integer.parseInt(e.date_end.substring(5, 7)) - 1;
                int year = Integer.parseInt(e.date_end.substring(0, 4));
                dateEndField.getModel().setDay(day);
                dateEndField.getModel().setMonth(month);
                dateEndField.getModel().setYear(year);
                dateEndField.getModel().setSelected(true);
            }
            quantityField.setText(e.quantity);
            costField.setText(e.cost);
            add(nameLabel);
            add(name1Label);
        }
        catch (NullPointerException er){
            nameField.setText("");
            typeField.setText("");
            descriptionField.setText("");
            quantityField.setText("");
            costField.setText("");
            add(nameLabel);
            add(nameField);
        }

        add(typeLabel);
        add(typeField);
        add(descriptionLabel);
        add(descriptionField);
        add(dateStartLabel);
        add(dateStartField);
        add(deld1);
        add(dateEndLabel);
        add(dateEndField);
        add(deld2);
        add(quantityLabel);
        add(quantityField);
        add(costLabel);
        add(costField);
        add(del1);
        add(del);
        add(cancel);
        add(submit);
    }
    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private String datePattern = "dd-MM-yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }

    }
}