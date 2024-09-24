package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        @Autowired
        private UserMapper userMapper;
        @Autowired
        private WeChatPayUtil weChatPayUtil;
        
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
        
        /**
         * 订单支付
         *
         * @param ordersPaymentDTO 订单支付 DTO
         * @return {@link OrderPaymentVO }
         * @throws Exception 例外
         */
        public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception
            {
                // 当前登录用户id
                Long userId = BaseContext.getCurrentId();
                //TODO Encounter 2024/09/24 20:59 当前登录用户id，新增方法，无法验证是否正确
                User user = userMapper.getById(userId);
                
                //调用微信支付接口，生成预支付交易单
                JSONObject jsonObject = weChatPayUtil.pay(
                        ordersPaymentDTO.getOrderNumber(), //商户订单号
                        new BigDecimal(0.01), //支付金额，单位 元
                        "苍穹外卖订单", //商品描述
                        user.getOpenid() //微信用户的openid
                );
                
                if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID"))
                    {
                        throw new OrderBusinessException("该订单已支付");
                    }
                
                OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
                vo.setPackageStr(jsonObject.getString("package"));
                
                return vo;
            }
        
        /**
         * 支付成功，修改订单状态
         *
         * @param outTradeNo 交易编号
         */
        public void paySuccess(String outTradeNo)
            {
                // 当前登录用户id
                Long userId = BaseContext.getCurrentId();
                
                // 根据订单号查询当前用户的订单
                Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);
                
                // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
                Orders orders = Orders.builder()
                        .id(ordersDB.getId())
                        .status(Orders.TO_BE_CONFIRMED)
                        .payStatus(Orders.PAID)
                        .checkoutTime(LocalDateTime.now())
                        .build();
                
                orderMapper.update(orders);
            }
    }
