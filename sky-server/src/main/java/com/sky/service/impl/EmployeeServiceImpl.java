package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService
    {
        
        @Autowired
        private EmployeeMapper employeeMapper;
        
        /**
         * 登录
         *
         * @param employeeLoginDTO 员工登录 DTO
         * @return {@link Employee }
         */
        public Employee login(EmployeeLoginDTO employeeLoginDTO)
            {
                String username = employeeLoginDTO.getUsername();
                String password = employeeLoginDTO.getPassword();
                
                //1、根据用户名查询数据库中的数据
                Employee employee = employeeMapper.getByUsername(username);
                
                //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
                if (employee == null)
                    {
                        //账号不存在
                        throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
                    }
                
                //密码比对
                //对前端传过来的明文密码进行md5加密处理
                password = DigestUtils.md5DigestAsHex(password.getBytes());
                if (!password.equals(employee.getPassword()))
                    {
                        //密码错误
                        throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
                    }
                
                if (employee.getStatus() == StatusConstant.DISABLE)
                    {
                        //账号被锁定
                        throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
                    }
                
                //3、返回实体对象
                return employee;
            }
        
        /**
         * 新增员工
         *
         * @param employeeDTO 员工 DTO
         */
        @Override
        public void save(EmployeeDTO employeeDTO)
            {
                System.out.println("当前线程id：" + Thread.currentThread().getId());
                Employee employee = new Employee();
                
                //对象属性拷贝
                BeanUtils.copyProperties(employeeDTO, employee);
                
                //账号状态，默认1正常， 0锁定
                employee.setStatus(StatusConstant.ENABLE);
                
                //默认密码 123456
                employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
                
                //创建时间及当前修改时间
                employee.setCreateTime(LocalDateTime.now());
                employee.setUpdateTime(LocalDateTime.now());
                
                //当前操作人id及修改人id
                employee.setCreateUser(BaseContext.getCurrentId());
                employee.setUpdateUser(BaseContext.getCurrentId());
                
                employeeMapper.insert(employee);
                
            }
        
        /**
         * 页面查询
         *
         * @param employeePageQueryDTO 员工页面查询 DTO
         * @return {@link PageResult }
         */
        @Override
        public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO)
            {
                //开始分页
                //TODO Encounter 2024/09/06 20:06 分页功能不生效
                PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
                
                Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
                return new PageResult(page.getTotal(), page.getResult());
            }
        
        /**
         * 开始或停止
         *
         * @param status 地位
         * @param id     身份证
         */
        @Override
        public void startOrStop(Integer status, Long id)
            {
                //两种创建对象的编程风格，效果一样
                Employee employee=new Employee();
                employee.setId(id);
                employee.setStatus(status);
                
                employee.setUpdateTime(LocalDateTime.now());
                employee.setUpdateUser(BaseContext.getCurrentId());
                /*Employee employee = Employee.builder()
                        .id(id)
                        .status(status)
                        .build();*/
                
                employeeMapper.update(employee);
            }
        
    }
