package fun.haoyang666.www.job;

import cn.hutool.core.text.StrBuilder;
import com.google.gson.Gson;
import fun.haoyang666.www.domain.entity.Information;
import fun.haoyang666.www.domain.vo.SpiderVO;
import fun.haoyang666.www.service.InformationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author yang
 * @createTime 2023/2/22 15:23
 * @description
 */
@Component
@Slf4j
public class SpiderJob {
    @Resource
    private InformationService informationService;

    //每月最后一日的上午10:15触发
    @Scheduled(cron = "0 15 10 L * ?")
    @SneakyThrows
    public void spider() {
        log.info("爬虫定时任务开启。。。。。");
        //todo 服务器路径
        String args1 = "python3 " + "/usr/spider/craw.py";
        System.out.println(args1);
        Process process = Runtime.getRuntime().exec(args1);
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
        String line;
        Gson gson = new Gson();
        LinkedList<Information> informationList = new LinkedList<>();
        while ((line = in.readLine()) != null) {
            SpiderVO spiderVO = gson.fromJson(line, SpiderVO.class);
            Information information = new Information();
            information.setContent(spiderVO.getContent());
            information.setLink(spiderVO.getLink());
            LocalDate time = LocalDate.parse(spiderVO.getPutTime());
            information.setPutTime(time);
            information.setTitle(spiderVO.getTitle());
            information.setSource(spiderVO.getSource());
            informationList.add(information);
        }
        informationService.saveBatch(informationList);
        log.info("爬虫完成。。。。");
        in.close();
        process.waitFor();
    }
}
