package com.example.seckill.service;


import com.example.seckill.Vo.LoginVo;
import com.example.seckill.dao.MiaoshaUserDao;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.redis.KeyPrefix;
import com.example.seckill.redis.MiaoshaUserKey;
import com.example.seckill.redis.UserKey;
import com.example.seckill.result.CodeMsg;
import com.example.seckill.util.MD5Util;
import com.example.seckill.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {
    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id){

        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.id, String.valueOf(id), MiaoshaUser.class);
        if(miaoshaUser != null)
            return miaoshaUser;
        miaoshaUser = miaoshaUserDao.getById(id);
        if(miaoshaUser !=null){
            //添加缓存
            redisService.set(MiaoshaUserKey.id,String.valueOf(id),miaoshaUser);
        }
        return miaoshaUser;
    }



    public  boolean updatePassword(String token,long id,String password){
        //取user对象
        MiaoshaUser user = getById(id);
        if(user == null)
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);

        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(password,user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);

        /**
         * 当数据库中user更新，之前token和id保存的缓存中都有user信息，需要更新
         *
         */
        //更新否则无法登陆
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,user);
        //删除
        redisService.delete(MiaoshaUserKey.id,String.valueOf(id));

        return true;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null)
            throw new GlobalException(CodeMsg.SERVER_ERROR);

        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

        //唯一一次请求数据库
        MiaoshaUser user = getById(Long.parseLong(mobile));

        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDb = user.getSalt();

        String calcu = MD5Util.formPassToDBPass(formPass, saltDb);
        if(!calcu.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //登录成功生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response,token, user);
        return token;

    }

    private void addCookie(HttpServletResponse response,String token, MiaoshaUser user) {

        //将用户信息缓存到redis
        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //通过cookieToken获取用户
    public MiaoshaUser getByToken(HttpServletResponse response,String token) {

        if(StringUtils.isEmpty(token)){
            return null;
        }

        MiaoshaUser miaoshaUser =
                redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);

        if(miaoshaUser != null){
            //有token只延长有效期，不新生成token
            addCookie(response,token,miaoshaUser);
        }

        return miaoshaUser;
    }
}
