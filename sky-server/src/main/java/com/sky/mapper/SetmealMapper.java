package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
    
    /**
     * 动态条件查询套餐
     *
     * @param setmeal 套餐
     * @return {@link List }<{@link Setmeal }>
     */
    List<Setmeal> list(Setmeal setmeal);
    
    /**
     * 根据套餐id查询菜品选项
     *
     * @param setmealId SetMeal ID
     * @return {@link List }<{@link DishItemVO }>
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
    
    @AutoFill(OperationType.INSERT)
    void save(Setmeal setmeal);
}
