package com.xuefei.custom.propertyeditor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 21:50
 */
public class PropertyEditorApp {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("xuefei/propertyEditor.xml");
        XfEntity entity = context.getBean(XfEntity.class);
        System.out.println(entity.getAddress().getProvince());
        System.out.println(entity.getAddress().getCity());
    }


}
