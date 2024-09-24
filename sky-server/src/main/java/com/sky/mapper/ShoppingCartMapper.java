package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/23 21:49<br/>
 */
@Mapper
public interface ShoppingCartMapper
    {
        /**
         * 动态查询购物车
         *
         * @param shoppingCart 购物车
         * @return {@link List }<{@link ShoppingCart }>
         */
        List<ShoppingCart> list(ShoppingCart shoppingCart);
        
        /**
         * 修改购物车中商品数量
         *
         * @param shoppingCart 购物车
         */
        @Update("update shopping_cart set number = #{number} where id = #{id}")
        void updateNumberById(ShoppingCart shoppingCart);
        
        /**
         * 插入购物车数据
         *
         * @param shoppingCart 购物车
         */
        @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, " +
                "dish_flavor, number, amount, create_time) " +
                "values(#{name},#{image},#{userId},#{dishId},#{setmealId}," +
                "#{dishFlavor},#{number},#{amount},#{createTime}) ")
        void insert(ShoppingCart shoppingCart);
        
        /**
         * 动态删除购物车中商品
         *
         * @param ids 要删除的id集合
         */
        void delete(List<Long> ids);
        
        /**
         * 按用户 ID 删除
         *
         * @param userId 当前用户 ID
         */
        @Delete("delete from shopping_cart where user_id = #{userId}")
        void deleteByUserId(Long userId);
    }
