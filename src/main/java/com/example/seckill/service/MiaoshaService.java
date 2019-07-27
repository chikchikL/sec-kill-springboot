package com.example.seckill.service;


import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.domain.MiaoshaOrder;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.domain.OrderInfo;
import com.example.seckill.redis.MiaoshaKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
