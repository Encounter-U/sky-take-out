package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

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
    }
