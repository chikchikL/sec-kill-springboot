package com.example.seckill.controller;

import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.domain.MiaoshaOrder;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.domain.OrderInfo;
import com.example.seckill.result.CodeMsg;
import com.example.seckill.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("do_miaosha")
    public String miaosha(Model model, MiaoshaUser user,
                       @RequestParam("goodsId")Long goodsId){
        model.addAttribute("user",user);

        if(user == null){
            return "login";
        }

        //判断商品库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stock = goods.getStockCount();

        if(stock <= 0){
            model.addAttribute("errMsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }


        //判断是否秒杀成功
        MiaoshaOrder miaoshaOrder =
                orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);

        if(miaoshaOrder != null){
            model.addAttribute("errMsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        //减库存 下订单 写入秒杀订单（事务）
        //秒杀需要的是秒杀用户和商品信息生成订单信息
        //因为秒杀成功后会进入订单详情页面
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);

        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);

        return "order_detail";

    }

}
