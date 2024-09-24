package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Encounter
 * @date 2024/09/24 17:30<br/>
 */
@Service
public class OrderServiceImpl implements OrderService
    {
        @Autowired
        private OrderMapper orderMapper;
        @Autowired
        private OrderDetailMapper orderDetailMapper;
        @Autowired
        private AddressBookMapper addressBookMapper;
        @Autowired
        private ShoppingCartMapper shoppingCartMapper;
        
        /**
         * 提交订单
         *
         * @param ordersSubmitDTO 订单提交 DTO
         * @return {@link OrderSubmitVO }
         */
        @Transactional
        @Override
        public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO)
            {
                //处理各种业务异常（地址簿为空，购物车数量为空）
                AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
                if (addressBook == null)
                    throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
                //查询当前购物车数据
                ShoppingCart shoppingCart = new ShoppingCart();
                Long userId = BaseContext.getCurrentId();
                shoppingCart.setUserId(userId);
                List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
                if (shoppingCarts == null || shoppingCarts.isEmpty())
                    throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
                
                //向订单表插入一条数据
                Orders orders = new Orders();
                BeanUtils.copyProperties(ordersSubmitDTO, orders);
                orders.setOrderTime(LocalDateTime.now());//下单时间
                orders.setPayStatus(Orders.UN_PAID);//订单支付状态  未支付
                orders.setStatus(Orders.PENDING_PAYMENT);//订单状态  未付款
                orders.setNumber(String.valueOf(System.currentTimeMillis()));//用时间戳当作订单号
                orders.setPhone(addressBook.getPhone());//从地址簿获取手机号
                orders.setConsignee(addressBook.getConsignee());//收货人
                orders.setUserId(userId);
                
                orderMapper.insert(orders);
                
                //向订单表插入n条数据
                List<OrderDetail> orderDetailList = new ArrayList<>();
                for (ShoppingCart cart : shoppingCarts)
                    {
                        OrderDetail orderDetail = new OrderDetail();//订单明细
                        BeanUtils.copyProperties(cart, orderDetail);
                        orderDetail.setOrderId(orders.getId());//设置当前订单明细关联的订单id
                        orderDetailList.add(orderDetail);
                    }
                
                orderDetailMapper.insertBatch(orderDetailList);
                
                //清空当前用户购物车
                shoppingCartMapper.deleteByUserId(userId);
                
                //封装VO返回结果
                return OrderSubmitVO.builder()
                        .id(orders.getId())//订单id
                        .orderTime(orders.getOrderTime())//下单时间
                        .orderNumber(orders.getNumber())//订单编号
                        .orderAmount(orders.getAmount())//订单金额
                        .build();
            }
    }
