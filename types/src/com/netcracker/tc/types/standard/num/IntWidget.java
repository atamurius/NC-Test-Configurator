package com.netcracker.tc.types.standard.num;

import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.netcracker.tc.configurator.ui.EditorWidget;
import com.netcracker.tc.model.Parameter;
import com.netcracker.tc.model.Type;

public class IntWidget implements EditorWidget, ChangeListener
{
    protected JSpinner spinner = new JSpinner();
    private Parameter property; 

    public IntWidget()
    {
        spinner.addChangeListener(this);
    }
    
    @Override
    public void setProperty(Parameter property)
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

    @Override
    public Class<? extends Type> getType()
    {
        return IntType.class;
    }
}
