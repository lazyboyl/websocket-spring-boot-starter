package com.github.lazyboyl.websocket.factory;


import com.github.lazyboyl.websocket.beans.NettyBeanDefinition;
import com.github.lazyboyl.websocket.beans.NettyFieldDefinition;
import com.github.lazyboyl.websocket.beans.NettyMethodDefinition;
import com.github.lazyboyl.websocket.constant.ClassType;
import com.github.lazyboyl.websocket.server.NettySocketServer;
import com.github.lazyboyl.websocket.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author linzf
 * @since 2020/7/7
 * 类描述： 默认的通用工厂方法的实现
 */
public class NettyDefaultBeanFactory implements NettySingletonBeanRegistry {

    /**
     * 定义用于存放netty的bean的map集合
     */
    private final Map<String, NettyBeanDefinition> nettyBeanDefinitionMap = new ConcurrentHashMap<>(256);

    /**
     * 定义相应的地址的映射，/dict/addDict 对应相应的类中的相应的方法
     */
    private final Map<String, NettyMethodDefinition> nettyMethodDefinitionMap = new ConcurrentHashMap<>(256);

    /**
     * 定义当前已经初始化的bean的名称
     */
    private final Set<String> nettyBeanDefinitionSet = new LinkedHashSet<>();

    /**
     * 定义当前已经初始化的接口的名称
     */
    private final Set<String> nettyMethodDefinitionSet = new LinkedHashSet<>();

    /**
     * 定义当前已经初始化的类的属性使用类的全称加上类的属性名称
     */
    private final Set<String> nettyFieldDefinitionSet = new LinkedHashSet<>();

    /**
     * 功能描述： 注册netty的bean的类的扫描
     *
     * @param c           需要进行处理的类的对象
     * @param environment 系统配置的环境对象
     */
    protected void registerNettyBeanDefinition(Class c, Environment environment) {

    }

    /**
     * 功能描述： 根据bean的名称来获取bean信息
     *
     * @param name bean的全称
     * @return NettyBeanDefinition
     */
    public NettyBeanDefinition getNettyBeanDefinition(String name) {
        return nettyBeanDefinitionMap.get(name);
    }

    /**
     * 功能描述： 根据uri来获取响应的method
     *
     * @param uri 响应地址
     * @return NettyMethodDefinition
     */
    public NettyMethodDefinition getNettyMethodDefinition(String uri) {
        return nettyMethodDefinitionMap.get(uri);
    }

    /**
     * 功能描述：将class的注册到系统中
     *
     * @param c           类的路径
     * @param environment 环境对象
     */
    public void registerBean(Class c, Environment environment) {
        if (!beanIsInit(c.getName())) {
            registerNettyBeanDefinition(c, environment);
        }
    }

    /**
     * @return Map
     */
    public Map<String, NettyBeanDefinition> getNettyBeanDefinitionMap() {
        return nettyBeanDefinitionMap;
    }

    /**
     * @return Map
     */
    public Map<String, NettyMethodDefinition> getNettyMethodDefinitionMap() {
        return nettyMethodDefinitionMap;
    }

    /**
     * 功能描述：注册对象的属性信息
     *
     * @param c                   class对象
     * @param o                   实例化的对象
     * @param nettyBeanDefinition netty的bean的信息
     * @param environment         环境对象
     * @throws IllegalAccessException 安全权限异常
     */
    protected void registerNettyFieldDefinition(Class c, Object o, NettyBeanDefinition nettyBeanDefinition, Environment environment) throws IllegalAccessException {
        Field[] sf = c.getDeclaredFields();
        Map<String, NettyFieldDefinition> fieldMap = new HashMap<>(sf.length);
        String name;
        for (Field f : sf) {
            name = f.getName();
            if (fieldIsInit(name)) {
                continue;
            }
            nettyFieldDefinitionSet.add(c.getName() + "." + name);
            NettyFieldDefinition nettyFieldDefinition = new NettyFieldDefinition();
            nettyFieldDefinition.setFieldAnnotation(f.getAnnotations());
            nettyFieldDefinition.setFieldName(name);
            nettyFieldDefinition.setFieldType(f.getType());
            nettyFieldDefinition.setF(f);
            fieldMap.put(f.getName(), nettyFieldDefinition);
            Annotation a = f.getAnnotation(Autowired.class);
            if (a == null) {
                Annotation val = f.getAnnotation(Value.class);
                if (val == null) {
                    continue;
                }
                // 注入@value注解的属性
                doInjectValue(f, o, val, environment);
                continue;
            }
            Object fieldObject = NettySocketServer.getBean(name);
            if (fieldObject != null) {
                f.setAccessible(true);
                f.set(o, fieldObject);
            }
        }
        nettyBeanDefinition.setFieldMap(fieldMap);
    }

