package com.netcracker.tc.configurator.data;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.netcracker.tc.model.Parameter;
import com.netcracker.tc.model.Schema;
import com.netcracker.tc.model.Schemas;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.tests.anns.Output;
import com.netcracker.tc.tests.anns.Outputs;
import com.netcracker.tc.tests.anns.Param;
import com.netcracker.tc.tests.anns.Params;
import com.netcracker.tc.tests.anns.Scenario;
import com.netcracker.tc.tests.anns.Scenarios;
import com.netcracker.tc.types.standard.ref.RefTypeReader;
import com.netcracker.util.classes.ClassRegistry;

/**
 * This class represents logic for analyzing test scenario classes and
 * collect it's information into {@link Schemas}.
 * <p>
 * Test cases class must be annotated with {@link Scenarios},
 * public non-static methods of this class, annotated with {@link Scenario}
 * will be mapped into {@link Schema} object.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 * 
 * @see com.netcracker.tc.tests.anns
 */
public class AnnotationSchemaReader implements ClassRegistry
{
    private final List<TypeReader> readers = new ArrayList<TypeReader>();

    public void register(Class<?> type)
    {
        try {
            if (TypeReader.class.isAssignableFrom(type))
                readers.add(type.asSubclass(TypeReader.class).newInstance());
        }
        catch (Exception e) {
            System.out.println("AnnotationSchemaReader: Failed to load "+ type);
        }
    }
    
    {
        register(RefTypeReader.class);
    }
    
    private final Schemas actions = new Schemas();
    
    public final ClassRegistry analizator = new ClassRegistry() {
        public void register(Class<?> type) {
            analyze(type); } };
    
    public void analyze(Class<?> type)
    {
        if (type.isAnnotationPresent(Scenarios.class)) {
            String group = getTitle(type.getAnnotation(Scenarios.class).value(), type.getSimpleName()); 
            
            for (Method method : type.getMethods())
                if (method.isAnnotationPresent(Scenario.class))
                    actions.add(group, getAction(method));
        }
    }

    private Schema getAction(Method method)
    {
        return new Schema(
                method.getDeclaringClass().getName() + "." + method.getName(), 
                getTitle(method.getAnnotation(Scenario.class).value(), method.getName()), 
                getScenatioParameters(method), 
                getScenarioOutputs(method));
    }

    @SuppressWarnings("unchecked")
    private List<com.netcracker.tc.model.Output> getScenarioOutputs(Method method)
    {
        if (method.getReturnType() == Void.TYPE)
            return Collections.EMPTY_LIST;
        
        if (method.getReturnType().isAnnotationPresent(Outputs.class))
            return getOutputsObject(method.getReturnType());
        else {
            Output out = method.getAnnotation(Scenario.class).output();
            return Collections.singletonList(new com.netcracker.tc.model.Output(
                    "returnValue", 
                    out.value(), 
                    out.type().isEmpty() ? method.getReturnType().getName() : out.type()));
        }
    }

    private List<com.netcracker.tc.model.Output> getOutputsObject(Class<?> type)
    {
        List<com.netcracker.tc.model.Output> res = new ArrayList<com.netcracker.tc.model.Output>();

        for (Field field : type.getFields())
            if (field.isAnnotationPresent(Output.class)) {
                Output out = field.getAnnotation(Output.class);
                res.add(new com.netcracker.tc.model.Output(
                        field.getName(), 
                        getTitle(out.value(), field.getName()), 
                        out.type().isEmpty() ? field.getClass().getName() : out.type()));
            }
        
        return res;
    }

    @SuppressWarnings("unchecked")
    private List<Parameter> getScenatioParameters(Method scenario)
    {
        if (scenario.getParameterTypes().length == 0)
            return Collections.EMPTY_LIST;
        
        Annotation[][] anns = scenario.getParameterAnnotations();
        
        if (anns[0].length == 1 && anns[0][0] instanceof Params)
            return getFieldParameters(scenario.getParameterTypes()[0]);
        else
            return getParamParameters(scenario);
    }

    private List<Parameter> getParamParameters(Method scenario)
    {
        List<Parameter> props = new ArrayList<Parameter>();
        
        Class<?>[] types = scenario.getParameterTypes();
        Annotation[][] anns = scenario.getParameterAnnotations();
        
        for (int i = 0; i < types.length; i++) {
            Annotations as = new Annotations(anns[i]);
            String name = as.getAnnotation(Param.class).value();
            props.add(getParameter(name, types[i], as));
        }
        
        return props;
    }

    private List<Parameter> getFieldParameters(Class<?> type)
    {
        List<Parameter> props = new ArrayList<Parameter>();

        for (Field field : type.getFields())
            if (field.isAnnotationPresent(Param.class)) {
                props.add(getParameter(
                        field.getName(), 
                        field.getType(),
                        field));
            }
        
        return props;
    }

    private Parameter getParameter(String name, Class<?> type, AnnotatedElement elem)
    {
        Param param = elem.getAnnotation(Param.class);
        Type pType = readType(name, type, elem);
        Object value = (param.defValue().equals(Param.VALUE_NONE))
                ? pType.defaultValue()
                : pType.valueOf(param.defValue());
        return new Parameter(
                name, 
                getTitle(param.title(), name), 
                param.description(), 
                pType, 
                value);
    }

    private Type readType(String name, Class<?> type, AnnotatedElement elem)
    {
        for (TypeReader reader : readers) {
            Type t = reader.read(type, elem);
            if (t != null)
                return t;
        }
        throw new RuntimeException("Unknown type for "+ name);
    }

    private String getTitle(String title, String name)
    {
        if (title.isEmpty())
            title = decamelcase(name);
        return title;
    }
    
    private String decamelcase(String name)
    {
        Matcher m = Pattern.compile("([A-Z]|^)[^A-Z]*").matcher(name);
        StringBuilder sb = new StringBuilder();
        while (m.find())
            if (sb.length() == 0)
                sb.append(m.group().substring(0, 1).toUpperCase()).
                   append(m.group().substring(1).toLowerCase());
            else
                sb.append(" ").append(m.group().toLowerCase());
        return sb.toString();
    }

    public Schemas getActions()
    {
        return actions;
    }
}

final class Annotations implements AnnotatedElement
{
    private final Annotation[] anns;

    public Annotations(Annotation[] anns)
    {
        this.anns = anns;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
    {
        for (Annotation a : anns)
            if (annotationClass.isInstance(a))
                return (T) a;
        return null;
    }

    @Override
    public Annotation[] getAnnotations()
    {
        return anns;
    }

    @Override
    public Annotation[] getDeclaredAnnotations()
    {
        return anns;
    }

    @Override
    public boolean isAnnotationPresent(
            Class<? extends Annotation> annotationClass)
    {
        for (Annotation a : anns)
            if (annotationClass.isInstance(a))
                return true;
        return false;
    }
    
}













