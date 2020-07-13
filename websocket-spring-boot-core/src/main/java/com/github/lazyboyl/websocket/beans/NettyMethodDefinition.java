package com.github.lazyboyl.websocket.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author linzf
 * @since 2020/6/28
 * 类描述： 方法的定义信息
 */
public class NettyMethodDefinition {

    /**
     * 方法对象
     */
    private Method method;

    /**
     * 方法调用的请求方式：
     * GET,
     * HEAD,
     * POST,
     * PUT,
     * PATCH,
     * DELETE,
     * OPTIONS,
     * TRACE;
     */
    private String nettyRequestMethod;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法上的注解
     */
    private Annotation[] methodAnnotation;

    /**
     * 方法上的请求的入参类型
     */
    private Class[] parameterTypesClass;

    /**
     * 方法上的请求的入参名称
     */
    private Parameter[] parameters;

    /**
     * 方法调用返回的class
     */
    private Class returnClass;

    /**
     * 当前方法所处类的bean的名称
     */
    private String beanName;

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }

    public String getNettyRequestMethod() {
        return nettyRequestMethod;
    }

    public void setNettyRequestMethod(String nettyRequestMethod) {
        this.nettyRequestMethod = nettyRequestMethod;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Annotation[] getMethodAnnotation() {
        return methodAnnotation;
    }

    public void setMethodAnnotation(Annotation[] methodAnnotation) {
        this.methodAnnotation = methodAnnotation;
    }

    public Class[] getParameterTypesClass() {
        return parameterTypesClass;
    }

    public void setParameterTypesClass(Class[] parameterTypesClass) {
        this.parameterTypesClass = parameterTypesClass;
    }

    public Class getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(Class returnClass) {
        this.returnClass = returnClass;
    }
}