    /**
     * 功能描述： 实现六大类型和基本类型的注入
     *
     * @param f           属性对象
     * @param o           类对象
     * @param val         需要注入的值
     * @param environment 全局环境配置
     * @throws IllegalAccessException 错误异常
     */
    protected void doInjectValue(Field f, Object o, Annotation val, Environment environment) throws IllegalAccessException {
        Value v = (Value) val;
        String envVal = environment.getProperty(v.value().replace("${", "").replace("}", ""));
        if (envVal != null && !"".equals(envVal)) {
            f.setAccessible(true);
            switch (ClassType.getByClassName(f.getType().getName())) {
                case StringType:
                    f.set(o, envVal);
                    break;
                case LongType:
                    f.set(o, Long.parseLong(envVal));
                    break;
                case longType:
                    f.set(o, Long.parseLong(envVal));
                    break;
                case BooleanType:
                    if ("true".equals(envVal)) {
                        f.set(o, true);
                    } else {
                        f.set(o, false);
                    }
                    break;
                case booleanType:
                    if ("true".equals(envVal)) {
                        f.set(o, true);
                    } else {
                        f.set(o, false);
                    }
                    break;
                case FloatType:
                    f.set(o, Float.parseFloat(envVal));
                    break;
                case floatType:
                    f.set(o, Float.parseFloat(envVal));
                    break;
                case DoubleType:
                    f.set(o, Double.parseDouble(envVal));
                    break;
                case doubleType:
                    f.set(o, Double.parseDouble(envVal));
                    break;
                case IntegerType:
                    f.set(o, Integer.parseInt(envVal));
                    break;
                case intType:
                    f.set(o, Integer.parseInt(envVal));
                    break;
                case MapType:
                    f.set(o, JsonUtils.jsonToMap(envVal.replaceAll("\"", "").replaceAll("'", "\"")));
                    break;
                case ListType:
                    f.set(o, Arrays.stream(envVal.split(",")).collect(Collectors.toList()));
                    break;
            }
        }
    }


    /**
     * 功能描述： 新增
     *
     * @param BeanFullName        bean的全名
     * @param nettyBeanDefinition bean的实体对象
     */
    public void nettyBeanDefinitionMapPut(String BeanFullName, NettyBeanDefinition nettyBeanDefinition) {
        nettyBeanDefinitionMap.put(BeanFullName, nettyBeanDefinition);
    }

    /**
     * 功能描述： 注册方法到nettyMethodDefinitionMap集合中
     *
     * @param methodFullName        类的全称加上方法的名称
     * @param nettyMethodDefinition 方法对象
     */
    public void nettyMethodDefinitionMapPut(String methodFullName, NettyMethodDefinition nettyMethodDefinition) {
        nettyMethodDefinitionMap.put(methodFullName, nettyMethodDefinition);
    }

    /**
     * 功能描述：新增类的名称
     *
     * @param beanFullName bean的名称
     */
    public void nettyBeanDefinitionSetAdd(String beanFullName) {
        nettyBeanDefinitionSet.add(beanFullName);
    }

    /**
     * 功能描述： 新增方法的名称
     *
     * @param methodFullName 类的全称加上类的方法名称
     */
    public void nettyMethodDefinitionSetAdd(String methodFullName) {
        nettyMethodDefinitionSet.add(methodFullName);
    }

    /**
     * 功能描述： 新增属性
     *
     * @param beanFieldFullName 类的全称加上类的属性名称
     */
    public void nettyFieldDefinitionSetAdd(String beanFieldFullName) {
        nettyFieldDefinitionSet.add(beanFieldFullName);
    }


    /**
     * @param beanName        bean的名称
     * @param singletonObject bean对象
     */
    @Override
    public void registerSingleton(String beanName, Object singletonObject) {

    }

    /**
     * @param beanName bean的名称
     * @return 返回相应的对象
     */
    @Override
    public Object getSingleton(String beanName) {
        return null;
    }

    /**
     * @return 返回bean的数组的集合
     */
    @Override
    public String[] getSingletonNames() {
        return new String[0];
    }

    /**
     * @return 返回总的实例化的bean的数量
     */
    @Override
    public int getSingletonCount() {
        return 0;
    }

    /**
     * 功能描述： 判断当前的属性是否已经初始化过了
     *
     * @param fieldName 属性名称
     * @return true:已经初始化过；false：未初始化
     */
    protected Boolean fieldIsInit(String fieldName) {
        return nettyFieldDefinitionSet.contains(fieldName);
    }

    /**
     * 功能描述： 判断当前的bean是否已经初始化过
     *
     * @param name bean的名称
     * @return true:已经存在；false：不存在
     */
    protected Boolean beanIsInit(String name) {
        return nettyBeanDefinitionSet.contains(name);
    }

    /**
     * 功能描述： 判断当前响应的方法是否已经存在
     *
     * @param url 响应的url
     * @return true:已经存在；false：不存在
     */
    protected Boolean methodIsInit(String url) {
        return nettyMethodDefinitionSet.contains(url);
    }

}
