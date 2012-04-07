package com.netcracker.sova.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import com.netcracker.sova.model.Observer;
import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Scenario;

// TODO: one editor for one scenario or reset it for current edited scenario?
public class ScenarioEditor extends JScrollPane implements Observer 
{
    private static final long serialVersionUID = 1L;
    
    private final class Field
    {
        JLabel errorField = new JLabel();
        Parameter property;
        EditorWidget editor;

        Field(Parameter property)
        {
            this.property = property;
            editor = registry.editorFor(property);
            errorField.setForeground(Color.red);
            errorField.setFont(errorField.getFont().deriveFont(Font.ITALIC));
            errorField.setVisible(false);
        }
        
        void update()
        {
            editor.update();
            validate();
        }
        
        void validate()
        {
            String error = property.getError();
            if (error == null)
                errorField.setVisible(false);
            else {
                errorField.setVisible(true);
                errorField.setText(error + 
                        (property.getDescription().isEmpty() ? "" : ": "+ property.getDescription()));
            }
        }
    }
    
    private final JPanel panel;
    private final Map<Parameter,Field> fields = new HashMap<Parameter,Field>();
    private final EditorRegistry registry;
    private Scenario scenario;
    private JLabel header = new JLabel();

    public ScenarioEditor(EditorRegistry registry)
    {
        this.registry = registry;
        panel = new JPanel(new GridBagLayout());
        setViewportView(panel);
        setViewportBorder(null);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 15, 5));
        
        Font f = header.getFont();
        header.setFont(f.deriveFont(Font.BOLD, f.getSize() * 1.3f));
    }
    
    public void reset()
    {
        begin();
        end();
        scenario = null;
    }
    
    public void reset(Scenario scenario)
    {
        begin();
        this.scenario = scenario;
        
        addHeader();
        
        scenario.getParent().getParent().addObserver(this);
        for (Parameter prop : scenario.parameters().values()) {
            Field field = new Field(prop);
            fields.put(prop, field);
            addField(field);
            field.update();
        }
        end();
    }
    
    private void addHeader()
    {
        updateHeader();

        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 15, 5, 15);
        panel.add(header, c);
    }

    private void updateHeader()
    {
        header.setText(scenario.getParent().getTitle() + ": " + scenario.getTitle());
    }

    private void begin()
    {
        panel.setVisible(false);
        panel.removeAll();
        fields.clear();
        if (scenario != null) {
            scenario.getParent().getParent().deleteObserver(this);
        }
    }
    
    private void addField(Field f)
    {
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 15, 5, 15);
        
        if (panel.getComponentCount() > 0)
            panel.add(new JSeparator(), c);
        
        c.gridx = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        JLabel label = new JLabel(f.property.getTitle());
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        panel.add(label, c);
        if (! f.property.getDescription().isEmpty())
            label.setToolTipText(f.property.getDescription());
        
        c.gridx = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        
        panel.add(f.editor.getComponent(), c);
        
        c.insets = new Insets(0, 15, 0, 15);
        panel.add(f.errorField, c);
    }

    private void end()
    {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weighty = 1;
        panel.add(new JLabel(), c);
        panel.setVisible(true);
    }

    @Override
    public void update(Object source, Event event)
    {
        if (event == Event.CHANGED && fields.containsKey(source))
            fields.get(source).update();
        
        if (event == Event.CHANGED && source == scenario)
            updateHeader();
    }
}
