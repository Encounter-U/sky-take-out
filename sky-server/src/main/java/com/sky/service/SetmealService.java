package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.DishItemVO;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/20 18:37<br/>
 */
public interface SetmealService
    {
        
        /**
         * 条件查询
         *
         * @param setmeal 套餐
         * @return {@link List }<{@link Setmeal }>
         */
        List<Setmeal> list(Setmeal setmeal);
        
        /**
         * 根据id查询菜品选项
         *
         * @param id 身份证
         * @return {@link List }<{@link DishItemVO }>
         */
        List<DishItemVO> getDishItemById(Long id);
        
        /**
         * 新增菜品
         *
         * @param setmealDTO Setmeal DTO
         */
        void save(SetmealDTO setmealDTO);
        
    }
