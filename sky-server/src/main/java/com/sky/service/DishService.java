package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/08 20:34<br/>
 */
public interface DishService
    {
        /**
         * Save with flavor （保存风味）
         *
         * @param dishDTO dish dto
         */
        void saveWithFlavor(DishDTO dishDTO);
        
        /**
         * 菜品分页查询
         *
         * @param dishPageQueryDTO dish 页面查询 DTO
         * @return {@link Dish }
         */
        PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
        
        /**
         * 删除批处理
         *
         * @param ids IDS
         */
        void deleteBatch(List<Long> ids);
        
        /**
         * 按 ID 获取dish
         *
         * @param id Dish id
         * @return {@link Dish }
         */
        Dish getById(Long id);
    }
