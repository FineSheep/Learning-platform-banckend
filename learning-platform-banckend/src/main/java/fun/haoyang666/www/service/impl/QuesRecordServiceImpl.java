package fun.haoyang666.www.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.haoyang666.www.domain.entity.Quesrecord;
import fun.haoyang666.www.domain.vo.GradeVO;
import fun.haoyang666.www.mapper.QuesrecordMapper;
import fun.haoyang666.www.service.QuesrecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yang
 * @createTime 2023/1/17 11:44
 * @description
 */
@Service
public class QuesRecordServiceImpl extends ServiceImpl<QuesrecordMapper, Quesrecord>
        implements QuesrecordService {
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void saveRecordQues(long recordId, long userId, List<GradeVO> correctList, List<GradeVO> falseList) {
        List<Quesrecord> listCorrect = correctList.stream().map(item -> toQuesRe(recordId, userId, item)).collect(Collectors.toList());
        List<Quesrecord> listFalse = falseList.stream().map(item -> toQuesRe(recordId, userId, item)).collect(Collectors.toList());
        this.saveBatch(listCorrect);
        this.saveBatch(listFalse);
    }

    private Quesrecord toQuesRe(long recordId, long userId, GradeVO vo) {
        Quesrecord quesrecord = new Quesrecord();
        quesrecord.setRecordId(recordId);
        quesrecord.setQuestionId(vo.getQuesId());
        quesrecord.setUserId(userId);
        quesrecord.setUserAnswer(vo.getUserAnswer());
        int isCorrect = vo.isCorrect() ? 1 : 0;
        quesrecord.setIsCorrect(isCorrect);
        return quesrecord;
    }
}
