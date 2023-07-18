package com.sky.mapper.appmapper;

import com.sky.entity.OrderDetail;
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
