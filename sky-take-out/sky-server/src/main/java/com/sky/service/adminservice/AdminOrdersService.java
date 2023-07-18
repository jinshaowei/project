package com.sky.service.adminservice;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderOverViewVO;
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
    OrderOverViewVO countStatus();


}
