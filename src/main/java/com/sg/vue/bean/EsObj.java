package com.sg.vue.bean;

import java.io.Serializable;
import java.util.List;

public class EsObj implements Serializable {

    private List<MyUser> userlist;
    private String name;
    private List<String> numbers;
    private List<String> addrs;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<MyUser> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<MyUser> userlist) {
        this.userlist = userlist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public List<String> getAddrs() {
        return addrs;
    }

    public void setAddrs(List<String> addrs) {
        this.addrs = addrs;
    }
}
