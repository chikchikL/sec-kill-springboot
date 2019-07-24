package com.example.seckill.service;


import com.example.seckill.Vo.LoginVo;
import com.example.seckill.dao.MiaoshaUserDao;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.result.CodeMsg;
import com.example.seckill.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoshaUserService {

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    public MiaoshaUser getById(long id){
        return miaoshaUserDao.getById(id);
    }

    public boolean login(LoginVo loginVo) {
        if(loginVo == null)
            throw new GlobalException(CodeMsg.SERVER_ERROR);

        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

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


        return true;

    }
}
