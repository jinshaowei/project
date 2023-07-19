package com.sky.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.exception.BaseException;
import com.sky.mapper.appmapper.OrdersMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.appservice.UserOrdersService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController("UserOrdersController")
@Api(tags = "C端-订单接口")
@RequestMapping("user/order")
public class OrdersController {


    @Autowired
    private UserOrdersService userOrdersService;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @ApiOperation("用户下单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> insertOrders(@RequestBody OrdersSubmitDTO submitDTO) throws Exception {
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
//        OrderPaymentVO orderPaymentVO = userOrdersService.payment(ordersPaymentDTO);
//        log.info("生成预支付交易单：{}", orderPaymentVO);

        Long oderId = ordersMapper.selectNumber(ordersPaymentDTO.getOrderNumber());

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(oderId)
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);

        //支付成功推送消息给管理端

        //发送的消息封装到Map中
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("type",1);
        paramMap.put("orderId",orders.getId());
        paramMap.put("content", "订单号："+ ordersPaymentDTO.getOrderNumber());
        log.info("向管理端发送消息：{}", paramMap);
        //调用websocket里的方法发送消息给管理端（与前端约定好格式）
        webSocketServer.sendMessageToAllClient(JSONObject.toJSONString(paramMap));

        return Result.success();
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


    static Long ida = 0L;

    @ApiOperation("催单")
    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable Long id) throws Exception {
        //判断如果为同一份订单将不会催单
        if (id != ida ) {
            log.info("催单的订单id为：{}", id);
            //与前端约定的好的内容
            OrderVO orderVO = ordersMapper.selectById(id);

            //将要发送的内容存储到map集合中
            Map<String, Object> message = new HashMap<>();
            message.put("type", 2);
            message.put("orderId", id);
            message.put("content", "订单号：" + orderVO.getNumber());
            //再将集合转换成JSON格式发送给前端
            webSocketServer.sendMessageToAllClient(JSONObject.toJSONString(message));
            ida = id;
        }else {
            throw new BaseException("亲，已经在加急做了哦");
        }
        return Result.success();
    }

}
