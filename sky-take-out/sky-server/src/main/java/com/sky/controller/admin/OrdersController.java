package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.adminservice.AdminOrdersService;
import com.sky.service.appservice.UserOrdersService;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "订单管理接口")
@RestController("AdminOrdersController")
@RequestMapping("/admin/order")
@Slf4j
public class OrdersController {
    @Autowired
    private AdminOrdersService adminOrdersService;




    @GetMapping("/conditionSearch")
    public Result<PageResult> selectOrder(OrdersPageQueryDTO pageQueryDTO){
        log.info("订单搜索");
        PageResult pageResult = adminOrdersService.selectConditionSearch(pageQueryDTO);
        return Result.success(pageResult);
    }


}
