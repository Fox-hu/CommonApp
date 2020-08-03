package com.component.kotlintest.test;

/**
 * @Author fox
 * @Date 2020/6/4 22:22
 */
public class Son extends Father{

    static {
        System.out.println("子类静态代码块调用");
    }

    public Son() {
        System.out.println("子类构造方法调用");
    }

    private String mValue = getLocalString();
    {
        System.out.println("子类普通代码块调用");
    }
    private static String value = getString();


    private String getLocalString() {
        System.out.println("子类成员变量调用");
        return "value";
    }

    private static String getString() {
        System.out.println("子类静态变量调用");
        return "value";
    }
}
