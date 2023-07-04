package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);


        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //  后期需要进行md5加密，然后再进行比对
        //调用工具类对密码进行加密再做比较
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     */
    @Override
    public void getStaff(EmployeeDTO employee) {

        // 补全剩下的属性
        Employee employee1 = new Employee();

        employee1.setName(employee.getName());
        employee1.setUsername(employee.getUsername());
        employee1.setPhone(employee.getPhone());
        employee1.setSex(employee.getSex());
        employee1.setIdNumber(employee.getIdNumber());

        //调用工具类加密
        employee1.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设定用户账户状态
        employee1.setStatus(StatusConstant.ENABLE);
        //创建时间
        employee1.setCreateTime(LocalDateTime.now());
        //修改时间
        employee1.setUpdateTime(LocalDateTime.now());
        //当前线程登录的用户ID
        employee1.setCreateUser(BaseContext.getCurrentId());
        employee1.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.getStaff(employee1);
    }


    /**
     * 分页查询
     * */
    @Override
    public PageResult page(EmployeePageQueryDTO pageQueryDTO) {
        //获取分页的结果
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());

        //根据员工姓名查询的结果封装到List集合
        List<Employee> employeelist = employeeMapper.list(pageQueryDTO.getName());
        System.out.println(employeelist);

        //封装结果
        Page<Employee> page =(Page<Employee>) employeelist;

        //返回封装结果
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }


    /**
     * 启用 - 禁用 账号
     * */
    @Override
    public void enableOrDisable(Integer status, Long id) {
        //将修改的属性封装到Employee类中
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();

        employeeMapper.update(employee);

    }

    /**
     *
     * 根据员工id查询数据
     * */
    @Override
    public Employee select(Integer id) {
        Employee employee = employeeMapper.select(id);
        log.info("查询返回员工的信息：{}", employee);
        return employee;
    }

    /**
     * 修改员工信息
     * */
    @Override
    public void update(Employee employee) {
        //修改时间
        employee.setUpdateTime(LocalDateTime.now());
        //修改人id
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }

}
