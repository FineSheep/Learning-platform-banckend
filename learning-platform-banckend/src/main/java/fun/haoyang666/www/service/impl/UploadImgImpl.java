package fun.haoyang666.www.service.impl;

import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import fun.haoyang666.www.common.enums.ErrorCode;
import fun.haoyang666.www.domain.entity.User;
import fun.haoyang666.www.exception.BusinessException;
import fun.haoyang666.www.service.UploadImgService;
import fun.haoyang666.www.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yang
 * @createTime 2023/1/28 15:08
 * @description
 */
@Service
@Slf4j
public class UploadImgImpl implements UploadImgService {

    @Autowired
    private FileStorageService fileStorageService;//注入实列
    @Autowired
    private UserService userService;

    @Override
    public FileInfo userUrl(MultipartFile file, Long userId) {
        FileInfo upload = null;
        try {
            upload = upload(file);
            String url = upload.getUrl();
            userService.lambdaUpdate().eq(User::getId, userId).set(User::getAvatarUrl, url).update();
        } catch (Exception e) {
            log.error("error:", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
        return upload;
    }

    @Override
    public FileInfo uploadImg(MultipartFile img) {
        return upload(img);
    }

    private FileInfo upload(MultipartFile file) {
        return fileStorageService.of(file)
                .setPath("person/")
                .image(img -> img.size(1000, 1000))  //将图片大小调整到 1000*1000
                .thumbnail(th -> th.size(200, 200))  //再生成一张 200*200 的缩略图
                .upload();
    }
}
