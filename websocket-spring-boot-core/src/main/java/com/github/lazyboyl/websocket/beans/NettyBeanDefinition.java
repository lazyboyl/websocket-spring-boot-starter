package com.github.lazyboyl.websocket.beans;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author linzf
 * @since 2020/6/28
 * 类描述：
 */
public class NettyBeanDefinition {

    /**
     * 当前实例化的对象
     */
    private Object object;

    /**
     * 类的全名
     */
    private String className;

    /**
     * 类上的注解
     */
    private Annotation[] classAnnotation;

    /**
     * 方法名称作为key，方法的信息作为value进行保存
     */
    private Map<String, NettyMethodDefinition> methodMap;

    /**
     * 类上的属性名称未做KEY，属性的信息作为value进行保存
     */
    private Map<String, NettyFieldDefinition> fieldMap;

    /**
     * 类上的响应的注解的路径
     */
    private String [] mappingPath;

    /**
     * 当前执行的级别
     */
    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String[] getMappingPath() {
        return mappingPath;
    }

    public void setMappingPath(String[] mappingPath) {
        this.mappingPath = mappingPath;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Annotation[] getClassAnnotation() {
        return classAnnotation;
    }

    public void setClassAnnotation(Annotation[] classAnnotation) {
        this.classAnnotation = classAnnotation;
    }

    public Map<String, NettyMethodDefinition> getMethodMap() {
        return methodMap;
    }

    public void setMethodMap(Map<String, NettyMethodDefinition> methodMap) {
        this.methodMap = methodMap;
    }

    public Map<String, NettyFieldDefinition> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, NettyFieldDefinition> fieldMap) {
        this.fieldMap = fieldMap;
    }
}
