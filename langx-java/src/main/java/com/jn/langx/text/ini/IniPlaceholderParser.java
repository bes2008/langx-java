package com.jn.langx.text.ini;

import com.jn.langx.text.PlaceholderParser;
import com.jn.langx.util.Emptys;


public class IniPlaceholderParser implements PlaceholderParser {
    private Ini ini;

    public IniPlaceholderParser(Ini ini) {
        this.ini = ini;
    }

    @Override
    public String parse(String placeholderName) {
        if(this.ini==null){
            return null;
        }
        placeholderName = placeholderName.trim();
        String sectionName = null;
        String propertyName = placeholderName;
        if(placeholderName.startsWith("[") && placeholderName.contains("].")){
            int sectionNameEndIndex= placeholderName.indexOf("].");
            sectionName = placeholderName.substring(1, sectionNameEndIndex);
            propertyName = placeholderName.substring(sectionNameEndIndex+2);
        }
        if(Emptys.isEmpty(sectionName) || Emptys.isEmpty(propertyName)){
            return placeholderName;
        }
        return ini.getSectionProperty(sectionName, propertyName );
    }
}
