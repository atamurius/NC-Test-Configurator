/**
 * Annotations for adding meta-information to test case classes, that is used to configure test scenarios with
 * {@link com.netcracker.tc.configurator.ui.Configurator}.
 * 
 * <h1>Adding information about test scenarios using annotations</h1>
 * 
 * <h2>Test scenarios</h2>
 * <p>
 * Test scenarios are described as methods, annotated with {@link com.netcracker.tc.tests.anns.Scenario}, in class,
 * annotated with {@link com.netcracker.tc.tests.anns.Scenarios}:
 * <pre>
 *  &#064;Scenatios
 *  public class Person
 *  {
 *      &#064;Scenario
 *      public void create();
 * 
 *      &#064;Scenario
 *      public void remove();
 *  }
 * </pre>
 * 
 * <h2>Test scenario input parameters</h2>
 * Scenario input parameters can be described in two ways:
 * <ol>
 * 
 * <li> scenario method parameters, annotated with {@link com.netcracker.tc.tests.anns.Param} 
 * and (optional) type-specific annotations.
 * <pre>
 * &#064;Scenario
 * public void create(&#064;Param("name") String name) { ... }
 * </pre>
 * <p>
 * For this type of parameters {@link Param#value} must be set, other attributes are optional:
 * <code>&#064;Param(value = "name", title = "Name", description = "Person name", defValue = "Jhon Doe")</code>
 * 
 * <li> single scenario method parameter, annotated with {@link com.netcracker.tc.tests.anns.Params} of class with fields, 
 * annotated with {@link com.netcracker.tc.tests.anns.Param}.
 * <pre>
 * public class PersonData
 * {
 *     &#064;Param
 *     public String name;
 * }   
 * 
 * &#064;Scenario
 * public void create(&#064;Params PersonData person) { ... }
 * </pre>
 * <p>
 * For this type of parameter passing {@link com.netcracker.tc.tests.anns.Param#value} is optional.
 * 
 * </ol>
 * <p>
 * Scenario parameter type is infered from field/parameter java type and type-specific annotations
 * (for example {@link com.netcracker.tc.types.standard.ref.Ref}).
 * 
 * <h2>Test scenario output results</h2>
 * Scenario output results can be described also in two ways:
 * <ol>
 * 
 * <li> single scenario method return value, configured with {@link com.netcracker.tc.tests.anns.Scenario#output}.
 * <pre>
 * &#064;Scenario(ouput = &#064;Output("Person"))
 * public PersonData create() { ... return id; }
 * </pre>
 * 
 * <li> scenario method returns object of class, annotated with {@link com.netcracker.tc.tests.anns.Outputs},
 * it's fields, annotated with {@link com.netcracker.tc.tests.anns.Output}, will be independent scenario output.
 * <pre>
 * &#064;Outputs
 * public class Result
 * {
 *     &#064;Output(type = "person:id")
 *     public String employee;
 *     
 *     &#064;Output(type = "person:id")
 *     public String manager;
 *     
 *     &#064;Output(type = "time")
 *     public int timeElapsed;
 * }
 * &#064;Scenario
 * public Result create() { ... }
 * </pre>
 * </ol>
 * <p>
 * Type of output result can be set as string id (for example "person:id"), or as type name, if type is not set.
 * 
 * </body>
 * </html>
 */
package com.netcracker.tc.tests.anns;