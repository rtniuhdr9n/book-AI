package com.bookai.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // 交换机
    public static final String EXCHANGE_NAME = "bookstore.topic";
    
    // 队列
    public static final String QUEUE_ORDER_CREATE = "order.create";
    public static final String QUEUE_ORDER_CANCEL = "order.cancel";
    public static final String QUEUE_SMS_SEND = "sms.send";
    public static final String QUEUE_EMAIL_SEND = "email.send";
    public static final String QUEUE_STAT_SYNC = "stat.sync";
    public static final String QUEUE_SEARCH_SYNC = "search.sync";
    
    // Routing Key
    public static final String ROUTING_ORDER_CREATE = "order.create.*";
    public static final String ROUTING_ORDER_CANCEL = "order.cancel.*";
    public static final String ROUTING_SMS_SEND = "notify.sms.*";
    public static final String ROUTING_EMAIL_SEND = "notify.email.*";
    public static final String ROUTING_STAT_SYNC = "stat.*";
    public static final String ROUTING_SEARCH_SYNC = "search.*";

    @Bean
    public TopicExchange bookstoreExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue orderCreateQueue() {
        return new Queue(QUEUE_ORDER_CREATE, true);
    }

    @Bean
    public Queue orderCancelQueue() {
        return new Queue(QUEUE_ORDER_CANCEL, true);
    }

    @Bean
    public Queue smsSendQueue() {
        return new Queue(QUEUE_SMS_SEND, true);
    }

    @Bean
    public Queue emailSendQueue() {
        return new Queue(QUEUE_EMAIL_SEND, true);
    }

    @Bean
    public Queue statSyncQueue() {
        return new Queue(QUEUE_STAT_SYNC, true);
    }

    @Bean
    public Queue searchSyncQueue() {
        return new Queue(QUEUE_SEARCH_SYNC, true);
    }

    @Bean
    public Binding orderCreateBinding() {
        return BindingBuilder.bind(orderCreateQueue())
                .to(bookstoreExchange())
                .with(ROUTING_ORDER_CREATE);
    }

    @Bean
    public Binding orderCancelBinding() {
        return BindingBuilder.bind(orderCancelQueue())
                .to(bookstoreExchange())
                .with(ROUTING_ORDER_CANCEL);
    }

    @Bean
    public Binding smsSendBinding() {
        return BindingBuilder.bind(smsSendQueue())
                .to(bookstoreExchange())
                .with(ROUTING_SMS_SEND);
    }

    @Bean
    public Binding emailSendBinding() {
        return BindingBuilder.bind(emailSendQueue())
                .to(bookstoreExchange())
                .with(ROUTING_EMAIL_SEND);
    }

    @Bean
    public Binding statSyncBinding() {
        return BindingBuilder.bind(statSyncQueue())
                .to(bookstoreExchange())
                .with(ROUTING_STAT_SYNC);
    }

    @Bean
    public Binding searchSyncBinding() {
        return BindingBuilder.bind(searchSyncQueue())
                .to(bookstoreExchange())
                .with(ROUTING_SEARCH_SYNC);
    }
}
