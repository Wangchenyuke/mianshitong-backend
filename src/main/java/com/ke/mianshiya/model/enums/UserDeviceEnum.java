package com.ke.mianshiya.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户登录设备枚举
 *
 * @author <a href="https://github.com/like">程序员鱼皮</a>
 * @from <a href="https://ke.icu">编程导航知识星球</a>
 */
public enum UserDeviceEnum {

    MINI_PROGRAM("小程序", "miniProgram"),
    MOBILE("手机端", "mobile"),
    PC("pc端", "pc"),
    Pad("平板", "pad");

    private final String text;

    private final String value;

    UserDeviceEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static UserDeviceEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserDeviceEnum anEnum : UserDeviceEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }


    public String getText() {
        return text;
    }
}
