package com.netcracker.tconf.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.netcracker.tconf.annotated.anns.Default;
import com.netcracker.tconf.annotated.anns.Output;
import com.netcracker.tconf.annotated.anns.Outputs;
import com.netcracker.tconf.annotated.anns.Param;
import com.netcracker.tconf.annotated.anns.Params;
import com.netcracker.tconf.annotated.anns.Scenario;
import com.netcracker.tconf.annotated.anns.Scenarios;
import com.netcracker.tconf.model.Parameter;
import com.netcracker.tconf.model.Schema;
import com.netcracker.tconf.model.Schemas;
import com.netcracker.tconf.model.Type;
import com.netcracker.util.ClassRegistry;

/**
 * This class represents logic for analyzing test scenario classes and
 * collect it's information into {@link Schemas}.
 * <p>
 * Test cases class must be annotated with {@link Scenarios},
 * public non-static methods of this class, annotated with {@link Scenario}
 * will be mapped into {@link Schema} object.
 * 
 * Scenario name will be "ClassName.methodName",
 * parameters are named "[{@link #METHOD_PARAM_PREFIX}][index]",
 * or "[{@link #METHOD_PARAM_PREFIX}][index].objectField"
 * Scenario outputs are named {@link #RETURN_OUTPUT_NAME} for
 * simple return value or field name for complex output object.
 * 
 * @author Aleksej Dsiuba <Dsiuba@NetCracker.com>
 * 
 * @see com.netcracker.tconf.annotated.anns
 */
public class AnnotationSchemaReader implements ClassRegistry
{
    public static final String METHOD_PARAM_PREFIX = "arg#";

    public static final String RETURN_OUTPUT_NAME = "::return";

    private final List<TypeReader> readers = new ArrayList<TypeReader>();

    public void register(Class<?> type)
    {
        try {
            if (TypeReader.class.isAssignableFrom(type)) {
                readers.add(type.asSubclass(TypeReader.class).newInstance());
                System.out.println("AnnotationSchemaReader: registered type: "+ type);
            }
        }
        catch (Exception e) {
            System.out.println("AnnotationSchemaReader: Failed to load "+ type);
        }
    }
    
    private final Schemas actions = new Schemas();
    
    public final ClassRegistry analizator = new ClassRegistry() {
        public void register(Class<?> type) {
            analyze(type); } };
    
    public void analyze(Class<?> type)
    {
        if (type.isAnnotationPresent(Scenarios.class) && 
                ! Modifier.isAbstract(type.getModifiers())) {
            
            System.out.println("Scenarios found: "+ type.getName());
            
            String group = getScenariosTitle(type); 
            
            for (Method method : type.getMethods()) {
                if (method.isAnnotationPresent(Scenario.class)) {
                    actions.add(group, analyzeScenario(type, method));
                }
            }
        }
    }

    public Schemas getActions()
    {
        return actions;
    }

    // --- private section ----------------------------------------------------

    private String getScenariosTitle(Class<?> type)
    {
        String title = type.getAnnotation(Scenarios.class).value();
        if (title.isEmpty()) {
            title = decamelcase(type.getSimpleName());
        }
        return title;
    }

    private Schema analyzeScenario(Class<?> type, Method method)
    {
        return new Schema(
                method.getDeclaringClass().getName() + "." + method.getName(), 
                getScenarioTitle(method), 
                getScenatioParameters(type, method), 
                getScenarioOutputs(method));
    }

    private String getScenarioTitle(Method method)
    {
        String title = method.getAnnotation(Scenario.class).value();
        if (title.isEmpty()) {
            title = decamelcase(method.getName());
        }
        return title;
    }

    @SuppressWarnings("unchecked")
    private List<com.netcracker.tconf.model.Output> getScenarioOutputs(Method method)
    {
        if (method.getReturnType() == Void.TYPE)
            return Collections.EMPTY_LIST;
        
        if (method.getReturnType().isAnnotationPresent(Outputs.class) &&
                ! method.isAnnotationPresent(Output.class)) {
            return getOutputsObject(method.getReturnType());
        }
        else if (method.isAnnotationPresent(Output.class)) {
            return Collections.singletonList(
                    getOutput(
                            method.getAnnotation(Output.class), 
                            RETURN_OUTPUT_NAME, 
                            method.getReturnType().getName()));
        }
        else
            throw new SchemaFormatException(
                    "Scenario method should either return void, be annotated with Output " +
                    "or return class, annotated with Outputs: "+ method);
    }

