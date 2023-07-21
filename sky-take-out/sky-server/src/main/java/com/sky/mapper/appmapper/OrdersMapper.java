package com.sky.mapper.appmapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper {

    /**
     * 用户下单
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号和用户id查询订单
     *
     * @param orderNumber
     * @param userId
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页查询订单
     *
     * @param pageQueryDTO
     * @return
     */
    Page<Orders> select(OrdersPageQueryDTO pageQueryDTO);

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    OrderVO selectById(Long id);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     * @return
     */
    @Select("select id from orders where number = #{orderNumber}")
    Long selectNumber(String orderNumber);

    /**
     * 定时任务
     *
     * @param paid
     * @param localDateTime
     * @return
     */
    @Select("select * from orders where status = #{paid} and order_time < #{localDateTime}")
    List<Orders> selectCancelById(Integer paid, LocalDateTime localDateTime);


    /**
     * 营业额统计
     *
     * @param completed
     * @param beginTime
     * @param endTime
     * @return
     */
    @Select("select sum(amount) from orders where status = #{completed} and order_time between #{beginTime} and #{endTime}")
    String selectLocalDate(Integer completed, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 指定日期范围用户总数
     *
     * @return
     */
    @Select("select count(id) from  user where create_time between #{beginTime} and #{endTime}")
    String selectCountUser(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 统计全部用户
     *
     * @return
     */
    @Select("select count(id) from user;")
    Integer CountAllUser();

    /**
     * 查询用户开始时间之前的总数量
     *
     * @param begin
     * @return
     */
    @Select("select count(id) from user where create_time < #{begin};")
    Integer selectBeginTime(LocalDate begin);

    /**
     * 统计订单总数
     *
     * @return
     */
    @Select("select count(id) from orders")
    Integer selectSumOrder();


    /**
     * 统计订单有效数
     *
     * @return
     */
    @Select("select count(id) from orders where status = #{completed}")
    Integer selectValidOrder(Integer completed);

    /**
     * 统计每日有效订单
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    @Select("select COUNT(id) from orders where order_time between #{beginTime} and #{endTime}")
    String selectOrdersStatistics(Integer completed, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 每日订单数
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    @Select("select COUNT(id) from orders where order_time between #{beginTime} and #{endTime}")
    String selectOrderAll(LocalDateTime beginTime, LocalDateTime endTime);


    /**
     * 查询指定时间的订单id
     * @param beginTime
     * @param endTime
     * @param completed
     * @return
     */
    @Select("select id from orders where order_time between #{beginTime} and #{endTime} and status = #{completed};")
    List<Integer> selectOrdersByIds(LocalDateTime beginTime, LocalDateTime endTime, Integer completed);

    /**
     * 根据订单id查询出指定时间内的菜品
     * @param orderId
     * @return
     */
    List<String> selectOrderDetail(List<Integer> orderId);

    /**
     * 根据订单id查询出指定时间内的菜品id
     * @param orderId
     * @return
     */
    List<String> selectOrderDetailId(List<Integer> orderId);
}
