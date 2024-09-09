package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Encounter
 * @date 2024/09/08 20:28<br/>
 * 菜品管理
 */
@RestController
@Slf4j
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController
    {
        @Autowired
        private DishService dishService;
        
        /**
         * 新增菜品
         *
         * @param dishDTO dish dto
         * @return {@link Result }
         */
        @PostMapping
        @ApiOperation("新增菜品")
        public Result save(@RequestBody DishDTO dishDTO)
            {
                log.info("新增菜品：{}", dishDTO);
                dishService.saveWithFlavor(dishDTO);
                return Result.success();
            }
        
        /**
         * 菜品分页查询
         *
         * @param dishPageQueryDTO 菜品分页查询 DTO
         * @return {@link Result }<{@link Dish }>
         */
        @GetMapping("/page")
        @ApiOperation("菜品分页查询")
        public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO)
            {
                log.info("菜品分页查询参数：{}", dishPageQueryDTO);
                PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
                
                return Result.success(pageResult);
            }
    }
