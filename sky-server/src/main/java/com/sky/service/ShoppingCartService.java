package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/23 21:41<br/>
 */
public interface ShoppingCartService
    {
        /**
         * 添加购物车
         *
         * @param shoppingCartDTO 购物车 DTO
         */
        void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
        
        /**
         * 查询购物车
         *
         * @return {@link List }<{@link ShoppingCart }>
         */
        List<ShoppingCart> list();
    }
