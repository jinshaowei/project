package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.adminservice.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */

@Api(tags = "员工登录管理")
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation("员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
    *
    * 新增员工
    * */
    @ApiOperation("新增员工")
    @PostMapping
    public Result get(@RequestBody EmployeeDTO employee) {

        log.info("新增员工，{}", employee);
        employeeService.getStaff(employee);
        return Result.success();
    }

    /**
     * 分页查询
     * */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO pageQueryDTO){
        log.info("查询员工，{}",pageQueryDTO);
        PageResult pageResult = employeeService.page(pageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 启用 - 禁用 账号
     * */
    @ApiOperation("启用 - 禁用账号")
    @PostMapping("/status/{status}")
    //路径参数注解
    public Result enableOrDisable(@PathVariable Integer status, Long id){
        log.info("启用或禁用员工，{},{}",status,id);
        employeeService.enableOrDisable(status,id);
       return Result.success();
    }

    /**
     * 根据id查询员工信息
     * */
    @ApiOperation("根据员工ID查询")
    @GetMapping("/{id}")
    public Result<Employee> select(@PathVariable Integer id){
        log.info("根据id查询：{}", id);
        Employee employee = employeeService.select(id);
        return Result.success(employee);
    }

    /**
     * 修改员工信息
     * */
    @ApiOperation("修改员工")
    @PutMapping
    public Result update(@RequestBody Employee employee){
        log.info("修改员工信息：{}", employee);
        employeeService.update(employee);
        return Result.success();
    }


    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("退出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

}
