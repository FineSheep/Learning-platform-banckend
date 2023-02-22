package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.vo.RecordVO;
import fun.haoyang666.www.service.RecordsService;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yang
 * @createTime 2023/1/8 11:15
 * @description
 */
@RestController
@RequestMapping("records")
@Slf4j
public class RecordsController {
    @Resource
    private RecordsService recordsService;

    @GetMapping("getRecords")
    public BaseResponse getRecords(int curPage, int pageSize) {
        if (curPage < 0 || pageSize > 50) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Long userId = ThreadLocalUtils.get();
        List<RecordVO> record = recordsService.getRecordsByUid(userId, curPage, pageSize);
        return ResultUtils.success(record);
    }

    @GetMapping("pkRecords")
    public BaseResponse pkRecords() {
        Long userId = ThreadLocalUtils.get();
        List<RecordVO> pkRecords = recordsService.getPKRecords(userId);
        return ResultUtils.success(pkRecords);
    }


}
