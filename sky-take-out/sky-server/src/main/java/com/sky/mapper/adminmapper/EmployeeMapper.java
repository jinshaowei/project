package com.sky.mapper.adminmapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    /**
    *    新增员工
    * */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user) VALUES" +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void getStaff(Employee employee1);

    /**
     *
     * 分页查询
     * */
    List<Employee> list(String name);


    /**
     * 修该账号状态 启用- 禁用
     * */
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);


    /**
     * 根据id查询员工信息
     * */
    @Select("select * from employee where id = #{id}")
    Employee select(Integer id);
}
