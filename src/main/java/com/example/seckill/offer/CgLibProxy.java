package com.example.seckill.offer;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CgLibProxy {
    static class MyHandler implements MethodInterceptor {
        CglibTestSon realObj;
        public MyHandler(CglibTestSon realObj) {
            this.realObj = realObj;
        }
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println("---------before---------");
            Object invoke = method.invoke(realObj, objects);
            return invoke;
        }
    }


    static class CglibTestSon {
        public CglibTestSon() {
        }
        public void gotoHome() {
            System.out.println("============gotoHome============");
        }
        public void gotoSchool() {
            System.out.println("===========gotoSchool============");
        }
        public void oneday() {
            gotoHome();
            gotoSchool();
        }
        public final void onedayFinal() {
            gotoHome();
            gotoSchool();
        }
    }

    public static void main(String[] args){

        CglibTestSon realobj = new CglibTestSon();
        MyHandler myHandler = new MyHandler(realobj);
        //由于不需要实现顶层接口
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CglibTestSon.class);
        enhancer.setCallback(myHandler);

        CglibTestSon cglibTestSonProxy = (CglibTestSon) enhancer.create();
        cglibTestSonProxy.gotoHome();
        cglibTestSonProxy.gotoSchool();

        cglibTestSonProxy.oneday();
        cglibTestSonProxy.onedayFinal();
    }

}
