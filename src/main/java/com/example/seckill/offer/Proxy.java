package com.example.seckill.offer;

public class Proxy {


    interface Image {
        void showImage();
    }


    static class RealObject implements Image{
        String imgUrl;
        String name;

        public RealObject(String url) {
            this.imgUrl = url;
            this.name = "张三";
        }

        @Override
        public void showImage() {
            System.out.println("被代理对象");
        }
    }

    static class ProxyObject implements Image{
        RealObject realObject;

        public ProxyObject(RealObject object) {

            this.realObject = object;
        }

        @Override
        public void showImage() {

            //拿到realObject对象进行具体逻辑操作

            realObject.showImage();
        }
    }


    public static void main(String[] args){


        ProxyObject proxyObject = new ProxyObject(new RealObject("www.test.com"));
        proxyObject.showImage();
    }
}
