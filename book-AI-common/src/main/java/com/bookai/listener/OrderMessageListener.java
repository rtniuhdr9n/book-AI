package com.bookai.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class OrderMessageListener {

    @RabbitListener(queues = "order.create")
    public void handleOrderCreate(Map<String, Object> message) {
        log.info("处理订单创建消息: {}", message);
        
        try {
            Long orderId = ((Number) message.get("orderId")).longValue();
            Long userId = ((Number) message.get("userId")).longValue();
            String orderNo = (String) message.get("orderNo");
            
            // 异步业务逻辑：
            // 1. 发送订单创建通知（短信/邮件）
            sendNotification(userId, orderNo, "订单创建成功");
            
            // 2. 更新用户购买统计
            updateUserPurchaseStats(userId);
            
            // 3. 更新商品销售热度统计
            updateBookSalesStats(orderId);
            
            log.info("订单创建消息处理完成: orderId={}", orderId);
        } catch (Exception e) {
            log.error("处理订单创建消息失败: {}", message, e);
        }
    }

    @RabbitListener(queues = "order.cancel")
    public void handleOrderCancel(Map<String, Object> message) {
        log.info("处理订单取消消息: {}", message);
        
        try {
            Long orderId = ((Number) message.get("orderId")).longValue();
            Long userId = ((Number) message.get("userId")).longValue();
            String orderNo = (String) message.get("orderNo");
            
            // 异步业务逻辑：
            // 1. 发送订单取消通知
            sendNotification(userId, orderNo, "订单已取消");
            
            // 2. 恢复库存已在主流程处理，这里可以做额外的统计更新
            updateCancelledOrderStats(orderId);
            
            log.info("订单取消消息处理完成: orderId={}", orderId);
        } catch (Exception e) {
            log.error("处理订单取消消息失败: {}", message, e);
        }
    }

    @RabbitListener(queues = "sms.send")
    public void handleSmsSend(Map<String, Object> message) {
        log.info("发送短信: {}", message);
        
        try {
            String phone = (String) message.get("phone");
            String content = (String) message.get("content");
            
            // TODO: 调用第三方短信服务API
            // smsService.send(phone, content);
            
            log.info("短信发送成功: phone={}", phone);
        } catch (Exception e) {
            log.error("短信发送失败: {}", message, e);
        }
    }

    @RabbitListener(queues = "email.send")
    public void handleEmailSend(Map<String, Object> message) {
        log.info("发送邮件: {}", message);
        
        try {
            String email = (String) message.get("email");
            String subject = (String) message.get("subject");
            String content = (String) message.get("content");
            
            // TODO: 调用邮件服务API
            // emailService.send(email, subject, content);
            
            log.info("邮件发送成功: email={}", email);
        } catch (Exception e) {
            log.error("邮件发送失败: {}", message, e);
        }
    }

    @RabbitListener(queues = "stat.sync")
    public void handleStatSync(Map<String, Object> message) {
        log.info("同步统计数据: {}", message);
        
        try {
            String statType = (String) message.get("statType");
            
            // TODO: 根据统计类型更新相应的统计表
            // if ("daily_sales".equals(statType)) {
            //     statService.updateDailySales();
            // }
            
            log.info("统计数据同步完成: statType={}", statType);
        } catch (Exception e) {
            log.error("统计数据同步失败: {}", message, e);
        }
    }
    
    /**
     * 发送通知（短信/邮件）
     */
    private void sendNotification(Long userId, String orderNo, String message) {
        log.info("发送通知: userId={}, orderNo={}, message={}", userId, orderNo, message);
        
        // 构建消息并发送到短信/邮件队列
        Map<String, Object> notification = new java.util.HashMap<>();
        notification.put("userId", userId);
        notification.put("orderNo", orderNo);
        notification.put("message", message);
        
        // TODO: 根据用户偏好选择发送方式
        // rabbitTemplate.convertAndSend("sms.send", notification);
    }
    
    /**
     * 更新用户购买统计
     */
    private void updateUserPurchaseStats(Long userId) {
        log.info("更新用户购买统计: userId={}", userId);
        // TODO: 更新用户购买次数、消费金额等统计信息
    }
    
    /**
     * 更新商品销售统计
     */
    private void updateBookSalesStats(Long orderId) {
        log.info("更新商品销售统计: orderId={}", orderId);
        // TODO: 更新商品销量、热度等统计信息
    }
    
    /**
     * 更新取消订单统计
     */
    private void updateCancelledOrderStats(Long orderId) {
        log.info("更新取消订单统计: orderId={}", orderId);
        // TODO: 更新订单取消率等统计信息
    }
}
