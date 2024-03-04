package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author tyl
 * @date 2023/9/9
 */
@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;

    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        ResponseResult responseResult = wmMaterialService.uploadPicture(multipartFile);
        return responseResult;
    }

    @PostMapping("/list")
    public ResponseResult findList( @RequestBody WmMaterialDto dto){
      return wmMaterialService.findList(dto);
    }

    @GetMapping("/collect/{materialId}")
    public ResponseResult collect(@PathVariable("materialId") Integer materialId){
        return wmMaterialService.collect(materialId);
    }

    @GetMapping("del_picture/{materialId}")
    public ResponseResult delPicture(@PathVariable("materialId") Integer materialId){
        return  wmMaterialService.delPicture(materialId);
    }
}
