package com.sky.service.impl.adminserviceImpl;

import com.sky.entity.Orders;
import com.sky.mapper.appmapper.OrdersMapper;
import com.sky.service.adminservice.StatisticsService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


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
                if (localDate.isEqual(end)) {
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
            if (countAmount == null) {
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
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        //调用方法获取指定时间范围的每一天时间
        List<LocalDate> localDates = getLocalDates(begin, end);

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
            strUser = strUser + countUser;
            userAllId.add(strUser + "");
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

    /**
     * 统计订单
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        //封装到实体类
        OrderReportVO orderReportVO = new OrderReportVO();
        //指定时间的获取开始和结束时间
        LocalDateTime beginDate = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(end, LocalTime.MAX);

        //获取时间
        List<LocalDate> localDates = getLocalDates(begin, end);
        //转换时间
        String StrLocalDates = StringUtils.join(localDates);

        //每日订单数
        StringJoiner orders = new StringJoiner(",");
        //每日有效订单列表
        StringJoiner validOrders = new StringJoiner(",");
        for (LocalDate localDate : localDates) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            //每日订单
            String order = ordersMapper.selectOrderAll(beginTime, endTime);
            //每日有效订单
            String validOrder = ordersMapper.selectOrdersStatistics(Orders.COMPLETED, beginTime, endTime);
            //拼接每日有效订单
            validOrders.add(validOrder);
            //拼接每日订单
            orders.add(order);
        }

        //有效订单数
        Integer sumOrderId = ordersMapper.selectValidOrder(Orders.COMPLETED, beginDate, endDate);

        //订单总数
        Integer sumOrderIds = ordersMapper.selectSumOrder(beginDate, endDate);

        //获取订单完成率
        Double orderId = ((sumOrderId * 1.0) / (sumOrderIds * 1.0));
        //订单完成率
        orderReportVO.setOrderCompletionRate(orderId);

        //日期
        orderReportVO.setDateList(StrLocalDates);
        //每日订单数
        orderReportVO.setOrderCountList(orders.toString());
        //每日有效订单数
        orderReportVO.setValidOrderCountList(validOrders.toString());
        //订单总数
        orderReportVO.setTotalOrderCount(sumOrderIds);
        //有效订单数
        orderReportVO.setValidOrderCount(sumOrderId);

        return orderReportVO;
    }

    /**
     * 查询菜品销量排名
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO selectTop(LocalDate begin, LocalDate end) {
        //统计菜品名字
        StringJoiner dishName = new StringJoiner(",");
        //统计菜品数量
        StringJoiner dishIds = new StringJoiner(",");

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MAX);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MIN);
        //获取指定时间内的所有已完成订单id
        List<Integer> orderIds = ordersMapper.selectOrdersByIds(beginTime, endTime, Orders.COMPLETED);
        System.out.println(orderIds);

        //得到订单的全部名字（根据每天菜品id统计，降序）
        List<String> strings = ordersMapper.selectOrderDetail(orderIds);

        //菜品数据只统计10以内
        for (int i = 0; i < strings.size(); i++) {
            if (i > 10) {
                break;
            }
            dishName.add(strings.get(i));
        }

        //获得菜品对应数量
        List<String> dishNumber = ordersMapper.selectOrderDetailId(orderIds);
        for (int i = 0; i < dishNumber.size(); i++) {
            if (i > 10) {
                break;
            }
            dishIds.add(dishNumber.get(i));
        }

        //封装数据
        String dishNames = dishName.toString();

        String dishId = dishIds.toString();


        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();
        salesTop10ReportVO.setNameList(dishNames);
        salesTop10ReportVO.setNumberList(dishId);
        return salesTop10ReportVO;
    }

    /**
     * 导出运营数据报表
     */

    @Autowired
    private HttpServletResponse httpServletResponse;

    @SneakyThrows
    @Override
    public void deriveExcel() {
        //获取近一个月的起始时间和结束时间
        LocalDate localDate = LocalDate.now();
        //当前时间之前30天
        LocalDate begin = localDate.minusDays(30);
        //当前时间之前1天
        LocalDate end = localDate.minusDays(1);
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        String localDat = "当前时间: " + begin.toString() + " - " + end.toString();

        //查询报表数据

        //营业额
        String turnover = ordersMapper.selectLocalDate(Orders.COMPLETED, beginTime, endTime);
        //获取订单总数
        String countUser = ordersMapper.selectOrderAll(beginTime, endTime);
        //获取有效订单数
        String doneOrder = ordersMapper.selectOrdersStatistics(Orders.COMPLETED, beginTime, endTime);
        //今日订单完成率
        double doneOrders = (Integer.parseInt(doneOrder) * 1.0) / (Integer.parseInt(countUser) * 1.0);
        //今日新增用户数
        String user = ordersMapper.selectCountUser(beginTime, endTime);

        //平均客单价
        double DTurnover = Double.parseDouble(turnover);
        int IDoneOrder = Integer.parseInt(doneOrder);
        Double unitPrice = DTurnover / IDoneOrder;

        //加载报表Excel模板文件
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("formdocument/运营数据报表模板.xlsx");
        Workbook workbook = new XSSFWorkbook(inputStream);
        //获取表单
        Sheet sheet = workbook.getSheetAt(0);

        //定位单元格，填充数据 -- 数据概览
        //例如：填充表单第2行，第1列数据
        sheet.getRow(1).getCell(1).setCellValue(localDat);    //时间
        sheet.getRow(3).getCell(2).setCellValue(turnover);    //营业额
        sheet.getRow(3).getCell(4).setCellValue(doneOrders);   //订单完成率
        sheet.getRow(3).getCell(6).setCellValue(user);   //新增用户数
        sheet.getRow(4).getCell(2).setCellValue(doneOrder);   //获取有效订单数
        sheet.getRow(4).getCell(4).setCellValue(unitPrice);    //平均客单价

        //填充近一个月每一天的信息
        for (int i = 0; i < 30; i++) {
            //获取30天内每一天的时间
            LocalDate everDate = begin.plusDays(i);
            LocalDateTime everDateTime = LocalDateTime.of(everDate, LocalTime.MIN);
            LocalDateTime _everDateTime = LocalDateTime.of(everDate, LocalTime.MAX);

            //查询每一天的运营数据
            //营业额
            String _turnover = ordersMapper.selectLocalDate(Orders.COMPLETED, everDateTime, _everDateTime);
            //获取订单总数
            String _countUser = ordersMapper.selectOrderAll(everDateTime, _everDateTime);
            //获取有效订单数
            String _doneOrder = ordersMapper.selectOrdersStatistics(Orders.COMPLETED, everDateTime, _everDateTime);
            double _doneOrders;
            if (_doneOrder == null || _countUser == null) {
                _doneOrders = 0;
            } else {
                //今日订单完成率
                _doneOrders = (Integer.parseInt(_doneOrder) * 1.0) / (Integer.parseInt(_countUser) * 1.0);
            }

            //今日新增用户数
            String _user = ordersMapper.selectCountUser(everDateTime, _everDateTime);

            Row row = sheet.getRow(7 + i);
            //平均客单价
            if (_turnover != null) {
                double _DTurnover = Double.parseDouble(_turnover);
                int _IDoneOrder = Integer.parseInt(_doneOrder);
                Double aDouble = _DTurnover / _IDoneOrder;
                row.getCell(5).setCellValue(aDouble);   //平均客单价
                row.getCell(2).setCellValue(_turnover);   //营业额
            } else {
                row.getCell(5).setCellValue(0);   //平均客单价
                row.getCell(2).setCellValue(0);   //营业额
            }
            //填充每一天报表信息
            row.getCell(1).setCellValue(everDate.toString());   //日期
            row.getCell(3).setCellValue(_doneOrder);   //有效订单
            row.getCell(4).setCellValue(_doneOrders);   //订单完成率
            row.getCell(6).setCellValue(_user);   //新增用户数
        }

        //注入输出流
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        //下载文件 - 流
        workbook.write(outputStream);
    }


    //获取指定时间范围的每一天时间
    private List<LocalDate> getLocalDates(LocalDate begin, LocalDate end) {
        //获取到指定时间内的每一天时间
        List<LocalDate> localDates = begin.datesUntil(end).collect(Collectors.toList());
        localDates.add(end);

        return localDates;
    }


}
