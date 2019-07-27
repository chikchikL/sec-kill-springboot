package com.example.seckill.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    //设置监听的queue
    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String msgStr){
        log.info("receive message"+ msgStr);

    }
}
