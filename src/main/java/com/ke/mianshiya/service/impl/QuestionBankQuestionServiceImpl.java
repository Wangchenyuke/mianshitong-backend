package com.ke.mianshiya.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ke.mianshiya.common.ErrorCode;
import com.ke.mianshiya.constant.CommonConstant;
import com.ke.mianshiya.exception.BusinessException;
import com.ke.mianshiya.exception.ThrowUtils;
import com.ke.mianshiya.mapper.QuestionBankQuestionMapper;
import com.ke.mianshiya.model.dto.questionbankquestion.QuestionBankQuestionQueryRequest;
import com.ke.mianshiya.model.entity.Question;
import com.ke.mianshiya.model.entity.QuestionBank;
import com.ke.mianshiya.model.entity.QuestionBankQuestion;
import com.ke.mianshiya.model.entity.User;
import com.ke.mianshiya.model.vo.QuestionBankQuestionVO;
import com.ke.mianshiya.model.vo.UserVO;
import com.ke.mianshiya.service.QuestionBankQuestionService;
import com.ke.mianshiya.service.QuestionBankService;
import com.ke.mianshiya.service.QuestionService;
import com.ke.mianshiya.service.UserService;
import com.ke.mianshiya.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 题目题库关系表服务实现
 *
 * @author <a href="https://github.com/like">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Service
