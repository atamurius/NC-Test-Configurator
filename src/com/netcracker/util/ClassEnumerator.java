package com.netcracker.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ClassEnumerator
{
    private final File root;
    private final ClassLoader classLoader;

    public ClassEnumerator(File path)
    {
        this.root = path;
        this.classLoader = classLoaderFor(path);
    }
    
    private void registerClassesFromJar(File jar, ClassRegistry ... regs) throws IOException
    {
        try {
            ZipFile zip = new ZipFile(jar);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class") && ! name.equals("package-info.class")) {
                    String tname = name.substring(0, name.length() - 6).replaceAll("/", ".");
                    try {
                        Class<?> type = classLoader.loadClass(tname);
                        int mod = type.getModifiers();
                        if (! Modifier.isAbstract(mod))
                            for (ClassRegistry reg : regs)
                                reg.register(type);
                    }
                    catch (NoClassDefFoundError e) {
                        System.out.println("Failed to load class "+ tname);
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
    
    public void registerClasses(ClassRegistry ... regs) throws IOException
    {
        try {
            registerSubFiles(root, "", regs);
        }
        catch (MalformedURLException e) {
            throw new IOException(e);
        }
    }
    
    private void registerSubFiles(File path, String prefix, ClassRegistry[] regs) 
            throws IOException
    {
        for (File file : path.listFiles()) {
            
            if (file.isDirectory())
                registerSubFiles(file, withPrefix(prefix, file.getName()), regs);
            
            else if (file.getName().endsWith(".jar")) {
                registerClassesFromJar(file, regs);
            }
            else if (file.getName().endsWith(".class") && ! file.getName().equals("package-info.class")) {
                String tname = withPrefix(prefix, file.getName().substring(0, file.getName().length() - 6));
                try {
                    Class<?> type = classLoader.loadClass(tname);
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

    private final String withPrefix(String prefix, String name)
    {
        return prefix.isEmpty() ? name : prefix + "." + name;
    }

    private final ClassLoader classLoaderFor(File path)
    {
        try {
            List<URL> urls = new ArrayList<URL>();
            urls.add(path.toURI().toURL());
    
            for (File file : path.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    urls.add(file.toURI().toURL());
                }
            }
    
            return new URLClassLoader(urls.toArray(new URL[0]), ClassEnumerator.class.getClassLoader());
        }
        catch (MalformedURLException e) { 
            // It can't be
            throw new RuntimeException(e);
        }
    }

    public ClassLoader getClassLoader()
    {
        return classLoader;
    }
}
