package com.sky.service.impl.adminserviceImpl;

import com.sky.entity.Orders;
import com.sky.mapper.appmapper.OrdersMapper;
import com.sky.service.adminservice.StatisticsService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class StatisticsServiceImpl implements StatisticsService {


    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO statistics(LocalDate begin, LocalDate end) {
        //begin.datesUntil(end)  获取范围内的每一天的日期（含头不含尾）

        //统计出指定时间内的每一天日期
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        //创建集合添加指定日期的时间
        List<LocalDate> localDates = new ArrayList<>();
        //添加第一天的时间
        localDates.add(begin);

        while (true) {
            //判断开始日期不在结束日期之后进行增加
            if (!begin.isAfter(end)) {
                //日期自增
                LocalDate localDate = begin.plusDays(1);
                localDates.add(localDate);
                //日期为最后一天时跳出循环
                if (localDate.isEqual(end)){
                    break;
                }
                begin = localDate;
            }
        }

        //将时间集合转换成String类型
        String localDate = StringUtils.join(localDates);

        //将时间封装到实体类中
        turnoverReportVO.setDateList(localDate);

        //统计指定日期内的每一天的营业额（注意已完成的订单金额）

        StringJoiner sj = new StringJoiner(",");
        //遍历指定时间内的日期
        for (LocalDate localDate1 : localDates) {
            //获取日期的开始时间
            LocalDateTime beginTime = LocalDateTime.of(localDate1, LocalTime.MIN);
            //获取日期的结束时间
            LocalDateTime endTime = LocalDateTime.of(localDate1, LocalTime.MAX);
            //查询日期时间范围内的已完成订单金额
            String countAmount = ordersMapper.selectLocalDate(Orders.COMPLETED, beginTime, endTime);
            //当天没有营业额则为0
            if (countAmount == null){
                countAmount = "0";
            }
            //拼接成字符串返回
            sj.add(countAmount);
        }

        //封装到实体类中
        turnoverReportVO.setTurnoverList(sj.toString());

        return turnoverReportVO;
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        //获取到指定时间内的每一天时间
        List<LocalDate> localDates = begin.datesUntil(end).collect(Collectors.toList());

        //含头不含尾
        localDates.add(end);

        //将日期转换成String
        String strLocalDate = StringUtils.join(localDates);
        System.out.println(strLocalDate);

        //拼接字符串
        StringJoiner sj = new StringJoiner(",");
        StringJoiner userAllId = new StringJoiner(",");

        //获取用户开始日期之前的总用户数量
        Integer strUser = ordersMapper.selectBeginTime(begin);

        //遍历时间集合
        for (LocalDate localDate : localDates) {
            //获取日期的开始时间
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            //获取日期的结束时间
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            //查询出当前日期用户总数
            String userId = ordersMapper.selectCountUser(beginTime, endTime);
            int countUser = Integer.parseInt(userId);
            userAllId.add((countUser + strUser) + "");
            sj.add(userId);

        }



        //获取到的数据封装到实体类中
        UserReportVO userReportVO = new UserReportVO();
        //日期
        userReportVO.setDateList(strLocalDate);
        //新增用户
        String number = sj.toString();
        System.out.println(number);
        userReportVO.setNewUserList(number);
        //用户总数
        String sum = userAllId.toString();
        System.out.println(sum);
        userReportVO.setTotalUserList(sum);

        return userReportVO;
    }


}
