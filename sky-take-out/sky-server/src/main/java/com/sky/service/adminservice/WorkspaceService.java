package com.sky.service.adminservice;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.stereotype.Service;

@Service
public interface WorkspaceService {

    /**
    查询今日运营数据
     */
    BusinessDataVO selectOperationalData();

    /**
     * 查询套装总览
     * @return
     */
    SetmealOverViewVO selectSetmeals();

    /**
     * 查询菜品套餐总览
     * @return
     */
    DishOverViewVO countDishStatus();
}
