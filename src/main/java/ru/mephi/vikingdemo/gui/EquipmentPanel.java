// ru/mephi/vikingdemo/gui/EquipmentPanel.java - обновленная версия
package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.EquipmentQuality;
import ru.mephi.vikingdemo.service.EquipmentFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class EquipmentPanel extends JPanel {
    private final EquipmentTableModel tableModel;
    private JTable equipmentTable;
    private JComboBox<String> nameCombo;
    private JComboBox<EquipmentQuality> qualityCombo;

    public EquipmentPanel() {
        setLayout(new BorderLayout(10, 10));

        tableModel = new EquipmentTableModel();
        equipmentTable = new JTable(tableModel);
        equipmentTable.setRowHeight(25);
        equipmentTable.setFillsViewportHeight(true);

        add(new JScrollPane(equipmentTable), BorderLayout.CENTER);

        // Панель добавления
        JPanel addPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Метка "Название:"
        gbc.gridx = 0;
        gbc.gridy = 0;
        addPanel.add(new JLabel("Название:"), gbc);

        // Комбобокс с предустановленными названиями из EquipmentFactory
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        Vector<String> equipmentNames = new Vector<>(EquipmentFactory.getEquipmentNames());
        nameCombo = new JComboBox<>(equipmentNames);
        nameCombo.setEditable(true); // Разрешаем ввод своего варианта
        nameCombo.setToolTipText("Выберите из списка или введите своё название");
        addPanel.add(nameCombo, gbc);

        // Метка "Качество:"
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        addPanel.add(new JLabel("Качество:"), gbc);

        // Комбобокс с качеством
        gbc.gridx = 1;
        gbc.gridy = 1;
        qualityCombo = new JComboBox<>(EquipmentQuality.values());
        addPanel.add(qualityCombo, gbc);

        // Кнопка добавления
        gbc.gridx = 2;
        gbc.gridy = 1;
        JButton addButton = new JButton("➕ Добавить");
        addButton.addActionListener(e -> addEquipment());
        addPanel.add(addButton, gbc);

        // Кнопка удаления
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        JButton removeButton = new JButton("🗑️ Удалить выбранное");
        removeButton.addActionListener(e -> removeSelectedEquipment());
        addPanel.add(removeButton, gbc);

        add(addPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(550, 280));
    }

    private void addEquipment() {
        // Получаем название (либо выбранное из списка, либо введённое)
        Object selectedItem = nameCombo.getSelectedItem();
        String name = (selectedItem != null) ? selectedItem.toString().trim() : "";

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Введите или выберите название снаряжения",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        EquipmentQuality quality = (EquipmentQuality) qualityCombo.getSelectedItem();

        tableModel.addEquipment(new EquipmentItem(name, quality));

        // Сбрасываем комбобокс, но оставляем возможность быстрого добавления следующего
        nameCombo.setSelectedIndex(-1);
        nameCombo.setEditable(true);
        nameCombo.setSelectedItem(null);

        // Не сбрасываем качество, чтобы можно было быстро добавлять несколько предметов
        // с одинаковым качеством
    }

    private void removeSelectedEquipment() {
        int selectedRow = equipmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeEquipment(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Выберите элемент для удаления (кликните по строке)",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public void setEquipment(List<EquipmentItem> equipment) {
        tableModel.setEquipment(equipment);
    }

    public List<EquipmentItem> getEquipment() {
        return tableModel.getEquipment();
    }

    private static class EquipmentTableModel extends AbstractTableModel {
        private final String[] columns = {"Название", "Качество"};
        private final List<EquipmentItem> equipment = new ArrayList<>();

        public void addEquipment(EquipmentItem item) {
            equipment.add(item);
            fireTableRowsInserted(equipment.size() - 1, equipment.size() - 1);
        }

        public void removeEquipment(int row) {
            equipment.remove(row);
            fireTableRowsDeleted(row, row);
        }

        public void setEquipment(List<EquipmentItem> items) {
            equipment.clear();
            equipment.addAll(items);
            fireTableDataChanged();
        }

        public List<EquipmentItem> getEquipment() {
            return new ArrayList<>(equipment);
        }

        @Override
        public int getRowCount() {
            return equipment.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            EquipmentItem item = equipment.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> item.name();
                case 1 -> item.quality();
                default -> "";
            };
        }
    }
}