package com.component.kotlintest.demo.java.annotation.demo;

/**
 * @Author Fox
 * @Date 2020/10/15 22:05
 */
class AnnotationDemoTest {

    @MyBefore
    public void init() {
        System.out.println("init");
    }

    @MyAfter
    public void destroy() {
        System.out.println("destroy");
    }

    @MyTest
    public void testDoingSomething() {
        System.out.println("doing something...");
    }

    @MyTest
    public void testDoingOther() {
        System.out.println("doing other...");
    }
}
