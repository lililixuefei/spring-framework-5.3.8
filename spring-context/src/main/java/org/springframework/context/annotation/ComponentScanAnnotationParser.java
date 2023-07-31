/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Parser for the @{@link ComponentScan} annotation.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @see ClassPathBeanDefinitionScanner#scan(String...)
 * @see ComponentScanBeanDefinitionParser
 * @since 3.1
 */
class ComponentScanAnnotationParser {

	private final Environment environment;

	private final ResourceLoader resourceLoader;

	private final BeanNameGenerator beanNameGenerator;

	private final BeanDefinitionRegistry registry;


	public ComponentScanAnnotationParser(Environment environment, ResourceLoader resourceLoader,
										 BeanNameGenerator beanNameGenerator, BeanDefinitionRegistry registry) {

		this.environment = environment;
		this.resourceLoader = resourceLoader;
		this.beanNameGenerator = beanNameGenerator;
		this.registry = registry;
	}


	/**
	 * 以上方法执行流程如下：
	 * 定义一个扫描器，用来扫描包
	 * 判断是否重写了默认的命名规则
	 * 解析 @ComponentScan 注解的属性，完善扫描器
	 * 处理 @ComponentScan 的 scopedProxy 属性
	 * 处理 @ComponentScan 的 resourcePattern 属性
	 * 处理 @ComponentScan 的 includeFilters 属性
	 * 处理 @ComponentScan 的 excludeFilters 属性
	 * 处理 @ComponentScan 的 basePackages 属性，这个属性的类型是 String
	 * 处理 @ComponentScan 的 basePackageClasses 属性， 这个属性的类型是 Class
	 * 如果没有指定扫包规则，默认会把配置类所在的包名作为扫描路径
	 * 添加排除规则，这里就把注册类自身当作排除规则，真正执行匹配的时候，会把自身给排除
	 * 调用 ClassPathBeanDefinitionScanner#doScan 完成扫描
	 *
	 * @param componentScan
	 * @param declaringClass
	 * @return
	 */
	public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, final String declaringClass) {
		// 1. 定义一个扫描器，用来扫描包
		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry,
				componentScan.getBoolean("useDefaultFilters"), this.environment, this.resourceLoader);

		// 2. 判断是否重写了默认的命名规则
		Class<? extends BeanNameGenerator> generatorClass = componentScan.getClass("nameGenerator");
		boolean useInheritedGenerator = (BeanNameGenerator.class == generatorClass);
		scanner.setBeanNameGenerator(useInheritedGenerator ? this.beanNameGenerator :
				BeanUtils.instantiateClass(generatorClass));

		// 3. 解析 @ComponentScan 注解的属性
		// 3.1 处理 @ComponentScan 的 scopedProxy 属性
		ScopedProxyMode scopedProxyMode = componentScan.getEnum("scopedProxy");
		if (scopedProxyMode != ScopedProxyMode.DEFAULT) {
			scanner.setScopedProxyMode(scopedProxyMode);
		} else {
			Class<? extends ScopeMetadataResolver> resolverClass = componentScan.getClass("scopeResolver");
			scanner.setScopeMetadataResolver(BeanUtils.instantiateClass(resolverClass));
		}

		// 3.2 处理 @ComponentScan 的 resourcePattern 属性
		scanner.setResourcePattern(componentScan.getString("resourcePattern"));

		// 3.3 处理 @ComponentScan 的 includeFilters 属性
		// addIncludeFilter addExcludeFilter,最终是往List<TypeFilter>里面填充数据
		for (AnnotationAttributes filter : componentScan.getAnnotationArray("includeFilters")) {
			for (TypeFilter typeFilter : typeFiltersFor(filter)) {
				scanner.addIncludeFilter(typeFilter);
			}
		}
		// 3.4 处理 @ComponentScan 的 excludeFilters 属性
		for (AnnotationAttributes filter : componentScan.getAnnotationArray("excludeFilters")) {
			for (TypeFilter typeFilter : typeFiltersFor(filter)) {
				scanner.addExcludeFilter(typeFilter);
			}
		}

		boolean lazyInit = componentScan.getBoolean("lazyInit");
		if (lazyInit) {
			scanner.getBeanDefinitionDefaults().setLazyInit(true);
		}

		Set<String> basePackages = new LinkedHashSet<>();
		// 3.5. @ComponentScan 指定了 basePackages 属性，这个属性的类型是String
		String[] basePackagesArray = componentScan.getStringArray("basePackages");
		for (String pkg : basePackagesArray) {
			String[] tokenized = StringUtils.tokenizeToStringArray(this.environment.resolvePlaceholders(pkg),
					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
			Collections.addAll(basePackages, tokenized);
		}
		// 3.6. @ComponentScan 指定了 basePackageClasses， 这个属性的类型是Class，
		//    即只要是与这几个类同级的，或者在这几个类下级的都可以被扫描到
		for (Class<?> clazz : componentScan.getClassArray("basePackageClasses")) {
			basePackages.add(ClassUtils.getPackageName(clazz));
		}
		// 3.7 以上都没有指定，默认会把配置类所在的包名作为扫描路径
		if (basePackages.isEmpty()) {
			basePackages.add(ClassUtils.getPackageName(declaringClass));
		}

		// 3.8 添加排除规则，这里就把注册类自身当作排除规则，真正执行匹配的时候，会把自身给排除
		scanner.addExcludeFilter(new AbstractTypeHierarchyTraversingFilter(false, false) {
			@Override
			protected boolean matchClassName(String className) {
				return declaringClass.equals(className);
			}
		});

		// 4. 到这里，才开始扫描 @ComponentScan 指定的包
		// 扫描完成后，对符合条件的类，spring会将其添加到beanFactory的BeanDefinitionMap中
		return scanner.doScan(StringUtils.toStringArray(basePackages));
	}

	private List<TypeFilter> typeFiltersFor(AnnotationAttributes filterAttributes) {
		List<TypeFilter> typeFilters = new ArrayList<>();
		FilterType filterType = filterAttributes.getEnum("type");

		for (Class<?> filterClass : filterAttributes.getClassArray("classes")) {
			switch (filterType) {
				case ANNOTATION:
					Assert.isAssignable(Annotation.class, filterClass,
							"@ComponentScan ANNOTATION type filter requires an annotation type");
					@SuppressWarnings("unchecked")
					Class<Annotation> annotationType = (Class<Annotation>) filterClass;
					typeFilters.add(new AnnotationTypeFilter(annotationType));
					break;
				case ASSIGNABLE_TYPE:
					typeFilters.add(new AssignableTypeFilter(filterClass));
					break;
				case CUSTOM:
					Assert.isAssignable(TypeFilter.class, filterClass,
							"@ComponentScan CUSTOM type filter requires a TypeFilter implementation");

					TypeFilter filter = ParserStrategyUtils.instantiateClass(filterClass, TypeFilter.class,
							this.environment, this.resourceLoader, this.registry);
					typeFilters.add(filter);
					break;
				default:
					throw new IllegalArgumentException("Filter type not supported with Class value: " + filterType);
			}
		}

		for (String expression : filterAttributes.getStringArray("pattern")) {
			switch (filterType) {
				case ASPECTJ:
					typeFilters.add(new AspectJTypeFilter(expression, this.resourceLoader.getClassLoader()));
					break;
				case REGEX:
					typeFilters.add(new RegexPatternTypeFilter(Pattern.compile(expression)));
					break;
				default:
					throw new IllegalArgumentException("Filter type not supported with String pattern: " + filterType);
			}
		}

		return typeFilters;
	}

}
