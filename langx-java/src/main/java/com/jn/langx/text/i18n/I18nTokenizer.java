package com.jn.langx.text.i18n;

import java.util.*;

class I18nTokenizer implements Iterator {
    private static final String LOCALE_SEPARATOR = ",";
    private static final char QUALITY_SEPARATOR = ';';
    private static final Float DEFAULT_QUALITY = 1.0F;
    private ArrayList locales = new ArrayList(3);

    public I18nTokenizer(String header) {
        I18nTokenizer.AcceptLanguage acceptLang;
        for (StringTokenizer tok = new StringTokenizer(header, ","); tok.hasMoreTokens(); this.locales.add(acceptLang)) {
            acceptLang = new I18nTokenizer.AcceptLanguage();
            String element = tok.nextToken().trim();
            int index;
            if ((index = element.indexOf(59)) != -1) {
                String q = element.substring(index);
                element = element.substring(0, index);
                if ((index = q.indexOf(61)) != -1) {
                    try {
                        acceptLang.quality = Float.valueOf(q.substring(index + 1));
                    } catch (NumberFormatException var8) {
                        // NOOP
                    }
                }
            }

            element = element.trim();
            if ((index = element.indexOf(45)) == -1) {
                acceptLang.locale = new Locale(element, "");
            } else {
                acceptLang.locale = new Locale(element.substring(0, index), element.substring(index + 1));
            }
        }

        Collections.sort(this.locales, Collections.reverseOrder());
    }

    public boolean hasNext() {
        return !this.locales.isEmpty();
    }

    public Object next() {
        if (this.locales.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return ((I18nTokenizer.AcceptLanguage) this.locales.remove(0)).locale;
        }
    }

    public final void remove() {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support remove()");
    }

    private class AcceptLanguage implements Comparable {
        Locale locale;
        Float quality;

        private AcceptLanguage() {
            this.quality = I18nTokenizer.DEFAULT_QUALITY;
        }

        public final int compareTo(Object acceptLang) {
            return this.quality.compareTo(((I18nTokenizer.AcceptLanguage) acceptLang).quality);
        }
    }
}
