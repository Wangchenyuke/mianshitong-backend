package com.ke.mianshiya.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量删除题目请求类
 */
@Data
public class QuestionDeleteBatchRequest implements Serializable {

    /**
     * 题目id列表
     */
    public List<Long> questionIdList;

    private static final long serialVersionUID = 1L;
}
