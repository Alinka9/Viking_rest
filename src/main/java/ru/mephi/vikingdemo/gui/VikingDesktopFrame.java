// ru/mephi/vikingdemo/gui/VikingDesktopFrame.java
package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel;
    private JTable vikingTable;  // Объявляем как поле класса

    public VikingDesktopFrame(VikingService vikingService) {
        this.vikingService = vikingService;
        this.tableModel = new VikingTableModel();

        setTitle("Viking Demo - Управление викингами");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1300, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo - CRUD Operations", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        // СОЗДАЁМ ТАБЛИЦУ
        vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);

        // ВЫЗЫВАЕМ НАСТРОЙКУ ШИРИНЫ СТОЛБЦОВ (ПОСЛЕ СОЗДАНИЯ ТАБЛИЦЫ)
        setupColumnWidths();

        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        // Панель с кнопками
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));

        JButton createRandomButton = new JButton("🎲 Create Random Viking");
        JButton addSpecificButton = new JButton("➕ Add Specific Viking");
        JButton editButton = new JButton("✏️ Edit Viking");
        JButton deleteButton = new JButton("🗑️ Delete Viking");

        createRandomButton.addActionListener(e -> onCreateRandomViking());
        addSpecificButton.addActionListener(e -> onAddSpecificViking());
        editButton.addActionListener(e -> onEditViking());
        deleteButton.addActionListener(e -> onDeleteViking());

        buttonPanel.add(createRandomButton);
        buttonPanel.add(addSpecificButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        JLabel infoLabel = new JLabel("Всего викингов: 0", SwingConstants.CENTER);
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.ITALIC));
        bottomPanel.add(infoLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        tableModel.addTableModelListener(e -> updateInfoLabel(infoLabel));
    }

    // ========== МЕТОД НАСТРОЙКИ ШИРИНЫ СТОЛБЦОВ ==========
    private void setupColumnWidths() {
        // Сначала настраиваем фиксированные столбцы
        TableColumn idColumn = vikingTable.getColumnModel().getColumn(0);
        idColumn.setPreferredWidth(60);
        idColumn.setMaxWidth(80);
        idColumn.setResizable(false);

        TableColumn nameColumn = vikingTable.getColumnModel().getColumn(1);
        nameColumn.setPreferredWidth(120);

        TableColumn ageColumn = vikingTable.getColumnModel().getColumn(2);
        ageColumn.setPreferredWidth(50);
        ageColumn.setMaxWidth(70);
        ageColumn.setResizable(false);

        TableColumn heightColumn = vikingTable.getColumnModel().getColumn(3);
        heightColumn.setPreferredWidth(70);
        heightColumn.setMaxWidth(90);
        heightColumn.setResizable(false);

        TableColumn hairColorColumn = vikingTable.getColumnModel().getColumn(4);
        hairColorColumn.setPreferredWidth(100);

        TableColumn beardStyleColumn = vikingTable.getColumnModel().getColumn(5);
        beardStyleColumn.setPreferredWidth(110);

        // Для Equipment используем автоматическое изменение
        TableColumn equipmentColumn = vikingTable.getColumnModel().getColumn(6);
        equipmentColumn.setPreferredWidth(350);
        equipmentColumn.setMinWidth(200);

        // Включаем авто-resize для последнего столбца
        vikingTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }
    // =====================================================

    private void updateInfoLabel(JLabel infoLabel) {
        infoLabel.setText("Всего викингов: " + tableModel.getRowCount());
    }

    private void onCreateRandomViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
        JOptionPane.showMessageDialog(this,
                "Создан случайный викинг: " + viking.name(),
                "Успех",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void onAddSpecificViking() {
        VikingDialog dialog = new VikingDialog(this, "Добавить викинга", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Viking newViking = dialog.getResultViking();
            Viking savedViking = vikingService.addViking(newViking);
            tableModel.addViking(savedViking);
            JOptionPane.showMessageDialog(this,
                    "Викинг " + savedViking.name() + " успешно добавлен! (ID: " + savedViking.id() + ")",
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onEditViking() {
        java.util.List<Viking> vikings = vikingService.findAll();
        if (vikings.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Нет викингов для редактирования",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        SelectVikingDialog selectDialog = new SelectVikingDialog(this, "Выберите викинга для редактирования", vikings);
        selectDialog.setVisible(true);

        if (selectDialog.isConfirmed()) {
            Viking toEdit = selectDialog.getSelectedViking();
            VikingDialog editDialog = new VikingDialog(this, "Редактировать викинга", toEdit);
            editDialog.setVisible(true);

            if (editDialog.isConfirmed()) {
                Viking updatedViking = editDialog.getResultViking();
                vikingService.updateViking(toEdit.id(), updatedViking);

                refreshTable();
                JOptionPane.showMessageDialog(this,
                        "Викинг " + updatedViking.name() + " успешно обновлен!",
                        "Успех",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void onDeleteViking() {
        java.util.List<Viking> vikings = vikingService.findAll();
        if (vikings.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Нет викингов для удаления",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        SelectVikingDialog selectDialog = new SelectVikingDialog(this, "Выберите викинга для удаления", vikings);
        selectDialog.setVisible(true);

        if (selectDialog.isConfirmed()) {
            Viking toDelete = selectDialog.getSelectedViking();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите удалить викинга " + toDelete.name() + " (ID: " + toDelete.id() + ")?",
                    "Подтверждение удаления",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                vikingService.deleteViking(toDelete.id());
                refreshTable();
                JOptionPane.showMessageDialog(this,
                        "Викинг " + toDelete.name() + " успешно удален!",
                        "Успех",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void refreshTable() {
        tableModel.clear();
        java.util.List<Viking> vikings = vikingService.findAll();
        for (Viking v : vikings) {
            tableModel.addViking(v);
        }
    }

    public void addNewViking(Viking viking) {
        tableModel.addViking(viking);
    }
}