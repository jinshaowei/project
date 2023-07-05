package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 *        通过AOP切面类为公共字段赋值
 *
 * */

@Slf4j
@Aspect
@Component
public class AutoFillAspect {

    /**
     *   方法一： 通过方法签名，反射获取注解
     * */

    //将AutoFill注解只限制在mapper层增强
/*    @Before(" execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillProperty(JoinPoint joinPoint) throws Exception {


        log.info("进入AOP程序，为公共属性赋值");
        //通过原始方法运行时传入的参数，获取第一个(对象)
        //    void getStaff(Employee employee1);
        Object[] obj = joinPoint.getArgs();
        System.out.println(obj);

        //判断参数是否空
        if (ObjectUtils.isEmpty(obj)){
            return;
        }
        Object args = obj[0];
        System.out.println(args);

        //通过反射获取到对象对应方法
        Method setCreateTime = args.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
        log.info("获取方法：{}",setCreateTime);
        Method setCreateUser = args.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
        Method setUpdateTime = args.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method setUpdateUser = args.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

        //获取注解对应的value属性
        *//**getSignature()拿到方法的签名信息，getSignature本质上是MethodSignature，可以做强转成MethodSignature
         * 再调用方法getMethod拿到原始方法对象，再调用getAnnotation()通过反射拿到原始方法上的指定注解，并调用value方法获取
         * 注解的属性
         *
         *//*
        log.info("测试是否运行到了这里：{}",obj);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取原始方法上的注解
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        log.info("测试是否运行到了这里：{}",obj);
        //判断，如果是insert，为4个属性赋值
        if (operationType.equals(OperationType.INSERT)){
            setCreateTime.invoke(args,LocalDateTime.now());
            setCreateUser.invoke(args,BaseContext.getCurrentId());
        }
        *//**
        修改人的id和时间，每一次都需要执行
         *//*
        setUpdateTime.invoke(args, LocalDateTime.now());
        setUpdateUser.invoke(args, BaseContext.getCurrentId());

        log.info("赋值后obj属性：{}",obj);
*//*        //判断，如果是update，为2个属性赋值
        if (operationType.equals(OperationType.UPDATE)){
            setUpdateTime.invoke(LocalDateTime.now());
            setUpdateUser.invoke(BaseContext.getCurrentId());
        }*//*

    }*/



    /**
     *   方法二：  快速获取方法上的注解
     * */

    //将AutoFill注解只限制在mapper层增强  （拦截的是mapper接口下的autoFill注解）
    @Before(" execution(* com.sky.mapper.*.*(..)) && @annotation(autoFill)")
    /** 注意：相对于方法一的获取AutoFill注解，可以在方法参数中指定注解  将切入点表达式的全类名改成注解的形参 （相对于已经指定注解的类型）*/
    public void autoFillProperty(JoinPoint joinPoint, AutoFill autoFill) throws Exception {


        log.info("进入AOP程序，为公共属性赋值");
        //通过原始方法运行时传入的参数，获取第一个(对象)
        //    void getStaff(Employee employee1);
        Object[] obj = joinPoint.getArgs();
        System.out.println(obj);

        //判断参数是否空
        if (ObjectUtils.isEmpty(obj)){
            return;
        }
        Object args = obj[0];
        System.out.println(args);

        //通过反射获取到对象对应方法
        Method setCreateTime = args.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
        Method setCreateUser = args.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
        Method setUpdateTime = args.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method setUpdateUser = args.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

        //获取注解对应的value属性
        OperationType operationType = autoFill.value();


        /** 判断，如果是insert，为属性赋值   */
        if (operationType.equals(OperationType.INSERT)){
            setCreateTime.invoke(args,LocalDateTime.now());
            setCreateUser.invoke(args,BaseContext.getCurrentId());
        }

        /**
         修改人的id和时间，每一次都需要执行
         */
        setUpdateTime.invoke(args, LocalDateTime.now());
        setUpdateUser.invoke(args, BaseContext.getCurrentId());

    }

}
