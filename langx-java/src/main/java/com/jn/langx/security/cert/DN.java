package com.jn.langx.security.cert;

/**
 * distinguished name
 *
 * @see sun.security.x509.X500Name
 * @see java.security.cert.X509Certificate
 *
 * @see {@link "http://www.voidcn.com/article/p-nmndmjyz-bwh.html"}
 *
 * @see  {@link "https://www.ietf.org/rfc/rfc3280.txt"}
 */
public class DN {

    // oid: 2.5.4.3
    // CN
    private String commonName;
    // oid: 2.5.4.4
    // SN
    private String surname;
    // oid: 2.5.4.5
    // serialNumber
    private String serialnumber;
    // oid: 2.5.4.6
    // C
    private String countryName;
    // oid: 2.5.4.7
    // L
    private String localityName;
    // oid: 2.5.4.8
    // ST
    private String stateName;
    // oid: 2.5.4.9
    // streetAddress
    private String streetAddress;
    // oid: 2.5.4.10
    // O
    private String orgName;
    // oid: 2.5.4.11
    // OU
    private String orgUnitName;
    // oid: 2.5.4.12
    // title
    private String title;
    // oid: 2.5.4.17
    // postalCode
    private String postalCode;
    // oid: 2.5.4.42
    // GN
    private String givenName;
    // oid: 2.5.4.43
    // initials
    private String initials;
    // oid: 2.5.4.44
    // generationQualifier
    private String generationQualifier;
    // oid: 2.5.4.46
    // dnQualifier
    private String dnQualifier;
    // oid: 2.5.4.65
    // dnQualifier
    private String pseudonym;
    // oid: 1.3.6.1.4.1.42.2.11.2.1
    // ipAddress ,简称需要再确认
    private String ipAddress;
    // oid: 0.9.2342.19200300.100.1.25
    // DC
    private String domainComponent;
    // oid: 0.9.2342.19200300.100.1.1
    // userid
    private String userId;

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgUnitName() {
        return orgUnitName;
    }

    public void setOrgUnitName(String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getGenerationQualifier() {
        return generationQualifier;
    }

    public void setGenerationQualifier(String generationQualifier) {
        this.generationQualifier = generationQualifier;
    }

    public String getDnQualifier() {
        return dnQualifier;
    }

    public void setDnQualifier(String dnQualifier) {
        this.dnQualifier = dnQualifier;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDomainComponent() {
        return domainComponent;
    }

    public void setDomainComponent(String domainComponent) {
        this.domainComponent = domainComponent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
