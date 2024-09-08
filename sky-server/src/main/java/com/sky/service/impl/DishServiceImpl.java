package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/08 20:35<br/>
 */
@Service
public class DishServiceImpl implements DishService
    {
        @Autowired
        private DishMapper dishMapper;
        @Autowired
        private DishFlavorMapper dishFlavorMapper;
        
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
                            {
                                flavor.setDishId(dishId);
                            }
                        //向口味表插入n条数据
                        dishFlavorMapper.insertBatch(flavors);
                    }
            }
    }
