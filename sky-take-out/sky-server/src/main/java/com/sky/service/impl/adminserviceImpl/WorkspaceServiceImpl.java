package com.sky.service.impl.adminserviceImpl;

import com.sky.entity.Orders;
import com.sky.mapper.adminmapper.SetMealMapper;
import com.sky.mapper.appmapper.OrdersMapper;
import com.sky.mapper.appmapper.UserMapper;
import com.sky.service.adminservice.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 查询今日运营数据
     *
     * @return
     */
    @Override
    public BusinessDataVO selectOperationalData() {
        BusinessDataVO businessDataVO = new BusinessDataVO();
        //获取当前时间的起始和结束时间
        LocalDate localDate = LocalDate.now();
        LocalDateTime begin = LocalDateTime.of(localDate, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(localDate, LocalTime.MAX);
        //今日新增用户数
        String user = ordersMapper.selectCountUser(begin, end);

        //获取每日订单总数
        String countUser = ordersMapper.selectOrderAll(begin, end);
        //获取每日有效订单数
        String doneOrder = ordersMapper.selectOrdersStatistics(Orders.COMPLETED, begin, end);

        //今日订单完成率
        double doneOrders = (Integer.parseInt(doneOrder) * 1.0) / (Integer.parseInt(countUser) * 1.0);

        //营业额
        String turnover = ordersMapper.selectLocalDate(Orders.COMPLETED, begin, end);

        //平均客单价 （金额 / 有效订单数）
        if (turnover != null){
            double DTurnover = Double.parseDouble(turnover);
            int IDoneOrder = Integer.parseInt(doneOrder);
            Double unitPrice = DTurnover / IDoneOrder;
            //营业额
            businessDataVO.setTurnover(Double.parseDouble(turnover));
            //平均客单价
            businessDataVO.setUnitPrice(unitPrice);
        }else {
            businessDataVO.setUnitPrice(0.0);
            businessDataVO.setTurnover(0.0);
        }

        //有效订单数
        businessDataVO.setValidOrderCount(Integer.parseInt(doneOrder));
        //订单完成率
        businessDataVO.setOrderCompletionRate(doneOrders);
        //新增用户数
        businessDataVO.setNewUsers(Integer.parseInt(user));

        return businessDataVO;
    }

    /**
     * 查询套装总览
     * @return
     */
    @Override
    public SetmealOverViewVO selectSetmeals() {
        //查询统计起售套餐数量
        Integer countOnSale = setMealMapper.selectStatus(Orders.PAID);
        //查询统计停售套餐数量
        Integer countStatus = setMealMapper.selectStatus(Orders.UN_PAID);
        return new SetmealOverViewVO(countOnSale,countStatus);
    }


}
