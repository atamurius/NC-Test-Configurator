package com.netcracker.util.xml;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Xml
{
    private static final XPath XPATH = XPathFactory.newInstance().newXPath();

    public static Document load(String filename) throws SAXException, IOException, ParserConfigurationException
    {
        return load(new BufferedInputStream(new FileInputStream(filename)));
    }
    
    public static Document load(InputStream in) throws SAXException, IOException, ParserConfigurationException
    {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringComments(true);
            dbf.setIgnoringElementContentWhitespace(true);
            return dbf.newDocumentBuilder().parse(in);
        }
        finally {
            in.close();
        }
    }
    
    public static Iterable<Element> elements(Node ctx, String xpath)
    {
        try {
            return new Elements((NodeList) XPATH.evaluate(xpath, ctx, XPathConstants.NODESET));
        }
        catch (XPathExpressionException e) {
            throw new XPathExpressionRuntimeException(e);
        }
    }
    
    public static Element element(Node ctx, String xpath)
    {
        try {
            return (Element) XPATH.evaluate(xpath, ctx, XPathConstants.NODE);
        }
        catch (XPathExpressionException e) {
            throw new XPathExpressionRuntimeException(e);
        }
    }
    
    public static String value(Node ctx, String xpath)
    {
        try {
            return (String) XPATH.evaluate(xpath, ctx, XPathConstants.STRING);
        }
        catch (XPathExpressionException e) {
            throw new XPathExpressionRuntimeException(e);
        }
    }

    public static double number(Node ctx, String xpath)
    {
        try {
            return (Double) XPATH.evaluate(xpath, ctx, XPathConstants.NUMBER);
        }
        catch (XPathExpressionException e) {
            throw new XPathExpressionRuntimeException(e);
        }
    }
}









