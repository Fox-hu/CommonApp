package com.component.kotlintest.test;

import com.component.kotlintest.demo.java.Son;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @Author fox.hu
 * @Date 2020/10/22 14:46
 */
@RunWith(JUnit4.class)
public class JavaTest {

    @Test
    public void testOrder() {
        new Son();
    }
}