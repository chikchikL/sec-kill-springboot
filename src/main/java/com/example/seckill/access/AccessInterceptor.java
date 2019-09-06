package com.example.seckill.access;

import com.alibaba.fastjson.JSON;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.redis.AccessKey;
import com.example.seckill.result.CodeMsg;
import com.example.seckill.result.Result;
import com.example.seckill.service.MiaoshaUserService;
import com.example.seckill.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

/**
 * 实现一个拦截器
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    /**
     * 方法执行前进行拦截
     * @param request
     * @param response
     * @param handler
     * @return false则不进入方法
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){

            MiaoshaUser user = getUser(request, response);
            UserContext.setUser(user);

            HandlerMethod hm = (HandlerMethod) handler;
            /**
             * 获取方法上注解信息
             */
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null){
                return true;
            }

            boolean needLogin = accessLimit.needLogin();
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();

            String key = request.getRequestURI();

            if(needLogin){
                if(user == null){
                    render(response, CodeMsg.SESSION_ERROR);
                    //return false就不进入注解的方法了
                    return false;
                }
                //生成对于访问控制的redis key
                key += "_" + user.getId();
            }else{
                //do nothing
            }

            //确定登录有效，首先判断该用户对该url的限流
            AccessKey accessKey = AccessKey.withExpire(seconds);
            Integer accessCount = redisService.get(accessKey, key, Integer.class);
            if(accessCount == null){
                //首次访问时将访问次数置为1
                redisService.set(accessKey,key,1);
            }else if(accessCount < maxCount){
                redisService.incr(accessKey,key);
            }else{
                render(response,CodeMsg.ACCESS_LIMIT);
                return false;
            }

        }

        return true;
    }

    //发生参数错误时，将错误信息返回给前端
    private void render(HttpServletResponse response, CodeMsg cm) {
        //设置response编码方式
        response.setContentType("application/json;charset=UTF-8");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String str = JSON.toJSONString(Result.error(cm));
            outputStream.write(str.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private MiaoshaUser getUser(HttpServletRequest request,HttpServletResponse response){
        String paramToken = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);
        String cookieToken = getCookieValue(request,MiaoshaUserService.COOKI_NAME_TOKEN);

        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }

        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;

        return userService.getByToken(response,token);

    }


    private String getCookieValue(HttpServletRequest request, String cookiNameToken) {

        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length <=0)
            return null;
        for (Cookie cookie :cookies) {
            if(cookie.getName().equals(cookiNameToken)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
