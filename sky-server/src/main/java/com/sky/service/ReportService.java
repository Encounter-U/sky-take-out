package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

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
    }
