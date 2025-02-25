package com.jn.langx.validator;


import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Lists;

import java.util.List;

public class ValidationResult {
    boolean valid = false;
    private List<String> errorMessages;
    public ValidationResult(){}

    private ValidationResult(boolean valid, String errorMessage){
        this.valid= valid;
        this.errorMessages = Lists.newArrayList(errorMessage);
    }

    public static ValidationResult ofInvalid(String errorMessage){
        return new ValidationResult(false, errorMessage);
    }

    public static ValidationResult ofValid(){
        return new ValidationResult(true,null);
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public String getErrorMessagesString() {
        return Strings.join("; ", errorMessages);
    }

    public ValidationResult merge(ValidationResult another){
        if(another.isValid()){
            return this;
        }else{
            if(this.isValid()){
                return another;
            }else{
                this.errorMessages.addAll(another.getErrorMessages());
            }
            return this;
        }
    }

}
