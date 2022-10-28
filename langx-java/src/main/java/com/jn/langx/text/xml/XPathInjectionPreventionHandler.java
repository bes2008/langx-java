package com.jn.langx.text.xml;

import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.security.prevention.injection.InjectionPreventionHandler;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.spi.CommonServiceProvider;

import java.util.Iterator;

/**
 * @since 5.0.2
 */
public class XPathInjectionPreventionHandler extends InjectionPreventionHandler {
    private static final GenericRegistry<XPathHandler> registry;

    static {
        final GenericRegistry<XPathHandler> _registry = new GenericRegistry<XPathHandler>();
        Iterator<XPathHandler> iterator = new CommonServiceProvider<XPathHandler>().get(XPathHandler.class);
        Pipeline.<XPathHandler>of(iterator)
                .forEach(new Consumer<XPathHandler>() {
                    @Override
                    public void accept(XPathHandler xPathHandler) {
                        _registry.register(xPathHandler);
                    }
                });
        registry = _registry;
    }

    private static final XPathInjectionPreventionHandler INSTANCE = new XPathInjectionPreventionHandler();

    public static XPathInjectionPreventionHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public String apply(String xpathParameter) {
        XPathHandler OWASP_XPATH_ENCODER = registry.get("owasp-xpath-encoder");
        if (OWASP_XPATH_ENCODER != null) {
            return OWASP_XPATH_ENCODER.transform(xpathParameter);
        }
        return super.apply(xpathParameter);
    }
}
