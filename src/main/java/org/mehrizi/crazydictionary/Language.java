package org.mehrizi.crazydictionary;

import java.util.HashMap;
import java.util.Map;

import static org.mehrizi.crazydictionary.EditLayoutController.item;

public class Language {

    private static Language theInstance;
    public static HashMap<String,String> langs= new HashMap<>();

    public static Language instance()
    {
        if (theInstance==(null)){
            theInstance = new Language();
        }
        return theInstance;
    }

    private Language(){
        langs.put("eng","English");
        langs.put("fra","French");
        langs.put("deu","German");
        langs.put("tur","Turkish");
        langs.put("ita","Italian");
        langs.put("swe","Swedish");
        langs.put("ell","Greek");

    }
    public static String getShortForm(String longForm) {
        for(Map.Entry<String, String> item:Language.instance().langs.entrySet()){
            if (item.getValue() == longForm)
                return item.getKey();
        };
        return "";
    }
}
