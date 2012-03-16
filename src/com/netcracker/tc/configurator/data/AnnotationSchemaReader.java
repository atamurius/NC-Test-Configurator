package com.netcracker.tc.configurator.data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.netcracker.tc.model.Action;
import com.netcracker.tc.model.ActionGroup;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Result;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.tests.anns.Output;
import com.netcracker.tc.tests.anns.Outputs;
import com.netcracker.tc.tests.anns.Param;
import com.netcracker.tc.tests.anns.Params;
import com.netcracker.tc.tests.anns.Scenario;
import com.netcracker.tc.tests.anns.Scenarios;

public class AnnotationSchemaReader
{
    private final List<TypeReader> readers = new ArrayList<TypeReader>();

    public void register(Class<?> type) throws InstantiationException, IllegalAccessException
    {
        if (TypeReader.class.isAssignableFrom(type))
            readers.add(type.asSubclass(TypeReader.class).newInstance());
    }
    
    private final ActionGroup actions = new ActionGroup();
    
    public void analyze(Class<?> type)
    {
        if (type.isAnnotationPresent(Scenarios.class)) {
            String group = getTitle(type.getAnnotation(Scenarios.class).value(), type.getSimpleName()); 
            
            for (Method method : type.getMethods())
                if (method.isAnnotationPresent(Scenario.class))
                    actions.add(group, getAction(method));
        }
    }

    private Action getAction(Method method)
    {
        return new Action(
                method.getDeclaringClass().getName() + "." + method.getName(), 
                getTitle(method.getAnnotation(Scenario.class).value(), method.getName()), 
                getScenatioParameters(method), 
                getScenarioOutputs(method));
    }

    @SuppressWarnings("unchecked")
    private List<Result> getScenarioOutputs(Method method)
    {
        if (method.getReturnType() == Void.TYPE)
            return Collections.EMPTY_LIST;
        
        if (method.getReturnType().isAnnotationPresent(Outputs.class))
            return getOutputsObject(method.getReturnType());
        else {
            Output out = method.getAnnotation(Scenario.class).output();
            return Collections.singletonList(new Result(
                    "returnValue", 
                    out.value(), 
                    out.type().isEmpty() ? method.getReturnType().getName() : out.type()));
        }
    }

    private List<Result> getOutputsObject(Class<?> type)
    {
        List<Result> res = new ArrayList<Result>();

        for (Field field : type.getFields())
            if (field.isAnnotationPresent(Output.class)) {
                Output out = field.getAnnotation(Output.class);
                res.add(new Result(
                        field.getName(), 
                        getTitle(out.value(), field.getName()), 
                        out.type().isEmpty() ? field.getClass().getName() : out.type()));
            }
        
        return res;
    }

    @SuppressWarnings("unchecked")
    private List<Property> getScenatioParameters(Method scenario)
    {
        if (scenario.getParameterTypes().length == 0)
            return Collections.EMPTY_LIST;
        
        Annotation[][] anns = scenario.getParameterAnnotations();
        
        if (anns[0].length == 1 && anns[0][0] instanceof Params)
            return getFieldParameters(scenario.getParameterTypes()[0]);
        else
            return getParamParameters(scenario);
    }

    private List<Property> getParamParameters(Method scenario)
    {
        List<Property> props = new ArrayList<Property>();
        
        Class<?>[] types = scenario.getParameterTypes();
        Annotation[][] anns = scenario.getParameterAnnotations();
        
        for (int i = 0; i < types.length; i++) {
            String name = findAnnotation(anns[i], Param.class).value();
            props.add(getParameter(
                    name, types[i], anns[i]));
        }
        
        return props;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T findAnnotation(Annotation[] annotations,
            Class<T> type)
    {
        for (Annotation a : annotations)
            if (type.isInstance(a))
                return (T) a;
        return null;
    }

    private List<Property> getFieldParameters(Class<?> type)
    {
        List<Property> props = new ArrayList<Property>();

        for (Field field : type.getFields())
            if (field.isAnnotationPresent(Param.class)) {
                props.add(getParameter(
                        field.getName(), 
                        field.getType(),
                        field.getAnnotations()));
            }
        
        return props;
    }

    private Property getParameter(String name, Class<?> type, Annotation[] annotations)
    {
        Param param = findAnnotation(annotations, Param.class);
        Type pType = readType(name, type, annotations);
        Object value = (param.defValue().equals(Param.VALUE_NONE))
                ? pType.defaultValue()
                : pType.valueOf(param.defValue());
        return new Property(
                name, 
                getTitle(param.title(), name), 
                param.description(), 
                pType, 
                value);
    }

    private Type readType(String name, Class<?> type, Annotation[] annotations)
    {
        for (TypeReader reader : readers) {
            Type t = reader.read(type, annotations);
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

    public ActionGroup getActions()
    {
        return actions;
    }
}















