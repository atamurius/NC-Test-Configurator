package com.netcracker.tc.configurator.ui;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.netcracker.tc.configurator.data.DataException;
import com.netcracker.tc.configurator.data.TestGroupWriter;
import com.netcracker.tc.configurator.data.xml.XmlTestGroupReader;
import com.netcracker.tc.configurator.data.xml.XmlTestGroupWriter;
import com.netcracker.tc.model.Action;
import com.netcracker.tc.model.ActionGroup;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Test;
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
    private TestGroupWriter writer;
    private ActionGroup actions;
    
    //TODO:
    public final EditorRegistry widgetRegistry = new EditorRegistry();
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
                add(new JMenu(L.get("ui.menu.file"))
                {{
                    add(new JMenuItem(new AbstractAction(L.get("ui.menu.file.open")) {
                        {putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                                    Character.valueOf('o'), InputEvent.CTRL_DOWN_MASK));
                        }
                        public void actionPerformed(ActionEvent e) {
                            openTests();
                        }
                    }));
                    add(new JMenuItem(new AbstractAction(L.get("ui.menu.file.save")) {
                        {putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                                    Character.valueOf('s'), InputEvent.CTRL_DOWN_MASK));
                        }
                        public void actionPerformed(ActionEvent e) {
                            saveTests();
                        }
                    }));
                }});
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
    public void setActions(ActionGroup actions)
    {
        this.actions = actions;
        for (String group : actions.groups()) {
            JMenu groupMenu1 = new JMenu(group);
            menuNew.add(groupMenu1);
            JMenu groupMenu2 = new JMenu(group);
            popupNew.add(groupMenu2);
            
            for (final Action action : actions.actions(group)) {
                AbstractAction a = new AbstractAction(action.getTitle()) {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        Test test = getSelectedTest();
                        if (test == null)
                            displayErrorMessage(L.get("ui.error.no_test_selected"));
                        else
                            changeElementName(test.add(new Scenario(action)));
                    }
                };
                groupMenu1.add(new JMenuItem(a));
                groupMenu2.add(new JMenuItem(a));
            }
        }
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
    
    private void saveTests()
    {
        if (writer == null) {
            JFileChooser fc = new JFileChooser(".");
            if (fc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION)
                return;
            frame.setTitle(L.get("ui.title.file", fc.getSelectedFile().getName()));
            writer = new XmlTestGroupWriter(fc.getSelectedFile().toString());
        }
        try {
            writer.write(model.getRoot());
        }
        catch (DataException e) {
            e.printStackTrace();
            displayErrorMessage(e.getLocalizedMessage());
        }
    }

    private void openTests()
    {
        JFileChooser fc = new JFileChooser(".");
        if (fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION)
            return;
        frame.setTitle(L.get("ui.title.file", fc.getSelectedFile().getName()));
        XmlTestGroupReader reader = new XmlTestGroupReader(fc.getSelectedFile().toString());
        try {
            model.getRoot().clear();
            reader.read(model.getRoot(), actions);
            writer = new XmlTestGroupWriter(fc.getSelectedFile().toString());
        }
        catch (DataException e) {
            e.printStackTrace();
            displayErrorMessage(e.getLocalizedMessage());
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
    
    public void show()
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void selectElement(Object obj)
    {
        tree.setSelectionPath(model.getPathTo(obj));
    }
}




