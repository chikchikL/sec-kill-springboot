package com.example.seckill.service;


import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.dao.GoodsDao;
import com.example.seckill.domain.Goods;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.domain.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    /**
     * 秒杀事务 减库存 下订单 写入秒杀订单（事务）
     */
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {

        //减少库存
        goodsService.reduceStock(goods);

        //下订单 写入秒杀订单
        OrderInfo orderInfo = orderService.createOrder(user,goods);

        return orderInfo;
    }
}
