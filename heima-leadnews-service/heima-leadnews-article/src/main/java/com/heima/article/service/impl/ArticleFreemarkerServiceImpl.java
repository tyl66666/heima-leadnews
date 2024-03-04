package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.ArticleFreemarkerService;
import com.heima.common.constants.ArticleConstants;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.search.vos.SearchArticleVo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private Configuration configuration;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ApArticleService apArticleService;


    @Override
    @Async
    public void buildArticleToMinio(ApArticle apArticle, String content) {
        StringWriter out=new StringWriter();
        //1 获取文章内容
        if( StringUtils.isNotBlank(content)){
            // 文章内容通过freemarker生成html文件
            Template template = null;
            try {
                template = configuration.getTemplate("article.ftl");
                //合成 TODO ??????
                //数据模型 因为存的是json数据 需要转
                Map<String,Object> contentDataModel=new HashMap<>();
                contentDataModel.put("content",JSONArray.parseArray(content));
                template.process(contentDataModel,out);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 把html上传得到minio中 TODO ?????
            InputStream in = new ByteArrayInputStream((out.toString().getBytes(StandardCharsets.UTF_8)));
            String path = fileStorageService.uploadHtmlFile("", apArticle.getId() + ".html", in);

            //修改ap_article表,保存static_url字段
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,apArticle.getId()).set(ApArticle::getStaticUrl,path));

            //发送消息，创建索引
            createArticleESIndex(apArticle,content,path);
        }
    }

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    /**
     * 发送消息 创建索引
     * @param apArticle
     * @param content
     * @param path
     */
    private void createArticleESIndex(ApArticle apArticle, String content, String path) {
        SearchArticleVo vo = new SearchArticleVo();
        BeanUtils.copyProperties(apArticle,vo);
        vo.setContent(content);
        vo.setStaticUrl(path);

        kafkaTemplate.send(ArticleConstants.ARTICLE_ES_SYNC_TOPIC, JSON.toJSONString(vo));
    }
}
