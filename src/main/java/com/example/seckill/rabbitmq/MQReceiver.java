package com.example.seckill.rabbitmq;

import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.domain.MiaoshaMessage;
import com.example.seckill.domain.MiaoshaOrder;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.domain.OrderInfo;
import com.example.seckill.result.CodeMsg;
import com.example.seckill.result.Result;
import com.example.seckill.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MQReceiver {

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

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    //设置监听的queue
//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive1(String msgStr){
//        log.info("receive message"+ msgStr);
//
//    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String msgStr){
        log.info("queue1 receive message"+ msgStr);

    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String msgStr){
        log.info("queue2 receive message"+ msgStr );
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void receiveHeader(byte[] msg){
        log.info("header queue receive" + new String(msg));
    }


    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String msg){
        log.info("receive message:"+msg);
        MiaoshaMessage miaoshaMessage = RedisService.strToBean(msg, MiaoshaMessage.class);

        MiaoshaUser user = miaoshaMessage.getUser();
        Long goodsId = miaoshaMessage.getGoodsId();


        //判断商品库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stock = goods.getStockCount();

        if(stock <= 0){
            return;
        }

        //判断是否秒杀成功
        MiaoshaOrder miaoshaOrder =
                orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);

        if(miaoshaOrder != null){
            return;
        }

        //减库存 下订单 写入秒杀订单（事务）
        //秒杀需要的是秒杀用户和商品信息生成订单信息
        //因为秒杀成功后会进入订单详情页面
        miaoshaService.miaosha(user,goods);

    }
}
