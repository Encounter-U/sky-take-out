package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
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
 * @date 2024/09/23 13:29<br/>
 */
@RestController
@Slf4j
@RequestMapping("/user/shoppingCart")
@Api("C端-购物车相关接口")
public class ShoppingCartController
    {
        @Autowired
        private ShoppingCartService shoppingCartService;
        
        /**
         * 添加购物车
         *
         * @param shoppingCartDTO 购物车 DTO
         * @return {@link Result }
         */
        @PostMapping("/add")
        @ApiOperation("添加购物车")
        public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO)
            {
                log.info("要添加的商品：{}", shoppingCartDTO);
                shoppingCartService.addShoppingCart(shoppingCartDTO);
                return Result.success();
            }
    }
