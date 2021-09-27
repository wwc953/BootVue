package com.sg.vue.model.dto;

import com.sg.vue.converter.annotion.EnableUserInfoTransform;
import com.sg.vue.converter.annotion.TransfCode;
import com.sg.vue.converter.annotion.TransfUser;

@EnableUserInfoTransform
public class ConvBean {
    private String type;
    @TransfCode(valueFrom = "type", codeType = "mycodetype")
    private String typeName;

    private String testPerson;
    @TransfUser(valueFrom = "testPerson")
    private String testPersonName;

    public String getTestPerson() {
        return testPerson;
    }

    public void setTestPerson(String testPerson) {
        this.testPerson = testPerson;
    }

    public String getTestPersonName() {
        return testPersonName;
    }

    public void setTestPersonName(String testPersonName) {
        this.testPersonName = testPersonName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
