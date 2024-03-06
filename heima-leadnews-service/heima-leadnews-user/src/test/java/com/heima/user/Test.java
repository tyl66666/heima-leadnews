package com.heima.user;

import com.heima.apis.article.IArticleClient;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
    private IArticleClient articleClient;

    @org.junit.Test
    public void test() {
        ArticleDto articleDto=new ArticleDto();
        articleDto.setContent("aaaaaaaabbbbb");
        articleDto.setAuthorName("aaaawwwaaaaaa");
        ResponseResult responseResult = articleClient.saveArticle(articleDto);
        System.out.println(responseResult.getData());
    }
}
