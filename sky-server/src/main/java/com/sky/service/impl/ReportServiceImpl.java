package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                        LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
                        LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
                        
                        Map<Object, Object> map = new HashMap<>();
                        map.put("beginTime", beginTime);
                        map.put("endTime", endTime);
                        map.put("status", Orders.COMPLETED);
                        Double turnover = orderMapper.sumByMap(map);//若没有营业额返回为null
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
                        .dateList(StringUtils.join(dateList,","))
                        .totalUserList(StringUtils.join(totalUserList,","))
                        .newUserList(StringUtils.join(newUserList,","))
                        .build();
            }
    }
