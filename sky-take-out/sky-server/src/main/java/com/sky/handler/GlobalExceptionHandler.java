package com.sky.handler;

import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        //获取控制台异常信息
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }


    /**
     * 捕获DuplicateKeyException，该异常会在数据字段（唯一约束）重复时抛出
     * */
    @ExceptionHandler
    public Result exceptionHandler(DuplicateKeyException ex){
        log.error("异常信息：{}", ex.getMessage());
        ex.printStackTrace();
        //输出异常信息
        String errorMessage = "";
        //获取异常中的详细信息
        String message = ex.getCause().getMessage();
        //判断异常信息是否为空
        if (StringUtils.hasLength(message)){
            //截取异常信息的第三个主要异常信息
            String[] msgs = message.split(" ");
            errorMessage = msgs[2];
        }
        //拼接异常信息
        return Result.error(errorMessage + "已存在");
    }


    /**
     * 捕获全部异常
     * */
    @ExceptionHandler
    public Result exceptionHandler(Exception ex){
        ex.printStackTrace();         //输出异常的堆栈信息
        log.error("异常信息：{}", ex.getMessage());
        return Result.error("未知异常");
    }



}
