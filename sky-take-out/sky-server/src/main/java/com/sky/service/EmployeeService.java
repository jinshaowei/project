package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
    * */
    void getStaff(EmployeeDTO employee);


    /**
     *
     * 分页查询
     * */

    PageResult page(EmployeePageQueryDTO pageQueryDTO);


    /**
     * 启用或禁用员工
     * */
    void enableOrDisable(Integer status, Long id);


    /**
     *
     * 根据ID查询员工
     * */
    Employee select(Integer id);

    /**
     * 修该员工信息
     * */
    void update(Employee employee);
}
