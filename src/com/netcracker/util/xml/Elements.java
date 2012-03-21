package com.netcracker.util.xml;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Elements implements Iterable<Element>
{
    private final NodeList nodes;
    
    public Elements(NodeList nodes)
    {
        this.nodes = nodes;
    }

    @Override
    public Iterator<Element> iterator()
    {
        return new Iterator<Element>()
        {
            int index = 0;
            { currElement(); }
            
            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Element next()
            {
                Element e = currElement();
                index++;
                if (e == null)
                    throw new NoSuchElementException();
                else
                    return e;
            }
            
            private Element currElement()
            {
                while (hasNext())
                    if (nodes.item(index) instanceof Element)
                        return (Element) nodes.item(index);
                    else
                        index++;
                return null;
            }

            @Override
            public boolean hasNext()
            {
                return index < nodes.getLength();
            }
        };
    }
}
