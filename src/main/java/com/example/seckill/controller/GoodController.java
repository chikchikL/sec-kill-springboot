package com.example.seckill.controller;

import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.domain.User;
import com.example.seckill.service.MiaoshaUserService;
import com.example.seckill.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/goods")
public class GoodController {


    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_list")
    public String toGoodList(Model model,MiaoshaUser user){
        model.addAttribute("user",user);
        return "goods_list";
    }

//    @RequestMapping("/to_detail")
//    public String toGoodDetail(HttpServletResponse response,Model model,
//                             @CookieValue(value = MiaoshaUserService.COOKI_NAME_TOKEN,required = false) String cookieToken,
//                             @RequestParam(value = MiaoshaUserService.COOKI_NAME_TOKEN,required = false) String paramToken){
//
//
//        return "goods_list";
//    }
}
