package com.jn.langx.text.i18n;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Une énumération pour représenter le langue d'une locale définie dans la norme
 * <a href="http://www.iso.org/iso/iso_catalogue/catalogue_ics/catalogue_detail_ics.htm?csnumber=22109&ICS1=1&ICS2=140&ICS3=20"><code>ISO
 * 639-1:1998  (ICS n° 01.140.20)</code></a>.
 * <p>
 * <a href="http://www.loc.gov/standards/iso639-2/php/French_list.php">la liste
 * des codes</a>
 */
public enum LanguageEnum {

    aa, // Afar
    ab, // Abkhazian
    af, // Afrikaans
    am, // Amharic
    ar, // Arabic
    as, // Assamese
    ay, // Aymara
    az, // Azerbaijani
    ba, // Bashkir
    be, // Byelorussian
    bg, // Bulgarian
    bh, // Bihari
    bi, // Bislama
    bn, // Bengali; Bangla
    bo, // Tibetan
    br, // Breton
    ca, // Catalan
    co, // Corsican
    cs, // Czech
    cy, // Welsh
    da, // Danish
    de, // German
    dz, // Bhutani
    el, // Greek
    en, // English
    eo, // Esperanto
    es, // Spanish
    et, // Estonian
    eu, // Basque
    fa, // Persian
    fi, // Finnish
    fj, // Fiji
    fo, // Faroese
    fr, // French
    fy, // Frisian
    ga, // Irish
    gd, // Scots Gaelic
    gl, // Galician
    gn, // Guarani
    gu, // Gujarati
    ha, // Hausa
    he, // Hebrew (formerly iw)
    hi, // Hindi
    hr, // Croatian
    hu, // Hungarian
    hy, // Armenian
    ia, // Interlingua
    id, // Indonesian (formerly in)
    ie, // Interlingue
    ik, // Inupiak
    is, // Icelandic
    it, // Italian
    iu, // Inuktitut
    ja, // Japanese
    jw, // Javanese
    ka, // Georgian
    kk, // Kazakh
    kl, // Greenlandic
    km, // Cambodian
    kn, // Kannada
    ko, // Korean
    ks, // Kashmiri
    ku, // Kurdish
    ky, // Kirghiz
    la, // Latin
    ln, // Lingala
    lo, // Laothian
    lt, // Lithuanian
    lv, // Latvian, Lettish
    mg, // Malagasy
    mi, // Maori
    mk, // Macedonian
    ml, // Malayalam
    mn, // Mongolian
    mo, // Moldavian
    mr, // Marathi
    ms, // Malay
    mt, // Maltese
    my, // Burmese
    na, // Nauru
    ne, // Nepali
    nl, // Dutch
    no, // Norwegian
    oc, // Occitan
    om, // (Afan) Oromo
    or, // Oriya
    pa, // Punjabi
    pl, // Polish
    ps, // Pashto, Pushto
    pt, // Portuguese
    qu, // Quechua
    rm, // Rhaeto-Romance
    rn, // Kirundi
    ro, // Romanian
    ru, // Russian
    rw, // Kinyarwanda
    sa, // Sanskrit
    sd, // Sindhi
    sg, // Sangho
    sh, // Serbo-Croatian
    si, // Sinhalese
    sk, // Slovak
    sl, // Slovenian
    sm, // Samoan
    sn, // Shona
    so, // Somali
    sq, // Albanian
    sr, // Serbian
    ss, // Siswati
    st, // Sesotho
    su, // Sundanese
    sv, // Swedish
    sw, // Swahili
    ta, // Tamil
    te, // Telugu
    tg, // Tajik
    th, // Thai
    ti, // Tigrinya
    tk, // Turkmen
    tl, // Tagalog
    tn, // Setswana
    to, // Tonga
    tr, // Turkish
    ts, // Tsonga
    tt, // Tatar
    tw, // Twi
    ug, // Uighur
    uk, // Ukrainian
    ur, // Urdu
    uz, // Uzbek
    vi, // Vietnamese
    vo, // Volapuk
    wo, // Wolof
    xh, // Xhosa
    yi, // Yiddish (formerly ji)
    yo, // Yoruba
    za, // Zhuang
    zh, // Chinese
    zu; // Zulu

    private static final Logger logger = LoggerFactory.getLogger(LanguageEnum.class);

    public static LanguageEnum valueOf(String language, LanguageEnum defaultValue) {
        LanguageEnum languageValue = null;
        try {
            languageValue = valueOf(language.toLowerCase());
        } catch (Exception e) {
            logger.warn("Unfound language {}, will use default one : {}", language, defaultValue);
        }
        return languageValue == null ? defaultValue : languageValue;
    }
}
