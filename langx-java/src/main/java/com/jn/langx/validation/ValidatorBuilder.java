package com.jn.langx.validation;

import com.jn.langx.Builder;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.ranges.DoubleRange;
import com.jn.langx.util.ranges.IntRange;
import com.jn.langx.util.ranges.LongRange;
import com.jn.langx.util.regexp.Regexp;

import java.util.List;

public class ValidatorBuilder implements Builder<Validator> {
    private List<Rule> rules = Lists.newArrayList();
    private ValidatorBuilder(){
    }

    public static ValidatorBuilder newBuilder(){
        return new ValidatorBuilder();
    }


    public ValidatorBuilder rule(Rule rule){
        if(rule != null) {
            rules.add(rule);
        }
        return this;
    }

    public ValidatorBuilder not(Rule rule, String errorMessage){
        return rule(new NotRule(rule, errorMessage));
    }

    public ValidatorBuilder predicate(Predicate<String> predicate, String errorMessage){
        return rule(new PredicateRule(predicate, errorMessage));
    }
    public ValidatorBuilder required(){
        return required(null);
    }
    public ValidatorBuilder required(String errorMessage){
        RequiredRule r = new RequiredRule(errorMessage);
        if(this.rules.isEmpty()) {
            rule(r);
        }else{
            this.rules.add(0, r);
        }
        return this;
    }

    public ValidatorBuilder length(int min){
        return rule(new LengthRule(min));
    }

    public ValidatorBuilder length(int min, int max){
        return rule(new LengthRule(min, max));
    }

    public ValidatorBuilder validChars(String charData){
        return rule(new CharRule(charData));
    }

    public ValidatorBuilder validChars(CharData charData){
        return rule(new CharRule(charData));
    }

    public ValidatorBuilder limitCharsOccurCount(CharData validChars, int min){
        return rule(new CharOccurCountRule(validChars, min));
    }

    public ValidatorBuilder limitCharSequenceMaxLength(CharSequenceData sequenceData, int maxLength){
        return rule(new CharSequenceRule(sequenceData, maxLength));
    }
    public ValidatorBuilder regexp(String errorMessage,String... regexp){
        return rule(new RegexpRule(errorMessage, regexp));
    }

    public ValidatorBuilder regexp(String errorMessage,Regexp... regexp){
        return rule(new RegexpRule(errorMessage, regexp));
    }

    public ValidatorBuilder sensitiveWords(String errorMessage, String... sensitiveWords){
        return rule(new SensitiveWordsRule(errorMessage, sensitiveWords));
    }

    public ValidatorBuilder historyRecords(String errorMessage, String... historyRecords){
        return rule(new HistoryRecordsRule(errorMessage, historyRecords));
    }

    public ValidatorBuilder ipv4Address(String errorMessage){
        return rule(new IPv4Rule(errorMessage));
    }

    public ValidatorBuilder ipv6Address(String errorMessage){
        return rule(new IPv6Rule(errorMessage));
    }

    public ValidatorBuilder macAddress(String errorMessage){
        return rule(new MacAddressRule(errorMessage));
    }

    public ValidatorBuilder rfc1123Hostname(String errorMessage){
        return rule(new Rfc1123HostnameRule(errorMessage));
    }

    public ValidatorBuilder port(String errorMessage){
        return rule(new PortRule(errorMessage));
    }

    public ValidatorBuilder portRange(IntRange intRange, String errorMessage){
        return rule(new PortRangeRule(intRange, errorMessage));
    }

    public ValidatorBuilder intRange(IntRange range, String errorMessage){
        return rule(new IntRangeRule(range, errorMessage));
    }

    public ValidatorBuilder intType(String errorMessage){
        return rule(new IntRule(errorMessage));
    }

    public ValidatorBuilder doubleRange(DoubleRange range, String errorMessage){
        return rule(new DoubleRangeRule(range, errorMessage));
    }

    public ValidatorBuilder doubleType(String errorMessage){
        return rule(new DoubleRule(errorMessage));
    }

    public ValidatorBuilder longRange(LongRange range, String errorMessage){
        return rule(new LongRangeRule(range, errorMessage));
    }

    public ValidatorBuilder longType(String errorMessage){
        return rule(new LongRule(errorMessage));
    }

    public ValidatorBuilder booleanType(String errorMessage){
        return rule(new BooleanRule(errorMessage));
    }

    /**
     * 身份证号
     */
    public ValidatorBuilder idCard(String errorMessage){
        return rule(new IdCardRule(errorMessage));
    }

    /**
     * 车牌号
     */
    public ValidatorBuilder plateNumber(String errorMessage){
        return rule(new PlateNumberRule(errorMessage));
    }

    public ValidatorBuilder dateString(String pattern, String errorMessage){
        return rule(new DateStringRule(pattern, errorMessage));
    }

    public ValidatorBuilder url(String errorMessage, String... schemas){
        return rule(new UrlRule(errorMessage, schemas));
    }

    public Validator build(){
        Validator validator = new Validator();
        validator.setRules(rules);
        return validator;
    }

}
