package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Encounter
 * @date 2024/09/06 21:20<br/>
 */
@Mapper
public interface SetmealMapper {
    
    /**
     * 根据分类id查询套餐的数量
     *
     * @param id 套餐ID
     * @return {@link Integer }
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);
    
}
