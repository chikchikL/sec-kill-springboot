package com.example.seckill.access;

import com.example.seckill.domain.MiaoshaUser;

/**
 * 将拦截到的user对象保存到ThreadLocal，因为他是线程安全的
 */
public class UserContext {

    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<>();


    public static void setUser(MiaoshaUser user){

        userHolder.set(user);

    }

    public static MiaoshaUser getUser(){
        return userHolder.get();
    }
}
