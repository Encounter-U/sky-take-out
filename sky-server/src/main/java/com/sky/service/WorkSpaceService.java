package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

/**
 * @author Encounter
 * @date 2024/09/27 19:27<br/>
 */
public interface WorkSpaceService {
    
    /**
     * 根据时间段统计营业数据
     *
     * @param begin 开始
     * @param end   结束
     * @return {@link BusinessDataVO }
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);
    
    /**
     * 查询订单管理数据
     *
     * @return {@link OrderOverViewVO }
     */
    OrderOverViewVO getOrderOverView();
    
    /**
     * 查询菜品总览
     *
     * @return {@link DishOverViewVO }
     */
    DishOverViewVO getDishOverView();
    
    /**
     * 查询套餐总览
     *
     * @return {@link SetmealOverViewVO }
     */
    SetmealOverViewVO getSetmealOverView();
    
}
