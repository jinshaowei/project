package com.sky.service.adminservice;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface StatisticsService {

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO statistics(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);

    /**
     * 查询菜品销量排名
     * @param begin
     * @param end
     * @return
     */

    SalesTop10ReportVO selectTop(LocalDate begin, LocalDate end);
}
