package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/09 15:42<br/>
 */
@Mapper
public interface SetmealDishMapper
    {
        /**
         * 按菜品 ID 获取 SetMeal数量
         *
         * @param ids Dish IDS
         * @return {@link List }<{@link Long }>
         */
        List<Long> getSetmealIdsByDishIds(List<Long> ids);
        
        /**
         * 新增套餐菜品关系
         *
         * @param setmealDish 套餐
         */
        void save(SetmealDish setmealDish);
    }
