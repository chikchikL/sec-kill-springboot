package com.example.seckill.config;

import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.redis.MiaoshaUserKey;
import com.example.seckill.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 这个解析器本身就是在web请求回调中对请求的参数比如Model，request，response等进行赋值
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoshaUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> type = methodParameter.getParameterType();
        //只有true才处理，即这里实现的是当请求的入参为MiaoshaUser时，才进入resolveArgument回调中
        return type == MiaoshaUser.class;
    }

    /**
     * 这个方法最后返回的参数会赋值到那个以MiaoshaUser为入参的函数中
        *
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

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
        for (Cookie cookie :cookies) {
            if(cookie.getName().equals(cookiNameToken)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
