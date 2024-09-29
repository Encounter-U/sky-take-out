package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

/**
 * @author Encounter
 * @date 2024/09/25 20:40<br/>
 */
public interface ReportService
    {
        /**
         * 统计指定时间内的营业额数据
         *
         * @param begin 开始
         * @param end   结束
         * @return {@link TurnoverReportVO }
         */
        TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
        
        /**
         * 获取用户统计信息
         *
         * @param begin 开始时间
         * @param end   结束时间
         * @return {@link UserReportVO }
         */
        UserReportVO getUserStatistics(LocalDate begin, LocalDate end);
        
        /**
         * 获取订单统计数据
         *
         * @param begin 开始
         * @param end   结束
         * @return {@link OrderReportVO }
         */
        OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end);
        
        /**
         * 获取 Sales Top10
         *
         * @param begin 开始
         * @param end   结束
         * @return {@link SalesTop10ReportVO }
         */
        SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
        
        /**
         * 导出业务数据
         *
         * @param response 响应
         */
        void exportBusinessData(HttpServletResponse response);
    }
