package com.component.kotlintest.annotation;


import com.component.kotlintest.java.annotation.AnnotationDemo;
import com.component.kotlintest.java.annotation.TestAnnotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author fox.hu
 * @Date 2020/9/22 10:14
 */
@RunWith(JUnit4.class)
public class AnnotationTest {

    @Test
    public void testClassAnnotation() throws Exception {
        //获取类上的注解
        Class<AnnotationDemo> clazz = AnnotationDemo.class;
        TestAnnotation annotation = clazz.getAnnotation(TestAnnotation.class);
        System.out.println(annotation.getValue());

        //获取成员变量上的注解
        Field name = clazz.getField("name");
        TestAnnotation annotationOnFiled = name.getAnnotation(TestAnnotation.class);
        System.out.println(annotationOnFiled.getValue());

        //获取方法上的注解
        Method hello = clazz.getMethod("hello");
        TestAnnotation annotationOnMethod = hello.getAnnotation(TestAnnotation.class);
        System.out.println(annotationOnMethod.getValue());

        //获取构造方法上的注解
        Constructor<AnnotationDemo> constructor = clazz.getDeclaredConstructor();
        TestAnnotation annotationOnConstructor = constructor.getAnnotation(TestAnnotation.class);
        System.out.println(annotationOnConstructor.getValue());

        //获取方法参数上的注解 一个参数可以有多个注解 一个方法可以有多个参数 所以最终获取的是一个二维数组
        Method local = clazz.getMethod("local", String.class);
        Annotation[][] parameterAnnotations = local.getParameterAnnotations();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation paramAnnotation : parameterAnnotation) {
                if ((paramAnnotation instanceof TestAnnotation)) {
                    System.out.println(((TestAnnotation) paramAnnotation).getValue());
                }
            }
        }

        TestAnnotation annotation1 = local.getAnnotation(TestAnnotation.class);

    }
}
