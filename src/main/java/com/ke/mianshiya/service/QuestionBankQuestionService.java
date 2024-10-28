package com.ke.mianshiya.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ke.mianshiya.model.dto.questionbankquestion.QuestionBankQuestionQueryRequest;
import com.ke.mianshiya.model.entity.QuestionBankQuestion;
import com.ke.mianshiya.model.entity.User;
import com.ke.mianshiya.model.vo.QuestionBankQuestionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题目题库关系表服务
 *
 * @author <a href="https://github.com/like">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
public interface QuestionBankQuestionService extends IService<QuestionBankQuestion> {

    /**
     * 校验数据
     *
     * @param questionBankQuestion
     * @param add 对创建的数据进行校验
     */
    void validQuestionBankQuestion(QuestionBankQuestion questionBankQuestion, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionBankQuestionQueryRequest
     * @return
     */
    QueryWrapper<QuestionBankQuestion> getQueryWrapper(QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest);
    
    /**
     * 获取题目题库关系表封装
     *
     * @param questionBankQuestion
     * @param request
     * @return
     */
    QuestionBankQuestionVO getQuestionBankQuestionVO(QuestionBankQuestion questionBankQuestion, HttpServletRequest request);

    /**
     * 分页获取题目题库关系表封装
     *
     * @param questionBankQuestionPage
     * @param request
     * @return
     */
    Page<QuestionBankQuestionVO> getQuestionBankQuestionVOPage(Page<QuestionBankQuestion> questionBankQuestionPage, HttpServletRequest request);


    /**
     *  批量添加题目到题库
     * @param questionBankId
     * @param questionIdList
     * @param loginUser
     */
    void batchAddQuestionToBank(Long questionBankId, List<Long> questionIdList, User loginUser);

    /**
     * 分批处理插入事务
     * @param questionBankQuestionList
     */
    void batchAddQuestionToBankInner(List<QuestionBankQuestion> questionBankQuestionList);

    /**
     * 批量从题库中移除题目
     * @param questionBankId
     * @param questionIdList
     */
    void batchRemoveQuestionFromBank(Long questionBankId, List<Long> questionIdList);
}
