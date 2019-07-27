package com.example.seckill.rabbitmq;

import com.example.seckill.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    AmqpTemplate amqpTemplate;


    public void send(Object msg){
        String msgStr = RedisService.beanToStr(msg);
        log.info("send message" + msgStr);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);

    }

}
