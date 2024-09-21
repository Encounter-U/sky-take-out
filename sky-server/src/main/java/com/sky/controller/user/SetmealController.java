package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/20 18:35<br/>
 */
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
@Api(tags = "C端-套餐浏览接口")
public class SetmealController
    {
        @Autowired
        private SetmealService setmealService;
        
        /**
         * 条件查询
         *
         * @param categoryId 类别 ID
         * @return {@link Result }<{@link List }<{@link Setmeal }>>
         */
        @GetMapping("/list")
        @ApiOperation("根据分类id查询套餐")
        public Result<List<Setmeal>> list(Long categoryId)
            {
                log.info("菜品分类：{}", categoryId);
                
                Setmeal setmeal = new Setmeal();
                setmeal.setCategoryId(categoryId);
                setmeal.setStatus(StatusConstant.ENABLE);
                
                List<Setmeal> list = setmealService.list(setmeal);
                return Result.success(list);
            }
        
        /**
         * 根据套餐id查询包含的菜品列表
         *
         * @param id 身份证
         * @return {@link Result }<{@link List }<{@link DishItemVO }>>
         */
        @GetMapping("/dish/{id}")
        @ApiOperation("根据套餐id查询包含的菜品列表")
        public Result<List<DishItemVO>> dishList(@PathVariable Long id)
            {
                log.info("套餐id：{}", id);
                
                List<DishItemVO> list = setmealService.getDishItemById(id);
                return Result.success(list);
            }
    }
