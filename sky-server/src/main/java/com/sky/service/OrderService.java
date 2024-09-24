package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
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
    }
