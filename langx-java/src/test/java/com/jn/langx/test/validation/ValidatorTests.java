package com.jn.langx.test.validation;

import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.ranges.DoubleRange;
import com.jn.langx.util.ranges.IntRange;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.validation.rule.CharData;
import com.jn.langx.validation.rule.LengthRule;
import com.jn.langx.validation.rule.ValidationResult;
import com.jn.langx.validation.TextValidator;
import com.jn.langx.validation.TextValidatorBuilder;
import org.junit.Test;

public class ValidatorTests {

    @Test
    public void test_length() {
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
        validatorBuilder.regexp("邮箱格式不正确", emailRegexp);

        showTestResult(validatorBuilder.build(), "test@example.com");

    }

    @Test
    public void test_password_validate_1() {
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .length(8)
                .limitCharsOccurCount(CharData.UPPER_CASE, 1)
                .limitCharsOccurCount(CharData.LOWER_CASE, 4)
                .limitCharsOccurCount(CharData.DIGITS, 1)
                .limitCharsOccurCount(CharData.SPECIAL_ASCII, 1)
                .build();
        showTestResult(validator, "abcdefg1@");
        showTestResult(validator, "ABcdeFG1@@");
        showTestResult(validator, "Abcdefg1@");
        showTestResult(validator, "Abcdefg1@@");
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


    @Test
    public void test_requiredRule() {
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .required("该字段不能为空")
                .build();

        showTestResult(validator, null);      // 预期失败
        showTestResult(validator, "");        // 预期失败
        showTestResult(validator, "valid");   // 预期通过
    }

    @Test
    public void test_notRule() {
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .not(new LengthRule(5), "长度不能等于5")
                .build();

        showTestResult(validator, "1234");    // 通过（长度≠5）
        showTestResult(validator, "12345");   // 失败（长度=5）
    }

    @Test
    public void test_predicateRule() {
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .predicate(new Predicate<String>() {
                    @Override
                    public boolean test(String s) {
                        return s.startsWith("ID-");
                    }
                }, "必须以ID-开头")
                .build();

        showTestResult(validator, "ID-1001"); // 通过
        showTestResult(validator, "1001");    // 失败
    }

    @Test
    public void test_charRule() {
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .validChars(CharData.DIGITS)
                .build();

        showTestResult(validator, "12345");     // 通过
        showTestResult(validator, "12a45");     // 失败（含字母）
    }

    @Test
    public void test_charOccurCountRule() {
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .limitCharsOccurCount(CharData.UPPER_CASE, 2)
                .build();

        showTestResult(validator, "ABcdef");   // 通过（2个大写）
        showTestResult(validator, "AbcDef");   // 失败（3个大写）
    }

    @Test
    public void test_sensitiveWordsRule() {
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .sensitiveWords("包含敏感词", "暴力", "色情")
                .build();

        showTestResult(validator, "正常内容");  // 通过
        showTestResult(validator, "包含暴力内容"); // 失败
    }

    @Test
    public void test_ipRules() {
        // IPv4
        TextValidator v4 = TextValidatorBuilder.newBuilder()
                .ipv4Address("无效IPv4地址")
                .build();
        showTestResult(v4, "192.168.1.1");    // 通过
        showTestResult(v4, "256.0.0.1");      // 失败

        // IPv6
        TextValidator v6 = TextValidatorBuilder.newBuilder()
                .ipv6Address("无效IPv6地址")
                .build();
        showTestResult(v6, "2001:0db8:85a3:0000:0000:8a2e:0370:7334"); // 通过
        showTestResult(v6, "2001::0db8::1");  // 失败
    }

    @Test
    public void test_macAddressRule() {
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .macAddress(null,"无效MAC地址")
                .build();

        showTestResult(validator, "00:1A:C2:7B:00:47");  // 通过
        showTestResult(validator, "00-1A-C2-7B-00-47");  // 通过（带-分隔）
        showTestResult(validator, "001AC27B0047");
    }

    @Test
    public void test_dateStringRule() {
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .dateString("yyyy-MM-dd", "日期格式错误")
                .build();

        showTestResult(validator, "2023-12-31");  // 通过
        showTestResult(validator, "31/12/2023");  // 失败
    }

    @Test
    public void test_idCardRule() {
        TextValidator validator = TextValidatorBuilder.newBuilder()
                .idCard("身份证格式错误")
                .build();

        showTestResult(validator, "11010119900307567X");  // 通过（示例）
        showTestResult(validator, "123456789012345");     // 失败（15位旧格式）
    }

    @Test
    public void test_portRules() {
        // 单端口
        TextValidator singlePort = TextValidatorBuilder.newBuilder()
                .port("端口必须为8080")
                .build();
        showTestResult(singlePort, "8080");  // 通过
        showTestResult(singlePort, "8090");  // 失败

        // 端口范围
        TextValidator portRange = TextValidatorBuilder.newBuilder()
                .portRange(new IntRange(1024, 65535), "端口超出范围")
                .build();
        showTestResult(portRange, "2048");   // 通过
        showTestResult(portRange, "80");     // 失败
    }

    @Test
    public void test_numberRangeRules() {
        // Int范围
        TextValidator intRange = TextValidatorBuilder.newBuilder()
                .intRange(new IntRange(18, 60), "年龄必须在18-60岁")
                .build();
        showTestResult(intRange, "25");      // 通过
        showTestResult(intRange, "17");      // 失败

        // Double范围
        TextValidator doubleRange = TextValidatorBuilder.newBuilder()
                .doubleRange(new DoubleRange(0.0, 100.0), "分数必须在0-100")
                .build();
        showTestResult(doubleRange, "89.5");  // 通过
        showTestResult(doubleRange, "101");   // 失败
    }

}
