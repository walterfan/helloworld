package com.github.walterfan.hellospring;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class LogBeanPostProcessor implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        log.info("Bean '" + beanName + "' postProcessBeforeInitialization ");
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        log.info("Bean '" + beanName + "' postProcessAfterInitialization : " + bean.toString());
        return bean;
    }
}
