package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/08 20:59<br/>
 */
@Mapper
public interface DishFlavorMapper
    {
        /**
         * 插入批处理
         *
         * @param flavors 口味
         */
        void insertBatch(List<DishFlavor> flavors);
        
        /**
         * 按 Dish ID 批量删除
         *
         * @param dishIds Dish ID
         */
        void deleteByDishIds(List<Long> dishIds);
        
        /**
         * 按 DishID 获取菜品风味
         *
         * @param dishId Dish ID
         * @return {@link DishFlavor }
         */
        List<DishFlavor> getDishFlavorById(Long dishId);
        
        /**
         * 按菜品 ID 获取
         *
         * @param dishId 菜品 ID
         * @return {@link List }<{@link DishFlavor }>
         */
        @Select("select * from dish_flavor where dish_id = #{dishId}")
        List<DishFlavor> getByDishId(Long dishId);
    }
