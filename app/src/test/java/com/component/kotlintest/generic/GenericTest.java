package com.component.kotlintest.generic;


import com.component.kotlintest.java.generic.B;
import com.component.kotlintest.java.generic.C;
import com.component.kotlintest.java.generic.TypeTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Field;
import java.lang.reflect.TypeVariable;
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
    public void testThis(){
        new B();
        new C();
    }
}
