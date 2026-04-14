// Добавьте этот вспомогательный класс в проект
// ru/mephi/vikingdemo/gui/TableColumnAdjuster.java
package ru.mephi.vikingdemo.gui;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class TableColumnAdjuster implements PropertyChangeListener, ActionListener {
    private JTable table;
    private int spacing;
    private Map<TableColumn, Integer> columnSizes = new HashMap<>();

    public TableColumnAdjuster(JTable table) {
        this(table, 6);
    }

    public TableColumnAdjuster(JTable table, int spacing) {
        this.table = table;
        this.spacing = spacing;
    }

    public void adjustColumns() {
        TableColumnModel tcm = table.getColumnModel();

        for (int i = 0; i < tcm.getColumnCount(); i++) {
            adjustColumn(i);
        }
    }

    private void adjustColumn(int column) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);

        if (!tableColumn.getResizable()) return;

        int columnHeaderWidth = getColumnHeaderWidth(column);
        int columnDataWidth = getColumnDataWidth(column);
        int preferredWidth = Math.max(columnHeaderWidth, columnDataWidth);

        updateTableColumn(column, preferredWidth);
    }

    private int getColumnHeaderWidth(int column) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        Object value = tableColumn.getHeaderValue();
        FontMetrics fm = table.getFontMetrics(table.getTableHeader().getFont());

        return fm.stringWidth(value.toString()) + 14;
    }

    private int getColumnDataWidth(int column) {
        int preferredWidth = 0;
        int maxWidth = table.getColumnModel().getColumn(column).getMaxWidth();

        for (int row = 0; row < table.getRowCount(); row++) {
            preferredWidth = Math.max(preferredWidth, getCellWidth(row, column));

            if (preferredWidth >= maxWidth) break;
        }

        return preferredWidth;
    }

    private int getCellWidth(int row, int column) {
        Object value = table.getValueAt(row, column);
        if (value == null) return 0;

        FontMetrics fm = table.getFontMetrics(table.getFont());
        return fm.stringWidth(value.toString()) + 14;
    }

    private void updateTableColumn(int column, int width) {
        final TableColumn tableColumn = table.getColumnModel().getColumn(column);

        if (!tableColumn.getResizable()) return;

        width += spacing;

        columnSizes.put(tableColumn, width);
        tableColumn.setPreferredWidth(width);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("ancestor".equals(evt.getPropertyName())) {
            adjustColumns();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adjustColumns();
    }
}