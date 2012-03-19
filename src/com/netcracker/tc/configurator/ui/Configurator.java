package com.netcracker.tc.configurator.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.netcracker.tc.configurator.data.AnnotationSchemaReader;
import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.configurator.data.ConfigurationReader;
import com.netcracker.tc.configurator.data.ConfigurationWriter;
import com.netcracker.tc.configurator.data.xml.XmlTestGroupReader;
import com.netcracker.tc.configurator.data.xml.XmlTestGroupWriter;
import com.netcracker.tc.model.Schema;
import com.netcracker.tc.model.Schemas;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Test;
import com.netcracker.tc.model.Configuration;
import com.netcracker.util.ClassPath;
import com.netcracker.util.Label;

public class Configurator
{
    private final Label.Bundle L = Label.getBundle("ui");

    private final ConfiguratorForm form;
    private final Configuration tests = new Configuration();
    
    private ConfigurationWriter writer;
    private ConfigurationReader reader;
    private Schemas actions;

    private String currentFile;


    public Configurator()
    {
        EditorRegistry widgets = new EditorRegistry();
        XmlTestGroupReader reader = new XmlTestGroupReader();
        XmlTestGroupWriter writer = new XmlTestGroupWriter();
        AnnotationSchemaReader schema = new AnnotationSchemaReader();
        
        try {
            for (String typeName : ClassPath.enumerateClassesAt(
                    new File("bin"), "com.netcracker.tc.types.standard")) {
                
                Class<?> type = Class.forName(typeName);
                widgets.register(type);
                reader.register(type);
                writer.register(type);
                schema.register(type);
            }
            for (String typeName : ClassPath.enumerateClassesAt(
                    new File("bin"), "com.netcracker.tc.tests.examples"))
                schema.analyze(Class.forName(typeName));
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        this.writer = writer;
        this.reader = reader;
        this.actions = schema.getActions();

        form = new ConfiguratorForm(this, widgets, tests);
        form.addActions(actions);
        
        form.show();
    }
    
    public static void main(String[] args)
    {
        new Configurator();
    }
    
    public void createTest()
    {
        form.editElementName(
                tests.add(
                        new Test(L.get("ui.default_test_title"))));
    }
    
    public void moveScenario(int delta)
    {
        Object selected = form.getSelection();
        if (! (selected instanceof Scenario))
            form.displayErrorMessage(L.get("ui.error.no_scenario_selected"));
        else {
            Scenario scenario = (Scenario) selected;
            scenario.moveBy(delta);
            form.selectElement(scenario);
        }
    }
    
    public void saveTests() throws DataException
    {
        if (currentFile == null) {
            File file = form.showSaveDialog();
            if (file == null)
                return;
            form.setTitle(L.get("ui.title.file", file.getName()));
            currentFile = file.toString();
        }
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(currentFile));
            try {
                writer.write(out, tests);
            }
            finally {
                out.close();
            }
        }
        catch (IOException e) {
            throw new DataException(e);
        }
    }

    public void openTests() throws DataException
    {
        File file = form.showOpenDialog();
        if (file == null)
            return;
        form.setTitle(L.get("ui.title.file", file.getName()));
        currentFile = file.getName();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(currentFile));
            try {
                tests.clear();
                reader.read(in, tests, actions);
                form.selectionChanged();
            }
            finally {
                in.close();
            }
        }
        catch (IOException e) {
            throw new DataException(e);
        }
    }

    public void createAction(Schema action)
    {
        Object selected = form.getSelection();
        Test test = selected instanceof Scenario 
                ? ((Scenario) selected).getParent() 
                : (Test) selected; 
        if (test == null)
            form.displayErrorMessage(L.get("ui.error.no_test_selected"));
        else
            form.editElementName(test.add(new Scenario(action)));
    }

    public void close()
    {
        form.close();
    }

    public void deleteElement(Object obj)
    {
        if (obj instanceof Scenario) {
            Scenario s = (Scenario) obj;
            s.getParent().remove(s);
        }
        if (obj instanceof Test) {
            Test t = (Test) obj;
            t.getParent().remove(t);
        }
    }

    public void newTests()
    {
        currentFile = null;
        form.setTitle(L.get("ui.title"));
        tests.clear();
        form.selectionChanged();
    }
}
