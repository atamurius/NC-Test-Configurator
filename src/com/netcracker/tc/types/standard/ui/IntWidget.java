package com.netcracker.tc.types.standard.ui;

import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.netcracker.tc.configurator.ui.EditorWidget;
import com.netcracker.tc.model.Property;

public class IntWidget implements EditorWidget, ChangeListener
{
    private JSpinner spinner = new JSpinner();
    private Property property; 
    
    {
        spinner.addChangeListener(this);
    }
    
    @Override
    public void setProperty(Property property)
    {
        this.property = property;
    }

    @Override
    public Component getComponent()
    {
        return spinner;
    }

    @Override
    public void update()
    {
        spinner.setValue((Number) property.getValue());
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        property.setValue(spinner.getValue());
    }
}
