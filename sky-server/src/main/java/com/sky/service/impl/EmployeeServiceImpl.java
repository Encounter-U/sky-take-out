package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
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
         * 员工登录
         *
         * @param employeeLoginDTO
         * @return
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
                //TODO Encounter 2024/09/05 21:12 后期改为当前登录用户的值
                
                employee.setCreateUser(BaseContext.getCurrentId());
                employee.setUpdateUser(BaseContext.getCurrentId());
                
                employeeMapper.insert(employee);
                
            }
        
    }
