package com.example.seckill.service;

import com.alibaba.fastjson.JSON;
import com.example.seckill.redis.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    public <T> T get(KeyPrefix prefix,String key,Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();

            //生成realkey
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = strToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }

    }


    public <T> boolean set(KeyPrefix prefix
            , String key, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToStr(value);
            if(str == null || str.length() <= 0){
                return false;
            }

            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();

            //判断过期时间是否永久
            if(seconds <=0){
                jedis.set(realKey,str);
            }else{
                jedis.setex(realKey,seconds,str);
            }

            return true;

        }finally {
            returnToPool(jedis);

        }


    }

    public <T> boolean exists(KeyPrefix prefix
            , String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;

            return jedis.exists(realKey);

        }finally {
            returnToPool(jedis);

        }


    }

    /**
     * 删除
     * @param prefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix prefix, String key){

        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;

            Long del = jedis.del(realKey);
            return del>0;

        }finally {
            returnToPool(jedis);

        }
    }


    /**
     * 增加值,计数用的
     * */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     * */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    public static  <T> String beanToStr(T value) {
        if(value == null)
            return null;
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class){
            return ""+value;
        }else if(clazz == String.class){
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class){
            return ""+value;
        }

        return JSON.toJSONString(value);

    }

    //字符串转化为对象
    public static  <T> T strToBean(String str,Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null){
            return null;
        }
        if(clazz == int.class || clazz == Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class){
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class){
            return (T)Long.valueOf(str);
        }




        return JSON.toJavaObject(JSON.parseObject(str),clazz);
    }

    private void returnToPool(Jedis jedis) {

        if(jedis != null)
            jedis.close();
    }


}
