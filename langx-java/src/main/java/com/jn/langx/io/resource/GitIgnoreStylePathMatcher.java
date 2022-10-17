package com.jn.langx.io.resource;

import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.pattern.patternset.AntPathMatcher;

import java.util.List;

public class GitIgnoreStylePathMatcher implements PathMatcher{
    private AntPathMatcher matcher;
    private String paths;

    public GitIgnoreStylePathMatcher(){

    }

    public GitIgnoreStylePathMatcher(String pathsExpression){
        setPaths(pathsExpression);
    }

    public void setPaths(String pathsExpression){
        if(Strings.isBlank(pathsExpression)){
            return;
        }
        this.paths = pathsExpression;
        final String[] paths = Strings.split(pathsExpression,";");
        final List<String> antPaths = Collects.emptyArrayList();
        Collects.forEach(paths, new Consumer<String>() {
            @Override
            public void accept(String path) {
                // .svn/ => /**/.svn/**
                // /.svn/ => /.svn/**
                // !.svn/ => !/**/.svn/**
                // !/.svn/ => !/.svn/**

                // *.json => /**/*.json
                // /*.json => /*.json
                // !*.json => !/**/*.json
                // !/*.json => !/*.json

                // !.properties => !/**/.properties
                // !/.properties => !/.properties

                boolean isDirectory = Strings.endsWith(path, "/");
                boolean notIgnoredMode = Strings.startsWith(path,"!");

                if(notIgnoredMode){
                    path = Strings.substring(path,1);
                }

                if(!Strings.startsWith(path,"/")){
                    path = "**/" + path;
                }
                if(notIgnoredMode){
                    path = "!"+path;
                }
                if(!Strings.startsWith(path,"/")){
                    path = "/"+path;
                }

                if(isDirectory){
                    String directory = Strings.substring(path,0, path.length()-1);
                    if(Strings.isNotEmpty(directory)){
                        antPaths.add(directory);
                    }
                    // 目录下所有文件
                    antPaths.add(path+"**");
                }else{
                    antPaths.add(path);
                }
            }
        });

        String expression = Strings.join(";", antPaths);
        this.matcher = new AntPathMatcher();
        this.matcher.setGlobal(true);
        this.matcher.setPatternExpression(expression);
    }

    public Boolean matches(String path){
        if(this.matcher == null){
            return true;
        }
        return this.matcher.matches(path);
    }

    @Override
    public String toString() {
        return "GitIgnoreStylePathMatcher{" +
                "paths='" + paths + '\'' +
                '}';
    }

    public boolean matches(@NotEmpty String path, @Nullable String pathRoot){
        if(Strings.isBlank(path)){
            return false;
        }
        path = path.trim();
        if(pathRoot!=null && Strings.startsWith(path, pathRoot)){
            path = Strings.substring(path, pathRoot.length());
        }
        path = Strings.replace(path, "\\","/");
        if(!Strings.startsWith(path,"/")){
            path="/"+path;
        }
        if(Strings.isEmpty(path)){
            return false;
        }
        boolean match = matches(path);
        return match;
    }

}
