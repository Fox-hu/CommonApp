package com.component.kotlintest.demo.java.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author fox.hu
 * @Date 2020/9/24 16:45
 */
public class TypeTest<T, V extends @Custom Number & Serializable> {
    private Number number;
    public T t;
    public V v;
    public List<T> list = new ArrayList<>();
    public Map<String,T> map = new HashMap<>();

    public T[] tArray;
    public List<T>[] ltArray;

    public TypeTest testClass;
    public TypeTest<T, Integer> testClass2;

    public Map<? super String, ? extends Number> mapWithWildcard;

    //泛型构造参数
    public <X extends Number> TypeTest(X x, T t) {
        number = x;
        this.t = t;
    }

    public <Y extends T> void method(Y y) {
        t = y;
    }
}
