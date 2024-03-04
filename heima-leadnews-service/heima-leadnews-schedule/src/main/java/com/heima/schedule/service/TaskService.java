package com.heima.schedule.service;

import com.heima.model.schedule.dtos.Task;

public interface TaskService {
    /**
     *  添加延迟任务
     * @param task
     * @return
     */
    public long addTask(Task task);

    /**
     * 取消任务
     * @param taskId
     * @return
     */
    public boolean cancelTask(long taskId);

    /**
     * 根据类型和优先级拉去任务
     * @param type
     * @param priority
     * @return
     */
    public Task poll(int type,int priority);
}
