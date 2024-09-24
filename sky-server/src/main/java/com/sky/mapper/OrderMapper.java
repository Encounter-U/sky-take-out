package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
        
        /**
         * 根据订单号和用户id查询订单
         *
         * @param orderNumber 订单号
         * @param userId      用户 ID
         * @return {@link Orders }
         */
        @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
        Orders getByNumberAndUserId(String orderNumber, Long userId);
        
        /**
         * 修改订单信息
         *
         * @param orders 订单
         */
        void update(Orders orders);
    }
