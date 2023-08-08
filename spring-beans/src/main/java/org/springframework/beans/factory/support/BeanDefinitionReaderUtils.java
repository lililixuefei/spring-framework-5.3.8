/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Utility methods that are useful for bean definition reader implementations.
 * Mainly intended for internal use.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @see PropertiesBeanDefinitionReader
 * @see org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader
 * @since 1.1
 */
public abstract class BeanDefinitionReaderUtils {

    /**
     * Separator for generated bean names. If a class name or parent name is not
     * unique, "#1", "#2" etc will be appended, until the name becomes unique.
     */
    public static final String GENERATED_BEAN_NAME_SEPARATOR = BeanFactoryUtils.GENERATED_BEAN_NAME_SEPARATOR;


    /**
     * Create a new GenericBeanDefinition for the given parent name and class name,
     * eagerly loading the bean class if a ClassLoader has been specified.
     *
     * @param parentName  the name of the parent bean, if any
     * @param className   the name of the bean class, if any
     * @param classLoader the ClassLoader to use for loading bean classes
     *                    (can be {@code null} to just register bean classes by name)
     * @return the bean definition
     * @throws ClassNotFoundException if the bean class could not be loaded
     */
    public static AbstractBeanDefinition createBeanDefinition(
            @Nullable String parentName, @Nullable String className, @Nullable ClassLoader classLoader) throws ClassNotFoundException {

        GenericBeanDefinition bd = new GenericBeanDefinition();
        // parentName可能为空
        bd.setParentName(parentName);
        if (className != null) {
            if (classLoader != null) {
                // 如果classLoader不为空，则使用传入的classLoader同一虚拟机加载类对象，否则只是记录className
                bd.setBeanClass(ClassUtils.forName(className, classLoader));
            } else {
                bd.setBeanClassName(className);
            }
        }
        return bd;
    }

    /**
     * Generate a bean name for the given top-level bean definition,
     * unique within the given bean factory.
     *
     * @param beanDefinition the bean definition to generate a bean name for
     * @param registry       the bean factory that the definition is going to be
     *                       registered with (to check for existing bean names)
     * @return the generated bean name
     * @throws BeanDefinitionStoreException if no unique name can be generated
     *                                      for the given bean definition
     * @see #generateBeanName(BeanDefinition, BeanDefinitionRegistry, boolean)
     */
    public static String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry)
            throws BeanDefinitionStoreException {

        // isInnerBean 如果是内部类表示true，这个工具类也能处理
        // isInnerBean：是为了区分内部bean(innerBean）和顶级bean（top-level bean).
        return generateBeanName(beanDefinition, registry, false);
    }

    /**
     * Generate a bean name for the given bean definition, unique within the
     * given bean factory.
     *
     * @param definition  the bean definition to generate a bean name for
     * @param registry    the bean factory that the definition is going to be
     *                    registered with (to check for existing bean names)
     * @param isInnerBean whether the given bean definition will be registered
     *                    as inner bean or as top-level bean (allowing for special name generation
     *                    for inner beans versus top-level beans)
     * @return the generated bean name
     * @throws BeanDefinitionStoreException if no unique name can be generated
     *                                      for the given bean definition
     */
    public static String generateBeanName(
            BeanDefinition definition, BeanDefinitionRegistry registry, boolean isInnerBean)
            throws BeanDefinitionStoreException {

        /**
         * 对于它的处理逻辑，可以总结为如下步骤：
         *
         * 读取待生成Bean实例的类名称（未必是运行时的实际类型）。
         * 如果类型为空，则判断是否存在parent bean，如果存在，读取parent bean的name + “$child”。
         * 如果parent bean 为空，那么判断是否存在factory bean ，如存在，factory bean name + “$created”。 到此处前缀生成完毕
         * 如果前缀为空，直接抛出异常，没有可以定义这个bean的任何依据。
         * 前缀存在，判断是否为内部bean（innerBean，此处默认为false），如果是，最终为前缀+分隔符+十六进制的hashcode码
         * 如果是顶级bean（top-level bean ），则判断前缀+数字的bean是否已存在，循环查询，知道查询到没有使用的id为止。
         * 处理完成（所以这个生成器肯定能保证Bean定义的唯一性，不会出现Bean name覆盖问题）
         */


        // 拿到Bean定义信息里面的BeanClassName全类名
        // 注意这个不是必须的，因为如果是继承关系，配上父类的依旧行了
        String generatedBeanName = definition.getBeanClassName();
        if (generatedBeanName == null) {
            // 若没有配置本类全类名，去拿到父类的全类名+$child"俩表示自己
            if (definition.getParentName() != null) {
                generatedBeanName = definition.getParentName() + "$child";
            }
            // 工厂Bean的  就用方法的名字+"$created"
            else if (definition.getFactoryBeanName() != null) {
                generatedBeanName = definition.getFactoryBeanName() + "$created";
            }
        }
        // 若一个都没找到，抛错~
        if (!StringUtils.hasText(generatedBeanName)) {
            throw new BeanDefinitionStoreException("Unnamed bean definition specifies neither " +
                    "'class' nor 'parent' nor 'factory-bean' - can't generate bean name");
        }

        if (isInnerBean) {
            // Inner bean: generate identity hashcode suffix.
            return generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + ObjectUtils.getIdentityHexString(definition);
        }

        // 如果不是内部类（绝大多数情况下都如此）
        // 此方法注意：一定能够保证到你的BeanName是唯一的~~~~
        // Top-level bean: use plain class name with unique suffix if necessary.
        return uniqueBeanName(generatedBeanName, registry);
    }

    /**
     * Turn the given bean name into a unique bean name for the given bean factory,
     * appending a unique counter as suffix if necessary.
     *
     * @param beanName the original bean name
     * @param registry the bean factory that the definition is going to be
     *                 registered with (to check for existing bean names)
     * @return the unique bean name to use
     * @since 5.1
     */
    public static String uniqueBeanName(String beanName, BeanDefinitionRegistry registry) {
        String id = beanName;
        int counter = -1;

        // Increase counter until the id is unique.
        String prefix = beanName + GENERATED_BEAN_NAME_SEPARATOR;
        while (counter == -1 || registry.containsBeanDefinition(id)) {
            counter++;
            id = prefix + counter;
        }
        return id;
    }

    /**
     * Register the given bean definition with the given bean factory.
     *
     * @param definitionHolder the bean definition including name and aliases
     * @param registry         the bean factory to register with
     * @throws BeanDefinitionStoreException if registration failed
     */
    public static void registerBeanDefinition(
            BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
            throws BeanDefinitionStoreException {

        // Register bean definition under primary name.
        // 使用beanName做唯一标识注册
        String beanName = definitionHolder.getBeanName();
        registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

        // Register aliases for bean name, if any.
        // 注册所有别名
        String[] aliases = definitionHolder.getAliases();
        if (aliases != null) {
            for (String alias : aliases) {
                registry.registerAlias(beanName, alias);
            }
        }
    }

    /**
     * Register the given bean definition with a generated name,
     * unique within the given bean factory.
     *
     * @param definition the bean definition to generate a bean name for
     * @param registry   the bean factory to register with
     * @return the generated bean name
     * @throws BeanDefinitionStoreException if no unique name can be generated
     *                                      for the given bean definition or the definition cannot be registered
     */
    public static String registerWithGeneratedName(
            AbstractBeanDefinition definition, BeanDefinitionRegistry registry)
            throws BeanDefinitionStoreException {

        String generatedName = generateBeanName(definition, registry, false);
        registry.registerBeanDefinition(generatedName, definition);
        return generatedName;
    }

}
