package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {


    // 声明AliOssUtil 的bean对象
    // 可以将 AliOssUtil工具类 直接交给IOC容器管理，声明bean对象的好处是在包不同名下依然可以被扫描到
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
         return new AliOssUtil(aliOssProperties.getEndpoint(),
                 aliOssProperties.getAccessKeyId(),
                 aliOssProperties.getAccessKeySecret(),
                 aliOssProperties.getBucketName());
    }

}
