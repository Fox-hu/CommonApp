package com.component.kotlintest.demo.java.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author Fox
 * @Date 2020/10/14 22:38
 */
public class A<T> {
    public A() {
        Class<? extends A> subClass = this.getClass();
//        System.out.println(subClass.getName());

        Type genericSuperclass = subClass.getGenericSuperclass();
        // 本质是ParameterizedTypeImpl，可以向下强转
        ParameterizedType parameterizedTypeSuperclass = (ParameterizedType) genericSuperclass;
        // 强转后可用的方法变多了，比如getActualTypeArguments()可以获取Class A<String>的泛型的实际类型参数
        Type[] actualTypeArguments = parameterizedTypeSuperclass.getActualTypeArguments();
        // 由于A类只有一个泛型，这里可以直接通过actualTypeArguments[0]得到子类传递的实际类型参数
        Class actualTypeArgument = (Class) actualTypeArguments[0];
        System.out.println(actualTypeArgument);
        System.out.println(subClass.getName());
    }
}
