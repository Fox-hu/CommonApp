package com.component.kotlintest.generic;


import com.component.kotlintest.demo.java.generic.B;
import com.component.kotlintest.demo.java.generic.C;
import com.component.kotlintest.demo.java.generic.TypeTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

/**
 * @Author fox.hu
 * @Date 2020/9/22 10:14
 */
@RunWith(JUnit4.class)
public class GenericTest {

    @Test
    public void testTypeVariable() throws Exception {
        Field v = TypeTest.class.getField("v");
        TypeVariable typeVariable = (TypeVariable) v.getGenericType();
        System.out.println("TypeVariable1:" + typeVariable);
        System.out.println("TypeVariable2:" + Arrays.asList(typeVariable.getBounds()));
        System.out.println("TypeVariable3:" + typeVariable.getGenericDeclaration());
        System.out.println("TypeVariable4:" + typeVariable.getName());
    }

    @Test
    public void testParameterizedType() throws Exception {
        Field list = TypeTest.class.getField("list");
        Type genericType = list.getGenericType();
        System.out.println("参数类型1：" + genericType.getTypeName());

        Field map = TypeTest.class.getField("map");
        Type genericType1 = map.getGenericType();
        System.out.println("参数类型2：" + genericType1.getTypeName());

        if (genericType1 instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) genericType1;
            Type[] types = pType.getActualTypeArguments();
            System.out.println("参数类型列表：" + Arrays.asList(types));
            System.out.println("参数原始类型：" + pType.getRawType());
            System.out.println("参数父类类型：" + pType.getOwnerType());
        }
    }

    @Test
    public void testGenericArrayType() throws Exception {
        Field tArray = TypeTest.class.getField("tArray");
        System.out.println("数组参数类型1：" + tArray.getGenericType());

        Field ltArray = TypeTest.class.getField("ltArray");
        System.out.println("数组参数类型2：" + ltArray.getGenericType());

        if (tArray.getGenericType() instanceof GenericArrayType) {
            GenericArrayType arrayType = (GenericArrayType) tArray.getGenericType();
            System.out.println("数组参数类型3:" + arrayType.getGenericComponentType());//数组参数类型3:T
        }
    }

    @Test
    public void testWildcardType() throws Exception {
        Field mapWithWildcard = TypeTest.class.getField("mapWithWildcard");
        Type wild = mapWithWildcard.getGenericType();//先获取属性的泛型类型 Map<? super String, ? extends Number>
        if (wild instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) wild;
            Type[] actualTypes = pType.getActualTypeArguments();//获取<>里面的参数变量 ? super String, ? extends Number
            System.out.println("WildcardType1:" + Arrays.asList(actualTypes));
            WildcardType first = (WildcardType) actualTypes[0];//? super java.lang.String
            WildcardType second = (WildcardType) actualTypes[1];//? extends java.lang.Number
            System.out.println("WildcardType2: lower:" + Arrays.asList(first.getLowerBounds()) + "  upper:" + Arrays.asList(first.getUpperBounds()));//WildcardType2: lower:[class java.lang.String]  upper:[class java.lang.Object]
            System.out.println("WildcardType3: lower:" + Arrays.asList(second.getLowerBounds()) + "  upper:" + Arrays.asList(second.getUpperBounds()));//WildcardType3: lower:[]  upper:[class java.lang.Number]
        }
    }

    @Test
    public void testGenericClass() throws Exception {
        Field testClass = TypeTest.class.getField("testClass");
        System.out.println("Class1:" + testClass.getGenericType());
        Field testClass2 = TypeTest.class.getField("testClass2");
        System.out.println("Class2:" + testClass2.getGenericType());
    }

    @Test
    public void testThis() {
        new B();
        new C();
    }
}
