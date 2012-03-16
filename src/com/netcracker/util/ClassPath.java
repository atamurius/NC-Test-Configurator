package com.netcracker.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassPath
{
    public static List<String> enumerateClassesAt(File path, String pckg)
    {
        List<String> classes = new ArrayList<String>();
        if (! pckg.isEmpty())
            path = new File(path, pckg.replaceAll("\\.", "/"));
        enumerateClassesAt(classes, path, pckg);
        return classes;
    }

    private static void enumerateClassesAt(List<String> classes, File path,
            String prefix)
    {
        for (File file : path.listFiles()) {
            
            if (file.isDirectory())
                enumerateClassesAt(classes, file, withPrefix(prefix, file.getName()));
            
            else if (file.getName().endsWith(".class"))
                classes.add(withPrefix(prefix, file.getName().substring(0, file.getName().length() - 6)));
        }
    }

    private static String withPrefix(String prefix, String name)
    {
        return prefix.isEmpty() ? name : prefix + "." + name;
    }
}
