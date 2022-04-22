package com.component.kotlintest.demo.java.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Fox
 * @Date 2020/9/26 0:10
 */
@Target({ElementType.FIELD,
         ElementType.TYPE,
         ElementType.CONSTRUCTOR,
         ElementType.METHOD,
         ElementType.LOCAL_VARIABLE,//只能保留在Source中 无法在RUNTIME中保留 暂时无用
         ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
    String getValue() default "default";
}
