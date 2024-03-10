package com.heima.search.controller.v1;

import com.alibaba.fastjson.JSON;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.HistorySearchDto;
import com.heima.search.service.ApUserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/history")
public class ApUserSearchController {

    @Autowired
    private ApUserSearchService apUserSearchService;

    @PostMapping("/load")
    public ResponseResult findUserSearch(){
       return apUserSearchService.findUserSearch();
    }

    @PostMapping("/del")
    public ResponseResult delUserSearch(@RequestBody String historySearchDto) {
        Map<String,String> map = JSON.parseObject(historySearchDto, Map.class);
        String id = map.get("id");
        HistorySearchDto historySearchDto1=new HistorySearchDto();
        historySearchDto1.setId(id);
        return apUserSearchService.delUserSearch(historySearchDto1);
    }


}
