package com.example.seckill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";
    public static final String MIAOSHA_QUEUE = "queue.miaosha";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";
    public static final String ROUTING_KEY1 = "topic.key1";
    public static final String ROUTING_KEY2 = "topic.#";
    public static final String HEADERS_EXCHANGE = "headers.exchange";
    public static final String HEADERS_QUEUE = "headers.queue";


    /**
     * Direct模式，交换机Exchange
     */
    @Bean
    public Queue queue(){
        return new Queue(MIAOSHA_QUEUE,true);
    }

//    /**
//     * Topic模式
//     * @return
//     */
//    @Bean
//    public Queue topicQueue1(){
//        return new Queue(TOPIC_QUEUE1,true);
//    }
//
//    @Bean
//    public Queue topicQueue2(){
//        return new Queue(TOPIC_QUEUE2,true);
//    }
//
//    @Bean
//    public TopicExchange topicExchange(){
//        return new TopicExchange(TOPIC_EXCHANGE);
//    }
//
//    @Bean
//    public Binding topicBinding1(){
//        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
//    }
//
//    @Bean
//    public Binding topicBinding2(){
//        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
//    }
//
//    /**
//     * 广播模式
//     * @return
//     */
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange(FANOUT_EXCHANGE);
//    }
//
//    @Bean
//    public Binding fanoutBinding1(){
//        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
//
//    }
//
//    @Bean
//    public Binding fanoutBinding2(){
//        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
//
//    }
//
//
//    /**
//     * Header模式 交换机Exchange
//     */
//    @Bean
//    public HeadersExchange headersExchange(){
//        return new HeadersExchange(HEADERS_EXCHANGE);
//
//    }
//
//    @Bean
//    public Queue headersQueue(){
//        return  new Queue(HEADERS_QUEUE,true);
//    }
//
//    @Bean
//    public Binding headersBinding(){
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("header1","val1");
//        map.put("header2","val2");
//
//        //只有满足whereALL指定的map中的全部header才会将消息放入队列
//        return BindingBuilder.bind(headersQueue())
//                .to(headersExchange())
//                .whereAll(map)
//                .match();
//    }
}
