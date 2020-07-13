package com.github.lazyboyl.websocket.factory;

/**
 * @author linzf
 * @since 2020/6/29
 * 类描述：
 */
public interface NettySingletonBeanRegistry {

    /**
     * 功能描述： 注册netty的bean
     *
     * @param beanName        bean的名称
     * @param singletonObject bean对象
     */
    void registerSingleton(String beanName, Object singletonObject);

    /**
     * 功能描述： 根据名称来获取bean
     *
     * @param beanName bean的名称
     * @return 相应的bean
     */
    Object getSingleton(String beanName);

    /**
     * 功能描述： 返回所有的单例的bean的信息
     * @return
     */
    String[] getSingletonNames();

    /**
     * 功能描述： 统计所有单例的bean的数量
     * @return
     */
    int getSingletonCount();

}
