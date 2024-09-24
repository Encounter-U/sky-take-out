package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

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
    }
