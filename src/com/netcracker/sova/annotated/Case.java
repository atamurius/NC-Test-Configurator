package com.netcracker.sova.annotated;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Case
{
    private static final Pattern CAMEL_CASE = Pattern.compile("([A-Z]?[a-z]+)|[A-Z]+_|[^A-Za-z]+");

    static public String fromCamel(String name)
    {
        name = name.substring(0,1).toUpperCase() + name.substring(1);
        Matcher m = CAMEL_CASE.matcher(name);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String part = m.group();
            if (part.endsWith("_")) {
                part = part.substring(0, part.length() - 1).toUpperCase();
            }
            sb.append(sb.length() > 0 ? " " : "").append(part);
        }
        return sb.toString();
    }    

    private static final Pattern CONST_CASE = Pattern.compile("[A-Z]+_{0,2}");

    static public String fromConst(String name)
    {
        Matcher m = CONST_CASE.matcher(name);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String part = m.group();
            boolean abbr = part.endsWith("__");
            if (part.endsWith("_"))
                part = part.substring(0, part.indexOf('_'));
            if (! abbr)
                part = part.substring(0,1).toUpperCase() + part.substring(1).toLowerCase();
            
            sb.append(sb.length() > 0 ? " " : "").append(part);
        }
        return sb.toString();
    }
}
