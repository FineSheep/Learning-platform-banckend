package fun.haoyang666.www.controller;

import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import fun.haoyang666.www.common.BaseResponse;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.service.UploadImgService;
import fun.haoyang666.www.utils.ResultUtils;
import fun.haoyang666.www.utils.ThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yang
 * @createTime 2023/1/28 11:30
 * @description
 */
@RestController
@RequestMapping("img")
public class ImgController {
    @Autowired
    private UploadImgService uploadImgService;

    /**
     * 上传图片，成功返回文件信息
     * 图片处理使用的是 https://github.com/coobird/thumbnailator
     */
    @PostMapping("/personUrl")
    public BaseResponse uploadImage(@RequestParam("file") MultipartFile file) {
        if (file == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Long userId = ThreadLocalUtils.get().getUserId();
        FileInfo fileInfo = uploadImgService.userUrl(file, userId);
        return ResultUtils.success(fileInfo);
    }

    @PostMapping("/postImg")
    public BaseResponse postImg(@RequestBody MultipartFile img) {
        if (img == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        FileInfo info = uploadImgService.uploadImg(img);
        return ResultUtils.success(info);
    }
}
