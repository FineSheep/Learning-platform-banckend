package fun.haoyang666.www.service;

import fun.haoyang666.www.domain.vo.LeaderBorderVo;

import java.util.List;
import java.util.Map;

public interface LeaderService {

    Map<String, List<LeaderBorderVo>> leaderBorder();
}
