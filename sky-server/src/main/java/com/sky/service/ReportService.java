package com.sky.service;

import com.sky.vo.TurnoverReportVO;

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
    }
