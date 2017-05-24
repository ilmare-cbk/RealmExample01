package com.ifnill.realmaddressbook.dto;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bo on 2017. 5. 24..
 */

public class User extends RealmObject {
    private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
