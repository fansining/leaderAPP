package com.example.page3;

import cn.bmob.v3.BmobObject;

/**
 * Created by wu on 2016/11/21.
 */
public class loginPerson extends BmobObject {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
