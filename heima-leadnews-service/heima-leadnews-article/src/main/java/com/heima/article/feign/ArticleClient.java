package com.heima.article.feign;

/**
 * @author tyl
 * @date 2023/9/11
 */
import com.heima.apis.article.IArticleClient;
import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ArticleClient implements IArticleClient{

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 保存app端文章
     * @param dto
     * @return
     */
    @Override
    public ResponseResult saveArticle(@RequestBody  ArticleDto dto) {
        return  apArticleService.saveArticle(dto);
    }
}
