package com.sky.service.impl.adminserviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.mapper.appmapper.OrdersMapper;
import com.sky.result.PageResult;
import com.sky.service.adminservice.AdminOrdersService;
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
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());
        //分页查询订单
        Page<Orders> page = ordersMapper.select(pageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }
}
