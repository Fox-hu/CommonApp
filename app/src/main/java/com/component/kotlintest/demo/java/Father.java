package com.component.kotlintest.demo.java;

/**
 * @Author fox.hu
 * @Date 2020/8/3 17:51
 */
public class Father {

    static {
        System.out.println("父类静态代码块调用");
    }

    {
        System.out.println("父类普通代码块调用");
    }

    private static String value = getString();

    private String mValue = getLocalString();

    private String getLocalString() {
        System.out.println("父类成员变量调用");
        return "value";
    }

    public Father() {
        System.out.println("父类构造方法调用");
        init();
    }

    protected void init() {
        System.out.println("父类init方法调用");
    }

    private static String getString() {
        System.out.println("父类静态变量调用");
        return "value";
    }
}
