package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController
    {
        
        @Autowired
        private EmployeeService employeeService;
        @Autowired
        private JwtProperties jwtProperties;
        
        
        /**
         * 登录
         *
         * @param employeeLoginDTO 员工登录 DTO
         * @return {@link Result }<{@link EmployeeLoginVO }>
         */
        @PostMapping("/login")
        @ApiOperation("员工登录")
        public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO)
            {
                log.info("员工登录：{}", employeeLoginDTO);
                
                Employee employee = employeeService.login(employeeLoginDTO);
                
                //登录成功后，生成jwt令牌
                Map<String, Object> claims = new HashMap<>();
                claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
                String token = JwtUtil.createJWT(
                        jwtProperties.getAdminSecretKey(),
                        jwtProperties.getAdminTtl(),
                        claims);
                
                EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                        .id(employee.getId())
                        .userName(employee.getUsername())
                        .name(employee.getName())
                        .token(token)
                        .build();
                
                return Result.success(employeeLoginVO);
            }
        
        
        /**
         * 注销
         *
         * @return {@link Result }<{@link String }>
         */
        @PostMapping("/logout")
        @ApiOperation("员工退出")
        public Result<String> logout()
            {
                return Result.success();
            }
        
        /**
         * 新增员工
         *
         * @param employeeDTO 员工 DTO
         * @return {@link Result }
         */
        @ApiOperation("新增员工")
        @PostMapping
        public Result save(@RequestBody EmployeeDTO employeeDTO)
            {
                log.info("新增员工,{}", employeeDTO);
                System.out.println("当前线程id：" + Thread.currentThread().getId());
                employeeService.save(employeeDTO);
                return Result.success();
            }
        
        /**
         * 页面查询
         *
         * @param employeePageQueryDTO 员工页面查询 DTO
         * @return {@link Result }<{@link PageResult }>
         */
        @GetMapping("/page")
        @ApiOperation("员工分页查询")
        public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO)
            {
                log.info("员工分页查询，参数为：{}", employeePageQueryDTO);
                PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);//后续定义
                return Result.success(pageResult);
            }
        
        
        /**
         * 启用或禁用
         *
         * @param status 状态
         * @param id     要更改的id
         * @return {@link Result }
         */
        @PostMapping("/status/{status}")
        @ApiOperation("启用或禁用 ")
        public Result startOrStop(@PathVariable Integer status, Long id)
            {
                log.info("启用或禁用，状态：{}，id：{}", status, id);
                employeeService.startOrStop(status, id);
                return Result.success();
            }
        
        /**
         * 按 ID 获取
         *
         * @param id 身份证
         * @return {@link Result }<{@link Employee }>
         */
        @GetMapping("/{id}")
        @ApiOperation("根据id查询员工")
        public Result<Employee> getById(@PathVariable Long id)
            {
                log.info("要查询的id：{}", id);
                return Result.success(employeeService.getById(id));
            }
        
        /**
         * 更新
         *
         * @param employeeDTO 员工 DTO
         * @return {@link Result }
         */
        @PutMapping
        @ApiOperation("编辑员工信息")
        public Result update(@RequestBody EmployeeDTO employeeDTO)
            {
                log.info("编辑员工信息：{}", employeeDTO);
                employeeService.update(employeeDTO);
                return Result.success();
            }
    }
