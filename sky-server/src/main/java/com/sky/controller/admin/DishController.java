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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
        @Autowired
        private RedisTemplate redisTemplate;
        
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
                
                //清理缓存数据
                String key = "dish_" + dishDTO.getCategoryId();
                cleanCache(key);
                
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
                
                //将所有的菜品缓存数据清理掉，所有以dish_开头的key
                cleanCache("dish_*");
                
                return Result.success();
            }
        
        /**
         * 按 ID 获取Dish
         *
         * @param id Dish ID
         * @return {@link Result }<{@link DishVO }>
         */
        @GetMapping("/{id}")
        @ApiOperation("根据id查询dish")
        public Result<DishVO> getById(@PathVariable Long id)
            {
                log.info("要查询的id：{}", id);
                DishVO dishVO = dishService.getById(id);
                return Result.success(dishVO);
            }
        
        /**
         * 修改Dish
         *
         * @param dishDTO dish dto
         * @return {@link Result }
         */
        @PutMapping
        @ApiOperation("修改菜品信息")
        public Result update(@RequestBody DishDTO dishDTO)
            {
                log.info("要修改的信息：{}", dishDTO);
                dishService.update(dishDTO);
                
                //将所有的菜品缓存数据清理掉，所有以dish_开头的key
                cleanCache("dish_*");
                
                return Result.success();
            }
        
        //TODO Encounter 2024/09/21 20:23 服务端菜品起售停售，暂未开发（根据id分类查询菜品）
        
        
        /**
         * 清理缓存
         *
         * @param pattern key的模式
         */
        private void cleanCache(String pattern)
            {
                //将所有的菜品缓存数据清理掉，所有以dish_开头的key
                Set keys = redisTemplate.keys(pattern);
                redisTemplate.delete(keys);
            }
    }
