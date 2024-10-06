package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/24 17:35<br/>
 */
@Mapper
public interface OrderDetailMapper
    {
        /**
         * 批量插入订单明细
         *
         * @param orderDetailList 订单详情列表
         */
        void insertBatch(List<OrderDetail> orderDetailList);
        
        /**
         * 获取菜品由次序id
         *
         * @param orderId 订单id
         * @return {@link Dish }
         */
        @Select("select id, name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount " +
                "from order_detail where order_id = #{orderId}")
        List<OrderDetail> getByOrderId(Long orderId);
    }
