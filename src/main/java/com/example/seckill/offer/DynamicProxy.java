package com.example.seckill.offer;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy {


    interface Subject{
        void rent();
        void hello(String name);
    }

    interface Subject1{
        void rent1();
    }


    static class RealSubject implements Subject,Subject1{

        @Override
        public void rent() {
            System.out.println("real object renting");
        }

        @Override
        public void hello(String name) {
            System.out.println("hello "+name);
        }

        @Override
        public void rent1() {
            System.out.println("real object renting 1111111");
        }
    }

    static class MyInvocationHandler implements InvocationHandler {


        private final Subject sub;

        public MyInvocationHandler(Subject sub) {

            this.sub = sub;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

            //这里的Object是Proxy.newInstance动态生成的对象
            if(o instanceof Subject)
                System.out.println("Proxy.newInstance动态生成的对象");
            System.out.println(o.getClass().getName() + "invoke!!!");
            System.out.println("handle pre");
            method.invoke(sub,objects);
            System.out.println("handle end");
            return null;
        }
        
    }
    
    
    public static void main(String[] args){
        RealSubject realSubject = new RealSubject();
        MyInvocationHandler handler = new MyInvocationHandler(realSubject);

        Subject1 subject = (Subject1) Proxy.newProxyInstance(handler.getClass().getClassLoader(),
                realSubject.getClass().getInterfaces(),
                handler);
//        subject.hello("尼玛");
        subject.rent1();

    }



}