    private com.netcracker.tconf.model.Output getOutput(Output out,
            String name, String type)
    {
        type = oneOf(out.type(), type);
        String title = oneOf(out.value(), decamelcase(name), type);
        return new com.netcracker.tconf.model.Output(name, title, type);
    }

    private String oneOf(String ... strings)
    {
        for (String str : strings)
            if (str != null && ! str.isEmpty())
                return str;
        return strings[strings.length - 1];
    }

    private List<com.netcracker.tconf.model.Output> getOutputsObject(Class<?> type)
    {
        List<com.netcracker.tconf.model.Output> res = new ArrayList<com.netcracker.tconf.model.Output>();

        for (Field field : getNonstaticFields(type)) {
            if (field.isAnnotationPresent(Output.class)) {
                res.add(
                    getOutput(
                        field.getAnnotation(Output.class), 
                        field.getName(), 
                        field.getType().getName()));
            }
        }
        return res;
    }

    private List<Parameter> getScenatioParameters(Class<?> cls, Method scenario)
    {
        List<Parameter> params = new ArrayList<Parameter>();

        Class<?>[] types = scenario.getParameterTypes();
        Annotation[][] anns = scenario.getParameterAnnotations();
        
        for (int i = 0; i < types.length; i++) {
            Annotations ans = new Annotations(anns[i]);
            Class<?> type = types[i];

            if (ans.isAnnotationPresent(Params.class)) {
                Object obj = tryToCreate(type);
                for (Field field : getNonstaticFields(type)) {
                    if (field.isAnnotationPresent(Param.class)) {
                        params.add(
                            getParameter(
                                METHOD_PARAM_PREFIX+ i + "." + field.getName(),
                                field.getName(),
                                field.getType(),
                                field,
                                tryToGetValue(obj, field)));
                    }
                }
            }
            else if (ans.isAnnotationPresent(Param.class)) {
                params.add(getParameter(METHOD_PARAM_PREFIX+ i, "Parameter "+ (i+1), type, ans, null));
            }
            else
                throw new SchemaFormatException(
                        "Parameter "+ scenario + "#" + i +" must have Param or Params annotation");
        }
        return params;
    }

    private List<Field> getNonstaticFields(Class<?> type)
    {
        List<Field> fields = new ArrayList<Field>();
        
        while (type != null) {
            for (Field f : type.getDeclaredFields())
                if (! Modifier.isStatic(f.getModifiers()))
                    fields.add(f);
            type = type.getSuperclass();
        }
        
        return fields;
    }

    private Object tryToGetValue(Object obj, Field field)
    {
        try {
            field.setAccessible(true);
            return field.get(obj);
        }
        catch (Exception e) {
            System.out.println("Getting "+ obj +"."+ field +": "+ e.getMessage());
            return null;
        }
    }

    private Object tryToCreate(Class<?> type)
    {
        try {
            return type.newInstance();
        }
        catch (Exception e) {
            System.out.println("Creating "+ type +": "+ e.getMessage());
            return null;
        }
    }

    private Parameter getParameter(String name, String tname, 
            Class<?> tp, AnnotatedElement elem, Object val)
    {
        Param param = elem.getAnnotation(Param.class);
        Type type = readType(name, tp, elem);
        if (elem.isAnnotationPresent(Default.class)) {
            val = type.valueOf(elem.getAnnotation(Default.class).value());
        }
        else if (val == null) {
            val = type.defaultValue();
        }
        return new Parameter(
                name, 
                oneOf(param.value(), decamelcase(tname)), 
                param.description(), 
                type, 
                val);
    }

    private Type readType(String name, Class<?> type, AnnotatedElement elem)
    {
        for (TypeReader reader : readers) {
            Type t = reader.read(type, elem);
            if (t != null)
                return t;
        }
        throw new SchemaFormatException("Unknown type for "+ name + ":" + type);
    }

    private String decamelcase(String name)
    {
        name = name.substring(0,1).toUpperCase() + name.substring(1);
        Matcher m = Pattern.compile("([A-Z]?[a-z]+)|[A-Z]+_|[^A-Za-z]+").matcher(name);
        Pattern p = Pattern.compile("[A-Z]+_");
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String part = m.group();
            if (p.matcher(part).matches()) {
                part = part.substring(0, part.length() - 1);
            }
            else if (sb.length() > 0) {
                part = part.toLowerCase();
            }
            sb.append(sb.length() > 0 ? " " : "").append(part);
        }
        return sb.toString();
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













