package com.netcracker.tc.configurator.ui;

import java.io.File;

import com.netcracker.tc.configurator.data.AnnotationSchemaReader;
import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.configurator.data.TestGroupWriter;
import com.netcracker.tc.configurator.data.xml.XmlTestGroupReader;
import com.netcracker.tc.configurator.data.xml.XmlTestGroupWriter;
import com.netcracker.tc.model.Action;
import com.netcracker.tc.model.ActionGroup;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Test;
import com.netcracker.tc.model.TestGroup;
import com.netcracker.tc.types.standard.BoolType;
import com.netcracker.tc.types.standard.EnumType;
import com.netcracker.tc.types.standard.IntType;
import com.netcracker.tc.types.standard.RefType;
import com.netcracker.tc.types.standard.SetType;
import com.netcracker.tc.types.standard.StringType;
import com.netcracker.tc.types.standard.ui.BoolWidget;
import com.netcracker.tc.types.standard.ui.EnumWidget;
import com.netcracker.tc.types.standard.ui.IntWidget;
import com.netcracker.tc.types.standard.ui.RefWidget;
import com.netcracker.tc.types.standard.ui.SetWidget;
import com.netcracker.tc.types.standard.ui.StringWidget;
import com.netcracker.util.Label;

public class Configurator
{
    private final Label.Bundle L = Label.getBundle("ui");

    private final ConfiguratorForm form;
    private final TestGroup tests = new TestGroup();
    
    private TestGroupWriter writer;
    private ActionGroup actions;

    public Configurator()
    {
        EditorRegistry widgetRegistry = new EditorRegistry();
        
        widgetRegistry.register(StringType.class, StringWidget.class);
        widgetRegistry.register(IntType.class, IntWidget.class);
        widgetRegistry.register(BoolType.class, BoolWidget.class);
        widgetRegistry.register(EnumType.class, EnumWidget.class);
        widgetRegistry.register(SetType.class, SetWidget.class);
        widgetRegistry.register(RefType.class, RefWidget.class);

        form = new ConfiguratorForm(this, widgetRegistry, tests);

        try {
            actions = new AnnotationSchemaReader(
                    "com.netcracker.tc.tests.examples.Person$Create",
                    "com.netcracker.tc.tests.examples.Person$Delete",
                    "com.netcracker.tc.tests.examples.Switch$Create",
                    "com.netcracker.tc.tests.examples.Switch$DeletePort",
                    "com.netcracker.tc.tests.examples.Switch$DeleteSwitch"
                    ).registerStandard().
                      readActionGroup();
            form.addActions(actions);
        }
        catch (DataException e) {
            e.printStackTrace();
            form.displayErrorMessage(e.getLocalizedMessage());
            System.exit(0);
        }
        
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
        if (writer == null) {
            File file = form.showSaveDialog();
            if (file == null)
                return;
            form.setTitle(L.get("ui.title.file", file.getName()));
            writer = new XmlTestGroupWriter(file.toString());
        }
        writer.write(tests);
    }

    public void openTests() throws DataException
    {
        File file = form.showOpenDialog();
        if (file == null)
            return;
        form.setTitle(L.get("ui.title.file", file.getName()));
        XmlTestGroupReader reader = new XmlTestGroupReader(file.toString());
        tests.clear();
        reader.read(tests, actions);
        writer = new XmlTestGroupWriter(file.toString());
    }

    public void createAction(Action action)
    {
        Object selected = form.getSelection();
        Test test = selected instanceof Scenario 
                ? ((Scenario) selected).getTest() 
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
            s.getTest().remove(s);
        }
        if (obj instanceof Test) {
            Test t = (Test) obj;
            t.getGroup().remove(t);
        }
    }
}