@Slf4j
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion> implements QuestionBankQuestionService {

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private QuestionService questionService;

    @Resource
    private QuestionBankService questionBankService;

    /**
     * 校验数据
     *
     * @param questionBankQuestion
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validQuestionBankQuestion(QuestionBankQuestion questionBankQuestion, boolean add) {

//        不校验
        ThrowUtils.throwIf(questionBankQuestion == null, ErrorCode.PARAMS_ERROR);

        Long questionBankId = questionBankQuestion.getQuestionBankId();
        if (questionBankId != null){
            QuestionBank questionBank = questionBankService.getById(questionBankId);
            ThrowUtils.throwIf(questionBank == null,ErrorCode.NOT_FOUND_ERROR,"题库不存在");
        }
        
        Long questionId = questionBankQuestion.getQuestionId();
        if (questionId != null){
            Question question = questionService.getById(questionId);
            ThrowUtils.throwIf(question == null,ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }


//        // todo 从对象中取值
//        String title = questionBankQuestion.getTitle();
//        // 创建数据时，参数不能为空
//        if (add) {
//            // todo 补充校验规则
//            ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
//        }
//        // 修改数据时，有参数则校验
//        // todo 补充校验规则
//        if (StringUtils.isNotBlank(title)) {
//            ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
//        }
    }

    /**
     * 获取查询条件
     *
     * @param questionBankQuestionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionBankQuestion> getQueryWrapper(QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest) {
        QueryWrapper<QuestionBankQuestion> queryWrapper = new QueryWrapper<>();
        if (questionBankQuestionQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = questionBankQuestionQueryRequest.getId();
        Long notId = questionBankQuestionQueryRequest.getNotId();
        String sortField = questionBankQuestionQueryRequest.getSortField();
        String sortOrder = questionBankQuestionQueryRequest.getSortOrder();
        Long userId = questionBankQuestionQueryRequest.getUserId();
        Long questionBankId = questionBankQuestionQueryRequest.getQuestionBankId();
        Long questionId = questionBankQuestionQueryRequest.getQuestionId();

        // todo 补充需要的查询条件


        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionBankId), "questionBankId", questionBankId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取题目题库关系表封装
     *
     * @param questionBankQuestion
     * @param request
     * @return
     */
    @Override
    public QuestionBankQuestionVO getQuestionBankQuestionVO(QuestionBankQuestion questionBankQuestion, HttpServletRequest request) {
        // 对象转封装类
        QuestionBankQuestionVO questionBankQuestionVO = QuestionBankQuestionVO.objToVo(questionBankQuestion);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = questionBankQuestion.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionBankQuestionVO.setUser(userVO);

        // endregion

        return questionBankQuestionVO;
    }

    /**
     * 分页获取题目题库关系表封装
     *
     * @param questionBankQuestionPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionBankQuestionVO> getQuestionBankQuestionVOPage(Page<QuestionBankQuestion> questionBankQuestionPage, HttpServletRequest request) {
        List<QuestionBankQuestion> questionBankQuestionList = questionBankQuestionPage.getRecords();
        Page<QuestionBankQuestionVO> questionBankQuestionVOPage = new Page<>(questionBankQuestionPage.getCurrent(), questionBankQuestionPage.getSize(), questionBankQuestionPage.getTotal());
        if (CollUtil.isEmpty(questionBankQuestionList)) {
            return questionBankQuestionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionBankQuestionVO> questionBankQuestionVOList = questionBankQuestionList.stream().map(questionBankQuestion -> {
            return QuestionBankQuestionVO.objToVo(questionBankQuestion);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionBankQuestionList.stream().map(QuestionBankQuestion::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        questionBankQuestionVOList.forEach(questionBankQuestionVO -> {
            Long userId = questionBankQuestionVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionBankQuestionVO.setUser(userService.getUserVO(user));

        });
        // endregion

        questionBankQuestionVOPage.setRecords(questionBankQuestionVOList);
        return questionBankQuestionVOPage;
    }

    @Override
    public void batchAddQuestionToBank(Long questionBankId, List<Long> questionIdList, User loginUser) {
        //参数校验
        ThrowUtils.throwIf(questionBankId == null || questionBankId <= 0,ErrorCode.PARAMS_ERROR, "题库非法");
        ThrowUtils.throwIf(CollUtil.isEmpty(questionIdList),ErrorCode.PARAMS_ERROR, "题目id不能为空");
        ThrowUtils.throwIf(loginUser == null,ErrorCode.NOT_LOGIN_ERROR, "未登录");

        //获取合法的题目
        LambdaQueryWrapper<Question> questionLambdaQueryWrapper = Wrappers.lambdaQuery(Question.class)
                .in(Question::getId, questionIdList)
                .select(Question::getId);
        List<Long> vaildQuestionIdList= questionService.listObjs(questionLambdaQueryWrapper, obj -> (Long) obj);

        //检查哪些题目还不存在于该题库中，避免重复插入
        LambdaQueryWrapper<QuestionBankQuestion> wrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                .eq(QuestionBankQuestion::getQuestionBankId, questionBankId)
                .in(QuestionBankQuestion::getQuestionId, vaildQuestionIdList);
        //查询到已经存在题库中的题目列表
        List<QuestionBankQuestion> existQuestionList = this.list(wrapper);
        Set<Long> existQuestionIdSet = existQuestionList.stream().map(QuestionBankQuestion::getQuestionId).collect(Collectors.toSet());

        //过滤掉已经存在题库中的题目
        vaildQuestionIdList = vaildQuestionIdList.stream().filter(questionId -> {
            return !existQuestionIdSet.contains(questionId);
        }).collect(Collectors.toList());
        ThrowUtils.throwIf(CollUtil.isEmpty(vaildQuestionIdList),ErrorCode.PARAMS_ERROR,"所有题目都已存在于题库中");

        //获取题库
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null,ErrorCode.PARAMS_ERROR, "题库不存在");

        //定义线程池
        ThreadPoolExecutor customExecutor = new ThreadPoolExecutor(
                20,     //核心线程数
                50,         //最大线程数
                60L,        //线程空闲存活时间
                TimeUnit.SECONDS,  //存活时间单位
                new LinkedBlockingQueue<>(1000),  //任务队列
                new ThreadPoolExecutor.CallerRunsPolicy()       //拒绝策略 由调用线程处理
        );
        //批量添加
        //分批添加 每次添加一千条
        int batchSize = 1000;
        int totalSize = vaildQuestionIdList.size();

        //创建一个数组用于存放 future
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < vaildQuestionIdList.size(); i+=batchSize) {
            List<Long> subList = vaildQuestionIdList.subList(i, Math.min(i + batchSize, totalSize));
            List<QuestionBankQuestion> questionBankQuestions = subList.stream().map(questionId -> {
                QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
                questionBankQuestion.setQuestionBankId(questionBankId);
                questionBankQuestion.setQuestionId(questionId);
                questionBankQuestion.setUserId(loginUser.getId());
                return questionBankQuestion;
            }).collect(Collectors.toList());
            //不能用this直接调用 会使事务无法生效
            QuestionBankQuestionService questionBankQuestionService =   (QuestionBankQuestionService) AopContext.currentProxy();

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                questionBankQuestionService.batchAddQuestionToBankInner(questionBankQuestions);
            },customExecutor);//指定我们创建的线程池 执行异步任务

            futures.add(future);
        }
        //在这里阻塞等待所有异步任务执行完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddQuestionToBankInner(List<QuestionBankQuestion> questionBankQuestions){

        try {
            boolean result = this.saveBatch(questionBankQuestions);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "向题库添加题目失败");
        } catch (DataIntegrityViolationException e) {
            log.error("数据库唯一键冲突或违反其他完整性约束, 错误信息: {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目已存在于该题库，无法重复添加");
        } catch (DataAccessException e) {
            log.error("数据库连接问题、事务问题等导致操作失败, 错误信息: {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "数据库操作失败");
        } catch (Exception e) {
            // 捕获其他异常，做通用处理
            log.error("添加题目到题库时发生未知错误，错误信息: {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "向题库添加题目失败");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRemoveQuestionFromBank(Long questionBankId, List<Long> questionIdList) {
        ThrowUtils.throwIf(questionBankId == null || questionBankId <= 0,ErrorCode.PARAMS_ERROR, "题库非法");
        ThrowUtils.throwIf(CollUtil.isEmpty(questionIdList),ErrorCode.PARAMS_ERROR, "题目列表为空");

        //批量删除
        for (Long questionId : questionIdList) {
            LambdaQueryWrapper<QuestionBankQuestion> wrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
                    .eq(QuestionBankQuestion::getQuestionBankId, questionBankId)
                    .eq(QuestionBankQuestion::getQuestionId, questionId);
            boolean result = this.remove(wrapper);
            if (!result)
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败");
        }
    }

}
