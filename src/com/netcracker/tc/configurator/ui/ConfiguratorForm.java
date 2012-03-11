package com.netcracker.tc.configurator.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.netcracker.tc.configurator.ui.widgets.BoolWidget;
import com.netcracker.tc.configurator.ui.widgets.EnumWidget;
import com.netcracker.tc.configurator.ui.widgets.IntWidget;
import com.netcracker.tc.configurator.ui.widgets.RefWidget;
import com.netcracker.tc.configurator.ui.widgets.SetWidget;
import com.netcracker.tc.configurator.ui.widgets.StringWidget;
import com.netcracker.tc.model.Action;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Result;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Test;
import com.netcracker.tc.model.TestGroup;
import com.netcracker.tc.model.types.BoolType;
import com.netcracker.tc.model.types.EnumType;
import com.netcracker.tc.model.types.IntType;
import com.netcracker.tc.model.types.RefType;
import com.netcracker.tc.model.types.SetType;
import com.netcracker.tc.model.types.StringType;
import com.netcracker.util.Label;

/**
 * Main application form with cases tree.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 *
 */
public class ConfiguratorForm
{
    static {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            System.out.println("Can't set system look and feel");
            e.printStackTrace();
            // Fine, use a default one :(
        }
    }

    private final Label.Bundle L = Label.getBundle("ui");

    private JFrame frame;
    private JTree tree;
    private TestGroupModel model;
    private ScenarioEditor editor;
    private EditorRegistry widgetRegistry = new EditorRegistry();
    private JMenu menuNew, popupNew;

    @SuppressWarnings("serial")
    private final AbstractAction actEditElement = new AbstractAction(L.get("ui.menu.edit_element")) {
        @Override public void actionPerformed(ActionEvent e) {
            changeElementName(); }};
            
    @SuppressWarnings("serial")
    private final AbstractAction actDelElement = new AbstractAction(L.get("ui.menu.delete_element")) {
        @Override public void actionPerformed(ActionEvent e) {
            deleteElement(); }};
            
    @SuppressWarnings("serial")
    private final AbstractAction actMoveUp = new AbstractAction(L.get("ui.menu.move_scenario_up")) {
        @Override public void actionPerformed(ActionEvent e) {
            moveScenario(-1); }};

    @SuppressWarnings("serial")
    private final AbstractAction actMoveDown = new AbstractAction(L.get("ui.menu.move_scenario_down")) {
        @Override public void actionPerformed(ActionEvent e) {
            moveScenario(+1); }};

    /**
     * Create gui.
     */
    @SuppressWarnings("serial")
    public ConfiguratorForm()
    {
        final AbstractAction actNewTest = new AbstractAction(L.get("ui.menu.new_test")) {
            @Override public void actionPerformed(ActionEvent e) {
                createTest(); }};

        frame = new JFrame(L.get("ui.title")) 
        {{
            setSize(800, 600);

            setJMenuBar(new JMenuBar() 
            {{
                add(new JMenu(L.get("ui.menu.edit"))
                {{
                    add(new JMenuItem(actNewTest));
                    add(menuNew = new JMenu(L.get("ui.menu.new_step")));
                    addSeparator();
                    add(new JMenuItem(actEditElement));
                    add(new JMenuItem(actDelElement));
                    add(new JMenuItem(actMoveUp));
                    add(new JMenuItem(actMoveDown));
                }});
            }});

            add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT) 
            {{
                setResizeWeight(0.3);
                setContinuousLayout(true);
                setDoubleBuffered(false);
                
                setLeftComponent(new JScrollPane(tree = 
                        new JTree(model = new TestGroupModel())
                {{
                    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                    setRootVisible(false);
                    setEditable(true);
                    setCellRenderer(new CustomCellRenderer(getCellRenderer()));
                    
                    addTreeSelectionListener(new TreeSelectionListener() {
                        @Override public void valueChanged(TreeSelectionEvent e) {
                            selectionChanged(); }});
                    
                    addMouseListener(new MouseAdapter() {
                        
                        JPopupMenu popup = new JPopupMenu()
                        {{
                            add(new JMenuItem(actNewTest));
                            add(popupNew = new JMenu(L.get("ui.menu.new_step")));
                            addSeparator();
                            add(new JMenuItem(actEditElement));
                            add(new JMenuItem(actDelElement));
                            add(new JMenuItem(actMoveUp));
                            add(new JMenuItem(actMoveDown));
                        }};
                        
                        public void mousePressed(MouseEvent e) {
                            if (e.isPopupTrigger()) {
                                setSelectionPath(getPathForLocation(e.getX(), e.getY()));
                                popup.show(tree, e.getX(), e.getY());
                            }
                        }
                    });
                }}));
            
                setRightComponent(editor = new ScenarioEditor(widgetRegistry));
            }});
        }};
        selectionChanged();
    }

    private void createTest()
    {
        changeElementName(model.getRoot().add(new Test(L.get("ui.default_test_title"))));
    }
    
    private void selectionChanged()
    {
        TreePath selection = tree.getSelectionPath();
        Object obj = selection == null ? null : selection.getLastPathComponent();
        
        actEditElement.setEnabled(obj != null);
        actDelElement.setEnabled(obj != null);
        actMoveDown.setEnabled(obj instanceof Scenario);
        actMoveUp.setEnabled(obj instanceof Scenario);
        menuNew.setEnabled(obj != null);
        popupNew.setEnabled(obj != null);
        
        Scenario step = getSelectedScenario();
        if (step != null)
            displayEditorFor(step);
    }
    
    private void displayEditorFor(Scenario action)
    {
        editor.reset(action);
    }

    /**
     * Find currently selected action if any
     * @return selected action or null if none
     */
    private Scenario getSelectedScenario()
    {
        TreePath path = tree.getSelectionPath();
        return (path == null || path.getPathCount() < 3) 
                ? null 
                : (Scenario) path.getPathComponent(2);
    }

    /**
     * Find currently selected test case or parent test case of selected action.
     * @return selected test case or null if none
     */
    private Test getSelectedTest()
    {
        TreePath path = tree.getSelectionPath();
        return (path == null || path.getPathCount() < 2) 
                ? null 
                : (Test) path.getPathComponent(1);
    }

    /**
     * Add new action type to "Create action menu"
     * @param type name to add
     */
    @SuppressWarnings("serial")
    public void addStepType(String group, final String title, final Action type)
    {
        JMenu groupMenu1 = null;
        JMenu groupMenu2 = null;
        for (Component com : menuNew.getMenuComponents())
            if (com instanceof JMenu && ((JMenu) com).getText().equals(group)) {
                groupMenu1 = (JMenu) com;
                break;
            }
        for (Component com : popupNew.getMenuComponents())
            if (com instanceof JMenu && ((JMenu) com).getText().equals(group)) {
                groupMenu2 = (JMenu) com;
                break;
            }
        if (groupMenu1 == null) {
            groupMenu1 = new JMenu(group);
            menuNew.add(groupMenu1);
            groupMenu2 = new JMenu(group);
            popupNew.add(groupMenu2);
        }
        AbstractAction action = new AbstractAction(title) {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Test test = getSelectedTest();
                if (test == null)
                    displayErrorMessage(L.get("ui.error.no_test_selected"));
                else
                    changeElementName(test.add(new Scenario(title, type)));
            }
        };
        groupMenu1.add(new JMenuItem(action));
        groupMenu2.add(new JMenuItem(action));
    }

    private void changeElementName(Object element)
    {
        tree.startEditingAtPath(model.getPathTo(element));
    }

    private void changeElementName()
    {
        TreePath selected = tree.getSelectionPath();
        if (selected == null)
            displayErrorMessage(L.get("ui.error.no_element_selected"));
        else {
            tree.startEditingAtPath(selected);
        }
    }
    
    private void deleteElement()
    {
        TreePath selected = tree.getSelectionPath();
        if (selected == null)
            displayErrorMessage(L.get("ui.error.no_element_selected"));
        
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(frame, 
                L.get("ui.confirm_delete", selected.getLastPathComponent()), 
                L.get("ui.confirm_delete_title"), 
                JOptionPane.YES_NO_OPTION)) {
            
            editor.reset();
            Object obj = selected.getLastPathComponent();
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
    
    private void moveScenario(int delta)
    {
        Scenario scenario = getSelectedScenario();
        if (scenario == null)
            displayErrorMessage(L.get("ui.error.no_scenario_selected"));
        else {
            scenario.moveBy(delta);
            selectElement(scenario);
        }
    }

    /**
     * Displays error message to user with given text.
     * @param label localized text label with error text
     */
    private void displayErrorMessage(String text)
    {
        JOptionPane.showMessageDialog(frame, 
                text, 
                L.get("ui.error.title"), 
                JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args)
    {
        ConfiguratorForm form = new ConfiguratorForm();
        
        form.widgetRegistry.register(StringType.class, StringWidget.class);
        form.widgetRegistry.register(IntType.class, IntWidget.class);
        form.widgetRegistry.register(BoolType.class, BoolWidget.class);
        form.widgetRegistry.register(EnumType.class, EnumWidget.class);
        form.widgetRegistry.register(SetType.class, SetWidget.class);
        form.widgetRegistry.register(RefType.class, RefWidget.class);
        
        Action a = new Action(
                Arrays.asList(
                    new Property("name", "Name", "Your name", new StringType(true)),
                    new Property("cardNumber", "Card number", "Card number as XXXX", new StringType(true, 4)),
                    new Property("x", "Some number", "Any number", new IntType()),
                    new Property("weight", "Your weight", "Integer weight", IntType.minBy(0)),
                    new Property("required", "Required", "This checkbox is required", new BoolType()),
                    new Property("registered", "Registered", "I'm registered", new BoolType()),
                    new Property("city", "Your city", "Select your city", new EnumType("Kiev",
                            new Object[] { "Sumy", "Moskow", "Kiev", "New York" })),
                    new Property("types", "Favorite types", "Your favorite types", new SetType(
                            new Object[] { "Integer", "String", "Boolean", "Enumeration" })),
                    new Property("id", "Id of scenario", "Id of previous scenario", 
                            new RefType("test.id"))
                ),
                Arrays.asList(
                    new Result("id", "ID number", "test.id")));
        
        form.addStepType("Examples", "Test scenario", a);
        
        Scenario s = ((TestGroup) form.model.getRoot()).add(new Test("Example")).add(new Scenario("Scenario", a));
        form.selectElement(s);
        
        form.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.frame.setVisible(true);
    }

    private void selectElement(Object obj)
    {
        tree.setSelectionPath(model.getPathTo(obj));
    }
}




