package com.netcracker.tc.types.standard.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.netcracker.tc.configurator.ui.EditorWidget;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.types.standard.RefType;

public class RefWidget implements EditorWidget, ActionListener
{
    private final JComboBox comboBox = new JComboBox();
    {
        comboBox.addActionListener(this);
    }
    private Property property;
    
    @Override
    public void setProperty(Property property)
    {
        this.property = property;
        RefType type = (RefType) property.getType();
        comboBox.setModel(new DefaultComboBoxModel(
                property.getScenario().getVisibleResults(type.getType()).toArray()));
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

}
