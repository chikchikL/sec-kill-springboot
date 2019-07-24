package com.example.seckill.controller;


import com.example.seckill.Vo.LoginVo;
import com.example.seckill.result.CodeMsg;
import com.example.seckill.result.Result;
import com.example.seckill.service.MiaoshaUserService;
import com.example.seckill.service.RedisService;
import com.example.seckill.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;


    @RequestMapping("/to_login")
    public String toLogin(Model model) {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response,@Valid LoginVo loginVo) {
        log.info(loginVo.toString());

        //登录
        miaoshaUserService.login(response,loginVo);
        return Result.success(true);

    }

}
