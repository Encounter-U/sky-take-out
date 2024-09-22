package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Encounter
 * @date 2024/09/22 14:53<br/>
 * 管理端套餐页面开发
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api("套餐相关接口")
@Slf4j
public class SetmealController
    {
        @Autowired
        private SetmealService setmealService;
        
        /**
         * 新增套餐
         *
         * @param setmealDTO Setmeal DTO
         * @return {@link Result }
         */
        @PostMapping
        @ApiOperation("新增套餐")
        public Result save(@RequestBody SetmealDTO setmealDTO)
            {
                log.info("新增套餐：{}", setmealDTO);
                setmealService.save(setmealDTO);
                
                return Result.success();
            }
        
        /**
         * 分页查询
         *
         * @param setmealPageQueryDTO setmeal 页面查询 DTO
         * @return {@link Result }<{@link PageResult }>
         */
        @GetMapping("/page")
        @ApiOperation("分页查询")
        public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO)
            {
                log.info("分页查询数据：{}", setmealPageQueryDTO);
                PageResult page = setmealService.page(setmealPageQueryDTO);
                return Result.success(page);
            }
        
        /**
         * 开始或停止
         *
         * @param status 套餐售卖状态
         * @param id     套餐id
         * @return {@link Result }
         */
        @PostMapping("/status/{status}")
        public Result startOrStop(@PathVariable Integer status, long id)
            {
                log.info("修改后的状态：{}，要修改的套餐id：{}", (status == 1 ? "起售" : "停售"), id);
                
                setmealService.startOrStop(status, id);
                return Result.success();
            }
        
        /**
         * 按 ID 获取套餐
         *
         * @param id 要查询的套餐id
         * @return {@link Result }
         */
        @GetMapping("/{id}")
        public Result<SetmealVO> getById(@PathVariable long id)
            {
                log.info("要查询的套餐id：{}", id);
                SetmealVO setmealVO=setmealService.getById(id);
                return Result.success(setmealVO);
            }
    }
