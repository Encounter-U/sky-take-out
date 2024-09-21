package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Encounter
 * @date 2024/09/08 20:35<br/>
 */
@Service
public class DishServiceImpl implements DishService
    {
        private static final Logger log = LoggerFactory.getLogger(DishServiceImpl.class);
        @Autowired
        private DishMapper dishMapper;
        @Autowired
        private DishFlavorMapper dishFlavorMapper;
        @Autowired
        private SetmealDishMapper setmealDishMapper;
        
        /**
         * Save with flavor （保存风味）
         *
         * @param dishDTO dish dto
         */
        @Override
        @Transactional
        public void saveWithFlavor(DishDTO dishDTO)
            {
                Dish dish = new Dish();
                BeanUtils.copyProperties(dishDTO, dish);
                //向菜品表插入1条数据
                dishMapper.insert(dish);
                //获取insert语句生成的主键值
                Long dishId = dish.getId();
                
                List<DishFlavor> flavors = dishDTO.getFlavors();
                if (flavors != null && !flavors.isEmpty())
                    {
                        for (DishFlavor flavor : flavors)
                            flavor.setDishId(dishId);
                        //向口味表插入n条数据
                        dishFlavorMapper.insertBatch(flavors);
                    }
            }
        
        /**
         * 分页查询菜品
         *
         * @param dishPageQueryDTO dish 页面查询 DTO
         * @return {@link PageResult }
         */
        @Override
        public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO)
            {
                //设置分页参数
                PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
                
                Page<DishVO> dishes = dishMapper.pageQuery(dishPageQueryDTO);
                
                log.info("dishs,{}", dishes);
                
                log.info("getTotal:{}", dishes.getTotal());
                log.info("getResult:{}", dishes.getResult());
                return new PageResult(dishes.getTotal(), dishes.getResult());
            }
        
        /**
         * 删除批处理
         *
         * @param ids IDS
         */
        @Override
        @Transactional
        public void deleteBatch(List<Long> ids)
            {
                for (Long id : ids)
                    {
                        Dish dish = dishMapper.getById(id);
                        if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE))
                            //起售中，不允许删除
                            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
                    }
                
                List<Long> setmealIdsByDishIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
                if (setmealIdsByDishIds != null && !setmealIdsByDishIds.isEmpty())
                    //当前要删除的菜品中有关联的套餐，不允许删除
                    throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
                
                dishMapper.deleteBatch(ids);
                dishFlavorMapper.deleteByDishIds(ids);
            }
        
        /**
         * 按 ID 获取Dish
         *
         * @param id Dish ID
         * @return {@link Dish }
         */
        @Override
        public DishVO getById(Long id)
            {
                //获取菜品信息
                Dish dish = dishMapper.getById(id);
                
                //获取口味信息
                List<DishFlavor> dishFlavor = dishFlavorMapper.getDishFlavorById(id);
                
                //封装DishVO
                DishVO dishVO = new DishVO();
                BeanUtils.copyProperties(dish, dishVO);
                dishVO.setFlavors(dishFlavor);
                
                return dishVO;
            }
        
        /**
         * 修改Dish
         *
         * @param dishDTO dish dto
         */
        @Override
        @Transactional
        public void update(DishDTO dishDTO)
            {
                //修改Dish表
                Dish dish = new Dish();
                BeanUtils.copyProperties(dishDTO, dish);
                dishMapper.update(dish);
                
                //删除原相关口味信息
                dishFlavorMapper.deleteByDishIds(Collections.singletonList(dishDTO.getId()));
                
                //添加新口味信息
                List<DishFlavor> flavors = dishDTO.getFlavors();
                if (flavors != null && !flavors.isEmpty())
                    {
                        //重新给DishID赋值是因为新增的flavor只有name和value值，DishID为null，所以要重新赋值
                        for (DishFlavor flavor : flavors)
                            flavor.setDishId(dishDTO.getId());
                        dishFlavorMapper.insertBatch(flavors);
                    }
                
            }
        
        /**
         * 条件查询菜品和口味
         *
         * @param dish 菜
         * @return {@link List }<{@link DishVO }>
         */
        public List<DishVO> listWithFlavor(Dish dish) {
            List<Dish> dishList = dishMapper.list(dish);
            
            List<DishVO> dishVOList = new ArrayList<>();
            
            for (Dish d : dishList) {
                DishVO dishVO = new DishVO();
                BeanUtils.copyProperties(d,dishVO);
                
                //根据菜品id查询对应的口味
                List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());
                
                dishVO.setFlavors(flavors);
                dishVOList.add(dishVO);
            }
            
            return dishVOList;
        }
    }
