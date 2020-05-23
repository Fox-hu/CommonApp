package com.fox.annotation;

import javax.lang.model.element.Element;

/**
 * Created by fox.hu on 2018/9/20.x
 */

public class Node {

    private Element rawType;        // Raw type of route
    private String id;            // Path of route

    public Element getRawType() {
        return rawType;
    }

    public void setRawType(Element rawType) {
        this.rawType = rawType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
