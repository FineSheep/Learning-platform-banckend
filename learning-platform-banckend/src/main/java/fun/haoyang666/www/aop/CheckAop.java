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

    @Around("@annotation(checkAuth)")
    public Object checkAuth(ProceedingJoinPoint jp, CheckAuth checkAuth) throws Throwable {
        String auth = ThreadLocalUtils.get().getAuth();
        if (auth.equals(checkAuth.value())){
            return jp.proceed();
        }
        throw new BusinessException(ErrorCode.NO_AUTH);
    }

}
