/**
 * Test cases configuration package.
 * 
 * {@link com.netcracker.sova.model.Configuration} contains {@link com.netcracker.sova.model.Test}*
 * {@link com.netcracker.sova.model.Test} contains {@link com.netcracker.sova.model.Scenario}*
 * {@link com.netcracker.sova.model.Scenario} contains {@link com.netcracker.sova.model.Parameter}* and {@link com.netcracker.sova.model.Output}*
 * {@link com.netcracker.sova.model.Schemas} contains {@link com.netcracker.sova.model.Schema}*
 * {@link com.netcracker.sova.model.Schema} is prototype of {@link com.netcracker.sova.model.Scenario}
 * 
 * <h1>Creating new parameter types</h1>
 * To create new parameter type you need:
 * <ol>
 *      <li>Implement {@link com.netcracker.sova.model.Type}
 *      <li>Implement {@link com.netcracker.sova.ui.EditorWidget}
 *      <li>Implement reader/writer modules.
 *          <p>For XML storage you need to implement:
 *          <ul>
 *              <li>{@link com.netcracker.sova.io.xml.ValueReader}
 *              <li>{@link com.netcracker.sova.io.xml.ValueWriter}
 *          </ul>
 *      <li>Implement {@link com.netcracker.sova.annotated.TypeReader}
 *      <li>(<em>optional</em>) Create type-specific annotations with <code>&#064;Target({PARAMETER,FIELD})</code>
 * </ol>
 * 
 * @see com.netcracker.tconf.types.standard
 */
package com.netcracker.sova.model;