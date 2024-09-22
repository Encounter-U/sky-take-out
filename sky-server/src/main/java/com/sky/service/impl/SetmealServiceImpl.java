package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/20 18:37<br/>
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService
    {
        
        @Autowired
        private SetmealMapper setmealMapper;
        @Autowired
        private SetmealDishMapper setmealDishMapper;
        @Autowired
        private DishMapper dishMapper;
        
        /**
         * 条件查询
         *
         * @param setmeal 套餐
         * @return {@link List }<{@link Setmeal }>
         */
        public List<Setmeal> list(Setmeal setmeal)
            {
                return setmealMapper.list(setmeal);
            }
        
        /**
         * 根据id查询菜品选项
         *
         * @param id 身份证
         * @return {@link List }<{@link DishItemVO }>
         */
        public List<DishItemVO> getDishItemById(Long id)
            {
                return setmealMapper.getDishItemBySetmealId(id);
            }
        
        /**
         * 新增套餐
         *
         * @param setmealDTO Setmeal DTO
         */
        @Override
        public void save(SetmealDTO setmealDTO)
            {
                //创建setmeal对象
                Setmeal setmeal = new Setmeal();
                BeanUtils.copyProperties(setmealDTO,setmeal);
                //更改起售状态，默认停售
                setmeal.setStatus(StatusConstant.DISABLE);
                setmealMapper.save(setmeal);
                
                List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
                if (setmealDishes != null && !setmealDishes.isEmpty())
                    {
                        for (SetmealDish setmealDish : setmealDishes)
                            {
                                setmealDishMapper.save(setmealDish);
                            }
                    }
            }
        
        
    }