package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.adminservice.AdminOrdersService;
import com.sky.service.appservice.UserOrdersService;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        OrderOverViewVO overViewVO = adminOrdersService.countStatus();
        return Result.success(overViewVO);
    }

    @ApiOperation("查询订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderVO> DetailsById(@PathVariable Long id){
        log.info("查询订单详情");
        OrderVO orderVO = userOrdersService.selectById(id);
        return Result.success(orderVO);
    }


}
