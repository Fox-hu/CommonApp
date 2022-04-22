package com.component.kotlintest.demo.java.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author Fox
 * @Date 2020/10/14 22:18
 */
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
    // 8种基本数据类型
    int intValue();
    long longValue();
    // ...其他类型省略

    // String
    String name();
    // 枚举
    CityEnum cityName();
    // Class类型
    Class<?> clazz();
    // 注解类型
    MyAnnotation2 annotation2();

    // 以上几种类型的数组类型
    int[] intValueArray();
    String[] names();
    // ...其他类型省略
}