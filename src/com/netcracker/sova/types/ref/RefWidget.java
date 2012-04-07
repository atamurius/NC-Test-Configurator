package com.netcracker.sova.types.ref;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.netcracker.sova.model.Output;
import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Type;
import com.netcracker.sova.ui.EditorWidget;
import com.netcracker.util.Label;

public class RefWidget implements EditorWidget, ActionListener
{
    private final JComboBox comboBox = new JComboBox();
    {
        comboBox.addActionListener(this);
        comboBox.setRenderer(new RefRenderer(comboBox.getRenderer()));
    }
    private Parameter property;
    
    @Override
    public void setProperty(Parameter property)
    {
        this.property = property;
        RefType type = (RefType) property.getType();
        DefaultComboBoxModel model = new DefaultComboBoxModel(
                property.getScenario().getVisibleResults(type.getType()).toArray());
        if (! type.isRequired())
            model.insertElementAt(null, 0);
        comboBox.setModel(model);
    }

    @Override
    public Component getComponent()
    {
        return comboBox;
    }

    @Override
    public void update()
    {
        comboBox.setSelectedItem(property.getValue());
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        property.setValue(comboBox.getSelectedItem());
    }

    @Override
    public Class<? extends Type> getType()
    {
        return RefType.class;
    }

}

class RefRenderer implements ListCellRenderer
{
    private final ResourceBundle L = ResourceBundle.getBundle("ui");
    
    private final ListCellRenderer backed;

    public RefRenderer(ListCellRenderer backed)
    {
        this.backed = backed;
    }

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel com = (JLabel) backed.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        
        Font font = com.getFont();
        if (value == null) {
            com.setText(Label.get(L, "ui.ref.none"));
            com.setFont(font.deriveFont(Font.ITALIC));
        }
        else {
            Output out = (Output) value;
            com.setText(Label.get(L, "ui.ref.title", out.getTitle(), out.getScenario().getTitle()));
            com.setFont(font.deriveFont(Font.PLAIN));
        }
        
        return com;
    }
    
}










