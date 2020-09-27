package com.component.kotlintest.java.annotation;

/**
 * @Author Fox
 * @Date 2020/9/27 21:19
 */
@TestAnnotation(getValue = "annotation on class")
public class AnnotationDemo {

    @TestAnnotation(getValue = "annotation on filed")
    public String name;

    @TestAnnotation(getValue = "annotation on constructor")
    AnnotationDemo() {}

    @TestAnnotation(getValue = "annotation on method")
    public void hello() {}

    public void local(@TestAnnotation(getValue = "annotation on param") String param) {
        @TestAnnotation(getValue = "annotation on local") String localParam = "abc";
    }
}
