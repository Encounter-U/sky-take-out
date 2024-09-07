package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author Encounter
 * @date 2024/09/07 13:53<br/>
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect
    {
        
        /**
         * 自动填充切入点<br/>
         * 仅填充mapper包下带有AutoFill注解的方法
         */
        @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
        public void autoFillPointcut()
            {
            }
        
        /**
         * AUT0 填充<br/>
         * 要有约定：实体类放在参数列表第一位
         *
         * @param joinPoint 加入点
         */
        @Before("autoFillPointcut()")
        public void aut0Fill(JoinPoint joinPoint)
            {
                log.info("Auto fill start");
                
                //获取到当前被拦截的方法上的数据库操作类型
                MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();//方法签名对象
                AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
                OperationType operationType = autoFill.value();//获得数据库操作对象
                
                //获取到当前被拦截的方法的参数--实体对象
                Object[] args = joinPoint.getArgs();
                //以防万一，判空，一般不会出现这种情况
                if (args == null || args.length == 0)
                    return;
                //获取到实体类
                Object entity = args[0];
                
                //准备赋值的数据
                LocalDateTime now = LocalDateTime.now();
                Long currentId = BaseContext.getCurrentId();
                
                //根据当前不同的数据类型，为对应的属性通过反射来赋值
                if (operationType == OperationType.INSERT)
                    {
                        //为4个公共字段赋值
                        try
                            {
                                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                                
                                //通过反射为对象属性赋值
                                setCreateTime.invoke(entity, now);
                                setUpdateTime.invoke(entity, now);
                                setCreateUser.invoke(entity, currentId);
                                setUpdateUser.invoke(entity, currentId);
                            }
                        catch (Exception e)
                            {
                                throw new RuntimeException(e);
                            }
                    }
                else if (operationType == OperationType.UPDATE)
                    {
                        //为2个公共字段赋值
                        try
                            {
                                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                                
                                //通过反射为对象属性赋值
                                setUpdateTime.invoke(entity, now);
                                setUpdateUser.invoke(entity, currentId);
                            }
                        catch (Exception e)
                            {
                                throw new RuntimeException(e);
                            }
                    }
            }
    }
