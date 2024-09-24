package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        
        /**
         * 查询购物车
         *
         * @return {@link Result }<{@link List }<{@link ShoppingCart }>>
         */
        @GetMapping("/list")
        @ApiOperation("查看购物车")
        public Result<List<ShoppingCart>> list()
            {
                log.info("查看购物车");
                List<ShoppingCart> shoppingCarts = shoppingCartService.list();
                return Result.success(shoppingCarts);
            }
        
        /**
         * 删除购物车中一个商品
         *
         * @param shoppingCartDTO 购物车 DTO
         * @return {@link Result }
         */
        @PostMapping("/sub")
        @ApiOperation("删除购物车中一个商品")
        public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO)
            {
                log.info("要操作的购物车商品：{}", shoppingCartDTO);
                shoppingCartService.sub(shoppingCartDTO);
                return Result.success();
            }
        
        /**
         * 清空当前用户的购物车
         *
         * @return {@link Result }
         */
        @DeleteMapping("/clean")
        @ApiOperation("清空当前用户购物车")
        public Result clean()
            {
                log.info("清空当前用户的购物车，当前用户id：{}", BaseContext.getCurrentId());
                shoppingCartService.clean();
                return Result.success();
            }
    }
