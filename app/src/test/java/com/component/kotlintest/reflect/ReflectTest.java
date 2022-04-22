package com.component.kotlintest.reflect;


import com.component.kotlintest.demo.java.AgeValidator;
import com.component.kotlintest.demo.java.Person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author fox.hu
 * @Date 2020/9/22 10:14
 */
@RunWith(JUnit4.class)
public class ReflectTest {

    @Test
    public void testClassLoader() throws ClassNotFoundException, FileNotFoundException {
        //获取应用程序类加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);

        //获取应用程序类加载器的父加载器（扩展类加载器）
        ClassLoader parent = systemClassLoader.getParent();
        System.out.println(parent);

        //获取扩展类加载器的父加载器（引导类加载器）
        ClassLoader grandParent = parent.getParent();
        System.out.println(grandParent);

        //测试当前类由哪个类加载器（应用程序类加载器）
        ClassLoader classLoader = Class.forName("com.component.kotlintest.demo.java.Person")
                .getClassLoader();
        System.out.println(classLoader);

        //jdk提供的类加载器（引导类加载器）
        ClassLoader jdkClassLoader = Class.forName("java.lang.Object").getClassLoader();
        System.out.println(jdkClassLoader);
    }

    @Test
    public void testClass() throws ClassNotFoundException {
        Class clazz = null;
        //通过类名
        clazz = Person.class;

        //通过对象来获得class
        Object person = new Person();
        clazz = person.getClass();

        //通过全类名 框架开发多用此方式
        String className = "com.component.kotlintest.demo.java.Person";
        clazz = Class.forName(className);
    }

    @Test
    public void testNewInstance() throws
            ClassNotFoundException,
            InstantiationException,
            IllegalAccessException {
        String className = "com.component.kotlintest.demo.java.Person";
        Class clazz = Class.forName(className);

        Object obj = clazz.newInstance();
        System.out.println(obj);
    }

    @Test
    public void testMethod() throws Exception {
        Class clazz = Class.forName("com.component.kotlintest.demo.java.Person");

        //获取该类即所继承的所有方法 不能获取private方法
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }

        //获取所有方法 包括私有方法 只获取当前类的方法
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            System.out.println(declaredMethod.getName());
        }

        //获取指定的方法，参数为方法名称和参数类型
        //注意 需要区分包装类型和原始类型 int.class 和Integer.class不相同
        Method setNameMethod = clazz.getDeclaredMethod("setName", String.class);
        System.out.println(setNameMethod);

        //执行方法 一定要指定执行的对象是哪一个
        Object obj = clazz.newInstance();
        //如果是私有方法，必须在调用invoke之前加上一句method.setAccessible（true）;
        setNameMethod.invoke(obj, "lili");
    }

    @Test
    public void testFiled() throws Exception {
        Class clazz = Class.forName("com.component.kotlintest.demo.java.Person");

        //获取所有共有、私有字段 不包含父类字段
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.getName());
        }

        //获取指定字段
        Field nameFiled = clazz.getDeclaredField("name");
        System.out.println(nameFiled.getName());

        //获取指定对象的指定字段值
        Person person = new Person("abc", 12);
        Object nameValue = nameFiled.get(person);
        System.out.println(nameValue);

        //设置指定对象的指定字段值
        nameFiled.set(person, "def");
        System.out.println(person.getName());

        //如果是私有的字段 则无论读写 都要先调用setAccessible（true）方法
        Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);
        System.out.println(ageField.get(person));
    }


    @Test
    public void testConstructor() throws Exception {
        Class clazz = Class.forName("com.component.kotlintest.demo.java.Person");

        //获取Person类中全部Constructor对象 包含public和private的
        Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor);
        }

        //获取Person类中全部访问权限是public的Constructor对象
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
        }

        //获取某一个 需要参数列表
        Constructor constructor = clazz.getConstructor(String.class, int.class);
        System.out.println(constructor);

        //调用构造器的newInstance()方法创建对象
        Object zhangsan = constructor.newInstance("zhangsan", 1);
        System.out.println(zhangsan);
    }

    @Test
    public void testAnnotation() throws Exception {
        Class clazz = Class.forName("com.component.kotlintest.demo.java.Person");
        Object obj = clazz.newInstance();

        Method method = clazz.getDeclaredMethod("setAge", int.class);

        int age = 22;
        AgeValidator annotation = method.getAnnotation(AgeValidator.class);
        if (annotation != null) {
            if (age < annotation.min() || age > annotation.max()) {
                throw new RuntimeException("年龄非法");
            }
        }
        method.invoke(obj, 20);
        System.out.println(obj);
    }

}
