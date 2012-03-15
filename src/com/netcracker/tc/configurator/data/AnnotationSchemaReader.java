package com.netcracker.tc.configurator.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.netcracker.tc.model.Action;
import com.netcracker.tc.model.ActionGroup;
import com.netcracker.tc.model.Property;
import com.netcracker.tc.model.Result;
import com.netcracker.tc.model.Type;
import com.netcracker.tc.tests.anns.Output;
import com.netcracker.tc.tests.anns.Parameter;
import com.netcracker.tc.tests.anns.Scenario;
import com.netcracker.tc.tests.anns.Scenarios;
import com.netcracker.tc.types.standard.BoolType;
import com.netcracker.tc.types.standard.EnumType;
import com.netcracker.tc.types.standard.IntType;
import com.netcracker.tc.types.standard.RefType;
import com.netcracker.tc.types.standard.StringType;
import com.netcracker.tc.types.standard.anns.Int;
import com.netcracker.tc.types.standard.anns.Ref;
import com.netcracker.tc.types.standard.anns.StringParam;

public class AnnotationSchemaReader implements SchemaReader
{
    public interface ParameterTypeReader
    {
        /**
         * Tries to read parameter type.
         * @param param field with {@link Parameter} annotation.
         * @return parameter type or null, if this reader don't know this type.
         * @throws DataException if type has errors
         */
        Type read(Field param) throws DataException;
    }
    
    private final String[] classes;
    private final List<ParameterTypeReader> readers = new ArrayList<ParameterTypeReader>();

    /**
     * @param classes list of class names with {@link Scenario} annotation.
     */
    public AnnotationSchemaReader(String ... classes)
    {
        this.classes = classes;
    }
    
    @Override
    public ActionGroup readActionGroup() throws DataException
    {
        try {
            ActionGroup group = new ActionGroup();
            
            for (String tName : classes) {
                Class<?> type = Class.forName(tName);
                
                if (type.isAnnotationPresent(Scenario.class)) {
                    Action action = new Action(
                            type.getName(), 
                            getTitle(type.getAnnotation(Scenario.class).value(), type.getSimpleName()), 
                            getScenatioParameters(type), 
                            getScenarioOutputs(type));
                    group.add(getScenarioGroup(type), action);
                }
            }
            
            return group;
        }
        catch (ClassNotFoundException e) {
            throw new DataException(e);
        }
    }

    private String getScenarioGroup(Class<?> type) throws DataException
    {
        String group = type.getAnnotation(Scenario.class).group();
        if (group.isEmpty() && 
                type.isMemberClass() &&
                type.getEnclosingClass().isAnnotationPresent(Scenarios.class)) {
            
            group = type.getEnclosingClass().getAnnotation(Scenarios.class).value();
            if (group.isEmpty())
                group = type.getEnclosingClass().getSimpleName();
        }
        if (group.isEmpty())
            throw new DataException(
                    "Scenario.group or Scenarios.value for enclosing class must be set for "+ type);
        return group;
    }

    private List<Result> getScenarioOutputs(Class<?> type)
    {
        List<Result> outs = new ArrayList<Result>();
        
        for (Field field : type.getFields())
            if (field.isAnnotationPresent(Output.class)) {
                Output annotation = field.getAnnotation(Output.class);
                outs.add(new Result(
                        field.getName(), 
                        getTitle(annotation.value(), field.getName()), 
                        annotation.type().isEmpty() ? field.getType().getName() : annotation.type()));
            }
        
        return outs;
    }

    private List<Property> getScenatioParameters(Class<?> type) throws DataException
    {
        List<Property> props = new ArrayList<Property>();
        for (Field field : type.getFields()) 
            if (field.isAnnotationPresent(Parameter.class)) {
                props.add(new Property(
                        field.getName(), 
                        getTitle(field.getAnnotation(Parameter.class).value(), field.getName()), 
                        field.getAnnotation(Parameter.class).description(), 
                        getPropertyType(field)));
                // TODO: property value
            }
        return props;
    }

    private Type getPropertyType(Field field) throws DataException
    {
        for (ParameterTypeReader reader : readers) {
            Type type = reader.read(field);
            if (type != null)
                return type;
        }
        throw new DataException("Undefined type: "+ field);
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

    public AnnotationSchemaReader registerStandard()
    {
        readers.add(new ParameterTypeReader() {
            public Type read(Field param) throws DataException
            {
                if (param.getType() == Boolean.TYPE ||
                    param.getType() == Boolean.class)
                    
                    return new BoolType();
                else
                    return null;
            }
        });
        readers.add(new ParameterTypeReader() {
            public Type read(Field param) throws DataException
            {
                if (param.getType() == Integer.TYPE ||
                    param.getType() == Integer.class) {
                    
                    Int a = param.getAnnotation(Int.class);
                    return (a == null)
                            ? new IntType()
                            : new IntType(a.min(), a.max());
                }
                else
                    return null;
            }
        });
        readers.add(new ParameterTypeReader() {
            public Type read(Field param) throws DataException
            {
                if (param.getType() == String.class &&
                        param.getAnnotation(Parameter.class).type() == Type.class &&
                        ! param.isAnnotationPresent(Ref.class)) {
                    
                    StringParam a = param.getAnnotation(StringParam.class);
                    return (a == null)
                            ? new StringType()
                            : new StringType(a.required(), a.max(), a.pattern());
                }
                else
                    return null;
            }
        });
        readers.add(new ParameterTypeReader() {
            public Type read(Field param) throws DataException
            {
                if (param.isAnnotationPresent(Ref.class)) {
                    
                    Ref a = param.getAnnotation(Ref.class);
                    return new RefType(
                            a.value().isEmpty() ? param.getType().getName() : a.value(), 
                            a.required());
                }
                else
                    return null;
            }
        });
        readers.add(new ParameterTypeReader() {
            public Type read(Field param) throws DataException
            {
                // TODO: check other variants
                if (Enum.class.isAssignableFrom(param.getType())) {
                    try {
                        Object[] values = (Object[]) param.getType().getMethod("values").invoke(null);
                        return new EnumType(null, values);
                    }
                    catch (Exception e) {
                        throw new DataException(e);
                    }
                }
                else
                    return null;
            }
        });
        return this;
    }
}















