package com.sky.service.adminservice;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.OrderStatisticsVO;
import org.springframework.stereotype.Service;



@Service
public interface AdminOrdersService {

    /**
     * 管理端订单搜索
     * @param pageQueryDTO
     * @return
     */
    PageResult selectConditionSearch(OrdersPageQueryDTO pageQueryDTO);

    /**
     * 订单状态统计
     * @return
     */
    OrderStatisticsVO countStatus();


    /**
     * 接单
     * @param ordersConfirmDTO
     */
    void updateConfirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     * @param ordersRejectionDTO
     */
    void updateRejection(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 派送订单
     * @param id
     */
    void updateById(Long id);

    /**
     * 取消订单
     * @param ordersCancelDTO
     */
    void updateCancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 完成订单
     * @param id
     */
    void updateCompletById(Long id);
}
