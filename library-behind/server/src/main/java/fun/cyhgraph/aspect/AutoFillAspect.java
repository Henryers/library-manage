package fun.cyhgraph.aspect;

import fun.cyhgraph.annotation.AutoFill;
import fun.cyhgraph.constant.AutoFillConstant;
import fun.cyhgraph.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点表达式，封装成一个小方法
     */
    @Pointcut("execution(* fun.cyhgraph.mapper.*.*(..)) && @annotation(fun.cyhgraph.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")  // 调用上面的切入点表达式
    public void autoFill(JoinPoint joinPoint) {
        // 可先进行调试，是否能进入该方法 提前在mapper方法添加AutoFill注解
        log.info("开始进行公共字段(create_time等)的自动填充...");
        // 获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();  // 方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); // 获得方法上的注解对象
        OperationType operationType = autoFill.value(); // 获得数据库操作类型
        // 获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];
        // 准备赋值的数据：自动填充当前时间
        LocalDateTime now = LocalDateTime.now();
        if (operationType == OperationType.INSERT) {
            // 为公共字段(时间字段)赋值，刚开始插入需要设置create字段
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                // 通过反射为对象属性赋值
                setCreateTime.invoke(entity, now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
