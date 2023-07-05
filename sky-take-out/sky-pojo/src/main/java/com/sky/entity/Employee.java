package com.sky.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")   //加上注解 可以将 View 传过来的 String类型转换为 Date 类型
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")   //加上注解 可以将 View 传过来的 String类型转换为 Date 类型
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
