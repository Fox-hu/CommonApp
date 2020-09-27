package com.component.kotlintest.java;

/**
 * @Author fox.hu
 * @Date 2020/9/23 16:35
 */
public class Person {
    public String name;
    private int age;

    //包含一个带参的构造器和一个不带参的构造器
    public Person(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    private Person(String name) {
        this.name = name;
    }

    public Person() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    @AgeValidator(max = 35, min = 18)
    public void setAge(int age) {
        this.age = age;
    }
}
