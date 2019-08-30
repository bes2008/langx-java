package com.jn.langx.text.i18n;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Une énumération pour représenter le pays d'une locale
 * <p>
 * <a href="http://www.iso.org/iso/country_codes"><code>ISO 3166-1:1998 (ICS n°
 * 01.140.20)</code></a>.
 * <p>
 * <a href="http://www.iso.org/iso/french_country_names_and_code_elements">la
 * liste des codes</a>
 * <p>
 * Chaque pays est repésenté ainsi :
 * <pre>A2, //     A3     Number Country name</pre>
 *
 * @author Tony Chemit - dev@tchemit.fr
 */
public enum CountryEnum {

    AF, //      AFG     004 AFGHANISTAN
    AL, //      ALB     008 ALBANIA
    DZ, //      DZA     012 ALGERIA
    AS, //      ASM     016 AMERICAN SAMOA
    AD, //      AND     020 ANDORRA
    AO, //      AGO     024 ANGOLA
    AI, //      AIA     660 ANGUILLA
    AQ, //      ATA     010 ANTARCTICA
    AG, //      ATG     028 ANTIGUA AND BARBUDA
    AR, //      ARG     032 ARGENTINA
    AM, //      ARM     051 ARMENIA
    AW, //      ABW     533 ARUBA
    AU, //      AUS     036 AUSTRALIA
    AT, //      AUT     040 AUSTRIA
    AZ, //      AZE     031 AZERBAIJAN
    BS, //      BHS     044 BAHAMAS
    BH, //      BHR     048 BAHRAIN
    BD, //      BGD     050 BANGLADESH
    BB, //      BRB     052 BARBADOS
    BY, //      BLR     112 BELARUS
    BE, //      BEL     056 BELGIUM
    BZ, //      BLZ     084 BELIZE
    BJ, //      BEN     204 BENIN
    BM, //      BMU     060 BERMUDA
    BT, //      BTN     064 BHUTAN
    BO, //      BOL     068 BOLIVIA
    BA, //      BIH     070 BOSNIA AND HERZEGOWINA
    BW, //      BWA     072 BOTSWANA
    BV, //      BVT     074 BOUVET ISLAND
    BR, //      BRA     076 BRAZIL
    IO, //      IOT     086 BRITISH INDIAN OCEAN TERRITORY
    BN, //      BRN     096 BRUNEI DARUSSALAM
    BG, //      BGR     100 BULGARIA
    BF, //      BFA     854 BURKINA FASO
    BI, //      BDI     108 BURUNDI
    KH, //      KHM     116 CAMBODIA
    CM, //      CMR     120 CAMEROON
    CA, //      CAN     124 CANADA
    CV, //      CPV     132 CAPE VERDE
    KY, //      CYM     136 CAYMAN ISLANDS
    CF, //      CAF     140 CENTRAL AFRICAN REPUBLIC
    TD, //      TCD     148 CHAD
    CL, //      CHL     152 CHILE
    CN, //      CHN     156 CHINA
    CX, //      CXR     162 CHRISTMAS ISLAND
    CC, //      CCK     166 COCOS (KEELING) ISLANDS
    CO, //      COL     170 COLOMBIA
    KM, //      COM     174 COMOROS
    CG, //      COG     178 CONGO
    CK, //      COK     184 COOK ISLANDS
    CR, //      CRI     188 COSTA RICA
    CI, //      CIV     384 COTE D'IVOIRE
    HR, //      HRV     191 CROATIA (local name: Hrvatska)
    CU, //      CUB     192 CUBA
    CY, //      CYP     196 CYPRUS
    CZ, //      CZE     203 CZECH REPUBLIC
    DK, //      DNK     208 DENMARK
    DJ, //      DJI     262 DJIBOUTI
    DM, //      DMA     212 DOMINICA
    DO, //      DOM     214 DOMINICAN REPUBLIC
    TP, //      TMP     626 EAST TIMOR
    EC, //      ECU     218 ECUADOR
    EG, //      EGY     818 EGYPT
    SV, //      SLV     222 EL SALVADOR
    GQ, //      GNQ     226 EQUATORIAL GUINEA
    ER, //      ERI    232 ERITREA
    EE, //      EST     233 ESTONIA
    ET, //      ETH     210 ETHIOPIA
    FK, //      FLK     238 FALKLAND ISLANDS (MALVINAS)
    FO, //      FRO     234 FAROE ISLANDS
    FJ, //      FJI     242 FIJI
    FI, //      FIN     246 FINLAND
    FR, //      FRA     250 FRANCE
    FX, //      FXX     249 FRANCE, METROPOLITAN
    GF, //      GUF     254 FRENCH GUIANA
    PF, //      PYF     258 FRENCH POLYNESIA
    TF, //      ATF     260 FRENCH SOUTHERN TERRITORIES
    GA, //      GAB     266 GABON
    GM, //      GMB     270 GAMBIA
    GE, //      GEO     268 GEORGIA
    DE, //      DEU     276 GERMANY
    GH, //      GHA     288 GHANA
    GI, //      GIB     292 GIBRALTAR
    GR, //      GRC     300 GREECE
    GL, //      GRL     304 GREENLAND
    GD, //      GRD     308 GRENADA
    GP, //      GLP     312 GUADELOUPE
    GU, //      GUM     316 GUAM
    GT, //      GTM     320 GUATEMALA
    GN, //      GIN     324 GUINEA
    GW, //      GNB     624 GUINEA-BISSAU
    GY, //      GUY     328 GUYANA
    HT, //      HTI     332 HAITI
    HM, //      HMD     334 HEARD AND MC DONALD ISLANDS
    HN, //      HND     340 HONDURAS
    HK, //      HKG     344 HONG KONG
    HU, //      HUN     348 HUNGARY
    IS, //      ISL     352 ICELAND
    IN, //      IND     356 INDIA
    ID, //      IDN     360 INDONESIA
    IR, //      IRN     364 IRAN (ISLAMIC REPUBLIC OF)
    IQ, //      IRQ     368 IRAQ
    IE, //      IRL     372 IRELAND
    IL, //      ISR     376 ISRAEL
    IT, //      ITA     380 ITALY
    JM, //      JAM     388 JAMAICA
    JP, //      JPN     392 JAPAN
    JO, //      JOR     400 JORDAN
    KZ, //      KAZ     398 KAZAKHSTAN
    KE, //      KEN     404 KENYA
    KI, //      KIR     296 KIRIBATI
    KP, //      PRK     408 KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF
    KR, //      KOR     410 KOREA, REPUBLIC OF
    KW, //      KWT     414 KUWAIT
    KG, //      KGZ     417 KYRGYZSTAN
    LA, //      LAO     418 LAO PEOPLE'S DEMOCRATIC REPUBLIC
    LV, //      LVA     428 LATVIA
    LB, //      LBN     422 LEBANON
    LS, //      LSO     426 LESOTHO
    LR, //      LBR     430 LIBERIA
    LY, //      LBY     434 LIBYAN ARAB JAMAHIRIYA
    LI, //      LIE     438 LIECHTENSTEIN
    LT, //      LTU     440 LITHUANIA
    LU, //      LUX     442 LUXEMBOURG
    MO, //      MAC     446 MACAU
    MK, //      MKD     807 (provis) MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF
    MG, //      MDG     450 MADAGASCAR
    MW, //      MWI     454 MALAWI
    MY, //      MYS     458 MALAYSIA
    MV, //      MDV     462 MALDIVES
    ML, //      MLI     466 MALI
    MT, //      MLT     470 MALTA
    MH, //      MHL     584 MARSHALL ISLANDS
    MQ, //      MTQ     474 MARTINIQUE
    MR, //      MRT     478 MAURITANIA
    MU, //      MUS     480 MAURITIUS
    YT, //      MYT     175 MAYOTTE
    MX, //      MEX     484 MEXICO
    FM, //      FSM     583 MICRONESIA, FEDERATED STATES OF
    MD, //      MDA     498 MOLDOVA, REPUBLIC OF
    MC, //      MCO     492 MONACO
    MN, //      MNG     496 MONGOLIA
    MS, //      MSR     500 MONTSERRAT
    MA, //      MAR     504 MOROCCO
    MZ, //      MOZ     508 MOZAMBIQUE
    MM, //      MMR     104 MYANMAR
    NA, //      NAM     516 NAMIBIA
    NR, //      NRU     520 NAURU
    NP, //      NPL     524 NEPAL
    NL, //      NLD     528 NETHERLANDS
    AN, //      ANT     530 NETHERLANDS ANTILLES
    NC, //      NCL     540 NEW CALEDONIA
    NZ, //      NZL     554 NEW ZEALAND
    NI, //      NIC     558 NICARAGUA
    NE, //      NER     562 NIGER
    NG, //      NGA     566 NIGERIA
    NU, //      NIU     570 NIUE
    NF, //      NFK     574 NORFOLK ISLAND
    MP, //      MNP     580 NORTHERN MARIANA ISLANDS
    NO, //      NOR     578 NORWAY
    OM, //      OMN     512 OMAN
    PK, //      PAK     586 PAKISTAN
    PW, //      PLW     585 PALAU
    PA, //      PAN     591 PANAMA
    PG, //      PNG     598 PAPUA NEW GUINEA
    PY, //      PRY     600 PARAGUAY
    PE, //      PER     604 PERU
    PH, //      PHL     608 PHILIPPINES
    PN, //      PCN     612 PITCAIRN
    PL, //      POL     616 POLAND
    PT, //      PRT     620 PORTUGAL
    PR, //      PRI     630 PUERTO RICO
    QA, //      QAT     634 QATAR
    RE, //      REU     638 REUNION
    RO, //      ROM     642 ROMANIA
    RU, //      RUS     643 RUSSIAN FEDERATION
    RW, //      RWA     646 RWANDA
    KN, //      KNA     659 SAINT KITTS AND NEVIS
    LC, //      LCA     662 SAINT LUCIA
    VC, //      VCT     670 SAINT VINCENT AND THE GRENADINES
    WS, //      WSM     882 SAMOA
    SM, //      SMR     674 SAN MARINO
    ST, //      STP     678 SAO TOME AND PRINCIPE
    SA, //      SAU     682 SAUDI ARABIA
    SN, //      SEN     686 SENEGAL
    SC, //      SYC     690 SEYCHELLES
    SL, //      SLE     694 SIERRA LEONE
    SG, //      SGP     702 SINGAPORE
    SK, //      SVK     703 SLOVAKIA (Slovak Republic)
    SI, //      SVN     705 SLOVENIA
    SB, //      SLB     090 SOLOMON ISLANDS
    SO, //      SOM     706 SOMALIA
    ZA, //      ZAF     710 SOUTH AFRICA
    ES, //      ESP     724 SPAIN
    LK, //      LKA     144 SRI LANKA
    SH, //      SHN     654 ST. HELENA
    PM, //      SPM     666 ST. PIERRE AND MIQUELON
    SD, //      SDN     736 SUDAN
    SR, //      SUR     740 SURINAME
    SJ, //      SJM     744 SVALBARD AND JAN MAYEN ISLANDS
    SZ, //      SWZ     748 SWAZILAND
    SE, //      SWE     752 SWEDEN
    CH, //      CHE     756 SWITZERLAND
    SY, //      SYR     760 SYRIAN ARAB REPUBLIC
    TW, //      TWN     158 TAIWAN, PROVINCE OF CHINA
    TJ, //      TJK     762 TAJIKISTAN
    TZ, //      TZA     834 TANZANIA, UNITED REPUBLIC OF
    TH, //      THA     764 THAILAND
    TG, //      TGO     768 TOGO
    TK, //      TKL     772 TOKELAU
    TO, //      TON     776 TONGA
    TT, //      TTO     780 TRINIDAD AND TOBAGO
    TN, //      TUN     788 TUNISIA
    TR, //      TUR     792 TURKEY
    TM, //      TKM     795 TURKMENISTAN
    TC, //      TCA     796 TURKS AND CAICOS ISLANDS
    TV, //      TUV     798 TUVALU
    UG, //      UGA     800 UGANDA
    UA, //      UKR     804 UKRAINE
    AE, //      ARE     784 UNITED ARAB EMIRATES
    GB, //      GBR     826 UNITED KINGDOM
    US, //      USA     840 UNITED STATES
    UM, //      UMI     581 UNITED STATES MINOR OUTLYING ISLANDS
    UY, //      URY     858 URUGUAY
    UZ, //      UZB     860 UZBEKISTAN
    VU, //      VUT     548 VANUATU
    VA, //      VAT     336 VATICAN CITY STATE (HOLY SEE)
    VE, //      VEN     862 VENEZUELA
    VN, //      VNM     704 VIET NAM
    VG, //      VGB     092 VIRGIN ISLANDS (BRITISH)
    VI, //      VIR     850 VIRGIN ISLANDS (U.S.)
    WF, //      WLF     876 WALLIS AND FUTUNA ISLANDS
    EH, //      ESH     732 WESTERN SAHARA
    YE, //      YEM     887 YEMEN
    YU, //      YUG     891 YUGOSLAVIA
    ZR, //      ZAR     180 ZAIRE
    ZM, //      ZMB     894 ZAMBIA
    ZW; //      ZWE     716 ZIMBABWE

    private static final Logger logger = LoggerFactory.getLogger(CountryEnum.class);

    public static CountryEnum valueOf(String country, CountryEnum defaultValue) {
        CountryEnum countryValue = null;
        try {
            countryValue = valueOf(country.toUpperCase());
        } catch (Exception e) {
            logger.warn("unfound country {}, will use default one : {}", country, defaultValue);
        }
        return countryValue == null ? defaultValue : countryValue;
    }
}
