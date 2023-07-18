package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.appservice.UserOrdersService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController("UserOrdersController")
@Api(tags = "C端-订单接口")
@RequestMapping("user/order")
public class OrdersController {

    @Autowired
    private UserOrdersService userOrdersService;

    @ApiOperation("用户下单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> insertOrders(@RequestBody OrdersSubmitDTO submitDTO){
        log.info("用户下单中...");
        OrderSubmitVO orderSubmitVO = userOrdersService.insert(submitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = userOrdersService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }


    @ApiOperation("查询历史订单")
    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO pageQueryDTO){
        log.info("查询历史订单");
        PageResult orderVOList = userOrdersService.selectPageOrders(pageQueryDTO);
        return Result.success(orderVOList);
    }

    @ApiOperation("根据id查询订单详情")
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> selectById(@PathVariable Long id){
        log.info("根据订单id查询订单详情，id为：{}", id);
        OrderVO orderVOList = userOrdersService.selectById(id);
        return Result.success(orderVOList);
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel/{id}")
    public Result update(@PathVariable Long id){
        log.info("取消订单id为：{}", id);
        userOrdersService.update(id);
        return Result.success();
    }


    @ApiOperation("再来一单")
    @PostMapping("/repetition/{id}")
    public Result inserts(@PathVariable Long id){
        log.info("根据订单id再来一单：{}", id);
        userOrdersService.inserts(id);
        return Result.success();
    }

}
