package com.netcracker.tc.configurator;

import com.netcracker.tc.configurator.data.xml.XmlActionReader;
import com.netcracker.tc.configurator.ui.ConfiguratorForm;
import com.netcracker.tc.configurator.ui.widgets.BoolWidget;
import com.netcracker.tc.configurator.ui.widgets.EnumWidget;
import com.netcracker.tc.configurator.ui.widgets.IntWidget;
import com.netcracker.tc.configurator.ui.widgets.RefWidget;
import com.netcracker.tc.configurator.ui.widgets.SetWidget;
import com.netcracker.tc.configurator.ui.widgets.StringWidget;
import com.netcracker.tc.model.types.BoolType;
import com.netcracker.tc.model.types.EnumType;
import com.netcracker.tc.model.types.IntType;
import com.netcracker.tc.model.types.RefType;
import com.netcracker.tc.model.types.SetType;
import com.netcracker.tc.model.types.StringType;

public class Start
{
    public static void main(String[] args) throws Exception
    {
        final ConfiguratorForm conf = new ConfiguratorForm();
        
        conf.widgetRegistry.register(StringType.class, StringWidget.class);
        conf.widgetRegistry.register(IntType.class, IntWidget.class);
        conf.widgetRegistry.register(BoolType.class, BoolWidget.class);
        conf.widgetRegistry.register(EnumType.class, EnumWidget.class);
        conf.widgetRegistry.register(SetType.class, SetWidget.class);
        conf.widgetRegistry.register(RefType.class, RefWidget.class);

        XmlActionReader reader = new XmlActionReader("schema.xml");
        conf.setActions(reader.readActionGroup());
        
        conf.show();
    }
}
