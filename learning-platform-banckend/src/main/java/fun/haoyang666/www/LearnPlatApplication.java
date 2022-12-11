package fun.haoyang666.www;

import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yang
 * @createTime 2022/12/9 19:20
 * @description
 */
@SpringBootApplication
@MapperScan("fun.haoyang666.www.mapper")
public class LearnPlatApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnPlatApplication.class,args);
    }
}
