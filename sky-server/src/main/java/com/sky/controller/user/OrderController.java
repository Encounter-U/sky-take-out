package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Encounter
 * @date 2024/09/24 17:26<br/>
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Api("用户端订单相关接口")
@Slf4j
public class OrderController
    {
        @Autowired
        private OrderService orderService;
        
        /**
         * 用户下单
         *
         * @param ordersSubmitDTO 订单提交 DTO
         * @return {@link Result }<{@link OrderSubmitVO }>
         */
        @PostMapping("/submit")
        @ApiOperation("用户下单")
        public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO)
            {
                log.info("用户下单提交数据：{}", ordersSubmitDTO);
                OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
                return Result.success(orderSubmitVO);
            }
        
        /**
         * 订单支付
         *
         * @param ordersPaymentDTO 订单支付 DTO
         * @return {@link Result }<{@link OrderPaymentVO }>
         * @throws Exception 例外
         */
        @PutMapping("/payment")
        @ApiOperation("订单支付")
        public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception
            {
                log.info("订单支付：{}", ordersPaymentDTO);
                OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
                log.info("生成预支付交易单：{}", orderPaymentVO);
                return Result.success(orderPaymentVO);
            }
        
        /**
         * 来电提醒
         *
         * @param id id
         * @return {@link Result }
         */
        @GetMapping("/reminder/{id}")
        @ApiOperation("客户催单")
        public Result reminder(@PathVariable Long id)
            {
                log.info("客户催单的订单号：{}", id);
                orderService.reminder(id);
                return Result.success();
            }
        
        /**
         * 历史订单查询
         *
         * @param page     页码
         * @param pageSize 每页展示数量
         * @param status   订单状态
         * @return {@link Result }<{@link PageResult }>
         */
        @GetMapping("/historyOrders")
        @ApiOperation("历史订单查询")
        public Result<PageResult> page(int page, int pageSize, Integer status)
            {
                log.info("查询数据：页码：{}，每页展示：{}条，订单状态：{}", page, pageSize, status);
                PageResult pageResult = orderService.pageQueryUser(page, pageSize, status);
                return Result.success(pageResult);
            }
        
        /**
         * 查询订单详情
         *
         * @param id id
         * @return {@link Result }<{@link OrderVO }>
         */
        @GetMapping("/orderDetail/{id}")
        @ApiOperation("查询订单详情")
        public Result<OrderVO> details(@PathVariable Long id)
            {
                log.info("要查询的订单id：{}", id);
                OrderVO orderVO = orderService.details(id);
                return Result.success(orderVO);
            }
        
        /**
         * 取消订单
         *
         * @param id id
         * @return {@link Result }
         * @throws Exception 异常
         */
        @PutMapping("/cancel/{id}")
        @ApiOperation("取消订单")
        public Result cancel(@PathVariable Long id) throws Exception
            {
                log.info("要取消的订单id：{}", id);
                orderService.userCancelById(id);
                return Result.success();
            }
        
        /**
         * 再来一单
         *
         * @param id 订单id
         * @return {@link Result }
         */
        @PostMapping("/repetition/{id}")
        @ApiOperation("再来一单")
        public Result repetition(@PathVariable Long id)
            {
                log.info("再来一单的订单id：{}", id);
                orderService.repetition(id);
                return Result.success();
            }
    }
