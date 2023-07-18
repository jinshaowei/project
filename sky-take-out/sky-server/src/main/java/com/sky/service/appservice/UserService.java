package com.sky.service.appservice;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /**
     * 登录请求
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

}
