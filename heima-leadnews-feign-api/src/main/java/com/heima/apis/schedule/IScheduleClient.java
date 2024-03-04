package com.heima.apis.schedule;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.schedule.dtos.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("leadnews-schedule")
public interface IScheduleClient {

    /**
     *  添加延迟任务
     * @param task
     * @return
     */
    @PostMapping("/api/v1/task/add")
    public ResponseResult addTask(@RequestBody Task task);

    /**
     * 取消任务
     * @param taskId
     * @return
     */
    @GetMapping("/api/v1/task/{taskId}")
    public ResponseResult cancelTask( @PathVariable  long taskId);

    /**
     * 根据类型和优先级拉去任务
     * @param type
     * @param priority
     * @return
     */
    @GetMapping("/api/v1/task/{type}/{priority}")
    public ResponseResult poll(@PathVariable  int type, @PathVariable int priority);
}
