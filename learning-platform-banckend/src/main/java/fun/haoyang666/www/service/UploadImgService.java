package fun.haoyang666.www.service;

import cn.xuyanwu.spring.file.storage.FileInfo;
import fun.haoyang666.www.domain.req.UploadImgReq;
import org.springframework.web.multipart.MultipartFile;

public interface UploadImgService {

    FileInfo userUrl(MultipartFile file, Long userId);

    FileInfo uploadImg(MultipartFile img);
}
