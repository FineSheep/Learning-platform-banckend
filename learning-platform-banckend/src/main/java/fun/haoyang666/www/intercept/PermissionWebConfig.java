package fun.haoyang666.www.intercept;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PermissionIntercept())
                .addPathPatterns("/**")    // 拦截哪些页面
                .excludePathPatterns("/user/getCode",
                        "/user/loginByPassword",
                        "/user/loginOrRegister",
                        "/game/**");   // 放行哪些页面
    }
}

