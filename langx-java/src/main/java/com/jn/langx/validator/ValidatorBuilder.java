package com.jn.langx.validator;

import com.jn.langx.Builder;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.regexp.Regexp;

import java.util.List;

public class ValidatorBuilder implements Builder<Validator> {
    private List<Rule> rules= Lists.newArrayList();
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

    public ValidatorBuilder regexp(Regexp regexp, String errorMessage){
        return rule(new RegexpRule(regexp, errorMessage));
    }


    public Validator build(){
        Validator validator = new Validator();
        validator.setRules(rules);
        return validator;
    }

}
