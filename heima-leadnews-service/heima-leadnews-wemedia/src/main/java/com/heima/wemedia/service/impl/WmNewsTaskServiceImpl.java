package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.apis.schedule.IScheduleClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.TaskTypeEnum;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.utils.common.ProtostuffUtil;
import com.heima.wemedia.service.WmNewsTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WmNewsTaskServiceImpl implements WmNewsTaskService {

    @Autowired
    private IScheduleClient scheduleClient;
    /**
     *  添加任务到延迟队列中
     * @param id 文章id
     * @param publishTime
     */
    @Override
    @Async
    public void addNewsToTask(Integer id, Date publishTime) {
        // TODO 有点不懂
        Task task=new Task();
        task.setExecuteTime(publishTime.getTime());
        // 这两个参数是为了从redis中获取值 平凑key
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        WmNews wmNews=new WmNews();
        wmNews.setId(id);
        task.setParameters(ProtostuffUtil.serialize(wmNews));

        scheduleClient.addTask(task);
    }

    @Autowired
    private WmNewsAutoScanServiceImpl wmNewsAutoScanService;

    /**
     * 消费任务 审核文章
     */
    @Scheduled(fixedRate = 10000)  // 表示每秒执行一次
    @Override
    public void scanNewsByTask() {
        ResponseResult responseResult = scheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        if(responseResult.getCode().equals(200) && responseResult.getData()!=null){
            Task task= JSON.parseObject(JSON.toJSONString(responseResult.getData()),Task.class);
            WmNews wmNews = ProtostuffUtil.deserialize(task.getParameters(), WmNews.class);
            wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        }
    }
}
