package com.ke.mianshiya.mapper;

import com.ke.mianshiya.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
* @author 王陈宇科
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2024-10-15 23:56:40
* @Entity com.ke.mianshiya.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 查询5分钟前创建的题目(包括被删除的题目)
     * @param fiveMinutesAgoDate
     * @return
     */
    @Select("select * from question where updateTime >= #{fiveMinutesAgoDate}")
    List<Question> listQuestionWithDelete(Date fiveMinutesAgoDate);
}




