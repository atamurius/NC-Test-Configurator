package com.netcracker.tc.configurator.ui;

import java.awt.Component;

import com.netcracker.tc.model.Property;

/**
 * {@link Property} editor widget.
 * On editor value changed or lost focus widget must
 * update properties value.
 * On property value changed widget will be updated,
 * so no need to add new observer to it's test group.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 */
public interface EditorWidget
{
    void setProperty(Property property);
    
    /**
     * Root editor component.
     * @return editor component
     */
    Component getComponent();

    /**
     * Updates editors value with current property value.
     * @param value
     */
    void update();
}
