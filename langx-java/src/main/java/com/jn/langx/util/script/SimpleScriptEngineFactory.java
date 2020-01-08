package com.jn.langx.util.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class SimpleScriptEngineFactory {
    public static ScriptEngine getScriptEngine(ScriptLanguage language) {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        return engineManager.getEngineByName(language.name());
    }
}