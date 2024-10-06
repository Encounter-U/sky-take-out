package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Encounter
 * @date 2024/09/24 17:30<br/>
 */
@Service
@Slf4j
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
        @Autowired
        private WebSocketServer webSocketServer;
        
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
                User user = userMapper.getById(userId);
                
                //调用微信支付接口，生成预支付交易单
                JSONObject jsonObject = weChatPayUtil.pay(
                        ordersPaymentDTO.getOrderNumber(), //商户订单号
                        new BigDecimal("0.01"), //支付金额，单位 元
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
                
                //通过websocket向客户端浏览器推送消息 type orderId content
                Map<Object, Object> map = new HashMap<>();
                map.put("type", 1); // 1表示来单提醒  2表示客户催单
                map.put("orderId", orders.getId()); //订单id
                map.put("content", "订单号：" + outTradeNo);
                
                String jsonString = JSON.toJSONString(map);
                webSocketServer.sendToAllClient(jsonString);
            }
        
        /**
         * 客户催单
         *
         * @param id 催单的订单号
         */
        @Override
        public void reminder(Long id)
            {
                //根据id查询订单
                Orders ordersDB = orderMapper.getById(id);
                
                //校验订单是否存在
                if (ordersDB == null)
                    throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
                
                Map<Object, Object> map = new HashMap<>();
                map.put("type", 2);
                map.put("orderId", ordersDB.getId());
                String jsonString = JSON.toJSONString(map);
                
                //通过websocket向客户端浏览器推送消息
                webSocketServer.sendToAllClient(jsonString);
            }
        
        /**
         * 动态订单页面查询
         *
         * @param ordersPageQueryDTO 订单页面查询 DTO
         * @return {@link PageResult }
         */
        @Override
        public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO)
            {
                //设置分页参数，开始分页
                PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
                
                //根据条件查询出符合的订单
                Page<Orders> ordersPage = orderMapper.pageQuery(ordersPageQueryDTO);
                List<Orders> ordersList = ordersPage.getResult();
                
                //将查询出的结果转换为ordersVO
                List<OrderVO> orderVOList = new ArrayList<>();
                if (!ordersList.isEmpty())
                    {
                        for (Orders orders : ordersList)
                            {
                                OrderVO ordersVO = new OrderVO();
                                ordersVO.setOrderDishes(dishNames(orders));
                                BeanUtils.copyProperties(orders, ordersVO);
                                log.info("ordersVO:{}", ordersVO);
                                orderVOList.add(ordersVO);
                            }
                    }
                
                //log.info("orderList:{}",orderVOList);
                
                //将最终结果转换
                return new PageResult(ordersPage.getTotal(), orderVOList);
            }
        
        /**
         * 各个状态的订单数量统计
         *
         * @return {@link OrderStatisticsVO }
         */
        @Override
        public OrderStatisticsVO statistics()
            {
                // 根据状态，分别查询出待接单、待派送、派送中的订单数量
                Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
                Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
                Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);
                
                // 将查询出的数据封装到orderStatisticsVO中响应
                OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
                orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
                orderStatisticsVO.setConfirmed(confirmed);
                orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
                return orderStatisticsVO;
            }
        
        /**
         * 查询订单
         *
         * @param id 订单id
         * @return {@link OrderVO }
         */
        @Override
        public OrderVO details(Long id)
            {
                //查询订单详情
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);
                //拼接菜品信息
                String dishNames = dishNames(orderMapper.getById(id));
                
                return new OrderVO(dishNames, orderDetails);
            }
        
        /**
         * 订单确认
         *
         * @param ordersConfirmDTO 订单确认 DTO
         */
        @Override
        public void confirm(OrdersConfirmDTO ordersConfirmDTO)
            {
                //修改订单
                Orders orders = Orders.builder()
                        .id(ordersConfirmDTO.getId())
                        .status(ordersConfirmDTO.getStatus())
                        .build();
                orderMapper.update(orders);
            }
        
        /**
         * 拒单
         *
         * @param ordersRejectionDTO 订单拒绝 DTO
         */
        @Override
        public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception
            {
                //根据id查询订单
                Orders orders = orderMapper.getById(ordersRejectionDTO.getId());
                
                //只有订单状态为待接单时才可以拒单 status==2
                if (orders == null || !orders.getStatus().equals(Orders.TO_BE_CONFIRMED))
                    throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
                
                //支付状态
                //TODO Encounter 2024/10/06 14:57 拒单时退款，因支付未实现，该功能也不支持
                Integer payStatus = orders.getPayStatus();
                if (Objects.equals(payStatus, Orders.PAID))
                    {
                        //用户已支付，需要退款
                        String refund = weChatPayUtil.refund(
                                orders.getNumber(),
                                orders.getNumber(),
                                new BigDecimal("0.01"),
                                new BigDecimal("0.01"));
                        log.info("申请退款：{}", refund);
                    }
                
                //取消订单
                Orders order = Orders.builder()
                        .id(ordersRejectionDTO.getId())
                        .status(Orders.CANCELLED)
                        .rejectionReason(ordersRejectionDTO.getRejectionReason())
                        .cancelTime(LocalDateTime.now())
                        .build();
                
                //更新订单状态
                orderMapper.update(order);
            }
        
        /**
         * 取消订单
         *
         * @param ordersCancelDTO 订单取消 DTO
         * @throws Exception 例外
         */
        @Override
        public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception
            {
                //根据id查询订单
                Orders orders = orderMapper.getById(ordersCancelDTO.getId());
                
                //只有订单状态为待接单时才可以取消订单 status==6
                if (orders == null || !Objects.equals(orders.getStatus(), Orders.CANCELLED))
                    throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
                
                //支付状态
                //TODO Encounter 2024/10/06 14:57 拒单时退款，因支付未实现，该功能也不支持
                Integer payStatus = orders.getPayStatus();
                if (Objects.equals(payStatus, Orders.PAID))
                    {
                        //用户已支付，需要退款
                        String refund = weChatPayUtil.refund(
                                orders.getNumber(),
                                orders.getNumber(),
                                new BigDecimal("0.01"),
                                new BigDecimal("0.01"));
                        log.info("申请退款：{}", refund);
                    }
                
                //取消订单
                Orders order = Orders.builder()
                        .id(ordersCancelDTO.getId())
                        .status(Orders.CANCELLED)
                        .rejectionReason(ordersCancelDTO.getCancelReason())
                        .cancelTime(LocalDateTime.now())
                        .build();
                
                //更新订单状态
                orderMapper.update(order);
            }
        
        /**
         * 订单派送
         *
         * @param id 订单id
         */
        @Override
        public void delivery(Long id)
            {
                // 根据id查询订单
                Orders orders = orderMapper.getById(id);
                
                // 订单不为null且状态为3
                if (orders == null || !orders.getStatus().equals(Orders.CONFIRMED))
                    throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
                
                Orders ordersUpdate = new Orders();
                ordersUpdate.setId(orders.getId());
                // 更新订单状态,状态转为派送中
                ordersUpdate.setStatus(Orders.DELIVERY_IN_PROGRESS);
                
                orderMapper.update(ordersUpdate);
            }
        
        /**
         * 订单完成
         *
         * @param id 订单id
         */
        @Override
        public void complete(Long id)
            {
                // 根据id查询订单
                Orders orders = orderMapper.getById(id);
                
                // 订单不为null且状态为4
                if (orders == null || !orders.getStatus().equals(Orders.DELIVERY_IN_PROGRESS))
                    {
                        throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
                    }
                
                Orders ordersUpdate = new Orders();
                ordersUpdate.setId(orders.getId());
                // 更新订单状态,状态转为完成
                ordersUpdate.setStatus(Orders.COMPLETED);
                ordersUpdate.setDeliveryTime(LocalDateTime.now());
                
                orderMapper.update(ordersUpdate);
            }
        
        /**
         * 拼接菜品名称
         *
         * @param orders 订单
         * @return {@link String }
         */
        private String dishNames(Orders orders)
            {
                //根据订单id查询菜品信息
                StringBuilder dishNames = new StringBuilder();
                
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orders.getId());
                for (OrderDetail orderDetail : orderDetails)
                    {
                        dishNames.append(orderDetail.getName()).append("*").append(orderDetail.getNumber()).append(";");
                    }
                
                return dishNames.toString();
            }
        
    }
