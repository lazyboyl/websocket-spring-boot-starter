package com.github.lazyboyl.websocket.factory;

import com.github.lazyboyl.websocket.beans.NettyBeanDefinition;
import com.github.lazyboyl.websocket.beans.NettyMethodDefinition;
import org.springframework.core.env.Environment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author linzf
 * @since 2020/7/14
 * 类描述： websocket鉴权的工厂类
 */
public class WebsocketInterfaceBeanFactory extends NettyDefaultBeanFactory {

    private WebsocketInterfaceBeanFactory() {
        super();
    }

    public static WebsocketInterfaceBeanFactory getInstance() {
        return new WebsocketInterfaceBeanFactory();
    }

    private List<NettyBeanDefinition> nettyBeanDefinitionList;

    /**
     * 功能描述： 获取排序以后的类
     *
     * @return bean的list集合
     */
    public List<NettyBeanDefinition> getNettyBeanDefinitionList() {
        if (nettyBeanDefinitionList == null) {
            List<NettyBeanDefinition> nettyBeanDefinitions = new ArrayList<>();
            getNettyBeanDefinitionMap().forEach((k, v) -> {
                v.getMethodMap().forEach((k1, v1) -> {
                    String[] k1s = k1.split("\\.");
                    if (k1s[k1s.length - 1].equals("level")) {
                        try {
                            v.setLevel((Integer) v1.getMethod().invoke(v.getObject()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        nettyBeanDefinitions.add(v);
                    }
                });
            });
            nettyBeanDefinitionList = nettyBeanDefinitions.stream().sorted(Comparator.comparing(NettyBeanDefinition::getLevel)).collect(Collectors.toList());
        }
        return nettyBeanDefinitionList;
    }

    /**
     * 功能描述： 注册netty的bean的类的扫描
     *
     * @param c           需要进行处理的类的对象
     * @param environment 环境对象
     */
    @Override
    protected void registerNettyBeanDefinition(Class c, Environment environment) {
        try {
            NettyBeanDefinition nettyBeanDefinition = new NettyBeanDefinition();
            nettyBeanDefinition.setClassName(c.getName());
            // 获取类上的注解的集合
            nettyBeanDefinition.setClassAnnotation(c.getAnnotations());
            // 初始化对象，包括构造函数的对象的初始化
            Object o = doInstance(c, environment);
            // 初始化类上的方法
            registerNettyMethodDefinition(c, nettyBeanDefinition);
            // 初始化类上的属性
            registerNettyFieldDefinition(c, o, nettyBeanDefinition, environment);
            // 实例化类
            nettyBeanDefinition.setObject(o);
            nettyBeanDefinitionSetAdd(c.getName());
            nettyBeanDefinitionMapPut(c.getName(), nettyBeanDefinition);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 功能描述： 注册对象的方法信息
     *
     * @param c                   class对象
     * @param nettyBeanDefinition netty的bean的信息
     */
    protected void registerNettyMethodDefinition(Class c, NettyBeanDefinition nettyBeanDefinition) {
        Method[] methods = c.getDeclaredMethods();
        Map<String, NettyMethodDefinition> methodMap = new HashMap<>(methods.length);
        for (Method m : methods) {
            NettyMethodDefinition nettyMethodDefinition = new NettyMethodDefinition();
            nettyMethodDefinition.setMethod(m);
            nettyMethodDefinition.setMethodAnnotation(m.getAnnotations());
            nettyMethodDefinition.setParameterTypesClass(m.getParameterTypes());
            nettyMethodDefinition.setReturnClass(m.getReturnType());
            nettyMethodDefinition.setMethodName(m.getName());
            nettyMethodDefinition.setBeanName(c.getName());
            nettyMethodDefinition.setParameters(m.getParameters());
            methodMap.put(c.getName() + "." + m.getName(), nettyMethodDefinition);
        }
        nettyBeanDefinition.setMethodMap(methodMap);
    }


}
