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
        @Select("select * from user where openid = #{openid} ")
        User getByOpenId(String openid);
        
        void insert(User user);
    }
