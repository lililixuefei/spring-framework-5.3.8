/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.beans;

import java.beans.PropertyDescriptor;

/**
 * The central interface of Spring's low-level JavaBeans infrastructure.
 *
 * <p>Typically not used directly but rather implicitly via a
 * {@link org.springframework.beans.factory.BeanFactory} or a
 * {@link org.springframework.validation.DataBinder}.
 *
 * <p>Provides operations to analyze and manipulate standard JavaBeans:
 * the ability to get and set property values (individually or in bulk),
 * get property descriptors, and query the readability/writability of properties.
 *
 * <p>This interface supports <b>nested properties</b> enabling the setting
 * of properties on subproperties to an unlimited depth.
 *
 * <p>A BeanWrapper's default for the "extractOldValueForEditor" setting
 * is "false", to avoid side effects caused by getter method invocations.
 * Turn this to "true" to expose present property values to custom editors.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see PropertyAccessor
 * @see PropertyEditorRegistry
 * @see PropertyAccessorFactory#forBeanPropertyAccess
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.validation.BeanPropertyBindingResult
 * @see org.springframework.validation.DataBinder#initBeanPropertyAccess()
 * <p>
 * BeanWrapper 是Spring提供的一个用来操作javaBean属性的工具，使用它可以直接修改一个对象的属性。
 * BeanWrapper大部分情况下是在spring ioc内部进行使用，通过BeanWrapper,spring ioc容器可以用统一的方式来访问bean的属性。用户很少需要直接使用BeanWrapper进行编程。
 * <p>
 * 对于bean属性的操作，我们熟知的主要有下面这些工具类：
 * <p>
 * Apache的BeanUtils和PropertyUtils
 * cglib的BeanMap和BeanCopier
 * spring的BeanUtils
 * Spring中BeanWrapper 的主要在于如下三点：
 * 1· 支持设置嵌套属性
 * 2. 支持属性值的类型转换（设置ConversionService）
 * 3. 提供分析和操作标准JavaBean的操作：获取和设置属性值（单独或批量），获取属性描述符以及查询属性的可读性/可写性的能力
 * @since 13 April 2001
 */
public interface BeanWrapper extends ConfigurablePropertyAccessor {

	/**
	 * 为数组和集合自动增长指定一个限制。在普通的BeanWrapper上默认是无限的。
	 * Specify a limit for array and collection auto-growing.
	 * <p>Default is unlimited on a plain BeanWrapper.
	 *
	 * @since 4.1
	 */
	void setAutoGrowCollectionLimit(int autoGrowCollectionLimit);

	/**
	 * 返回数组和集合自动增长的限制。
	 * Return the limit for array and collection auto-growing.
	 *
	 * @since 4.1
	 */
	int getAutoGrowCollectionLimit();

	/**
	 * 如果有的话,返回由此对象包装的bean实例
	 * Return the bean instance wrapped by this object.
	 */
	Object getWrappedInstance();

	/**
	 * 返回被包装的JavaBean对象的类型。
	 * Return the type of the wrapped bean instance.
	 */
	Class<?> getWrappedClass();

	/**
	 * 获取包装对象的PropertyDescriptors（由标准JavaBeans自省确定）。
	 * Obtain the PropertyDescriptors for the wrapped object
	 * (as determined by standard JavaBeans introspection).
	 *
	 * @return the PropertyDescriptors for the wrapped object
	 */
	PropertyDescriptor[] getPropertyDescriptors();

	/**
	 * 获取包装对象的特定属性的属性描述符。
	 * Obtain the property descriptor for a specific property
	 * of the wrapped object.
	 *
	 * @param propertyName the property to obtain the descriptor for
	 *                     (may be a nested path, but no indexed/mapped property)
	 * @return the property descriptor for the specified property
	 * @throws InvalidPropertyException if there is no such property
	 */
	PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException;

}
