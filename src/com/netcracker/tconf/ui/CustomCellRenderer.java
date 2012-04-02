package com.netcracker.tconf.ui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import com.netcracker.tconf.model.Scenario;
import com.netcracker.tconf.model.Test;

public class CustomCellRenderer implements TreeCellRenderer
{
    private final Icon warningIcon = new ImageIcon(CustomCellRenderer.class.getResource("images/warning.png"));

    private TreeCellRenderer renderer;
    
    public CustomCellRenderer(TreeCellRenderer cellRenderer)
    {
        renderer = cellRenderer;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        JLabel label = (JLabel) renderer.getTreeCellRendererComponent(
                tree, value, selected, expanded, leaf, row, hasFocus);
        
        if (! isValid(value))
            label.setIcon(warningIcon);
        else if (leaf)
            label.setIcon(null);
            
        return label;
    }

    private boolean isValid(Object value)
    {
        if (value instanceof Scenario)
            return ((Scenario) value).isValid();
        if (value instanceof Test)
            return ((Test) value).isValid();
        
        return true;
    }
}
