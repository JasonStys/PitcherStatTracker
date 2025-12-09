package edu.csusm.cs370.team8.pitcherstattracker.view;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

// Devon wrote this for Aidan to use. Widget JScrollPane that displays a table when given data.
public class TableWidget extends JScrollPane {
    TableWidgetModel tableModel;
    JTable table;
    JScrollPane scrollPane;

    // Constructor takes column names string array and a 2-dimensional array for object data.
    public TableWidget(String[] colNames,  Object[][] data) {
        TableWidgetModel tableModel = new TableWidgetModel(colNames, data);
        table = new JTable(tableModel);
        this.setViewportView(table);
        table.setFillsViewportHeight(true);
        this.revalidate();
    }

    // Change the preferred width of columns.
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

    // Override JTable so we can't edit the values in the cells.
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        rowData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}
