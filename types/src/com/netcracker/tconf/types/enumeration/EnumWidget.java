package com.netcracker.tconf.types.enumeration;

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

import com.netcracker.tconf.model.Parameter;
import com.netcracker.tconf.model.Type;
import com.netcracker.tconf.ui.EditorWidget;
import com.netcracker.util.Label;

// TODO: null value?
public class EnumWidget implements EditorWidget, ActionListener
{
    private JComboBox comboBox = new JComboBox();
    {
        comboBox.addActionListener(this);
        comboBox.setRenderer(new EnumRenderer(comboBox.getRenderer()));
    }
    private Parameter property;
    
    @Override
    public void setProperty(Parameter property)
    {
        this.property = property;
        EnumType type = (EnumType) property.getType();
        DefaultComboBoxModel model = new DefaultComboBoxModel(type.getValues());
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
        return EnumType.class;
    }
}

class EnumRenderer implements ListCellRenderer
{
    private final ResourceBundle L = ResourceBundle.getBundle("types");
    
    private static final long serialVersionUID = 1L;
    private final ListCellRenderer renderer;

    public EnumRenderer(ListCellRenderer renderer)
    {
        this.renderer = renderer;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel com = (JLabel) 
                renderer.getListCellRendererComponent(list, value, index, isSelected,
                            cellHasFocus);
        
        Font font = com.getFont();
        
        if (value != null) {
            StringBuilder sb = new StringBuilder();
            for (String part : value.toString().split("_"))
                if (sb.length() == 0)
                    sb.append(part.substring(0,1).toUpperCase()).append(part.substring(1).toLowerCase());
                else
                    sb.append(" ").append(part.toLowerCase());
            
            com.setText(sb.toString());
            com.setFont(font.deriveFont(Font.PLAIN));
        }
        else {
            com.setText(Label.get(L, "ui.enum.none"));
            com.setFont(font.deriveFont(Font.ITALIC));
        }
        return com;
    }
}
