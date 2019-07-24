package com.example.seckill.controller;


import com.example.seckill.domain.User;
import com.example.seckill.redis.UserKey;
import com.example.seckill.result.Result;
import com.example.seckill.service.RedisService;
import com.example.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","Joshua");
        return "hello";
    }


    @RequestMapping("/user")
    @ResponseBody
    public Result<User> getUser(){
        return Result.success(userService.getById(1));
    }

    @RequestMapping("/tx")
    @ResponseBody
    public Result<Boolean> tx(){
        return Result.success(userService.tx());
    }


    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user = redisService.get(UserKey.getById,"1", User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(1);
        user.setName("1号用户");
        boolean key3 = redisService.set(UserKey.getById,"1", user);
        return Result.success(true);
    }
}
