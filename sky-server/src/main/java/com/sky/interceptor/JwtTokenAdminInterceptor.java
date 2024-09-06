package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor
    {
        
        @Autowired
        private JwtProperties jwtProperties;
        
        /**
         * 预处理
         *
         * @param request  请求
         * @param response 响应
         * @param handler  处理器
         * @return boolean
         * @throws Exception 例外
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
            {
                //判断当前拦截到的是Controller的方法还是其他资源
                if (!(handler instanceof HandlerMethod))
                    {
                        //当前拦截到的不是动态方法，直接放行
                        return true;
                    }
                
                //1、从请求头中获取令牌
                String token = request.getHeader(jwtProperties.getAdminTokenName());
                
                //2、校验令牌
                try
                    {
                        log.info("jwt校验:{}", token);
                        Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
                        Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
                        log.info("当前员工id：{}", empId);
                        BaseContext.setCurrentId(empId);
                        //3、通过，放行
                        return true;
                    }
                catch (Exception ex)
                    {
                        //4、不通过，响应401状态码
                        response.setStatus(401);
                        return false;
                    }
            }
    }
