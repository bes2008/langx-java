package com.jn.langx.util.net.http;

import com.jn.langx.util.Numbers;
import com.jn.langx.util.Objs;
import com.jn.langx.text.StrTokenizer;

import java.util.*;


public class HttpAcceptLanguageHeaderTokenizer implements Iterator {
    private static final String LOCALE_SEPARATOR = ",";
    private static final char QUALITY_SEPARATOR = ';';
    private static final Float DEFAULT_QUALITY = 1.0F;
    private List locales = new ArrayList(3);

    public HttpAcceptLanguageHeaderTokenizer(String header) {
        HttpAcceptLanguageHeaderTokenizer.AcceptLanguage acceptLang;
        for (StrTokenizer tok = new StrTokenizer(header, ","); tok.hasNext(); this.locales.add(acceptLang)) {
            acceptLang = new HttpAcceptLanguageHeaderTokenizer.AcceptLanguage();
            String element = tok.next().trim();
            int index;
            if ((index = element.indexOf(59)) != -1) {
                String q = element.substring(index);
                element = element.substring(0, index);
                if ((index = q.indexOf(61)) != -1) {
                    try {
                        acceptLang.quality = Numbers.createFloat(q.substring(index + 1));
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
            return ((HttpAcceptLanguageHeaderTokenizer.AcceptLanguage) this.locales.remove(0)).locale;
        }
    }
    @Override
    public final void remove() {
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support remove()");
    }

    private static class AcceptLanguage implements Comparable {
        Locale locale;
        Float quality;

        private AcceptLanguage() {
            this.quality = HttpAcceptLanguageHeaderTokenizer.DEFAULT_QUALITY;
        }

        public final int compareTo(Object acceptLang) {
            return this.quality.compareTo(((HttpAcceptLanguageHeaderTokenizer.AcceptLanguage) acceptLang).quality);
        }
        public boolean equals(Object acceptLang){
            if(acceptLang==null){
                return false;
            }
            if(acceptLang==this){
                return true;
            }
            if(acceptLang.getClass()!=this.getClass()){
                return false;
            }
            AcceptLanguage ac = (AcceptLanguage)acceptLang;
            if(!Objs.equals(this.locale, ac.locale)){
                return false;
            }
            return Objs.equals(this.quality, ac.quality);
        }

        @Override
        public int hashCode() {
            return Objs.hash(locale, quality);
        }
    }
}
