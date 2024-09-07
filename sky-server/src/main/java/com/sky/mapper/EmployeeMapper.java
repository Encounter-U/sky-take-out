package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper
    {
        
        /**
         * 根据用户名查询员工
         *
         * @param username 用户名
         * @return {@link Employee }
         */
        @Select("select * from employee where username = #{username}")
        Employee getByUsername(String username);
        
        /**
         * 插入员工
         *
         * @param employee 员工
         */
        @Insert("insert into employee(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
                "values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
        @AutoFill(OperationType.INSERT)
        void insert(Employee employee);
        
        /**
         * 分页查询
         *
         * @param employeePageQueryDTO 员工页面查询 DTO
         * @return {@link Page }<{@link Employee }>
         */
        Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
        
        /**
         * 更新
         *
         * @param employee 员工
         */
        @AutoFill(OperationType.UPDATE)
        void update(Employee employee);
        
        /**
         * 按 ID 获取
         *
         * @param id 员工ID
         * @return {@link Employee }
         */
        @Select("select * from employee where id=#{id}")
        Employee getById(Long id);
    }
