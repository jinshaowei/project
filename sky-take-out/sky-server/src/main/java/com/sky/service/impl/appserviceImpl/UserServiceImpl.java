package com.sky.service.impl.appserviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.adminmapper.DishFlavorMapper;
import com.sky.mapper.appmapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.appservice.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String WEXIN_LOGIN_URL="https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 登录请求
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        //使用HttpClient发送请求到微信接口
        Map<String, String> paramMap = new HashMap<>();
        //小程序id
        paramMap.put("appid",weChatProperties.getAppid());
        //小程序密钥
        paramMap.put("secret",weChatProperties.getSecret());
        //小程序获取的code，需要发送到微信接口
        paramMap.put("js_code",userLoginDTO.getCode());
        //授权类型
        paramMap.put("grant_type","authorization_code");
        //得到一个字符串（包含openid）
        String result = HttpClientUtil.doGet(WEXIN_LOGIN_URL,paramMap);

        //非空判断
        if (!StringUtils.hasLength(result)){
            throw new ShoppingCartBusinessException(MessageConstant.LOGIN_FAILED);
        }

        log.info("微信小程序：{}",result);

        //将获取到的字符串转换成一个json格式
        JSONObject jsonObject = JSON.parseObject(result);

        //再通过键获取openid
        String openid = jsonObject.getString("openid");  //微信用户唯一标识

        //判断用户如果是第一次登录，则需要注册
        User user = userMapper.selectById(openid);
        if (user == null){
             user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
             userMapper.insert(user);
        }
        //返回结果
        return user;
    }

}
