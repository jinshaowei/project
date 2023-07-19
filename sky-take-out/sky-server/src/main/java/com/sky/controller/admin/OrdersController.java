package com.sky.controller.admin;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.mapper.appmapper.OrdersMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.adminservice.AdminOrdersService;
import com.sky.service.appservice.UserOrdersService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "订单管理接口")
@RestController("AdminOrdersController")
@RequestMapping("/admin/order")
@Slf4j
public class OrdersController {
    @Autowired
    private AdminOrdersService adminOrdersService;

    @Autowired
    private UserOrdersService userOrdersService;

    @Autowired
    private OrdersMapper ordersMapper;

    @ApiOperation("订单搜索")
    @GetMapping("/conditionSearch")
    public Result<PageResult> selectOrder(OrdersPageQueryDTO pageQueryDTO){
        log.info("订单搜索");
        PageResult pageResult = adminOrdersService.selectConditionSearch(pageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("订单状态数量统计")
    @GetMapping("/statistics")
    public Result OrdersStatistics(){
        log.info("订单状态数量统计");
        OrderStatisticsVO overViewVO = adminOrdersService.countStatus();
        return Result.success(overViewVO);
    }

    @ApiOperation("查询订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderVO> DetailsById(@PathVariable Long id){
        log.info("查询订单详情");
        OrderVO orderVO = userOrdersService.selectById(id);
        return Result.success(orderVO);
    }


    @ApiOperation("接单")
    @PutMapping("/confirm")
    public Result OrderConfirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单，订单id：{}", ordersConfirmDTO.getId());
        adminOrdersService.updateConfirm(ordersConfirmDTO);
        return Result.success();
    }

    @ApiOperation("拒单")
    @PutMapping("/rejection")
    public Result OrderRejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("拒单id：{}", ordersRejectionDTO.getId());
        adminOrdersService.updateRejection(ordersRejectionDTO);
        return Result.success();
    }

    @ApiOperation("派送订单")
    @PutMapping("/delivery/{id}")
    public Result OrderDelivery(@PathVariable Long id){
        log.info("派送订单id：{}", id);
        adminOrdersService.updateById(id);
        return Result.success();
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel")
    public Result OrderCancel(@RequestBody OrdersCancelDTO ordersCancelDTO){
        log.info("取消订单的id为：{}", ordersCancelDTO.getId());
        adminOrdersService.updateCancel(ordersCancelDTO);
        return Result.success();
    }

    @ApiOperation("完成订单")
    @PutMapping("/complete/{id}")
    public Result OrderComplete(@PathVariable Long id){
        log.info("完成订单，id为：{}", id);
        adminOrdersService.updateCompletById(id);
        return Result.success();
    }

    /**
     * 定时任务  将待支付超时的订单修改状态
     */
    //cron 表达式 （设置定时时间 秒 - 分 - 时 - 日 - 月 - 周 - （可选）年）
    @Scheduled(cron = "0/30 * * * * ?")
    public void cancelOrder(){
        //当前时间减15分钟
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(15);
        //查询出状态为待支付且下单时间小于15分钟前的订单
        List<Orders> ordersList = ordersMapper.selectCancelById(Orders.PAID,localDateTime);
        if (!CollectionUtils.isEmpty(ordersList)){
            for (Orders orders : ordersList) {
                //遍历订单修改订单的状态
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("订单超时，系统自动取消");
                ordersMapper.update(orders);
            }
        }
    }


    /**
     * 定时任务  将派送中超时的订单修改状态
     */
    //cron 表达式 （设置定时时间 秒 - 分 - 时 - 日 - 月 - 周 - （可选）年）
    @Scheduled(cron = "0 0 1 * * ?")
    public void cancelOrders(){
        //当前时间减2小时
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(2);
        //查询出状态为派送中且下单时间小于2小时前的订单
        List<Orders> ordersList = ordersMapper.selectCancelById(Orders.DELIVERY_IN_PROGRESS,localDateTime);
        if (!CollectionUtils.isEmpty(ordersList)){
            for (Orders orders : ordersList) {
                //遍历订单修改订单的状态
                orders.setStatus(Orders.COMPLETED);
                orders.setCancelTime(LocalDateTime.now());
                ordersMapper.update(orders);
            }
        }
    }

}
