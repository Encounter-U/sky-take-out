package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
        
        /**
         * 按状态和订单时间 LT 获取
         *
         * @param status    订单支付状态
         * @param orderTime 订购时间
         * @return {@link List }<{@link Orders }>
         */
        @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
        List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);
        
        /**
         * 按 ID 获取
         *
         * @param id 订单id
         * @return {@link Orders }
         */
        @Select("select * from orders where id = #{id}")
        Orders getById(Long id);
        
        /**
         * 根据动态条件统计营业额数据
         *
         * @param map Map集合封装了开始结束时间及状态
         * @return {@link Object }
         */
        Double sumByMap(Map<Object, Object> map);
        
        /**
         * 动态查询订单数
         *
         * @param map Map集合封装查询条件
         */
        Integer countByMap(Map<Object, Object> map);
        
        /**
         * 获取 Sales Top10
         *
         * @param begin 开始
         * @param end   结束
         * @return {@link List }<{@link GoodsSalesDTO }>
         */
        List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
        
        /**
         * 动态订单页面查询
         *
         * @param ordersPageQueryDTO 订单页面查询 DTO
         * @return {@link List }<{@link Orders }>
         */
        Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
        
        /**
         * 统计各种状态的订单数量
         *
         * @param status 订单状态
         * @return {@link Integer }
         */
        @Select("select count(id) from orders where status = #{status}")
        Integer countStatus(Integer status);
    }
