package fun.haoyang666.www;

import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author yang
 * @createTime 2022/12/9 19:20
 * @description
 */
@SpringBootApplication
@MapperScan("fun.haoyang666.www.mapper")
@EnableWebSocket
public class LearnPlatApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnPlatApplication.class,args);
    }
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
