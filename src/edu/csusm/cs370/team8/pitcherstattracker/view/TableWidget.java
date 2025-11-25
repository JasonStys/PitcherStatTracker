package edu.csusm.cs370.team8.pitcherstattracker.view;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class TableWidget extends JScrollPane {
    TableWidgetModel tableModel;
    JTable table;
    JScrollPane scrollPane;

    public TableWidget(String[] colNames,  Object[][] data) {
        TableWidgetModel tableModel = new TableWidgetModel(colNames, data);
        table = new JTable(tableModel);
        this.setViewportView(table);
        table.setFillsViewportHeight(true);
        this.revalidate();
    }

    public void setColumnWidth(int col, int width) {
        table.getColumnModel().getColumn(col).setPreferredWidth(width);
    }
}

class TableWidgetModel extends AbstractTableModel {
    String[] columnNames;
    Object[][] rowData;

    TableWidgetModel(String[] colNames,  Object[][] data) {
        this.columnNames = colNames;
        this.rowData = data;
    }

    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

    public int getRowCount() {
        return rowData.length;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int row, int col) {
        return rowData[row][col];
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        rowData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}
