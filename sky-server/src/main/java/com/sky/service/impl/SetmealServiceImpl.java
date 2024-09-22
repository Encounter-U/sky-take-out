package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
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
                BeanUtils.copyProperties(setmealDTO, setmeal);
                //更改起售状态，默认停售
                setmeal.setStatus(StatusConstant.DISABLE);
                log.info("添加套餐开始，{}",setmeal);
                setmealMapper.save(setmeal);
                
                List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
                if (setmealDishes != null && !setmealDishes.isEmpty())
                    {
                        for (SetmealDish setmealDish : setmealDishes)
                            {
                                log.info("添加套餐菜品关系开始,{}",setmealDish);
                                setmealDishMapper.save(setmealDish);
                            }
                    }
            }
        
        /**
         * 分页查询
         *
         * @param setmealPageQueryDTO setmeal 页面查询 DTO
         * @return {@link PageResult }
         */
        @Override
        public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO)
            {
                //设置分页参数
                PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
                
                Page<SetmealVO> page = setmealMapper.page(setmealPageQueryDTO);
                log.info("page:{}",page);
                log.info("total:{}", page.getTotal());
                log.info("page:{}", page.getPages());
                return new PageResult(page.getTotal(), page.getResult());
            }
        
        /**
         * 开始或停止
         *
         * @param status 售卖状态
         * @param id     套餐id
         */
        @Override
        public void startOrStop(Integer status, long id)
            {
                Setmeal setmeal = setmealMapper.getSetmealById(id);
                setmeal.setStatus(status);
                setmealMapper.update(setmeal);
            }
        
        
    }