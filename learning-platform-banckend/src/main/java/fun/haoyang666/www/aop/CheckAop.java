package fun.haoyang666.www.aop;

import fun.haoyang666.www.annotation.CheckAuth;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.utils.ThreadLocalUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author yang
 * @createTime 2023/3/5 12:02
 * @description
 */
@Aspect
@Component
public class CheckAop {

    @Around("execution(* fun.haoyang666.www.admin.controller.*.*(..))")
    public Object checkAuth(ProceedingJoinPoint jp) throws Throwable {
        CheckAuth annotation = jp.getTarget().getClass().getAnnotation(CheckAuth.class);
        String auth = ThreadLocalUtils.get().getAuth();
        if (annotation.value().equals(auth)) {
            return jp.proceed();
        }
        throw new BusinessException(ErrorCode.NO_AUTH);
    }

}
