package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/25 14:23<br/>
 * 定时任务类，定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask
    {
        @Autowired
        private OrderMapper orderMapper;
        
        /**
         * 处理超时订单  每分钟触发一次
         */
        @Scheduled(cron = "0 * * * * ?")
        public void processTimeOutOrder()
            {
                log.info("定时处理超时订单：{}", LocalDateTime.now());
                
                List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));
                
                if (ordersList != null && !ordersList.isEmpty())
                    {
                        for (Orders orders : ordersList)
                            {
                                orders.setStatus(Orders.CANCELLED);//订单取消
                                orders.setCancelReason("订单超时，自动取消");//取消原因
                                orders.setCancelTime(LocalDateTime.now());//取消时间
                                orderMapper.update(orders);
                            }
                    }
            }
        
        /**
         * 处理一直处于派送中的订单 每天凌晨1点触发一次
         */
        @Scheduled(cron = "0 0 1 * * ?")
        //@Scheduled(cron = "0-3 * * * * ?")//测试方法是否执行
        public void processDeliveryOrder()
            {
                log.info("定时处理处于派送中的订单：{}", LocalDateTime.now());
                
                List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));
                if (ordersList != null && !ordersList.isEmpty())
                    {
                        for (Orders orders : ordersList)
                            {
                                orders.setStatus(Orders.COMPLETED);//订单完成
                                orderMapper.update(orders);
                            }
                    }
            }
    }
