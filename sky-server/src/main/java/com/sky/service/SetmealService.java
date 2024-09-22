package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

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
        
        /**
         * 分页查询
         *
         * @param setmealPageQueryDTO setmeal 页面查询 DTO
         * @return {@link Page }<{@link SetmealVO }>
         */
        PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);
    }
