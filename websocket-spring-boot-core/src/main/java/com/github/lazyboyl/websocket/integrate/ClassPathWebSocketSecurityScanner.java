package com.github.lazyboyl.websocket.integrate;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Set;

/**
 * @author linzf
 * @since 2019/7/22
 * 类描述：实现拦截器的自定义的扫描
 */
public class ClassPathWebSocketSecurityScanner extends ClassPathBeanDefinitionScanner {

    public ClassPathWebSocketSecurityScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public ClassPathWebSocketSecurityScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public ClassPathWebSocketSecurityScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }


    /**
     * Calls the parent search that will search and register all the candidates.
     * Then the registered objects are post processed to set them as
     * MapperFactoryBeans
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            logger.warn("No DgbSecurity Spring Componet was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        }
        return beanDefinitions;
    }

}
