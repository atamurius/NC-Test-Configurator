package com.netcracker.tc.configurator.ui;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

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
import com.netcracker.tc.model.Schema;
import com.netcracker.tc.model.Schemas;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Configuration;
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

    private final Configurator configurator;
    
    private JFrame frame;
    private JTree tree;
    private TestGroupModel model;
    private ScenarioEditor editor;
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
            configurator.moveScenario(-1); }};

    @SuppressWarnings("serial")
    private final AbstractAction actMoveDown = new AbstractAction(L.get("ui.menu.move_scenario_down")) {
        @Override public void actionPerformed(ActionEvent e) {
            configurator.moveScenario(+1); }};

    /**
     * Create gui.
     * @param tests 
     * @param configurator2 
     */
    @SuppressWarnings("serial")
    public ConfiguratorForm(Configurator conf, final EditorRegistry widgetRegistry, final Configuration tests)
    {
        this.configurator = conf;
        
        final AbstractAction actNewTest = new AbstractAction(L.get("ui.menu.new_test")) {
            @Override public void actionPerformed(ActionEvent e) {
                configurator.createTest(); }};

        frame = new JFrame(L.get("ui.title")) 
        {{
            setSize(800, 600);
            
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    configurator.close(); }});

            setJMenuBar(new JMenuBar() 
            {{
                add(new JMenu(L.get("ui.menu.file"))
                {{
                    add(new JMenuItem(new AbstractAction(L.get("ui.menu.file.new")) {
                        public void actionPerformed(ActionEvent e) {
                            configurator.newTests(); }}));
                    
                    add(new JMenuItem(new AbstractAction(L.get("ui.menu.file.open")) {
                        {putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                                    Character.valueOf('o'), InputEvent.CTRL_DOWN_MASK));
                        }
                        public void actionPerformed(ActionEvent e) {
                            try {
                                configurator.openTests();
                            }
                            catch (DataException e1) {
                                e1.printStackTrace();
                                displayErrorMessage(e1.getLocalizedMessage());
                            }
                        }
                    }));
                    add(new JMenuItem(new AbstractAction(L.get("ui.menu.file.save")) {
                        {putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                                    Character.valueOf('s'), InputEvent.CTRL_DOWN_MASK));
                        }
                        public void actionPerformed(ActionEvent e) {
                            try {
                                configurator.saveTests();
                            }
                            catch (DataException e1) {
                                e1.printStackTrace();
                                displayErrorMessage(e1.getLocalizedMessage());
                            }
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
                        new JTree(model = new TestGroupModel(tests))
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

    public Object getSelection()
    {
        TreePath selection = tree.getSelectionPath();
        return (selection == null) 
                ? null 
                : selection.getLastPathComponent();
    }
    
    public void selectionChanged()
    {
        Object obj = getSelection();
        
        actEditElement.setEnabled(obj != null);
        actDelElement.setEnabled(obj != null);
        actMoveDown.setEnabled(obj instanceof Scenario);
        actMoveUp.setEnabled(obj instanceof Scenario);
        menuNew.setEnabled(obj != null);
        popupNew.setEnabled(obj != null);
        
        if (obj instanceof Scenario)
            editor.reset((Scenario) obj);
    }
    
    /**
     * Add new action type to "Create action menu"
     * @param type name to add
     */
    @SuppressWarnings("serial")
    public void addActions(Schemas actions)
    {
        for (String group : actions.groups()) {
            JMenu groupMenu1 = new JMenu(group);
            menuNew.add(groupMenu1);

            JMenu groupMenu2 = new JMenu(group);
            popupNew.add(groupMenu2);
            
            for (final Schema action : actions.group(group)) {
                AbstractAction a = new AbstractAction(action.getTitle()) {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        configurator.createAction(action);
                    }
                };
                groupMenu1.add(new JMenuItem(a));
                groupMenu2.add(new JMenuItem(a));
            }
        }
    }

    public void editElementName(Object element)
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
        Object obj = getSelection();
        if (obj == null) {
            displayErrorMessage(L.get("ui.error.no_element_selected"));
        }
        else {
            if (JOptionPane.YES_OPTION == 
                    JOptionPane.showConfirmDialog(frame, 
                            L.get("ui.confirm_delete", obj), 
                            L.get("ui.confirm_delete_title"), 
                            JOptionPane.YES_NO_OPTION)) {
                
                editor.reset();
                configurator.deleteElement(obj);
            }
        }
    }

    /**
     * Displays error message to user with given text.
     * @param label localized text label with error text
     */
    public void displayErrorMessage(String text)
    {
        JOptionPane.showMessageDialog(frame, 
                text, 
                L.get("ui.error.title"), 
                JOptionPane.ERROR_MESSAGE);
    }

    public void show()
    {
        frame.setVisible(true);
    }

    public void selectElement(Object obj)
    {
        tree.setSelectionPath(model.getPathTo(obj));
    }

    public File showSaveDialog()
    {
        JFileChooser fc = new JFileChooser(".");
        if (fc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION)
            return null;
        else
            return fc.getSelectedFile();
    }

    public File showOpenDialog()
    {
        JFileChooser fc = new JFileChooser(".");
        if (fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION)
            return null;
        else
            return fc.getSelectedFile();
    }

    public void setTitle(String title)
    {
        frame.setTitle(title);
    }

    public void close()
    {
        frame.dispose();
    }
}




