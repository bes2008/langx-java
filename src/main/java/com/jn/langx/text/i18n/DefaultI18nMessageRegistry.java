package com.jn.langx.text.i18n;

import com.jn.langx.lifecycle.Initializable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.*;

public class DefaultI18nMessageRegistry implements I18nMessageRegistry, Initializable {
    private static final Logger logger = LoggerFactory.getLogger(DefaultI18nMessageRegistry.class);
    private static final Object[] NO_ARGS = new Object[0];
    private HashMap bundles;
    private final List<String> bundleNames = new ArrayList<String>();
    private String defaultBundleName;
    private Locale defaultLocale = Locale.getDefault();
    private String defaultLanguage = Locale.getDefault().getLanguage();
    private String defaultCountry = Locale.getDefault().getCountry();
    private boolean devMode;

    public DefaultI18nMessageRegistry() {
    }

    public String getDefaultLanguage() {
        return this.defaultLanguage;
    }

    public String getDefaultCountry() {
        return this.defaultCountry;
    }

    public String getDefaultBundleName() {
        return this.defaultBundleName;
    }

    protected List<String> getBundleNames() {
        return this.bundleNames;
    }

    protected ResourceBundle getBundle() {
        return this.getBundle(this.getDefaultBundleName(), (Locale) null);
    }

    protected ResourceBundle getBundle(String bundleName) {
        return this.getBundle(bundleName, (Locale) null);
    }

    protected ResourceBundle getBundle(String bundleName, String languageHeader) {
        return this.getBundle(bundleName, this.getLocale(languageHeader));
    }

    public ResourceBundle getBundle(String bundleName, Locale locale) {
        bundleName = bundleName == null ? this.getDefaultBundleName() : bundleName.trim();
        if (this.devMode) {
            try {
                Class klass = ResourceBundle.getBundle(bundleName).getClass().getSuperclass();
                Field field = klass.getDeclaredField("cacheList");
                field.setAccessible(true);
                Object cache = field.get((Object) null);
                cache.getClass().getDeclaredMethod("clear", (Class[]) null).invoke(cache, (Object[]) null);
                field.setAccessible(false);
            } catch (Exception ex) {
                logger.warn(ex.getMessage());
            }
        }

        if (locale == null) {
            locale = this.getLocale(null);
        }

        HashMap bundlesByLocale = (HashMap) this.bundles.get(bundleName);
        ResourceBundle rb;
        if (bundlesByLocale != null) {
            rb = (ResourceBundle) bundlesByLocale.get(locale);
            if (rb == null) {
                rb = this.cacheBundle(bundleName, locale);
            }
        } else {
            rb = this.cacheBundle(bundleName, locale);
        }

        return rb;
    }

    public Locale getLocale() {
        return this.defaultLocale;
    }

    public Locale getLocale(String header) {
        if (!Strings.isEmpty(header)) {
            I18NTokenizer tok = new I18NTokenizer(header);
            if (tok.hasNext()) {
                return (Locale) tok.next();
            }
        }

        return this.defaultLocale;
    }

    public String getMessage(String key) {
        return this.getMessage(null, key);
    }

    public String getMessage(Locale locale, String key) {
        return this.getMessage(this.getDefaultBundleName(), locale, key);
    }

    public String getMessage(String bundleName, Locale locale, String key) {
        if (locale == null) {
            locale = this.getLocale(null);
        }

        ResourceBundle rb = this.getBundle(bundleName, locale);
        String value = this.getStringOrNull(rb, key);
        String name;
        if (value == null && !this.bundleNames.isEmpty()) {
            for (int i = 0; i < this.bundleNames.size(); ++i) {
                name = this.bundleNames.get(i);
                if (!name.equals(bundleName)) {
                    rb = this.getBundle(name, locale);
                    value = this.getStringOrNull(rb, key);
                    if (value != null) {
                        locale = rb.getLocale();
                        break;
                    }
                }
            }
        }

        if (value == null) {
            name = locale.toString();
            String mesg = "Noticed missing resource: bundleName=" + bundleName + ", locale=" + name + ", key=" + key;
            logger.debug(mesg);
            value = key;
        }
        return value;
    }

    public String getMessage(String bundleName, Locale locale, String key, Object... args) {
        String value = this.getMessage(bundleName, locale, key);
        if (args == null) {
            args = NO_ARGS;
        }

        MessageFormat messageFormat = new MessageFormat("");
        messageFormat.setLocale(locale);
        messageFormat.applyPattern(value);
        return messageFormat.format(args);
    }

    public void init() throws InitializationException {
        this.bundles = new HashMap();
        this.defaultLocale = new Locale(this.defaultLanguage, this.defaultCountry);
        this.initializeBundleNames();
        if ("true".equals(System.getProperty("PLEXUS_DEV_MODE"))) {
            this.devMode = true;
        }

    }

    protected void initializeBundleNames() {

    }

    private synchronized ResourceBundle cacheBundle(String bundleName, Locale locale) throws MissingResourceException {
        HashMap bundlesByLocale = (HashMap) this.bundles.get(bundleName);
        ResourceBundle rb = bundlesByLocale == null ? null : (ResourceBundle) bundlesByLocale.get(locale);
        if (rb == null) {
            bundlesByLocale = bundlesByLocale == null ? new HashMap(3) : new HashMap(bundlesByLocale);

            try {
                rb = ResourceBundle.getBundle(bundleName, locale);
            } catch (MissingResourceException var6) {
                rb = this.findBundleByLocale(bundleName, locale, bundlesByLocale);
                if (rb == null) {
                    throw (MissingResourceException) var6.fillInStackTrace();
                }
            }

            if (rb != null) {
                bundlesByLocale.put(rb.getLocale(), rb);
                HashMap bundlesByName = new HashMap(this.bundles);
                bundlesByName.put(bundleName, bundlesByLocale);
                this.bundles = bundlesByName;
            }
        }

        return rb;
    }

    private ResourceBundle findBundleByLocale(String bundleName, Locale locale, Map bundlesByLocale) {
        ResourceBundle rb = null;
        Locale withDefaultLanguage;
        if (!Strings.isNotEmpty(locale.getCountry()) && this.defaultLanguage.equals(locale.getLanguage())) {
            withDefaultLanguage = new Locale(locale.getLanguage(), this.defaultCountry);
            rb = (ResourceBundle) bundlesByLocale.get(withDefaultLanguage);
            if (rb == null) {
                rb = this.getBundleIgnoreException(bundleName, withDefaultLanguage);
            }
        } else if (!Strings.isNotEmpty(locale.getLanguage()) && this.defaultCountry.equals(locale.getCountry())) {
            withDefaultLanguage = new Locale(this.defaultLanguage, locale.getCountry());
            rb = (ResourceBundle) bundlesByLocale.get(withDefaultLanguage);
            if (rb == null) {
                rb = this.getBundleIgnoreException(bundleName, withDefaultLanguage);
            }
        }

        if (rb == null && !this.defaultLocale.equals(locale)) {
            rb = this.getBundleIgnoreException(bundleName, this.defaultLocale);
        }

        return rb;
    }

    private ResourceBundle getBundleIgnoreException(String bundleName, Locale locale) {
        try {
            return ResourceBundle.getBundle(bundleName, locale);
        } catch (MissingResourceException var4) {
            return null;
        }
    }

    protected final String getStringOrNull(ResourceBundle rb, String key) {
        if (rb != null) {
            try {
                return rb.getString(key);
            } catch (MissingResourceException var4) {
                ;
            }
        }

        return null;
    }
}
