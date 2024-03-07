package com.heima.wemedia.service;

import java.util.Date;

public interface WmNewsTaskService {
    /**
     * 添加任务到延迟队列中 发布之后就要添加到数据库中
     * @param id 文章id
     * @param publishTime
     */
    public void addNewsToTask(Integer id, Date publishTime);

    /**
     * 消费任务 审核文章
     */
    public void scanNewsByTask();
}
