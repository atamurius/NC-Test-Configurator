package com.netcracker.sova.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.netcracker.sova.annotated.anns.Default;
import com.netcracker.sova.annotated.anns.Outputs;
import com.netcracker.sova.annotated.anns.Param;
import com.netcracker.sova.annotated.anns.Params;
import com.netcracker.sova.annotated.anns.Scenario;
import com.netcracker.sova.annotated.anns.Scenarios;
import com.netcracker.sova.model.Output;
import com.netcracker.sova.model.Parameter;
import com.netcracker.sova.model.Schema;
import com.netcracker.sova.model.Schemas;
import com.netcracker.sova.model.Type;
import com.netcracker.sova.types.ref.RefTypeReader;
import com.netcracker.util.Case;
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
 * @see com.netcracker.sova.annotated.anns
 */
public class AnnotationSchemaReader implements ClassRegistry
{
    public static final String CLASS_PARAM_PREFIX = "this";

    public static final String METHOD_PARAM_PREFIX = "arg#";

    public static final String RETURN_OUTPUT_NAME = "::return";

    public final TypeReaders TYPE_READERS = new TypeReaders();
    {
        // register default types
        TYPE_READERS.register(RefTypeReader.class);
    }
    
    public final Schemas actions = new Schemas();
    
    public void register(Class<?> type)
    {
        if (type.isAnnotationPresent(Scenarios.class) && 
                ! Modifier.isAbstract(type.getModifiers())) {
            
            fetchScenarios(type);
        }
    }

    // --- private section ----------------------------------------------------

    // --- scenarios ---

    private void fetchScenarios(Class<?> scenarios)
    {
        String title = scenarios.getAnnotation(Scenarios.class).value();
        if (title.isEmpty()) {
            title = Case.fromCamel(scenarios.getSimpleName());
        }
        
        for (Method method : scenarios.getMethods()) {
            if (method.isAnnotationPresent(Scenario.class)) {
                actions.add(title, fetchScenario(scenarios, method));
            }
        }
    }

    // --- scenario ---

    private Schema fetchScenario(Class<?> scenarios, Method scenario)
    {
        String title = scenario.getAnnotation(Scenario.class).value();
        if (title.isEmpty()) {
            title = Case.fromCamel(scenario.getName());
        }
        return new Schema(
                scenarios.getName() + "." + scenario.getName(), 
                title, 
                fetchParameters(scenarios, scenario), 
                fetchOutputs(scenario));
    }

    // --- outputs ---

    @SuppressWarnings("unchecked")
    private List<Output> fetchOutputs(Method scenario)
    {
        if (scenario.getReturnType() == Void.TYPE)
            return Collections.EMPTY_LIST;
        
        if (scenario.getReturnType().isAnnotationPresent(Outputs.class) &&
                ! scenario.isAnnotationPresent(com.netcracker.sova.annotated.anns.Output.class)) {
            return fetchOutputsObject(scenario.getReturnType());
        }
        else if (scenario.isAnnotationPresent(com.netcracker.sova.annotated.anns.Output.class)) {
            return Collections.singletonList(
                    fetchOutput(
                            scenario.getAnnotation(com.netcracker.sova.annotated.anns.Output.class), 
                            RETURN_OUTPUT_NAME, 
                            scenario.getReturnType().getName()));
        }
        else
            throw new SchemaFormatException(
                    "Scenario method should either return void, be annotated with Output " +
                    "or return class, annotated with Outputs: "+ scenario);
    }

    private Output fetchOutput(com.netcracker.sova.annotated.anns.Output out, String name, String type)
    {
        type = oneOf(out.type(), type);
        String title = oneOf(out.value(), Case.fromCamel(name), type);
        return new com.netcracker.sova.model.Output(name, title, type);
    }

    private List<Output> fetchOutputsObject(Class<?> type)
    {
        List<Output> res = new ArrayList<Output>();

        for (Field field : Reflection.nonstaticFieldsOf(type)) {
            if (field.isAnnotationPresent(com.netcracker.sova.annotated.anns.Output.class)) {
                res.add(
                    fetchOutput(
                        field.getAnnotation(com.netcracker.sova.annotated.anns.Output.class), 
                        field.getName(), 
                        field.getType().getName()));
            }
        }
        return res;
    }
    
    // --- parameters ---

    private List<Parameter> fetchParameters(Class<?> scenarios, Method scenario)
    {
        List<Parameter> params = new ArrayList<Parameter>();

        // parameters
        
        Class<?>[] types = scenario.getParameterTypes();
        Annotation[][] anns = scenario.getParameterAnnotations();
        
        for (int i = 0; i < types.length; i++) {
            Annotations ans = new Annotations(anns[i]);
            Class<?> type = types[i];

            if (ans.isAnnotationPresent(Params.class)) {
                fetchClassParameters(params, type, METHOD_PARAM_PREFIX + i + ".");
            }
            else if (ans.isAnnotationPresent(Param.class)) {
                params.add(fetchParameter(METHOD_PARAM_PREFIX+ i, "Parameter "+ (i+1), type, ans, null));
            }
            else
                throw new SchemaFormatException(
                        "Parameter "+ scenario + "#" + i +" must have Param or Params annotation");
        }
        // fields
        
        fetchClassParameters(params, scenarios, CLASS_PARAM_PREFIX + ".");
        
        return params;
    }

    private void fetchClassParameters(List<Parameter> params, Class<?> type, String prefix)
    {
        Object obj = Reflection.maybeCreate(type);
        
        for (Field field : Reflection.nonstaticFieldsOf(type)) {
            
            if (field.isAnnotationPresent(Param.class)) {
                params.add(
                    fetchParameter(
                        prefix + field.getName(),
                        field.getName(),
                        field.getType(),
                        field,
                        Reflection.maybeGet(obj, field)));
            }
        }
    }

    private Parameter fetchParameter(
            String name, String tname, Class<?> tp, AnnotatedElement elem, Object val)
    {
        Param param = elem.getAnnotation(Param.class);
        Type type = TYPE_READERS.readType(name, tp, elem);
        if (elem.isAnnotationPresent(Default.class)) {
            val = type.valueOf(elem.getAnnotation(Default.class).value());
        }
        else if (val == null) {
            val = type.defaultValue();
        }
        return new Parameter(
                name, 
                oneOf(param.value(), Case.fromCamel(tname)), 
                param.description(), 
                type, 
                val);
    }

    // --- util ---
    
    private final static String oneOf(String ... strings)
    {
        for (String str : strings)
            if (str != null && ! str.isEmpty())
                return str;
        return strings[strings.length - 1];
    }
}










