package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
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
                //当前集合用于存放从begin到end范围内的每天的日期
                List<LocalDate> dateList = new ArrayList<>();
                dateList.add(begin);
                while (!begin.equals(end))
                    {
                        //计算指定日期的后一天
                        begin = begin.plusDays(1);
                        dateList.add(begin);
                    }
                
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
    }
