package com.sky.service.appservice;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.stereotype.Service;

@Service
public interface UserOrdersService {

    /**
     * 用户下单
     * @param submitDTO
     * @return
     */
    OrderSubmitVO insert(OrdersSubmitDTO submitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 查询历史订单
     * @param pageQueryDTO
     * @return
     */
    PageResult selectPageOrders(OrdersPageQueryDTO pageQueryDTO);

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderVO selectById(Long id);

    /**
     * 取消订单
     * @param id
     */
    void update(Long id);

    /**
     * 再来一单
     * @param id
     */
    void inserts(Long id);



}
