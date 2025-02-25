package com.jn.langx.test.validation;

import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.validation.rule.CharData;
import com.jn.langx.validation.rule.ValidationResult;
import com.jn.langx.validation.TextValidator;
import com.jn.langx.validation.TextValidatorBuilder;
import org.junit.Test;

public class ValidatorTests {

    @Test
    public void test_length() {
        // 创建一个 ValidatorBuilder 实例
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .required()
                .length(6)
                .build();

        String input = "test1";
        showTestResult(validator, input);

        input = "test12";
        showTestResult(validator, input);

        input = "";
        showTestResult(validator, input);

        input = null;
        showTestResult(validator, input);
    }

    @Test
    public void test_length_validChars() {
        // 创建一个 ValidatorBuilder 实例
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .required()
                .length(6, 14)
                .validChars(CharData.ALPHABET_DIGITS)
                .build();
        String input = "testexample#123";
        showTestResult(validator, input);
        input = "test@example.com";
        showTestResult(validator, input);
    }

    @Test
    public void test_email() {
        TextValidatorBuilder validatorBuilder = TextValidatorBuilder.newBuilder();

        // 添加正则表达式规则（验证邮箱格式）
        Regexp emailRegexp = Regexps.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        validatorBuilder.regexp("邮箱格式不正确",emailRegexp);

        showTestResult(validatorBuilder.build(), "test@example.com");

    }

    public void test_password_validate_1(){
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .length(8)
                .limitCharsOccurCount(CharData.UPPER_CASE, 1)
                .limitCharsOccurCount(CharData.LOWER_CASE, 4)
                .limitCharsOccurCount(CharData.DIGITS, 1)
                .limitCharsOccurCount(CharData.SPECIAL_ASCII, 1)
                .build();
    }


    private void showTestResult(TextValidator validator, String input) {

        // 使用 Validator 进行验证
        ValidationResult result = validator.validate(input);

        // 输出验证结果
        if (result.isValid()) {
            System.out.println("验证通过");
        } else {
            System.out.println("验证失败: " + result.getErrorMessagesString());
        }
    }


}
