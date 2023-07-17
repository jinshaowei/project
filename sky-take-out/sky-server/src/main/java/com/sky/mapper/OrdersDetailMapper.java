package com.sky.mapper;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrdersDetailMapper {

    /**
     * 插入订单明细表
     * @param orderDetailList
     */
    void insert(List<OrderDetail> orderDetailList);


    /**
     * 根据订单id查询订单明细表
     * @param id
     * @return
     */
    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> selectPageOrders(Long id);



}
