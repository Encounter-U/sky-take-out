package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * @author Encounter
 * @date 2024/09/24 17:30<br/>
 */
public interface OrderService
    {
        /**
         * 提交订单
         *
         * @param ordersSubmitDTO 订单提交 DTO
         * @return {@link OrderSubmitVO }
         */
        OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
        
        /**
         * 订单支付
         *
         * @param ordersPaymentDTO 订单支付 DTO
         * @return {@link OrderPaymentVO }
         * @throws Exception 例外
         */
        OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;
        
        /**
         * 支付成功，修改订单状态
         *
         * @param outTradeNo 交易编号
         */
        void paySuccess(String outTradeNo);
        
        /**
         * 客户催单
         *
         * @param id 催单的订单号
         */
        void reminder(Long id);
        
        /**
         * 动态订单页面查询
         *
         * @param ordersPageQueryDTO 订单页面查询 DTO
         * @return {@link PageResult }
         */
        PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
        
        /**
         * 各个状态的订单数量统计
         *
         * @return {@link OrderStatisticsVO }
         */
        OrderStatisticsVO statistics();
        
        /**
         * 查询订单
         *
         * @param id 订单id
         * @return {@link OrderVO }
         */
        OrderVO details(Long id);
        
        /**
         * 订单确认
         *
         * @param ordersConfirmDTO 订单确认 DTO
         */
        void confirm(OrdersConfirmDTO ordersConfirmDTO);
        
        /**
         * 拒单
         *
         * @param ordersRejectionDTO 订单拒绝 DTO
         */
        void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;
        
        /**
         * 取消订单
         *
         * @param ordersCancelDTO 订单取消 DTO
         */
        void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;
        
        /**
         * 订单派送
         *
         * @param id 订单id
         */
        void delivery(Long id);
        
        /**
         * 订单完成
         *
         * @param id 订单id
         */
        void complete(Long id);
    }
