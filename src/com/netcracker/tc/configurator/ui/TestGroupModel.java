package com.netcracker.tc.configurator.ui;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.netcracker.tc.model.Observer;
import com.netcracker.tc.model.Parameter;
import com.netcracker.tc.model.Scenario;
import com.netcracker.tc.model.Test;
import com.netcracker.tc.model.Configuration;

public class TestGroupModel implements TreeModel, Observer
{
    private static final long serialVersionUID = 1L;

    private final Configuration tests;
    
    public TestGroupModel()
    {
        this(new Configuration());
    }

    public TestGroupModel(Configuration tests)
    {
        this.tests = tests;
        tests.addObserver(this);
    }

    private Collection<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
    
    @Override
    public void addTreeModelListener(TreeModelListener l)
    {
        listeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l)
    {
        listeners.remove(l);
    }

    @Override
    public Object getChild(Object parent, int index)
    {
        if (parent == tests) {
            return tests.tests().get(index);
        }
        if (parent instanceof Test) {
            return ((Test) parent).scenarios().get(index);
        }
        else {
            return null;
        }
    }

    @Override
    public int getChildCount(Object parent)
    {
        if (parent == tests) {
            return tests.tests().size();
        }
        if (parent instanceof Test) {
            return ((Test) parent).scenarios().size();
        }
        else {
            return 0;
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child)
    {
        if (parent == tests) {
            return tests.tests().indexOf(child);
        }
        if (parent instanceof Test) {
            return ((Test) parent).scenarios().indexOf(child);
        }
        else {
            return -1;
        }
    }

    @Override
    public Configuration getRoot()
    {
        return tests;
    }

    @Override
    public boolean isLeaf(Object node)
    {
        return node instanceof Scenario;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue)
    {
        Object obj = path.getLastPathComponent();
        if (obj instanceof Test)
            ((Test) obj).setTitle((String) newValue);
        if (obj instanceof Scenario)
            ((Scenario) obj).setTitle((String) newValue);
    }
    
    @Override
    public void update(Object source, Event event)
    {
        switch (event) {
            case ADDED:
            case REMOVED:
            {
                TreeModelEvent e = new TreeModelEvent(this,
                        source == tests ? getPathTo(tests) : getPathTo(source).getParentPath());
                for (TreeModelListener l : listeners)
                    l.treeStructureChanged(e);
            }
            break;

            case CHANGED:
            {
                TreeModelEvent e = new TreeModelEvent(this, getPathTo(source));
                for (TreeModelListener l : listeners)
                    l.treeNodesChanged(e);
            }
            break;
        }
    }

    public TreePath getPathTo(Object source)
    {
        if (source == tests)
            return new TreePath(tests);
        if (source instanceof Test)
            return getPathTo(tests).pathByAddingChild(source);
        if (source instanceof Scenario)
            return getPathTo(((Scenario) source).getParent()).pathByAddingChild(source);
        else // Property
            return getPathTo(((Parameter) source).getScenario());
    }
}
