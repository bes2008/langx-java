package com.jn.langx.validation;

import com.jn.langx.Builder;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.ranges.DoubleRange;
import com.jn.langx.util.ranges.IntRange;
import com.jn.langx.util.ranges.LongRange;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.validation.rule.*;

import java.util.List;

public class TextValidatorBuilder implements Builder<TextValidator> {
    private List<Rule> rules = Lists.newArrayList();
    private ValidateMode validateMode = ValidateMode.VALIDATE_ALL;
    private RequiredRule requiredRule;
    private TextValidatorBuilder(){
    }

    public static TextValidatorBuilder newBuilder(){
        return new TextValidatorBuilder();
    }


    public TextValidatorBuilder rule(Rule rule){
        if(rule != null) {
            if(rule instanceof RequiredRule){
                this.requiredRule = (RequiredRule) rule;
            }else {
                rules.add(rule);
            }
        }
        return this;
    }

    public TextValidatorBuilder not(Rule rule, String errorMessage){
        return rule(new NotRule(rule, errorMessage));
    }

    public TextValidatorBuilder predicate(Predicate<String> predicate, String errorMessage){
        return rule(new PredicateRule(predicate, errorMessage));
    }
    public TextValidatorBuilder required(){
        return required(null);
    }
    public TextValidatorBuilder required(String errorMessage){
        RequiredRule r = new RequiredRule(errorMessage);
        this.requiredRule=r;
        return this;
    }

    public TextValidatorBuilder length(int min){
        return rule(new LengthRule(min));
    }

    public TextValidatorBuilder length(int min, int max){
        return rule(new LengthRule(min, max));
    }

    public TextValidatorBuilder validChars(String charData){
        return rule(new CharRule(charData));
    }

    public TextValidatorBuilder validChars(CharData charData){
        return rule(new CharRule(charData));
    }

    public TextValidatorBuilder limitCharsOccurCount(CharData validChars, int min){
        return rule(new CharOccurCountRule(validChars, min));
    }

    public TextValidatorBuilder limitCharSequenceMaxLength(CharSequenceData sequenceData, int maxLength){
        return rule(new CharSequenceRule(sequenceData, maxLength));
    }
    public TextValidatorBuilder regexp(String errorMessage, String... regexp){
        return rule(new RegexpRule(errorMessage, regexp));
    }

    public TextValidatorBuilder regexp(String errorMessage, Regexp... regexp){
        return rule(new RegexpRule(errorMessage, regexp));
    }

    public TextValidatorBuilder sensitiveWords(String errorMessage, String... sensitiveWords){
        return rule(new SensitiveWordsRule(errorMessage, sensitiveWords));
    }

    public TextValidatorBuilder historyRecords(String errorMessage, String... historyRecords){
        return rule(new HistoryRecordsRule(errorMessage, historyRecords));
    }

    public TextValidatorBuilder ipv4Address(String errorMessage){
        return rule(new IPv4Rule(errorMessage));
    }

    public TextValidatorBuilder ipv6Address(String errorMessage){
        return rule(new IPv6Rule(errorMessage));
    }

    public TextValidatorBuilder macAddress(String errorMessage){
        return rule(new MacAddressRule(errorMessage));
    }

    public TextValidatorBuilder rfc1123Hostname(String errorMessage){
        return rule(new Rfc1123HostnameRule(errorMessage));
    }

    public TextValidatorBuilder port(String errorMessage){
        return rule(new PortRule(errorMessage));
    }

    public TextValidatorBuilder portRange(IntRange intRange, String errorMessage){
        return rule(new PortRangeRule(intRange, errorMessage));
    }

    public TextValidatorBuilder intRange(IntRange range, String errorMessage){
        return rule(new IntRangeRule(range, errorMessage));
    }

    public TextValidatorBuilder intType(String errorMessage){
        return rule(new IntRule(errorMessage));
    }

    public TextValidatorBuilder doubleRange(DoubleRange range, String errorMessage){
        return rule(new DoubleRangeRule(range, errorMessage));
    }

    public TextValidatorBuilder doubleType(String errorMessage){
        return rule(new DoubleRule(errorMessage));
    }

    public TextValidatorBuilder longRange(LongRange range, String errorMessage){
        return rule(new LongRangeRule(range, errorMessage));
    }

    public TextValidatorBuilder longType(String errorMessage){
        return rule(new LongRule(errorMessage));
    }

    public TextValidatorBuilder booleanType(String errorMessage){
        return rule(new BooleanRule(errorMessage));
    }

    /**
     * 身份证号
     */
    public TextValidatorBuilder idCard(String errorMessage){
        return rule(new IdCardRule(errorMessage));
    }

    /**
     * 车牌号
     */
    public TextValidatorBuilder plateNumber(String errorMessage){
        return rule(new PlateNumberRule(errorMessage));
    }

    public TextValidatorBuilder dateString(String pattern, String errorMessage){
        return rule(new DateStringRule(pattern, errorMessage));
    }

    public TextValidatorBuilder url(String errorMessage, String... schemas){
        return rule(new UrlRule(errorMessage, schemas));
    }

    public TextValidator build(){
        TextValidator validator = new TextValidator();
        validator.setValidateMode(validateMode);
        validator.setRequiredRule(requiredRule);
        validator.setRules(rules);
        return validator;
    }

}
