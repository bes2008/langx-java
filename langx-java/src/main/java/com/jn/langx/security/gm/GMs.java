package com.jn.langx.security.gm;

import com.jn.langx.annotation.Singleton;
import com.jn.langx.lifecycle.AbstractInitializable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.Objs;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ServiceLoader;

@Singleton
public class GMs extends AbstractInitializable {
    private GenericRegistry<GmService> registry = new GenericRegistry<GmService>(new LinkedHashMap<String, GmService>());

    private static GMs INSTANCE;

    private GMs() {
        init();
    }

    public static GMs getGMs() {
        if (INSTANCE == null) {
            synchronized (GMs.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GMs();
                }
            }
        }
        return INSTANCE;
    }

    public GmService getDefault() {
        List<GmService> serviceList = registry.instances();
        if (Objs.isNotEmpty(serviceList)) {
            return serviceList.get(0);
        }
        return null;
    }

    @Override
    protected void doInit() throws InitializationException {
        for (GmService gmService : ServiceLoader.load(GmService.class)) {
            registry.register(gmService);
        }
    }

    public GmService getGmService(String name) {
        return registry.get(name);
    }
}
