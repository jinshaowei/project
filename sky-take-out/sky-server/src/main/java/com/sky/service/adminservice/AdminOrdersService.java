package com.sky.service.adminservice;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminOrdersService {

    /**
     * 管理端订单搜索
     * @param pageQueryDTO
     * @return
     */
    PageResult selectConditionSearch(OrdersPageQueryDTO pageQueryDTO);
}
