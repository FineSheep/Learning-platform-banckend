package fun.haoyang666.www.domain.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author yang
 * @createTime 2023/1/28 15:05
 * @description
 */
@Data
public class UploadImgReq implements Serializable {
    private static final long serialVersionUID = 2035143354862563512L;
    private Long userId;
    private MultipartFile file;
}
