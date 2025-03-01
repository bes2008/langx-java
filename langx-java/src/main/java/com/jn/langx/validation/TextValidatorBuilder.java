package com.jn.langx.validation;

import com.jn.langx.Builder;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.ranges.DoubleRange;
import com.jn.langx.util.ranges.IntRange;
import com.jn.langx.util.ranges.LongRange;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.validation.rule.CharData;
import com.jn.langx.validation.rule.CharSequenceData;
import com.jn.langx.validation.rule.*;

import java.util.List;

public class TextValidatorBuilder implements Builder<TextValidator> {
    private List<Rule> rules = Lists.newArrayList();
    private ValidateMode validateMode = ValidateMode.VALIDATE_ALL;
    private RequiredRule requiredRule = new RequiredRule("Required");

    private TextValidatorBuilder() {
    }

    public static TextValidatorBuilder newBuilder() {
        return new TextValidatorBuilder();
    }


    public TextValidatorBuilder rule(Rule rule) {
        if (rule != null) {
            if (rule instanceof RequiredRule) {
                this.requiredRule = (RequiredRule) rule;
            } else {
                rules.add(rule);
            }
        }
        return this;
    }

    public TextValidatorBuilder not(String errorMessage, Rule rule) {
        return rule(new NotRule(errorMessage, rule));
    }

    public TextValidatorBuilder predicate(String errorMessage, Predicate<String> predicate) {
        return rule(new PredicateRule(errorMessage, predicate));
    }

    public TextValidatorBuilder forwarding(Rule rule) {
        return forwarding(null, rule);
    }

    public TextValidatorBuilder forwarding(String errorMessage, Rule rule) {
        return rule(new ForwardingRule(errorMessage, rule));
    }

    public TextValidatorBuilder allMatch(String errorMessage, Predicate<String>... predicates) {
        AllMatchRule rule = new AllMatchRule(errorMessage);
        for (Predicate<String> predicate : predicates) {
            rule.addRule(predicate);
        }
        return rule(rule);
    }

    public TextValidatorBuilder anyMatch(String errorMessage, Predicate<String>... predicates) {
        AnyMatchRule rule = new AnyMatchRule(errorMessage);
        for (Predicate<String> predicate : predicates) {
            rule.addRule(predicate);
        }
        return rule(rule);
    }

    public TextValidatorBuilder noneMatch(String errorMessage, Predicate<String>... predicates) {
        NoneMatchRule rule = new NoneMatchRule(errorMessage);
        for (Predicate<String> predicate : predicates) {
            rule.addRule(predicate);
        }
        return rule(rule);
    }


    public TextValidatorBuilder optional() {
        this.requiredRule = null;
        return this;
    }

    public TextValidatorBuilder required(String errorMessage) {
        RequiredRule r = new RequiredRule(errorMessage);
        this.requiredRule = r;
        return this;
    }

    public TextValidatorBuilder lengthRange(int min) {
        return rule(new LengthRangeRule(min));
    }

    public TextValidatorBuilder lengthRange(int min, int max) {
        return rule(new LengthRangeRule(min, max));
    }

    public TextValidatorBuilder length(int fixedLength) {
        return rule(new LengthRule(fixedLength));
    }

    public TextValidatorBuilder validChars(String charData) {
        return rule(new CharRule(charData));
    }

    public TextValidatorBuilder validChars(CharData charData) {
        return rule(new CharRule(charData));
    }

    public TextValidatorBuilder limitCharsOccurCount(CharData validChars, int min) {
        return rule(new CharOccurCountRule(validChars, min));
    }

    public TextValidatorBuilder limitCharSequenceMaxLength(CharSequenceData sequenceData, int maxLength) {
        return rule(new CharSequenceRule(sequenceData, maxLength));
    }

    public TextValidatorBuilder regexp(String errorMessage, String... regexp) {
        return rule(new RegexpRule(errorMessage, regexp));
    }

    public TextValidatorBuilder regexp(String errorMessage, Regexp... regexp) {
        return rule(new RegexpRule(errorMessage, regexp));
    }

