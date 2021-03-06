package com.netcracker.sova.types.bool;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Type;
import com.netcracker.sova.ui.EditorWidget;

public class BoolWidget implements EditorWidget, ActionListener
{
    private JCheckBox checkBox = new JCheckBox();
    private Parameter property;
    {
        checkBox.addActionListener(this);
    }
    
    @Override
    public void setProperty(Parameter property)
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
