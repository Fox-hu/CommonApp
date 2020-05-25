package com.fox.annotation.view;

/**
 * @Author fox
 * @Date 2020/5/25 21:51
 */
public interface Unbinder {

    void unbind();

    Unbinder EMPTY = new Unbinder() {
        @Override
        public void unbind() {

        }
    };
}
