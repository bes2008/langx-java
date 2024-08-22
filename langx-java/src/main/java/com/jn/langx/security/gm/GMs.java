package com.jn.langx.security.gm;

import com.jn.langx.annotation.Singleton;
import com.jn.langx.lifecycle.AbstractInitializable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.text.properties.PropertiesAccessor;
import com.jn.langx.util.Objs;
import com.jn.langx.util.spi.CommonServiceProvider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ServiceLoader;

@Singleton
public class GMs extends AbstractInitializable {
    private GenericRegistry<GmService> registry = new GenericRegistry<GmService>(new LinkedHashMap<String, GmService>());

    private volatile static GMs INSTANCE;

    private GMs() {
        init();
    }

    private static final String SM2_C1C3C2_MODE_ENABLE_KEY="langx.security.gm.SM2.defaultMode.c1c3c2.enabled";
    public static final void enableGlobalSM2_C1C3C2(){
        System.setProperty(SM2_C1C3C2_MODE_ENABLE_KEY,"true");
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

    public static boolean sm2DefaultC1C3C2ModeEnabled(){
        boolean sm2DefaultC1C3C2ModeEnabled= new PropertiesAccessor(System.getProperties()).getBoolean(SM2_C1C3C2_MODE_ENABLE_KEY,false);
        return sm2DefaultC1C3C2ModeEnabled;
    }

    @Override
    protected void doInit() throws InitializationException {
        sm2DefaultC1C3C2ModeEnabled();
        for (GmService gmService : CommonServiceProvider.loadService(GmService.class)) {
            registry.register(gmService);
        }
    }

    public GmService getGmService(String name) {
        return registry.get(name);
    }
}
