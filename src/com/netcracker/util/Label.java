package com.netcracker.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class Label
{
    private static final Map<String, ResourceBundle> bundles = new HashMap<String, ResourceBundle>();
    
    public interface Bundle
    {
        String get(String label, Object ... args);
    }
    
    public static String get(String bundle, String label, Object ... args)
    {
        if (! bundles.containsKey(bundle))
            bundles.put(bundle, ResourceBundle.getBundle(bundle));
        
        return get(bundles.get(bundle), label, args);
    }
    
    public static Bundle getBundle(final String name)
    {
        return new Bundle()
        {
            public String get(String label, Object... args)
            {
                return Label.get(name, label, args);
            }
        };
    }

    public static Bundle getBundle(final Class<?> parent, final String name)
    {
        return new Bundle()
        {
            ResourceBundle bundle = ResourceBundle.
                    getBundle(name, Locale.getDefault(), parent.getClassLoader());
            
            public String get(String label, Object... args)
            {
                return Label.get(bundle, label, args);
            }
        };
    }

    public static String get(ResourceBundle bundle, String label,
            Object[] args)
    {
        String string = bundle.getString(label);
        if (args.length > 0)
            string = MessageFormat.format(string, args);
        return string;
    }
}
