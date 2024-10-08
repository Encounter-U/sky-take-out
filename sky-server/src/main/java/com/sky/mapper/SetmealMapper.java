package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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
    
    /**
     * 新增套餐
     *
     * @param setmeal 套餐
     */
    @AutoFill(OperationType.INSERT)
    void save(Setmeal setmeal);
    
    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO setmeal 页面查询 DTO
     * @return {@link List }<{@link SetmealVO }>
     */
    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);
    
    /**
     * 按 ID 获取 SetMeal
     *
     * @param id 套餐id
     * @return {@link Setmeal }
     */
    @Select("select id, category_id, name, price, status, description, image, " +
            "create_time, update_time, create_user, update_user from setmeal " +
            "where id = #{id}")
    Setmeal getSetmealById(Long id);
    
    /**
     * 修改套餐信息
     *
     * @param setmeal 套餐
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
    
    /**
     * 根据id删除批处理
     *
     * @param ids IDS
     */
    void deleteBatch(List<Long> ids);
    
    /**
     * 根据条件统计套餐数量
     *
     * @param map Map集合封装查询条件
     * @return {@link Integer }
     */
    Integer countByMap(Map<Object,Object> map);
}