    public TextValidatorBuilder sensitiveWords(String errorMessage, String... sensitiveWords) {
        return rule(new SensitiveWordsRule(errorMessage, sensitiveWords));
    }

    public TextValidatorBuilder historyRecords(String errorMessage, String... historyRecords) {
        return rule(new HistoryRecordsRule(errorMessage, historyRecords));
    }

    public TextValidatorBuilder ipv4(String errorMessage) {
        return rule(new IPv4Rule(errorMessage));
    }

    public TextValidatorBuilder ipv6(String errorMessage) {
        return rule(new IPv6Rule(errorMessage));
    }

    public TextValidatorBuilder macAddress(String errorMessage, String separator) {
        return rule(new MacAddressRule(errorMessage, separator));
    }

    public TextValidatorBuilder rfc1123DomainName(String errorMessage) {
        return rule(new Rfc1123DomainNameRule(errorMessage));
    }
    public TextValidatorBuilder port(int port){
        return rule(new PortRule(null, port));
    }

    public TextValidatorBuilder port(String errorMessage, int port){
        return rule(new PortRule(errorMessage, port));
    }

    public TextValidatorBuilder portRange(String errorMessage) {
        return rule(new PortRangeRule(errorMessage));
    }

    public TextValidatorBuilder portRange(String errorMessage, IntRange intRange ) {
        return rule(new PortRangeRule(errorMessage, intRange));
    }

    public TextValidatorBuilder hostname(String errorMessage) {
        return rule(new HostnameRule(errorMessage));
    }
    public TextValidatorBuilder intRange(String errorMessage, IntRange range) {
        return rule(new IntRangeRule(errorMessage, range));
    }

    public TextValidatorBuilder intType(String errorMessage) {
        return rule(new IntRule(errorMessage));
    }

    public TextValidatorBuilder doubleRange(String errorMessage, DoubleRange range) {
        return rule(new DoubleRangeRule(errorMessage, range));
    }

    public TextValidatorBuilder doubleType(String errorMessage) {
        return rule(new DoubleRule(errorMessage));
    }

    public TextValidatorBuilder longRange(String errorMessage, LongRange range) {
        return rule(new LongRangeRule(errorMessage, range));
    }

    public TextValidatorBuilder longType(String errorMessage) {
        return rule(new LongRule(errorMessage));
    }

    public TextValidatorBuilder booleanType(String errorMessage) {
        return rule(new BooleanRule(errorMessage));
    }

    /**
     * 身份证号
     */
    public TextValidatorBuilder idCard(String errorMessage) {
        return rule(new ChineseIdCardRule(errorMessage));
    }

    /**
     * 车牌号
     */
    public TextValidatorBuilder plateNumber(String errorMessage) {
        return rule(new PlateNumberRule(errorMessage));
    }

    public TextValidatorBuilder date(String errorMessage, String pattern) {
        return rule(new DateRule(errorMessage, pattern));
    }

    public TextValidatorBuilder url(String errorMessage, String... schemas) {
        return rule(new UrlRule(errorMessage, schemas));
    }

    public TextValidatorBuilder email(){
        return rule(new EmailRule());
    }

    public TextValidatorBuilder email(String errorMessage) {
        return rule(new EmailRule(errorMessage));
    }

    public TextValidatorBuilder email(String errorMessage, String... validDomains) {
        return rule(new EmailRule(errorMessage, validDomains));
    }

    public TextValidatorBuilder isbn(String errorMessage) {
        return rule(new ISBNRule(errorMessage));
    }

    public TextValidatorBuilder isbn10(String errorMessage) {
        return rule(new ISBN10Rule(errorMessage));
    }

    public TextValidatorBuilder isbn13(String errorMessage) {
        return rule(new ISBN13Rule(errorMessage));
    }

    public TextValidator build() {
        TextValidator validator = new TextValidator();
        validator.setValidateMode(validateMode);
        validator.setRequiredRule(requiredRule);
        validator.setRules(rules);
        return validator;
    }

}
