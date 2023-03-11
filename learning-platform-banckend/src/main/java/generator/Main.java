package generator;

import cn.hutool.core.io.resource.ResourceUtil;
import com.google.gson.Gson;
import fun.haoyang666.www.domain.entity.Information;
import fun.haoyang666.www.domain.vo.SpiderVO;
import io.swagger.models.auth.In;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author yang
 * @createTime 2023/2/22 11:34
 * @description
 */

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Main main = new Main();
        main.test();
        Queue<String> queue = new LinkedList<>();
    }

    public void test() throws IOException, InterruptedException {
        String args1 = "python " + "E:\\workspace\\idea\\learning-platform-banckend\\learning-platform-banckend\\src\\main\\resources\\craw.py";
        String path = this.getClass().getClassLoader().getResource("craw.py").getPath();
        System.out.println(path);
        Process process = Runtime.getRuntime().exec(args1);
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
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
        System.out.println(informationList);
        in.close();
        process.waitFor();
    }
}
