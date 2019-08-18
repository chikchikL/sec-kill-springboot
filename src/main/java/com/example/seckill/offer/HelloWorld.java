package com.example.seckill.offer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;


@Component("hello")
public class HelloWorld implements SmartInitializingSingleton, SmartLifecycle, InitializingBean,
        DisposableBean,MyInterface, BeanNameAware, BeanClassLoaderAware,
        BeanFactoryAware, ApplicationContextAware {


    private final Log logger = LogFactory.getLog(getClass());
    private boolean isRunning;



    public HelloWorld() {
        System.out.println("实例化");
    }


    public void sayHello(){
        System.out.println("hello World");
    }

    public void afterSingletonsInstantiated() {
        System.out.println("SmartInitializingSingleton afterSingletonsInstantiated");
    }

    public void start() {
        isRunning = true;
        System.out.println("LifeCycle start");
    }

    public void stop() {
        System.out.println("LifeCycle stop");
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isAutoStartup() {
        return true;
    }

    public void stop(Runnable callback) {
        System.out.println("LifeScycle stop");
        callback.run();
    }

    public int getPhase() {
        return 0;
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("afterproperties set");
    }

    public void destroy() throws Exception {
        System.out.println("destroy");
    }

    public void my(String str) {
        System.out.println(str);
    }

    public void setBeanName(String name) {
        System.out.println("set bean Name aware");
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("set Application Aware");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("set BeanFactory Aware");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("set BeanClassLoader Aware");
    }
}

//MyInterface接口
interface MyInterface {
    void my(String str);
}
