package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Encounter
 * @date 2024/09/14 13:38<br/>
 */
@Mapper
public interface UserMapper
    {
        /**
         * 按 Open ID 获取
         *
         * @param openid OpenID
         * @return {@link User }
         */
        @Select("select * from user where openid = #{openid} ")
        User getByOpenId(String openid);
        
        /**
         * 插入用户
         *
         * @param user 用户
         */
        void insert(User user);
        
        /**
         * 按 ID 获取
         *
         * @param userId 用户 ID
         * @return {@link User }
         */
        @Select("select * from user where id = #{userId}")
        User getById(Long userId);
    }
