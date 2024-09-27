package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Encounter
 * @date 2024/09/25 20:40<br/>
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService
    {
        @Autowired
        private OrderMapper orderMapper;
        
        @Autowired
        private UserMapper userMapper;
        
        /**
         * 获取日期列表
         *
         * @param begin 开始
         * @param end   结束
         * @return {@link List }<{@link LocalDate }>
         */
        private static List<LocalDate> getDateList(LocalDate begin, LocalDate end)
            {
                //当前集合用于存放从begin到end范围内的每天的日期
                List<LocalDate> dateList = new ArrayList<>();
                dateList.add(begin);
                while (!begin.equals(end))
                    {
                        //计算指定日期的后一天
                        begin = begin.plusDays(1);
                        dateList.add(begin);
                    }
                return dateList;
            }
        
        /**
         * 统计指定时间内的营业额数据
         *
         * @param begin 开始
         * @param end   结束
         * @return {@link TurnoverReportVO }
         */
        @Override
        public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end)
            {
                List<LocalDate> dateList = getDateList(begin, end);
                
                List<Double> turnoverList = new ArrayList<>();
                for (LocalDate date : dateList)
                    {
                        //查询date日期对应的营业额数据，营业额是指状态为“已完成”的订单金额合计
                        Double turnover = orderMapper.sumByMap(getConditionsMap(date, Orders.COMPLETED));//若没有营业额返回为null
                        turnover = turnover == null ? 0 : turnover;//将null重新赋值为0
                        turnoverList.add(turnover);
                    }
                
                //封装返回结果
                return TurnoverReportVO.builder()
                        .dateList(StringUtils.join(dateList, ","))
                        .turnoverList(StringUtils.join(turnoverList, ","))
                        .build();
            }
        
        /**
         * 获取用户统计信息
         *
         * @param begin 开始时间
         * @param end   结束时间
         * @return {@link UserReportVO }
         */
        @Override
        public UserReportVO getUserStatistics(LocalDate begin, LocalDate end)
            {
                //获取dateList
                List<LocalDate> dateList = getDateList(begin, end);
                
                //存放每天新增的用户数量
                List<Integer> newUserList = new ArrayList<>();
                //存放总的用户数量
                List<Integer> totalUserList = new ArrayList<>();
                
                for (LocalDate date : dateList)
                    {
                        LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
                        LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
                        
                        //先只存end查出总用户数
                        Map<Object, Object> map = new HashMap<>();
                        map.put("end", endTime);
                        Integer totalUser = userMapper.countByMap(map);
                        
                        //新增用户数
                        map.put("begin", beginTime);
                        Integer newUser = userMapper.countByMap(map);
                        
                        //将查询到的数据存入集合
                        totalUserList.add(totalUser);
                        newUserList.add(newUser);
                    }
                
                //封装返回数据
                return UserReportVO.builder()
                        .dateList(StringUtils.join(dateList, ","))
                        .totalUserList(StringUtils.join(totalUserList, ","))
                        .newUserList(StringUtils.join(newUserList, ","))
                        .build();
            }
        
        /**
         * 获取订单统计数据
         *
         * @param begin 开始
         * @param end   结束
         * @return {@link OrderReportVO }
         */
        @Override
        public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end)
            {
                //获取dateList
                List<LocalDate> dateList = getDateList(begin, end);
                
                //存放每天的订单总数
                List<Integer> orderCountList = new ArrayList<>();
                //存放每天的有效订单数
                List<Integer> validOrderCountList = new ArrayList<>();
                //总订单数
                Integer totalOrderCount = 0;
                //总有效订单数
                Integer validOrderCount = 0;
                
                //遍历dateList集合，查询每天的有效订单数和订单总数
                for (LocalDate date : dateList)
                    {
                        //查询每天的订单总数，无需status，直接传入null即可
                        Integer orderCount = orderMapper.countByMap(getConditionsMap(date, null));
                        totalOrderCount += orderCount;
                        
                        //查询每天的有效订单数
                        Integer validOrder = orderMapper.countByMap(getConditionsMap(date, Orders.COMPLETED));
                        validOrderCount += validOrder;
                        
                        //存入数据
                        orderCountList.add(orderCount);
                        validOrderCountList.add(validOrder);
                    }
                
                //避免除数出现0，先赋上初始值
                BigDecimal orderCompletionRate = new BigDecimal("0.0");
                BigDecimal totalOrder = new BigDecimal(totalOrderCount);
                BigDecimal validOrder = new BigDecimal(validOrderCount);
                if (totalOrderCount != 0)
                    orderCompletionRate = validOrder.divide(totalOrder, 2, BigDecimal.ROUND_HALF_UP);
                //log.info("订单完成率：{}", orderCompletionRate.doubleValue());
                
                //封装并返回数据
                return OrderReportVO.builder()
                        .dateList(StringUtils.join(dateList, ","))
                        .orderCountList(StringUtils.join(orderCountList, ","))
                        .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                        .totalOrderCount(totalOrderCount)
                        .validOrderCount(validOrderCount)
                        .orderCompletionRate(orderCompletionRate.doubleValue())
                        .build();
            }
        
        /**
         * 获取动态查询订单时的条件的map集合
         *
         * @param date   要查询的日期日期
         * @param status 订单状态
         * @return {@link Map }<{@link Object },{@link Object }>
         */
        private Map<Object, Object> getConditionsMap(LocalDate date, Integer status)
            {
                LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
                LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
                Map<Object, Object> map = new HashMap<>();
                map.put("begin", beginTime);
                map.put("end", endTime);
                map.put("status", status);
                return map;
            }
    }
