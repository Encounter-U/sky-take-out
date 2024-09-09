package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        
        /**
         * 删除
         *
         * @param ids 要删除的id集合
         * @return {@link Result }
         */
        @DeleteMapping
        @ApiOperation("删除菜品")
        public Result delete(@RequestParam List<Long> ids)
            {
                log.info("要删除的id集合：{}", ids);
                dishService.deleteBatch(ids);
                return Result.success();
            }
        
        @GetMapping("/{id}")
        @ApiOperation("根据id查询dish")
        public Result<DishVO> getById(@PathVariable Long id)
            {
                log.info("要查询的id：{}", id);
                DishVO dishVO = dishService.getById(id);
                return Result.success(dishVO);
            }
    }
