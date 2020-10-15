package com.component.kotlintest.java.annotation.demo;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MyJunitFrameWork {

    public static void handle() throws Exception {
        Class<AnnotationDemoTest> clazz = AnnotationDemoTest.class;
        AnnotationDemoTest annotationDemoTest = clazz.newInstance();

        Method[] methods = clazz.getMethods();
        ArrayList<Method> beforeList = new ArrayList<>();
        ArrayList<Method> afterList = new ArrayList<>();
        ArrayList<Method> testList = new ArrayList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(MyBefore.class)) {
                beforeList.add(method);
            }
            if(method.isAnnotationPresent(MyTest.class)){
                testList.add(method);
            }
            if(method.isAnnotationPresent(MyAfter.class)){
                afterList.add(method);
            }
        }

        for (Method test : testList) {
            for (Method before : beforeList) {
                before.invoke(annotationDemoTest);
            }

            test.invoke(annotationDemoTest);
            for (Method after : afterList) {
                after.invoke(annotationDemoTest);
            }
        }
    }
}