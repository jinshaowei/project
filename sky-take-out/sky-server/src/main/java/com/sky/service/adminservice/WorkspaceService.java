package com.sky.service.adminservice;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderReportVO;
import org.springframework.stereotype.Service;

@Service
public interface WorkspaceService {

    /*
    查询今日运营数据
     */
    BusinessDataVO selectOperationalData();
}
