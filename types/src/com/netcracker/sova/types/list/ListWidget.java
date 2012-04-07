package com.netcracker.sova.types.list;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Type;
import com.netcracker.sova.ui.EditorWidget;

public class ListWidget implements EditorWidget, DocumentListener, FocusListener
{
    private final JTextArea textArea = new JTextArea();
    {
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, textArea.getFont().getSize()));
        textArea.setRows(8);
        textArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        textArea.getDocument().addDocumentListener(this);
        textArea.addFocusListener(this);
    }
    private Parameter property;
    private boolean isSelfChanging = false;

    @Override
    public Class<? extends Type> getType()
    {
        return ListType.class;
    }

    @Override
    public void setProperty(Parameter property)
    {
        this.property = property;
        if (! property.getDescription().isEmpty())
            textArea.setToolTipText(property.getDescription());
    }

    @Override
    public Component getComponent()
    {
        return new JScrollPane(textArea);
    }

    @Override
    public void update()
    {
        if (! isSelfChanging) {
            List<?> list = (List<?>) property.getValue();
            StringBuilder sb = new StringBuilder();
            for (Object obj : list)
                sb.append(sb.length() == 0 ? "" : "\n").append(obj);
            textArea.setText(sb.toString());
        }
    }
    
    private void updateProperty()
    {
        isSelfChanging = true;
        if (textArea.getText().isEmpty())
            property.setValue(Collections.EMPTY_LIST);
        else
            property.setValue(Arrays.asList(textArea.getText().trim().split("\n")));
        isSelfChanging = false;
    }

    // --- listeners ---

    @Override
    public void changedUpdate(DocumentEvent arg0)
    {
        updateProperty();
    }

    @Override
    public void insertUpdate(DocumentEvent arg0)
    {
        updateProperty();
    }

    @Override
    public void removeUpdate(DocumentEvent arg0)
    {
        updateProperty();
    }

    @Override
    public void focusGained(FocusEvent arg0)
    {
        // Do nothing here.
    }

    @Override
    public void focusLost(FocusEvent arg0)
    {
        updateProperty();
    }

}
