package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SelectVikingDialog extends JDialog {
    private JComboBox<VikingComboItem> vikingCombo;
    private boolean confirmed = false;
    private Viking selectedViking;

    public SelectVikingDialog(JFrame parent, String title, List<Viking> vikings) {
        super(parent, title, true);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new FlowLayout());
        mainPanel.add(new JLabel("Выберите викинга:"));

        vikingCombo = new JComboBox<>();
        for (Viking v : vikings) {
            vikingCombo.addItem(new VikingComboItem(v));
        }
        mainPanel.add(vikingCombo);

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Выбрать");
        JButton cancelButton = new JButton("Отмена");

        okButton.addActionListener(e -> {
            VikingComboItem selected = (VikingComboItem) vikingCombo.getSelectedItem();
            if (selected != null) {
                selectedViking = selected.getViking();
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 150);
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Viking getSelectedViking() {
        return selectedViking;
    }

    private static class VikingComboItem {
        private final Viking viking;

        public VikingComboItem(Viking viking) {
            this.viking = viking;
        }

        public Viking getViking() {
            return viking;
        }

        @Override
        public String toString() {
            return viking.name() + " (ID: " + viking.id() + ")";
        }
    }
}