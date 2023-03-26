package fun.haoyang666.www.admin.controller;

import fun.haoyang666.www.admin.dto.InfoParamDto;
import fun.haoyang666.www.annotation.CheckAuth;
import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.job.SpiderJob;
import fun.haoyang666.www.service.InformationService;
import fun.haoyang666.www.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author yang
 * @createTime 2023/3/12 19:46
 * @description
 */
@RequestMapping("sysInfo")
@RestController
@CheckAuth("admin")
public class SysInformation {
    @Resource
    private InformationService informationService;

    @PostMapping("listInfo")
    public BaseResponse listInfo(@RequestBody InfoParamDto infoParamDto){
        return ResultUtils.success(informationService.listInfo(infoParamDto));
    }

    @GetMapping("changeInfo")
    public BaseResponse changeInfo(Long id,Integer type){
        if (id==null || type==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(informationService.changeInfo(id,type));
    }

    @Autowired
    private SpiderJob spiderJob;
    @GetMapping("test")
    public void  test(){
        spiderJob.spider();
    }
}
