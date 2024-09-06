package com.sky.mapper;

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
        
    }
