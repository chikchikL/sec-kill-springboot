package com.example.seckill.rabbitmq;

import com.example.seckill.domain.MiaoshaMessage;
import com.example.seckill.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    AmqpTemplate amqpTemplate;


//    public void send(Object msg){
//        String msgStr = RedisService.beanToStr(msg);
//        log.info("send message" + msgStr);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
//
//    }
//
//    public void sendTopic(Object msg){
//        String msgStr = RedisService.beanToStr(msg);
//        log.info("send message" + msgStr);
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY1,msgStr+"1");
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY2,msgStr+"2");
//
//
//    }
//
//    public void sendFanout(Object msg){
//        String str = RedisService.beanToStr(msg);
//        log.info("send fanout msg"+ str);
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,null,str);
//    }
//
//    public void sendHeader(Object msg){
//        String msgStr = RedisService.beanToStr(msg);
//        log.info("send header msg"+msgStr);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("header1","val1");
//        properties.setHeader("header2","val2");
//        Message message = new Message(msgStr.getBytes(), properties);
//        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,null,message);
//
//    }

    /**
     * 将秒杀信息发送到消息队列
     * @param miaoshaMessage
     */
    public void sendMiaoshaMessage(MiaoshaMessage miaoshaMessage){
        String msg = RedisService.beanToStr(miaoshaMessage);
        log.info("send miaosha message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,msg);
    }

}
