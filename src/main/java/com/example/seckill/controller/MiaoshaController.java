package com.example.seckill.controller;

import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.domain.MiaoshaOrder;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.domain.OrderInfo;
import com.example.seckill.result.CodeMsg;
import com.example.seckill.result.Result;
import com.example.seckill.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("miaosha")
public class MiaoshaController {


    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    //因为涉及数据库插入数据，应该用post方法
    //get post区别
    //get 幂等性，无论调用多少次对服务端数据无影响
    //post
    @RequestMapping(value = "do_miaosha",method = RequestMethod.POST)
    @ResponseBody//返回秒杀的订单信息
    public Result<OrderInfo> miaosha(Model model, MiaoshaUser user,
                                     @RequestParam("goodsId")Long goodsId){
        model.addAttribute("user",user);

        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //判断商品库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stock = goods.getStockCount();

        if(stock <= 0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }


        //判断是否秒杀成功
        MiaoshaOrder miaoshaOrder =
                orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);

        if(miaoshaOrder != null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //减库存 下订单 写入秒杀订单（事务）
        //秒杀需要的是秒杀用户和商品信息生成订单信息
        //因为秒杀成功后会进入订单详情页面
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);

        return Result.success(orderInfo);

    }

}
