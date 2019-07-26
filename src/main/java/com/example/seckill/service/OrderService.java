package com.example.seckill.service;

import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.dao.OrderDao;
import com.example.seckill.domain.MiaoshaOrder;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.domain.OrderInfo;
import com.example.seckill.redis.OrderKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;



    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId) {
        //此处改为直接从缓存取
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid,
                userId + "_" + goodsId, MiaoshaOrder.class);
    }


    //秒杀下单
    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insert(orderInfo);

        //创建miaoshaOrder
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);


        //下单完成时将订单信息写入缓存
        redisService.set(OrderKey.getMiaoshaOrderByUidGid,
                user.getId() + "_" + goods.getId(),
                miaoshaOrder);

        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {

        return orderDao.getOrderById(orderId);
    }
}
