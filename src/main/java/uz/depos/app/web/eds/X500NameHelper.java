/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uz.depos.app.web.eds;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Mukhriddin
 */
public class X500NameHelper {

    /**
     * Разделить строку содержащую имя X.500 в массив строк
     * @param x500Name имя X.500
     * @return массив строк
     */
    public static String[] split(String x500Name) {
        List<String> list = new LinkedList();
        Pattern pattern = Pattern.compile(",(\\s)*([A-Za-z]+|[0-9\\.]+)=");
        Matcher matcher = pattern.matcher(x500Name);
        if (matcher.find()) {
            list.add(x500Name.substring(0, matcher.start()));
            int last = matcher.start();
            while (matcher.find()) {
                list.add(x500Name.substring(last + 1, matcher.start()));
                last = matcher.start();
            }
            list.add(x500Name.substring(last + 1));
        } else {
            list.add(x500Name);
        }
        return list.toArray(new String[0]);
    }

    public static Map<String, String> map(String x500Name) {
        Map<String, String> m = new LinkedHashMap();
        String[] lines = split(x500Name);
        for (String line : lines) {
            int ei = line.indexOf("=", 0);
            if (ei != -1) {
                m.put(line.substring(0, ei), line.substring(ei + 1));
            }
        }
        return m;
    }

    /**
     * Получить значение X.500 атрибута
     * @param field поле аттрибута
     * @param x500Name имя X.500
     * @return значение атрибута
     */
    public static String get(String field, String x500Name) {
        String[] rdn = split(x500Name);
        for (String s : rdn) {
            if (s.startsWith(field)) {
                return s.substring(s.indexOf("=") + 1);
            }
        }
        return null;
    }
}
