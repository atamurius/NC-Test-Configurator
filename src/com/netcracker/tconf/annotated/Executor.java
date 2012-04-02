package com.netcracker.tconf.annotated;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.netcracker.tconf.model.Output;
import com.netcracker.tconf.model.Parameter;
import com.netcracker.tconf.model.Scenario;
import com.netcracker.tconf.model.Test;
import com.netcracker.tconf.types.ref.RefType;

public class Executor
{
    private static final String HR = "\n----------------------------------\n";
    
    private final ClassLoader classLoader;
    
    public Executor(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }

    /**
     * Executes scenarios,
     * scenario.schema.name expected to be "Class.method"
     * @param test
     */
    public void execute(Test test)
    {
        long time = System.currentTimeMillis();
        String last = null;
        try {
            int total = test.scenarios().size();
            int i = 1;
            for (Scenario scenario : test.scenarios()) {
                last = scenario.getSchema().getName();
                System.out.printf("%s[%d/%d] scenario \"%s\":%s", HR, i++, total, scenario.getTitle(), HR);
                String scType = scenario.getSchema().getName();
                int p = scType.lastIndexOf('.');
                Class<?> type = classLoader.loadClass(scType.substring(0,p));
                Method method = findMethod(type, scType.substring(p+1));
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
            System.out.println(HR + "Execution failed: "+ last);
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
                throw new IllegalStateException("Scenario output cannot be null: "+ scenario.getSchema().getName());
            }
            else {
                Object value = findField(result.getClass(), output.getName()).get(result);
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
                throw new IllegalArgumentException("Unknown parameter name: "+ param.getName());
            
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
                findField(types[index], m.group(3)).set(args[index], value);
            }
        }
        
        return args;
    }

    private Field findField(Class<?> type, String name) throws NoSuchFieldException
    {
        Class<?> cls = type;
        while (type != null) {
            try {
                Field field = cls.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            }
            catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchFieldException(type + "." + name);
    }

    private Method findMethod(Class<?> type, String name) throws NoSuchMethodException
    {
        Method found = null;
        for (Method m : type.getMethods())
            if (m.isAnnotationPresent(com.netcracker.tconf.annotated.anns.Scenario.class) && 
                    m.getName().equals(name)) {
                if (found == null)
                    found = m;
                else
                    throw new NoSuchMethodException(type + " must have only one method '"+ name +"'");
            }
        if (found != null)
            return found;
        else
            throw new NoSuchMethodException(type + "." + name);
    }
}
