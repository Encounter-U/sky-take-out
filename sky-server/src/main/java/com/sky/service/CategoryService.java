package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/06 21:20<br/>
 */
public interface CategoryService {
    
    /**
     * 新增菜品
     *
     * @param categoryDTO 类别 DTO
     */
    void save(CategoryDTO categoryDTO);
    
    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO 分类页查询 DTO
     * @return {@link PageResult }
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
    
    /**
     * 根据id删除分类
     *
     * @param id 身份证
     */
    void deleteById(Long id);
    
    /**
     * 修改分类
     *
     * @param categoryDTO 类别 DTO
     */
    void update(CategoryDTO categoryDTO);
    
    /**
     * 启用、禁用分类
     *
     * @param status 地位
     * @param id     身份证
     */
    void startOrStop(Integer status, Long id);
    
    /**
     * 根据类型查询分类
     *
     * @param type 类型
     * @return {@link List }<{@link Category }>
     */
    List<Category> list(Integer type);
}
