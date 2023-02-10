package fun.haoyang666.www.domain.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yang
 * @createTime 2022/12/31 17:12
 * @description
 */
@Data
public class PageREQ implements Serializable {
    protected int curPage;
    protected int pageSize;
}
