package fun.haoyang666.www.domain.req;

import lombok.Data;

/**
 * @author yang
 * @createTime 2022/12/31 17:12
 * @description
 */
@Data
public class PageReq {
    protected int curPage;
    protected int pageSize;
}
