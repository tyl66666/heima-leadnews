package com.heima.article.test;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.ArticleApplication;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.service.ApArticleService;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tyl
 * @date 2023/9/6
 */
@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class ArticleFreemarkerTest {

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private Configuration configuration;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ApArticleService apArticleService;

    @Test
    public void createStaticUrlTest() throws Exception {
      //1 获取文章内容
        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, "1302865008438296577L"));
        if(apArticleContent!=null && StringUtils.isNotBlank(apArticleContent.getContent())){
            // 文章内容通过freemarker生成html文件
            Template template = configuration.getTemplate("article.ftl");
            //数据模型 因为存的是json数据 需要转
            Map<String,Object> content=new HashMap<>();
            JSONArray objects = JSONArray.parseArray(apArticleContent.getContent());
            content.put("content",objects);

            //合成
            StringWriter out=new StringWriter();
            template.process(content,out);

            // 把html上传得到minio中 TODO ?????  转换流
            InputStream in = new ByteArrayInputStream((out.toString().getBytes(StandardCharsets.UTF_8)));
            String path = fileStorageService.uploadHtmlFile("", apArticleContent.getArticleId() + ".html", in);

            //修改ap_article表,保存static_url字段
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,apArticleContent.getArticleId()).set(ApArticle::getStaticUrl,path));
        }
    }
}
