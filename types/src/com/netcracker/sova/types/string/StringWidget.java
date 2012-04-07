package com.netcracker.sova.types.string;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Type;
import com.netcracker.sova.ui.EditorWidget;

public class StringWidget implements EditorWidget, ActionListener, FocusListener
{
    private Parameter property;
    
    private JTextField textField = new JTextField();
    {
        textField.addActionListener(this);
        textField.addFocusListener(this);
    }
    
    @Override
    public void setProperty(Parameter property)
    {
        this.property = property;
    }

    @Override
    public Component getComponent()
    {
        return textField;
    }

    @Override
    public void update()
    {
        textField.setText((String) property.getValue());
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        // Do nothing
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        actionPerformed(null);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        property.setValue(textField.getText());
    }

    @Override
    public Class<? extends Type> getType()
    {
        return StringType.class;
    }
}