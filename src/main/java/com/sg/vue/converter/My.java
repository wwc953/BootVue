package com.sg.vue.converter;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;

@Component
public class My extends BaseConverter {

    @Override
    protected String getRelaValue(Set<String> annotationType, String codeCls, String sourceValue) {
        String writeValue = sourceValue;
        if (!StringUtils.isEmpty(codeCls)) {
//                        writeValue=StCodeUtil.getCodeName(me.getCodeCls(),writeValue);
            writeValue = "mmmmmmm";
        }
        return writeValue;
    }
}
