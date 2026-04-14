// ru/mephi/vikingdemo/gui/VikingDialog.java
package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.*;

import javax.swing.*;
import java.awt.*;

public class VikingDialog extends JDialog {
    private JTextField nameField;
    private JSpinner ageSpinner;
    private JSpinner heightSpinner;
    private JComboBox<HairColor> hairColorCombo;
    private JComboBox<BeardStyle> beardStyleCombo;
    private EquipmentPanel equipmentPanel;
    private boolean confirmed = false;
    private Viking resultViking;

    public VikingDialog(JFrame parent, String title, Viking editViking) {
        super(parent, title, true);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Имя
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Имя:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        // Возраст
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Возраст:"), gbc);
        gbc.gridx = 1;
        ageSpinner = new JSpinner(new SpinnerNumberModel(30, 18, 100, 1));
        formPanel.add(ageSpinner, gbc);

        // Рост
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Рост (см):"), gbc);
        gbc.gridx = 1;
        heightSpinner = new JSpinner(new SpinnerNumberModel(180, 100, 250, 1));
        formPanel.add(heightSpinner, gbc);

        // Цвет волос
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Цвет волос:"), gbc);
        gbc.gridx = 1;
        hairColorCombo = new JComboBox<>(HairColor.values());
        formPanel.add(hairColorCombo, gbc);

        // Стиль бороды
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Стиль бороды:"), gbc);
        gbc.gridx = 1;
        beardStyleCombo = new JComboBox<>(BeardStyle.values());
        formPanel.add(beardStyleCombo, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Панель снаряжения
        equipmentPanel = new EquipmentPanel();
        add(equipmentPanel, BorderLayout.CENTER);

        // Кнопки
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            if (validateInput()) {
                resultViking = createVikingFromForm(editViking);
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Заполняем форму если редактируем
        if (editViking != null) {
            nameField.setText(editViking.name());
            ageSpinner.setValue(editViking.age());
            heightSpinner.setValue(editViking.heightCm());
            hairColorCombo.setSelectedItem(editViking.hairColor());
            beardStyleCombo.setSelectedItem(editViking.beardStyle());
            equipmentPanel.setEquipment(editViking.equipment());
        }

        setSize(550, 500);
        setLocationRelativeTo(parent);
    }

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Имя не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private Viking createVikingFromForm(Viking existingViking) {
        String name = nameField.getText().trim();
        int age = (Integer) ageSpinner.getValue();
        int height = (Integer) heightSpinner.getValue();
        HairColor hairColor = (HairColor) hairColorCombo.getSelectedItem();
        BeardStyle beardStyle = (BeardStyle) beardStyleCombo.getSelectedItem();
        java.util.List<EquipmentItem> equipment = equipmentPanel.getEquipment();

        if (existingViking != null) {
            return existingViking.withUpdatedFields(name, age, height, hairColor, beardStyle, equipment);
        } else {
            return new Viking(name, age, height, hairColor, beardStyle, equipment);
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Viking getResultViking() {
        return resultViking;
    }
}