package fun.haoyang666.www.intercept;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.domain.vo.UserAuth;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.utils.JwtUtil;
import fun.haoyang666.www.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yang
 * @createTime 2022/12/4 15:25
 * @description
 */
@Slf4j
public class PermissionIntercept implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //axios在发送请求之前需要先发送一个OPTIONS预请求，相当于请求两次，直接放行
        if ("OPTIONS".equals(request.getMethod().toString())) {
            return true; //true是直接放行，前端抓包会有options请求
            //false拒接访问，抓包就不会有options请求了
        }
        String token = request.getHeader("token");
        if (token == null) {
            throw new BusinessException(ErrorCode.LOGIN_EXPIRE);
        }
        try {
            DecodedJWT dj = JwtUtil.decodeToken(token);
            String userId = dj.getClaim("userId").asString();
            String auth = dj.getClaim("auth").asString();
            UserAuth userAuth = new UserAuth(Long.valueOf(userId), auth);
            ThreadLocalUtils.set(userAuth);
            // -------------------------------------------------------------------------------------------------------
            // 计算当前时间是否超过过期时间的一半，如果是就帮用户续签 --------------------------
            // 此处并不是永久续签，只是为 大于过期时间的一半 且 小于过期时间 的 token 续签
            Long expTime = dj.getExpiresAt().getTime();
            Long iatTime = dj.getIssuedAt().getTime();
            Long nowTime = System.currentTimeMillis();
            if ((nowTime - iatTime) > (expTime - iatTime) / 2) {
                // 生成新的jwt
                log.info("令牌续约");
                Map<String, String> payload = new HashMap<>();
                payload.put("userId", userId); // 加入一些非敏感的用户信息
                payload.put("auth", auth);
                String newJwt = JwtUtil.generateToken(payload);
                // 加入返回头
                response.addHeader("token", newJwt);
            }
        } catch (JWTDecodeException e) {
            log.error("令牌错误");
            throw new BusinessException(ErrorCode.LOGIN_EXPIRE);
        } catch (TokenExpiredException e) {
            log.error("令牌过期");
            throw new BusinessException(ErrorCode.LOGIN_EXPIRE);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtils.remove();
    }
}
