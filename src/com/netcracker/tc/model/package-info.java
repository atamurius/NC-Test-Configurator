/**
 * Test cases configuration package.
 * 
 * {@link com.netcracker.tc.model.Configuration} contains {@link com.netcracker.tc.model.Test}*
 * {@link com.netcracker.tc.model.Test} contains {@link com.netcracker.tc.model.Scenario}*
 * {@link com.netcracker.tc.model.Scenario} contains {@link com.netcracker.tc.model.Parameter}* and {@link com.netcracker.tc.model.Output}*
 * {@link com.netcracker.tc.model.Schemas} contains {@link com.netcracker.tc.model.Schema}*
 * {@link com.netcracker.tc.model.Schema} is prototype of {@link com.netcracker.tc.model.Scenario}
 * 
 * <h1>Creating new parameter types</h1>
 * To create new parameter type you need:
 * <ol>
 *      <li>Implement {@link com.netcracker.tc.model.Type}
 *      <li>Implement {@link com.netcracker.tc.configurator.ui.EditorWidget}
 *      <li>Implement reader/writer modules.
 *          <p>For XML storage you need to implement:
 *          <ul>
 *              <li>{@link com.netcracker.tc.configurator.data.xml.ValueReader}
 *              <li>{@link com.netcracker.tc.configurator.data.xml.ValueWriter}
 *          </ul>
 *      <li>Implement {@link com.netcracker.tc.configurator.data.TypeReader}
 *      <li>(<em>optional</em>) Create type-specific annotations with <code>&#064;Target({PARAMETER,FIELD})</code>
 * </ol>
 * 
 * @see com.netcracker.tc.types.standard
 */
package com.netcracker.tc.model;