package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Encounter
 * @date 2024/09/06 21:19<br/>
 */
@Mapper
public interface DishMapper
    {
        /**
         * 根据分类id查询菜品数量
         *
         * @param categoryId 类别 ID
         * @return {@link Integer }
         */
        @Select("select count(id) from dish where category_id = #{categoryId}")
        Integer countByCategoryId(Long categoryId);
        
        /**
         * 插入菜品数据<br/>
         * 自动注入，插入类型
         *
         * @param dish 菜
         */
        @AutoFill(OperationType.INSERT)
        void insert(Dish dish);
    }
