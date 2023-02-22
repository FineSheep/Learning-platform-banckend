package fun.haoyang666.www.domain.dto;

import fun.haoyang666.www.domain.vo.RecordDetailVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/2/22 9:33
 * @description
 */
@Data
public class PKRecordDTO {
    private List<RecordDetailVO> radio = new ArrayList<>();
    private List<RecordDetailVO> multi = new ArrayList<>();
    private String opponentUrl;
    private String opponentName;
    private Long time;
    private Long correct;
    private Boolean result;
}
