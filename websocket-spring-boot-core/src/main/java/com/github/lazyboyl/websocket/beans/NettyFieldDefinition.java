package com.github.lazyboyl.websocket.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author linzf
 * @since 2020/6/28
 * 类描述： 属性的定义信息
 */
public class NettyFieldDefinition {

    /**
     * 属性对象
     */
    private Field f;

    /**
     * 属性名称
     */
    private String fieldName;

    /**
     * 属性上的注解
     */
    private Annotation[] fieldAnnotation;

    /**
     * 属性类型
     */
    private Class fieldType;

    public Field getF() {
        return f;
    }

    public void setF(Field f) {
        this.f = f;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Annotation[] getFieldAnnotation() {
        return fieldAnnotation;
    }

    public void setFieldAnnotation(Annotation[] fieldAnnotation) {
        this.fieldAnnotation = fieldAnnotation;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class fieldType) {
        this.fieldType = fieldType;
    }
}
