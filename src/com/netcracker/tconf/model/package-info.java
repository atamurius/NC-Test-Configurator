/**
 * Test cases configuration package.
 * 
 * {@link com.netcracker.tconf.model.Configuration} contains {@link com.netcracker.tconf.model.Test}*
 * {@link com.netcracker.tconf.model.Test} contains {@link com.netcracker.tconf.model.Scenario}*
 * {@link com.netcracker.tconf.model.Scenario} contains {@link com.netcracker.tconf.model.Parameter}* and {@link com.netcracker.tconf.model.Output}*
 * {@link com.netcracker.tconf.model.Schemas} contains {@link com.netcracker.tconf.model.Schema}*
 * {@link com.netcracker.tconf.model.Schema} is prototype of {@link com.netcracker.tconf.model.Scenario}
 * 
 * <h1>Creating new parameter types</h1>
 * To create new parameter type you need:
 * <ol>
 *      <li>Implement {@link com.netcracker.tconf.model.Type}
 *      <li>Implement {@link com.netcracker.tconf.ui.EditorWidget}
 *      <li>Implement reader/writer modules.
 *          <p>For XML storage you need to implement:
 *          <ul>
 *              <li>{@link com.netcracker.tconf.io.xml.ValueReader}
 *              <li>{@link com.netcracker.tconf.io.xml.ValueWriter}
 *          </ul>
 *      <li>Implement {@link com.netcracker.tconf.annotated.TypeReader}
 *      <li>(<em>optional</em>) Create type-specific annotations with <code>&#064;Target({PARAMETER,FIELD})</code>
 * </ol>
 * 
 * @see com.netcracker.tconf.types.standard
 */
package com.netcracker.tconf.model;