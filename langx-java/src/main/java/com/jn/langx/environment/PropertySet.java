package com.jn.langx.environment;


public interface PropertySet<SRC> {

     String getName() ;

     SRC getSource();

    Object getProperty(String key);

     boolean hasProperty(String key);
}
