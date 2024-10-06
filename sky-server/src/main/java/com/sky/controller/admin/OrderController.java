package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Encounter
 * @date 2024/09/29 18:08<br/>
 * 订单相关接口
 */
@RestController
@RequestMapping("/admin/order")
@Api("订单相关接口")
@Slf4j
public class OrderController
    {
        @Autowired
        private OrderService orderService;
        
        /**
         * 动态分页查询订单
         *
         * @param ordersPageQueryDTO 订单页面查询 DTO
         * @return {@link Result }<{@link PageResult }>
         */
        @GetMapping("/conditionSearch")
        @ApiOperation("订单分页查询")
        public Result<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO)
            {
                log.info("动态分页查询订单：{}", ordersPageQueryDTO);
                PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
                return Result.success(pageResult);
            }
        
        /**
         * 各个状态的订单数量统计
         *
         * @return {@link Result }<{@link OrderStatisticsVO }>
         */
        @GetMapping("/statistics")
        @ApiOperation("各个状态的订单数量统计")
        public Result<OrderStatisticsVO> statistics()
            {
                OrderStatisticsVO orderStatisticsVO = orderService.statistics();
                return Result.success(orderStatisticsVO);
            }
        
        /**
         * 查询订单
         *
         * @param id id
         * @return {@link Result }<{@link OrderVO }>
         */
        @GetMapping("/details/{id}")
        @ApiOperation("查询订单详情")
        public Result<OrderVO> details(@PathVariable Long id)
            {
                OrderVO orderVO = orderService.details(id);
                return Result.success(orderVO);
            }
        
        /**
         * 确认
         *
         * @param ordersConfirmDTO 订单确认 DTO
         * @return {@link Result }
         */
        @PutMapping("/confirm")
        @ApiOperation("接单")
        public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO)
            {
                orderService.confirm(ordersConfirmDTO);
                return Result.success();
            }
        
        /**
         * 拒单
         *
         * @param ordersRejectionDTO 订单拒绝 DTO
         * @return {@link Result }
         * @throws Exception 例外
         */
        @PutMapping("/rejection")
        @ApiOperation("拒单")
        public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception
            {
                orderService.rejection(ordersRejectionDTO);
                return Result.success();
            }
        
        /**
         * 订单取消
         *
         * @param ordersCancelDTO 订单取消 DTO
         * @return {@link Result }
         * @throws Exception 例外
         */
        @PutMapping("/cancel")
        @ApiOperation("取消订单")
        public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception
            {
                orderService.cancel(ordersCancelDTO);
                return Result.success();
            }
        
        /**
         * 订单派送
         *
         * @param id id
         * @return {@link Result }
         */
        @PutMapping("/delivery/{id}")
        @ApiOperation("派送订单")
        public Result delivery(@PathVariable Long id)
            {
                orderService.delivery(id);
                return Result.success();
            }
        
        /**
         * 订单完成
         *
         * @param id id
         * @return {@link Result }
         */
        @PutMapping("/complete/{id}")
        @ApiOperation("完成订单")
        public Result complete(@PathVariable Long id)
            {
                orderService.complete(id);
                return Result.success();
            }
    }
