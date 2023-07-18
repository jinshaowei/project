package com.sky.service.impl.adminserviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.mapper.appmapper.OrdersMapper;
import com.sky.result.PageResult;
import com.sky.service.adminservice.AdminOrdersService;
import com.sky.vo.OrderOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AdminOrdersServiceImpl implements AdminOrdersService {


    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 订单搜索
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult selectConditionSearch(OrdersPageQueryDTO pageQueryDTO) {
        //分页插件
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());
        //分页查询订单
        Page<Orders> page = ordersMapper.select(pageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 订单状态统计
     * @return
     */
    @Override
    public OrderOverViewVO countStatus() {
        //查询全部订单
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        Page<Orders> select = ordersMapper.select(ordersPageQueryDTO);
        //获取到的状态封装到实体类中
        OrderOverViewVO overViewVO = new OrderOverViewVO();
        Integer WaitingOrders = 0;
        Integer DeliveredOrders = 0;
        Integer CompletedOrders = 0;
        Integer CancelledOrders = 0;
        Integer AllOrders = 0;
        //遍历每一个订单
        for (Orders orders : select) {
            if (orders.getStatus() == Orders.REFUND){
                WaitingOrders++;
            }else if (orders.getStatus() == Orders.DELIVERY_IN_PROGRESS){
                DeliveredOrders++;
            }else if (orders.getStatus() == Orders.COMPLETED){
                CompletedOrders++;
            }else if (orders.getStatus() == Orders.CANCELLED){
                CancelledOrders++;
            }
        }
        //待接单
        overViewVO.setWaitingOrders(WaitingOrders);
        //待派送
        overViewVO.setDeliveredOrders(DeliveredOrders);
        //已完成
        overViewVO.setCompletedOrders(CompletedOrders);
        //已取消
        overViewVO.setCancelledOrders(CancelledOrders);
        //全部订单
        overViewVO.setAllOrders(WaitingOrders + DeliveredOrders + CompletedOrders + CancelledOrders);
        return overViewVO;
    }






}
