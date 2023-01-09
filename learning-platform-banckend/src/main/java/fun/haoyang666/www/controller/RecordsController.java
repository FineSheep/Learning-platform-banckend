package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.ResultUtils;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.dto.ScrollerDto;
import fun.haoyang666.www.domain.entity.Records;
import fun.haoyang666.www.domain.vo.RecordVo;
import fun.haoyang666.www.service.RecordsService;
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
public class RecordsController {
    @Resource
    private RecordsService recordsService;

    @GetMapping("getRecords")
    public BaseResponse getRecords(long uid, int curPage, int pageSize) {
        if (curPage < 0 || pageSize > 50) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        ScrollerDto<RecordVo> record = recordsService.getRecordsByUid(uid, curPage, pageSize);
        return ResultUtils.success(record);
    }
}
