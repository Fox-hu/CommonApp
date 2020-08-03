package com.component.kotlintest.java;

/**
 * @Author fox.hu
 * @Date 2020/8/3 17:52
 */
public class Son extends Father{

    static {
        System.out.println("子类静态代码块调用");
    }

    {
        System.out.println("子类普通代码块调用");
    }

    private static String value = getString();

    private String mValue = getLocalString();

    private String getLocalString() {
        System.out.println("子类成员变量调用");
        return "value";
    }

    public Son() {
        System.out.println("子类构造方法调用");
    }

    @Override
    protected void init() {
        super.init();
        System.out.println("子类init方法调用");
    }

    private static String getString() {
        System.out.println("子类静态变量调用");
        return "value";
    }
}
