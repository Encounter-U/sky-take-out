package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @author Encounter
 * @date 2024/09/25 20:35<br/>
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api("数据统计相关接口")
public class ReportController
    {
        @Autowired
        private ReportService reportService;
        /**
         * 营业额统计
         *
         * @param begin 开始时间
         * @param end   结束时间
         * @return {@link Result }<{@link TurnoverReportVO }>
         */
        @GetMapping("/turnoverStatistics")
        @ApiOperation("营业额统计")
        public Result<TurnoverReportVO> turnoverStatistics(
                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end)
            {
                log.info("营业额数据统计时间 begin:{},end:{}", begin, end);
                return Result.success(reportService.getTurnoverStatistics(begin, end));
            }
    }
