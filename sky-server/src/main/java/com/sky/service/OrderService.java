package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;

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
    }
