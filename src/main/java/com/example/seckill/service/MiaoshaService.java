package com.example.seckill.service;


import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.domain.MiaoshaOrder;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.domain.OrderInfo;
import com.example.seckill.redis.MiaoshaKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    /**
     * 秒杀事务 减库存 下订单 写入秒杀订单（事务）
     */
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {

        //减少库存
        boolean success = goodsService.reduceStock(goods);

        //只有成功减少库存才创建订单
        if(!success){
            setGoodsOver(goods.getId());
            return  null;
        }
        //下订单 写入秒杀订单
        return orderService.createOrder(user,goods);
    }

    //设置秒杀结束缓存标记，用于客户端轮询秒杀结果时，判断是排队还是秒杀失败
    private void setGoodsOver(Long id) {

        redisService.set(MiaoshaKey.isGoodsOver,String.valueOf(id),true);
    }


    private boolean getGoodsOver(Long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver,String.valueOf(goodsId));
    }


    /**
     * 客户端轮询获取秒杀结果的业务逻辑，这里的逻辑在于，最多往消息队列中插入stock个消息请求
     * 所以每个消息被成功消费时，都是可以轮询秒杀请求的结果的，只要没秒杀成功或者秒杀
     * 完代表当前请求还有机会
     * @param id
     * @param goodsId
     * @return
     */
    public long getMiaoshaResult(Long id, Long goodsId) {
        MiaoshaOrder miaoshaorder = orderService.getMiaoshaOrderByUserIdGoodsId(id, goodsId);
        if(miaoshaorder != null)
            return miaoshaorder.getOrderId();
        else{
            //秒杀失败或者排队
             boolean isOver = getGoodsOver(goodsId);
             if(isOver){
                 return -1;
             }else{
                 return 0;
             }
        }
    }


    public void reset(List<GoodsVo> goodsVos) {
        goodsService.resetStock(goodsVos);
        orderService.deleteOrders();
    }
}
