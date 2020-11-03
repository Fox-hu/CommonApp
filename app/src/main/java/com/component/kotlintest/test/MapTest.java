package com.component.kotlintest.test;

import java.util.HashMap;

/**
 * @Author fox.hu
 * @Date 2020/10/22 14:44
 */
class MapTest {

    private final HashMap<String, Object> map = new HashMap<>();

    public void test() {
        //从map中取出元素后强转为 基本元素一定要判空
        //boolean testboolen = (boolean) map.get("111");
        String obj = null;
        String test = null;
        obj = (String) test;
    }
}
