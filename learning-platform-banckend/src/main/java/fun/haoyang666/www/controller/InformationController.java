package fun.haoyang666.www.controller;

import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.dto.ScrollerDTO;
import fun.haoyang666.www.domain.entity.Information;
import fun.haoyang666.www.domain.req.PageREQ;
import fun.haoyang666.www.service.InformationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yang
 * @createTime 2022/12/31 17:05
 * @description
 */
@RestController
@RequestMapping("information")
public class InformationController {
    @Resource
    private InformationService informationService;

    @GetMapping("getInfo")
    public BaseResponse getInfo(PageREQ pageReq) {
        if (pageReq == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        ScrollerDTO<Information> dto = informationService.getInformationPage(pageReq.getCurPage(), pageReq.getPageSize());
        return ResultUtils.success(dto);
    }

    @GetMapping("getNewById")
    public BaseResponse getNewById(Long id) {
        Information info = informationService.getNewById(id);
        return ResultUtils.success(info);
    }
}
