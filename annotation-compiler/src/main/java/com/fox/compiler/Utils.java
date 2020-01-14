package com.fox.compiler;

/**
 * Created by fox.hu on 2018/9/21.
 */

public class Utils {
    private static final String FACTORY_OUTPUT_PKG = "com.component.gen";
    private static final String DOT = ".";
    private static final String FACTORY = "MealFactory";
    public static final String MEAL_CLASS = "com.fox.annotation.Meal";
    public static final String PREFIX_OF_LOGGER = "[Router-Anno-Compiler]-- ";


    public static String genFactoryClass() {
        return FACTORY_OUTPUT_PKG + DOT + FACTORY;
    }
}
