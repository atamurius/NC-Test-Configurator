package com.netcracker.xml;

import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Constructor
{
    private final Document document;
    private Element context;

    public Constructor() throws ParserConfigurationException
    {
        this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }
    
    public Constructor node(String name)
    {
        Element element = document.createElement(name);
        if (context == null)
            document.appendChild(element);
        else
            context.appendChild(element);
        context = element;
        return this;
    }
    
    public Constructor attr(String name, String value)
    {
        context.setAttribute(name, value);
        return this;
    }
    
    public Constructor text(String text)
    {
        context.appendChild(document.createTextNode(text));
        return this;
    }
    
    public Constructor end()
    {
        context = (Element) context.getParentNode();
        return this;
    }
    
    public void writeTo(OutputStream out) throws TransformerException
    {
        writeTo(null, out);
    }

    public void writeTo(String doctype, OutputStream out) throws TransformerException
    {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        if (doctype != null)
            t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype);
        t.transform(new DOMSource(document), new StreamResult(out));
    }
}
