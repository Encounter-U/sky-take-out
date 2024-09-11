package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author Encounter
 * @date 2024/09/11 17:21<br/>
 */
//自定义Bean名
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api("店铺相关接口")
@Slf4j
public class ShopController
    {
        private static final String KEY="SHOP_STATUS";
        
        @Autowired
        private RedisTemplate redisTemplate;
        
        /**
         * 设置店铺营业状态
         *
         * @param status 营业状态
         * @return {@link Result }
         */
        @PutMapping("/{status}")
        @ApiOperation("设置店铺的营业状态")
        public Result setStatus(@PathVariable Integer status)
            {
                log.info("设置店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
                redisTemplate.opsForValue().set(KEY, status);
                return Result.success();
            }
        
        /**
         * 获取店铺营业状态
         *
         * @return {@link Result }<{@link Integer }>
         */
        @GetMapping("status")
        @ApiOperation("获取店铺营业状态")
        public Result<Integer> getStatus()
            {
                Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
                log.info("获取到的店铺营业状态为：{}", shopStatus);
                return Result.success(shopStatus);
            }
    }
