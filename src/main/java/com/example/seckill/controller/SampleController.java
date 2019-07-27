package com.example.seckill.controller;


import com.example.seckill.domain.User;
import com.example.seckill.rabbitmq.MQSender;
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

    @Autowired
    MQSender mqSender;

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

    @RequestMapping("/mq/direct")
    @ResponseBody
    public Result<String> mq(){
        mqSender.send("rmq测试消息");
////        mqSender.sendTopic("topic测试消息");
//
//        mqSender.sendFanout("fanout测试消息");
        return Result.success("direct测试成功");
    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> mqTopic(){
        mqSender.sendTopic("topic测试消息");

        return Result.success("topic测试成功");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> mqFanout(){

        mqSender.sendFanout("fanout测试消息");
        return Result.success("fanout测试成功");
    }


    @RequestMapping("/mq/headers")
    @ResponseBody
    public Result<String> mqHeaders(){

        mqSender.sendHeader("headers测试消息");
        return Result.success("headers测试成功");
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
