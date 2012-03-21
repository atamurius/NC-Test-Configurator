package com.netcracker.tc.types.standard.map;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.netcracker.tc.configurator.ui.EditorWidget;
import com.netcracker.tc.model.Parameter;
import com.netcracker.tc.model.Type;
import com.netcracker.util.Label;
import com.netcracker.util.Label.Bundle;

// TODO: non-unique keys
public class MapWidget implements EditorWidget, TableModelListener
{
    private TableModel model = new TableModel();
    private JTable table = new JTable(model);
    
    @SuppressWarnings("serial")
    public MapWidget()
    {
        table.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "insert");
        table.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "delete");
        table.getActionMap().put("insert", new AbstractAction() {
            public void actionPerformed(ActionEvent arg0)
            {
                if (table.getCellEditor() != null)
                    table.getCellEditor().stopCellEditing();
                int index = 
                    table.getSelectedRow() == -1 
                        ? model.getRowCount() 
                        : table.getSelectedRow() + 1;
                model.insertAt(index);
                table.editCellAt(index, 0);
                if (table.getEditorComponent() != null)
                    table.getEditorComponent().requestFocus();
                table.changeSelection(index, 0, false, false);
            }
        });
        table.getActionMap().put("delete", new AbstractAction() {
            public void actionPerformed(ActionEvent arg0)
            {
                if (table.getSelectedRow() != -1)
                    model.remove(table.getSelectedRow());
                table.clearSelection();
            }
        });
        table.setRowSorter(null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setAutoCreateColumnsFromModel(true);
        table.setBackground(Color.white);
        table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, table.getFont().getSize()));
        table.setRowHeight((int) (table.getRowHeight()*1.5));
        table.setPreferredScrollableViewportSize(new Dimension(100, 80));
        model.addTableModelListener(this);
    }
    private Parameter parameter;
    private boolean isChanging = false;

    @Override
    public Class<? extends Type> getType()
    {
        return MapType.class;
    }

    @Override
    public void setProperty(Parameter property)
    {
        parameter = property;
    }

    @Override
    public Component getComponent()
    {
        JScrollPane scroll = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update()
    {
        isChanging = true;
        model.setData((Map<String, String>) parameter.getValue());
        isChanging = false;
    }

    @Override
    public void tableChanged(TableModelEvent e)
    {
        if (! isChanging)
            parameter.setValue(model.getData());
    }
}

@SuppressWarnings("serial")
class TableModel extends AbstractTableModel
{
    private final Bundle L = Label.getBundle(TableModel.class, "types");

    List<String> keys = new ArrayList<String>();
    List<String> values = new ArrayList<String>();

    @Override
    public String getColumnName(int column)
    {
        return L.get(column == 0 ? "ui.map.key" : "ui.map.value");
    }
    
    @Override
    public int getColumnCount()
    {
        return 2;
    }

    @Override
    public int getRowCount()
    {
        return keys.size();
    }

    @Override
    public Object getValueAt(int row, int col)
    {
        return (col == 0 ? keys : values).get(row);
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }
    
    @Override
    public void setValueAt(Object aValue, int row, int col)
    {
        (col == 0 ? keys : values).set(row, aValue.toString());
        fireTableCellUpdated(row, col);
    }
    
    public void insertAt(int row)
    {
        keys.add(row, ""+ (row + 1));
        values.add(row, "");
        fireTableRowsInserted(row, row);
    }
    
    public void remove(int row)
    {
        keys.remove(row);
        values.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    public void setData(Map<String,String> data)
    {
        keys = new ArrayList<String>(data.keySet());
        values = new ArrayList<String>(data.values());
        fireTableDataChanged();
    }
    
    public Map<String,String> getData()
    {
        Map<String,String> map = new LinkedHashMap<String, String>();
        for (int i = 0; i < getRowCount(); i++)
            map.put(keys.get(i), values.get(i));
        return map;
    }
}
