package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/06 21:17<br/>
 */
@Mapper
public interface CategoryMapper {
    
    /**
     * 插入数据
     *
     * @param category 类别
     */
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);
    
    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO 分类页查询 DTO
     * @return {@link Page }<{@link Category }>
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
    
    /**
     * 根据id删除分类
     *
     * @param id 身份证
     */
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);
    
    /**
     * 根据id修改分类
     *
     * @param category 类别
     */
    void update(Category category);
    
    /**
     * 根据类型查询分类
     *
     * @param type 类型
     * @return {@link List }<{@link Category }>
     */
    List<Category> list(Integer type);
}
