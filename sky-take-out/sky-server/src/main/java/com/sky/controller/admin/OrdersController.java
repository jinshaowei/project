package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
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
import org.springframework.web.bind.annotation.*;

@Api(tags = "订单管理接口")
@RestController("AdminOrdersController")
@RequestMapping("/admin/order")
@Slf4j
public class OrdersController {
    @Autowired
    private AdminOrdersService adminOrdersService;

    @Autowired
    private UserOrdersService userOrdersService;

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

}
