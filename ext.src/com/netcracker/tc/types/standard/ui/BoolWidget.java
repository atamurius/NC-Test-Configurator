package com.netcracker.tc.types.standard.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import com.netcracker.tc.configurator.ui.EditorWidget;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.types.standard.BoolType;

public class BoolWidget implements EditorWidget, ActionListener
{
    private JCheckBox checkBox = new JCheckBox();
    private Property property;
    {
        checkBox.addActionListener(this);
    }
    
    @Override
    public void setProperty(Property property)
    {
        this.property = property;
        checkBox.setText(property.getDescription());
    }

    @Override
    public Component getComponent()
    {
        return checkBox;
    }

    @Override
    public void update()
    {
        checkBox.setSelected(property.getValue() == Boolean.TRUE);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        property.setValue(checkBox.isSelected());
    }

    @Override
    public Class<? extends Type> getType()
    {
        return BoolType.class;
    }
}
