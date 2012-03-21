package com.netcracker.tc.types.standard.enumeration;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.netcracker.tc.configurator.ui.EditorWidget;
import com.netcracker.tc.model.Parameter;
import com.netcracker.tc.model.Type;

// TODO: null value?
public class EnumWidget implements EditorWidget, ActionListener
{
    private JComboBox comboBox = new JComboBox();
    {
        comboBox.addActionListener(this);
    }
    private Parameter property;
    
    @Override
    public void setProperty(Parameter property)
    {
        this.property = property;
        EnumType type = (EnumType) property.getType();
        comboBox.setModel(new DefaultComboBoxModel(type.getValues()));
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
        return EnumType.class;
    }
}
