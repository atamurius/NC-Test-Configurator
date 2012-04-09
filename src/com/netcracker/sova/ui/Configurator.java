package com.netcracker.sova.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.netcracker.sova.annotated.AnnotationSchemaReader;
import com.netcracker.sova.annotated.Executor;
import com.netcracker.sova.io.ConfigurationReader;
import com.netcracker.sova.io.ConfigurationWriter;
import com.netcracker.sova.io.DataException;
import com.netcracker.sova.io.xml.XmlTestGroupReader;
import com.netcracker.sova.io.xml.XmlTestGroupWriter;
import com.netcracker.sova.model.Configuration;
import com.netcracker.sova.model.Scenario;
import com.netcracker.sova.model.Schema;
import com.netcracker.sova.model.Schemas;
import com.netcracker.sova.model.Test;
import com.netcracker.util.ClassEnumerator;
import com.netcracker.util.Label;

public class Configurator
{
    private final Label.Bundle L = Label.getBundle("ui");

    private final ConfiguratorForm form;
    private final Configuration tests = new Configuration();
    
    private ConfigurationWriter writer;
    private ConfigurationReader reader;
    private Schemas actions;
    private ExecutorForm executor;

    private String currentFile;

    public Configurator() throws IOException
    {
        final File EXT_DIR = new File(System.getProperty("extensions", "ext"));
        
        EditorRegistry widgets = new EditorRegistry();
        XmlTestGroupReader reader = new XmlTestGroupReader();
        XmlTestGroupWriter writer = new XmlTestGroupWriter();
        AnnotationSchemaReader schema = new AnnotationSchemaReader();
        
        ClassEnumerator classes = new ClassEnumerator(EXT_DIR);
        
        classes.registerClasses(reader, writer, schema.TYPE_READERS, widgets);

        classes.registerClasses(schema);

        this.writer = writer;
        this.reader = reader;
        this.actions = schema.actions;

        form = new ConfiguratorForm(this, widgets, tests);
        form.addActions(actions);
        this.executor = new ExecutorForm(form.getRoot(), new Executor(classes.getClassLoader()));
        
        form.show();
    }
    
    public static void main(String[] args) throws Exception
    {
        Configurator conf = new Configurator();
        if (args.length == 1) {
            File file = new File(args[0]);
            if (file.isFile())
                conf.openTests(file);
        }
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
        openTests(file);
    }

    public void openTests(File file) throws DataException
    {
        form.setTitle(L.get("ui.title.file", file.getName()));
        currentFile = file.getAbsolutePath();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(currentFile));
            try {
                form.resetEditor();
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

    public void executeTest()
    {
        Object selected = form.getSelection();
        Test test = selected instanceof Scenario 
                ? ((Scenario) selected).getParent() 
                : (Test) selected; 
        if (test == null) {
            form.displayErrorMessage(L.get("ui.error.no_test_selected"));
        }
        else if (! test.isValid()) {
            form.displayErrorMessage(L.get("ui.error.test_not_valid"));
        }
        else {
            executor.execute(test);
        }
    }
}
