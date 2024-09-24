package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Encounter
 * @date 2024/09/24 17:34<br/>
 */
@Mapper
public interface OrderMapper
    {
        /**
         * 插入订单数据
         *
         * @param orders 订单
         */
        void insert(Orders orders);
    }
