package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.dtos.ArticleInfoDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author tyl
 * @date 2023/9/4
 */
public interface ApArticleService extends IService<ApArticle> {
    /**
     * 根据参数加载文章列表
     * @param loadtype 1为加载更多  2为加载最新
     * @param dto
     * @return
     */
    ResponseResult load(Short loadtype, ArticleHomeDto dto);

    /**
     *  加载文章列表
     * @param loadtype  1为加载更多  2为加载最新
     * @param dto
     * @param firstPage  ture 为首页  false 不是首页
     * @return
     */
    ResponseResult load2(Short loadtype, ArticleHomeDto dto,boolean firstPage);

    ResponseResult saveArticle(ArticleDto dto);

    /**
     * 加载文章详情 数据回显
     * @param dto
     * @return
     */
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto);

}
