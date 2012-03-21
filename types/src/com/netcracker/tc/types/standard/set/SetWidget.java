package com.netcracker.tc.types.standard.set;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.netcracker.tc.configurator.ui.EditorWidget;
import com.netcracker.tc.model.Parameter;
import com.netcracker.tc.model.Type;

public class SetWidget implements EditorWidget, ActionListener
{
    private JPanel checkboxes = new JPanel(new GridLayout(0, 1));
    private Map<Object,JCheckBox> values = new HashMap<Object, JCheckBox>();
    private Parameter property;
    
    @Override
    public void setProperty(Parameter property)
    {
        this.property = property;
        checkboxes.removeAll();
        values.clear();
        SetType type = (SetType) property.getType();
        for (Object opt : type.getValues()) {
            JCheckBox checkBox = new JCheckBox(opt.toString());
            values.put(opt, checkBox);
            checkBox.addActionListener(this);
            checkboxes.add(checkBox);
        }
    }

    @Override
    public Component getComponent()
    {
        return checkboxes;
    }

    @Override
    public void update()
    {
        Set<?> values = (Set<?>) property.getValue();
        for (Map.Entry<Object, JCheckBox> entry : this.values.entrySet())
            entry.getValue().setSelected(values.contains(entry.getKey()));
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        Set<Object> values = new HashSet<Object>();
        for (Map.Entry<Object, JCheckBox> entry : this.values.entrySet())
            if (entry.getValue().isSelected())
                values.add(entry.getKey());
        property.setValue(values);
    }

    @Override
    public Class<? extends Type> getType()
    {
        return SetType.class;
    }

}
