package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.dto.ScrollerDTO;
import fun.haoyang666.www.domain.vo.RecordVO;
import fun.haoyang666.www.service.RecordsService;
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
    public BaseResponse getRecords(long uid, int curPage, int pageSize) {
        if (curPage < 0 || pageSize > 50) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        ScrollerDTO<RecordVO> record = recordsService.getRecordsByUid(uid, curPage, pageSize);
        return ResultUtils.success(record);
    }

    @GetMapping("pkRecords")
    public BaseResponse pkRecords(long uid) {
        log.info("uid---->{}",uid);
        List<RecordVO> pkRecords = recordsService.getPKRecords(uid);
        return ResultUtils.success(pkRecords);
    }
}
