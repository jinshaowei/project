package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.adminservice.StatisticsService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@Api(tags = "营业额统计接口")
@RestController
@Slf4j
@RequestMapping("/admin/report")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @ApiOperation("营业额统计")
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin ,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("统计时间为：{} - {} 的营业额", begin, end);
        TurnoverReportVO turnoverReportVO = statisticsService.statistics(begin,end);
        return Result.success(turnoverReportVO);
    }


    @ApiOperation("用户统计")
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin ,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("统计时间为：{} - {} 的用户", begin, end);
        UserReportVO userReportVO = statisticsService.userStatistics(begin,end);
        return Result.success(userReportVO);
    }



}
