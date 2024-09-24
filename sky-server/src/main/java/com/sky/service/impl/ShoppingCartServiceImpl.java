package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/23 21:41<br/>
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService
    {
        @Autowired
        private ShoppingCartMapper shoppingCartMapper;
        
        @Autowired
        private DishMapper dishMapper;
        
        @Autowired
        private SetmealMapper setmealMapper;
        
        /**
         * 添加购物车
         *
         * @param shoppingCartDTO 购物车 DTO
         */
        @Override
        public void addShoppingCart(ShoppingCartDTO shoppingCartDTO)
            {
                //当前加入购物车的商品是否已存在
                ShoppingCart shoppingCart = new ShoppingCart();
                
                //属性拷贝
                BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
                
                //拿到当前操作用户的id
                Long userId = BaseContext.getCurrentId();
                shoppingCart.setUserId(userId);
                
                List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
                
                //已存在，将数量加一
                if (!shoppingCarts.isEmpty())
                    {
                        //只会有一条数据，直接加一即可
                        ShoppingCart cart = shoppingCarts.get(0);
                        cart.setNumber(cart.getNumber() + 1);
                        //修改数量
                        shoppingCartMapper.updateNumberById(cart);
                        //修改完毕，直接退出方法
                        return;
                    }
                
                //不存在，插入一条购物车数据
                //根据dishId与setmealId是否为空判断添加的是菜品还是套餐
                Long dishId = shoppingCartDTO.getDishId();
                Long setmealId = shoppingCartDTO.getSetmealId();
                if (dishId != null)
                    {
                        //菜品
                        Dish dish = dishMapper.getById(dishId);
                        shoppingCart.setName(dish.getName());
                        shoppingCart.setImage(dish.getImage());
                        shoppingCart.setAmount(dish.getPrice());
                    }
                else
                    {
                        //套餐
                        Setmeal setmeal = setmealMapper.getSetmealById(setmealId);
                        shoppingCart.setName(setmeal.getName());
                        shoppingCart.setImage(setmeal.getImage());
                        shoppingCart.setAmount(setmeal.getPrice());
                    }
                //开始插入数据
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCart.setNumber(1);//初始数量为1
                shoppingCartMapper.insert(shoppingCart);
            }
        
        /**
         * 查询全部购物车
         *
         * @return {@link List }<{@link ShoppingCart }>
         */
        @Override
        public List<ShoppingCart> list()
            {
                return shoppingCartMapper.list(ShoppingCart.builder().userId(BaseContext.getCurrentId()).build());
            }
        
        /**
         * 删除购物车中一个商品
         *
         * @param shoppingCartDTO 购物车 DTO
         */
        @Override
        public void sub(ShoppingCartDTO shoppingCartDTO)
            {
                ShoppingCart shoppingCart = new ShoppingCart();
                BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
                //设置当前操作用户的id
                shoppingCart.setUserId(BaseContext.getCurrentId());
                
                //购物车中商品必定存在，且只有一条数据，取出该数据
                List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
                ShoppingCart cart = shoppingCarts.get(0);
                
                //商品数量减一，若为商品数量为1，直接删除数据
                if (cart.getNumber() == 1)
                    {
                        List<Long> ids = new ArrayList<>();
                        ids.add(cart.getId());
                        shoppingCartMapper.delete(ids);
                    }
                else
                    {
                        //商品数量大于一，修改数量
                        cart.setNumber(cart.getNumber() - 1);
                        shoppingCartMapper.updateNumberById(cart);
                    }
            }
    }
