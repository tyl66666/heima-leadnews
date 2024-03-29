package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 图片上传
     */

     ResponseResult uploadPicture(MultipartFile multipartFile);


    /**
     * 素材列表查询
     * @param dto
     * @return
     */
     ResponseResult findList(WmMaterialDto dto);


     //收藏
     public ResponseResult collect(Integer materialId);

     // 删除素材
     public ResponseResult delPicture(Integer materialId);

}