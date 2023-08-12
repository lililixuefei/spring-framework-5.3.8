package com.xuefei.custom.propertyeditor;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 21:47
 */
public class MyPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            String[] addrs = text.split("_");
            Address address = new Address();
            address.setProvince(addrs[0]);
            address.setCity(addrs[1]);
            this.setValue(address);
            return;
        }
        throw new IllegalArgumentException("MyPropertyEditor: " + text + " could not convert to the target type!");
    }

}
