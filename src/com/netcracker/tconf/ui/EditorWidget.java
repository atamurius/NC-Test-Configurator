package com.netcracker.tconf.ui;

import java.awt.Component;

import com.netcracker.tconf.model.Parameter;
import com.netcracker.tconf.model.Type;

/**
 * {@link Parameter} editor widget.
 * On editor value changed or lost focus widget must
 * update property value.
 * On property value changed widget will be updated,
 * so no need to add new observer to it's test group.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public interface EditorWidget
{
    /**
     * Widget type, for registration.
     */
    Class<? extends Type> getType();
    
    /**
     * Set property to edit on changed.
     * @param property
     */
    void setProperty(Parameter property);
    
    /**
     * Root editor component.
     * Just editor component without title.
     * @return editor component
     */
    Component getComponent();

    /**
     * Updates editors value with current property value.
     * Called when property was changed elsewhere.
     * @param value
     */
    void update();
}
