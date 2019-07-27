package com.example.seckill.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
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

}
