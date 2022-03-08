package com.jn.langx.util.bean;

public interface ModelMapper<Source, Target> {
    Target map(Source a);
}
