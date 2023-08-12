package com.xuefei.custom.propertyeditor;

import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 21:46
 */
@Component
public class XfEntity {

    private String id;
    private String name;
    private Address address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
