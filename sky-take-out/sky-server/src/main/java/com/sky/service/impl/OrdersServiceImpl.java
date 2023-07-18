package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.BaseException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrdersService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private UserShoppingCartMapper userShoppingCartMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrdersDetailMapper ordersDetailMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;



    /**
     * 用户下单
     * @param submitDTO
     * @return
     */
    @Transactional
    @Override
    public OrderSubmitVO insert(OrdersSubmitDTO submitDTO) {
        //查询判断用户下单是否有地址
        AddressBook addressBook = addressBookMapper.getById(submitDTO.getAddressBookId());
        if (addressBook == null){
            throw new BaseException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //查询判断用户下单购物车是否有菜品
        List<ShoppingCart> shoppingCartList = userShoppingCartMapper.selectUserById(BaseContext.getCurrentId());
        if (CollectionUtils.isEmpty(shoppingCartList)){
            throw new BaseException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //补全订单字段
        Orders orders = new Orders();
        BeanUtils.copyProperties(submitDTO,orders);

        //订单号
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        //订单状态
        orders.setStatus(Orders.PENDING_PAYMENT);
        //下单用户
        orders.setUserId(BaseContext.getCurrentId());
        //下单时间
        orders.setOrderTime(LocalDateTime.now());
        //支付状态
        orders.setPayStatus(Orders.UN_PAID);
        //手机号
        orders.setPhone(addressBook.getPhone());
        //详细地址
        orders.setAddress(addressBook.getDetail());
        //收货人
        orders.setConsignee(addressBook.getConsignee());


        //插入数据到订单表中
        ordersMapper.insert(orders);

        //插入订单明细相关信息
        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (ShoppingCart cart : shoppingCartList) {
            //订单明细表就是购物车表，将购物车中的菜品依次遍历添加到订单明细表中
            OrderDetail orderDetail = new OrderDetail();
            //拷贝的购物车里没有订单id，需要自己添加
            orderDetail.setOrderId(orders.getId());
            BeanUtils.copyProperties(cart,orderDetail);
            //将拷贝的数据添加到集合中
            orderDetailList.add(orderDetail);
        }
        log.info("查看订单详细表：{}", orderDetailList.get(0).getOrderId());
        //再将数据插入到订单明细表
        ordersDetailMapper.insert(orderDetailList);

        //清空购物车
        userShoppingCartMapper.delete(BaseContext.getCurrentId());

        //补全返回值相关信息
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderAmount(orders.getAmount())
                .orderNumber(orders.getNumber())
                .orderTime(orders.getOrderTime())
                .build();

        return orderSubmitVO;
    }



    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = ordersMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);
    }

    /**
     * 查询历史订单
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult selectPageOrders(OrdersPageQueryDTO pageQueryDTO) {
        //获取页码和总记录数
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());
        //获取用户id
        Long userId = BaseContext.getCurrentId();

        pageQueryDTO.setUserId(userId);
        //分页查询订单
        Page<Orders> page =  ordersMapper.select(pageQueryDTO);
        //创建集合存储拼接的对象
        List<OrderVO> voList = new ArrayList<>();
        //遍历集合根据订单查询订单明细
        if (page != null && page.getTotal() > 0){
            for (Orders orders : page) {
                //根据订单id查询菜品菜品明细
                List<OrderDetail> orderVO = ordersDetailMapper.selectPageOrders(orders.getId());
                OrderVO orderVO1 = new OrderVO();
                BeanUtils.copyProperties(orders,orderVO1);
                orderVO1.setOrderDetailList(orderVO);
                voList.add(orderVO1);
            }

        }
        return new PageResult(page.getTotal(),voList);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @Override
    public OrderVO selectById(Long id) {
        OrderVO orderVO = ordersMapper.selectById(id);
        List<OrderDetail> orderDetailList = ordersDetailMapper.selectPageOrders(orderVO.getId());
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 取消订单
     * @param id
     */
    @Override
    public void update(Long id) {
        Integer status = Orders.CANCELLED;
        ordersMapper.updateById(status, id);

    }
}
