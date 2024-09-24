package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    }
