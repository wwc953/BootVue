package com.sg.vue.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String name;
    private String age;
    private String addr;
}
