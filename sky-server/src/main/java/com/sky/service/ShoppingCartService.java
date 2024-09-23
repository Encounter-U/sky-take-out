package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

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
    }
