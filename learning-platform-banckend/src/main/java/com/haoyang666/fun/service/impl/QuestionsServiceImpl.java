package com.haoyang666.fun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoyang666.fun.domain.Questions;
import com.haoyang666.fun.service.QuestionsService;
import com.haoyang666.fun.mapper.QuestionsMapper;
import org.springframework.stereotype.Service;

/**
* @author yang
* @description 针对表【questions(题目)】的数据库操作Service实现
* @createDate 2022-12-10 16:13:21
*/
@Service
public class QuestionsServiceImpl extends ServiceImpl<QuestionsMapper, Questions>
    implements QuestionsService{

}




