package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.HistorySearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


public interface ApUserSearchService {
    /**
     * 保存用户搜索记录
     * @param keyword
     * @param userId
     */
    public void insert(String keyword,Integer userId);


    /**
     * 查询搜索历史
     * @return
     */
    public ResponseResult findUserSearch();

    /**
     删除搜索历史
     @param historySearchDto
     @return
     */
    ResponseResult delUserSearch(HistorySearchDto historySearchDto);
}
