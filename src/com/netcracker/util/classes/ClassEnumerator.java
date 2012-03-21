package com.netcracker.util.classes;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ClassEnumerator
{
    public static void registerClassesFromJar(File jar, ClassRegistry ... regs) throws IOException
    {
        try {
            URLClassLoader cloader = new URLClassLoader(new URL[] { jar.toURI().toURL() });
            ZipFile zip = new ZipFile(jar);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class") && ! name.equals("package-info.class")) {
                    String tname = name.substring(0, name.length() - 6).replaceAll("/", ".");
                    try {
                        Class<?> type = cloader.loadClass(tname);
                        int mod = type.getModifiers();
                        if (Modifier.isPublic(mod) && ! Modifier.isAbstract(mod))
                            for (ClassRegistry reg : regs)
                                reg.register(type);
                    }
                    catch (ClassNotFoundException e) {
                        System.out.println("Failed to load class "+ tname);
                    }
                }
            }
        }
        catch (MalformedURLException e) {
            throw new IOException(e);
        }
        catch (ZipException e) {
            throw new IOException(e);
        }
    }
    
    public static void registerClassesFromPath(File classpath, ClassRegistry ... regs) throws IOException
    {
        try {
            URLClassLoader cloader = new URLClassLoader(new URL[] { classpath.toURI().toURL() });
            registerSubFiles(cloader, classpath, "", regs);
        }
        catch (MalformedURLException e) {
            throw new IOException(e);
        }
    }
    
    private static void registerSubFiles(URLClassLoader cloader,
            File path, String prefix, ClassRegistry[] regs) throws IOException
    {
        for (File file : path.listFiles()) {
            
            if (file.isDirectory())
                registerSubFiles(cloader, file, withPrefix(prefix, file.getName()), regs);
            
            else if (file.getName().endsWith(".jar"))
                registerClassesFromJar(file, regs);
            
            else if (file.getName().endsWith(".class") && ! file.getName().equals("package-info.class")) {
                String tname = withPrefix(prefix, file.getName().substring(0, file.getName().length() - 6));
                try {
                    Class<?> type = cloader.loadClass(tname);
                    int mod = type.getModifiers();
                    if (Modifier.isPublic(mod) && ! Modifier.isAbstract(mod))
                        for (ClassRegistry reg : regs)
                            reg.register(type);
                }
                catch (ClassNotFoundException e) {
                    System.out.println("Failed to load class "+ tname);
                }
            }
        }
    }

    private static String withPrefix(String prefix, String name)
    {
        return prefix.isEmpty() ? name : prefix + "." + name;
    }
}
