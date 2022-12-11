package fun.haoyang666.www.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.google.gson.Gson;
import fun.haoyang666.www.domain.Questions;
import fun.haoyang666.www.mapper.QuestionsMapper;
import fun.haoyang666.www.service.QuestionsService;
import fun.haoyang666.www.service.impl.QuestionsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yang
 * @createTime 2022/12/11 16:51
 * @description
 */
@Slf4j
public class QuestionListener implements ReadListener<Questions> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<Questions> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private QuestionsService questionsService;

    public QuestionListener(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @Override
    public void invoke(Questions questions, AnalysisContext analysisContext) {
        Gson gson = new Gson();
        if (questions.getCorrect().length() != 1) {
            questions.setIsMulti(1);
        }
        log.info("解析到一条数据:{}", gson.toJson(questions));
        cachedDataList.add(questions);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            questionsService.saveBatch(cachedDataList);
            log.info("数据插入：{}", cachedDataList);
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (cachedDataList.size() != 0) {
            questionsService.saveBatch(cachedDataList);
            log.info("数据插入：{}", cachedDataList);
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
        log.info("数据读取完成");
    }
}
