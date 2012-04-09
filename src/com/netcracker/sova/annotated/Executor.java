package com.netcracker.sova.annotated;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.netcracker.sova.io.xml.XmlTestGroupReader;
import com.netcracker.sova.model.Configuration;
import com.netcracker.sova.model.Output;
import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Scenario;
import com.netcracker.sova.model.Test;
import com.netcracker.sova.types.ref.RefType;
import com.netcracker.util.ClassEnumerator;

public class Executor
{
    private static final String HR = "\n----------------------------------\n";
    
    private final ClassLoader classLoader;
    
    public Executor(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }
    
    public static void main(String[] args)
    {
        try {
            if (args.length != 1 && args.length != 2)
                halt("No arguments passed");
            
            String ext = System.getProperty("extensions");
            if (ext == null)
                halt("no extensions folder set (folder with types and schema jars)");

            File extf = new File(ext);
            if (! extf.exists() || ! extf.isDirectory())
                halt(ext +" doesn't exists or isn't directory");
            
            ClassEnumerator classes = new ClassEnumerator(extf);

            XmlTestGroupReader reader = new XmlTestGroupReader();
            AnnotationSchemaReader schema = new AnnotationSchemaReader();
            
            classes.registerClasses(reader, schema.TYPE_READERS);
            classes.registerClasses(schema);
            
            Configuration conf = new Configuration();
            
            InputStream in = new BufferedInputStream(new FileInputStream(args[0]));
            try {
                reader.read(in, conf, schema.actions);
            }
            finally {
                in.close();
            }
            
            Executor executor = new Executor(classes.getClassLoader());
            
            if (args.length == 2) {
                int index = Integer.parseInt(args[1]);
                Test test = conf.tests().get(index);
                executor.execute(test);
            }
            else {
                System.out.println("Executing all tests...");
                int total = conf.tests().size();
                int index = 0;
                for (Test test : conf.tests()) {
                    System.out.printf("%s%sTest %d of %d%n", HR, HR, ++index, total);
                    executor.execute(test);
                }
            }
        }
        catch (Exception e) {
            System.out.printf("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
            System.exit(2);
        }
    }

    private static void halt(String msg)
    {
        System.out.println(msg);
        File jar = new File(Executor.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String unit = jar.getName().endsWith(".jar") ?
                "-jar " + jar.getName() : Executor.class.getName();
        System.out.println(
                "Usage: java -Dextensions=folder "+ unit + " configuration.xml [test index from 0]");
        System.exit(1);
    }

    /**
     * Executes scenarios,
     * scenario.schema.name expected to be "Class.method"
     * @param test
     */
    public void execute(Test test)
    {
        System.out.printf("Executing \"%s\"%n", test.getTitle());
        long time = System.currentTimeMillis();
        try {
            int total = test.scenarios().size();
            int i = 1;
            for (Scenario scenario : test.scenarios()) {
                System.out.printf("%s[%d/%d] scenario \"%s\":%s", 
                        HR, i++, total, scenario.getTitle(), HR);
                
                String scType = scenario.getSchema().getName();
                int p = scType.lastIndexOf('.');
                Class<?> type = classLoader.loadClass(scType.substring(0,p));
                Method method = Reflection.findMethod(type, scType.substring(p+1));
                Object[] args = fillParameters(method.getParameterTypes(), scenario.parameters().values());
                Object instance = type.newInstance();
                Object result = method.invoke(instance, args);
                assignOutput(scenario, result);
                
                if (Thread.interrupted()) {
                    System.out.println(HR + "Interrupted");
                    break;
                }
            }
        }
        catch (Exception e) {
            System.out.println(HR + "Execution failed");
            e.printStackTrace(System.out);
        }
        System.out.printf("%sExecution time: %.1fs%n", HR, 
                (System.currentTimeMillis() - time) / 1000.);
    }
    
    private void assignOutput(Scenario scenario, Object result) 
            throws IllegalArgumentException, SecurityException, IllegalAccessException, 
                NoSuchFieldException
    {
        for (Output output : scenario.outputs().values()) {
            if (output.getName().equals(AnnotationSchemaReader.RETURN_OUTPUT_NAME)) {
                output.setValue(result);
            }
            else if (result == null) {
                throw new IllegalStateException("Scenario output cannot be null: "+ 
                        scenario.getSchema().getName());
            }
            else {
                Object value = Reflection.findField(result.getClass(), output.getName()).get(result);
                output.setValue(value);
            }
        }
    }

    private Object[] fillParameters(Class<?>[] types, Iterable<Parameter> parameters) 
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, 
                SecurityException, NoSuchFieldException
    {
        Object[] args = new Object[types.length];
        
        final Pattern pName = Pattern.compile(
                Pattern.quote(AnnotationSchemaReader.METHOD_PARAM_PREFIX) +
                "(\\d+)(\\.(.*))?");
        
        for (Parameter param : parameters) {
            Matcher m = pName.matcher(param.getName());
            if (! m.matches())
                throw new IllegalArgumentException(
                        "Unknown parameter name: "+ param.getName());
            
            int index = Integer.parseInt(m.group(1));
            
            Object value = param.getValue();
            if (param.getType() instanceof RefType) {
                if (value == null)
                    continue;
                value = ((Output) value).getValue();
            }
            
            if (m.group(2) == null) { // just plain value
                args[index] = value;
            }
            else { // complex object
                if (args[index] == null)
                    args[index] = types[index].newInstance();
                Reflection.findField(types[index], m.group(3)).set(args[index], value);
            }
        }
        
        return args;
    }
}
