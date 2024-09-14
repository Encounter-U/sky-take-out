package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author Encounter
 * @date 2024/09/14 13:29<br/>
 */
public interface UserService
    {
        /**
         * wx 登录
         *
         * @param userLoginDTO 用户登录 DTO
         * @return {@link User }
         */
        User wxLogin(UserLoginDTO userLoginDTO);
    }
