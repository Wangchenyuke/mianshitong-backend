package com.ke.mianshiya.esdao;

import com.ke.mianshiya.model.dto.question.QuestionEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/like">程序员鱼皮</a>
 * @from <a href="https://ke.icu">编程导航知识星球</a>
 */
public interface QuestionEsDao extends ElasticsearchRepository<QuestionEsDTO, Long> {

    //这里只要写出这个方法 ，Spring Data 就会自动帮我们按照方法名实现 按照userId查询
    List<QuestionEsDTO> findByUserId(Long userId);
}