package com.sky.service.impl.adminserviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.mapper.appmapper.OrdersMapper;
import com.sky.result.PageResult;
import com.sky.service.adminservice.AdminOrdersService;
import com.sky.vo.OrderStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


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
    public OrderStatisticsVO countStatus() {
        //查询全部订单
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        Page<Orders> select = ordersMapper.select(ordersPageQueryDTO);
        //获取到的状态封装到实体类中
        OrderStatisticsVO overViewVO = new OrderStatisticsVO();
        Integer toBeConfirmed = 0;
        Integer confirmed = 0;
        Integer deliveryInProgress = 0;

        //遍历每一个订单
        for (Orders orders : select) {
            if (orders.getStatus() == Orders.REFUND){
                toBeConfirmed++;
            }else if (orders.getStatus() == Orders.CONFIRMED){
                confirmed++;
            }else if (orders.getStatus() == Orders.DELIVERY_IN_PROGRESS) {
                deliveryInProgress++;
            }
        }
        //待接单
        overViewVO.setToBeConfirmed(toBeConfirmed);
        //待派送
        overViewVO.setConfirmed(confirmed);
        //派送中
        overViewVO.setDeliveryInProgress(deliveryInProgress);

        return overViewVO;
    }

    /**
     * 接单
     * @param ordersConfirmDTO
     */
    @Override
    public void updateConfirm(OrdersConfirmDTO ordersConfirmDTO) {
        //补全订单属性
        Orders orders = new Orders();
        //订单id
        orders.setId(ordersConfirmDTO.getId());
        //订单状态
        orders.setStatus(Orders.CONFIRMED);
        ordersMapper.update(orders);
    }

    /**
     * 拒单
     * @param ordersRejectionDTO
     */
    @Override
    public void updateRejection(OrdersRejectionDTO ordersRejectionDTO) {
        //补全订单信息
        Orders orders = new Orders();
        //订单id
        orders.setId(ordersRejectionDTO.getId());
        //订单状态
        orders.setStatus(Orders.CANCELLED);
        //取消时间
        orders.setCancelTime(LocalDateTime.now());
        //拒单原因
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        ordersMapper.update(orders);
    }

    /**
     * 派送订单
     * @param id
     */
    @Override
    public void updateById(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        //修改订单状态
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        ordersMapper.update(orders);
    }

    /**
     * 取消订单
     * @param ordersCancelDTO
     */
    @Override
    public void updateCancel(OrdersCancelDTO ordersCancelDTO) {
            //补全属性
            Orders orders = new Orders();
            //订单id
            orders.setId(ordersCancelDTO.getId());
            //修改状态
            orders.setStatus(Orders.CANCELLED);
            //取消时间
            orders.setCancelTime(LocalDateTime.now());
            //取消订单原因
            orders.setCancelReason(ordersCancelDTO.getCancelReason());

            ordersMapper.update(orders);

    }

    /**
     * 完成订单
     * @param id
     */
    @Override
    public void updateCompletById(Long id) {
        //补全属性
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());
        ordersMapper.update(orders);
    }

}
