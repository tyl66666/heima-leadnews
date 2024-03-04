package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustomException;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.utils.thread.WmThreadLocalUtil;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Service
@Transactional
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;


    /**
     * 图片上传  springboot中指定 MultipartFile multipartFile 接受文件
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {

        // 1 检查参数
        if(multipartFile==null || multipartFile.getSize()==0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 2 上传图片到minIO 中 将uuid中的-替换成""
        String fileName = UUID.randomUUID().toString().replace("-", "");
        // 获得文件名
        String originalFilename = multipartFile.getOriginalFilename();
        // 获得文件后缀
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId =null;
        try {
            // 理解为 MultipartFile 已经内部使用io将文件读取了 可以直接获取输入流后 使用输出流将文件读取到别处
            fileId = fileStorageService.uploadImgFile("", fileName + postfix, multipartFile.getInputStream());
            log.info("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("上传不成功");
        }

        // 3 保存到数据库中
        WmMaterial wmMaterial=new WmMaterial();
        wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
        wmMaterial.setUrl(fileId);
        // 是否收藏 0 不收藏 1 收藏
        wmMaterial.setIsCollection((short)0);
        //0 图片 1 视频
        wmMaterial.setType((short)0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);
        return ResponseResult.okResult(wmMaterial);
    }

    /**
     * 素材列表查询
     * @param dto
     * @return
     * TODO 分页去看下
     */
    @Override
    public ResponseResult findList(WmMaterialDto dto) {
        // 1 检查参数
        dto.checkParam();

        //2 分页查询
        IPage page=new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmMaterial> wrapper=new LambdaQueryWrapper<>();

        //是否收藏
        if(dto.getIsCollection()!=null && dto.getIsCollection()==1){
           wrapper.eq(WmMaterial::getIsCollection,dto.getIsCollection());
        }

        // 按照用户查询
        wrapper.eq(WmMaterial::getUserId,WmThreadLocalUtil.getUser().getId());

        //按照时间排序
        wrapper.orderByDesc(WmMaterial::getCreatedTime);
        page=page(page,wrapper);

        // 3 返回结果
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        pageResponseResult.setData(page.getRecords());
        return pageResponseResult;
    }

    @Override
    public ResponseResult collect(Integer materialId) {
        WmMaterial wmMaterial=new WmMaterial();
        wmMaterial.setId(materialId);
        wmMaterial.setIsCollection((short)1);
        this.updateById(wmMaterial);
        return ResponseResult.errorResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult delPicture(Integer materialId) {
        //判断是否素材被使用
        int result = wmNewsMaterialMapper.findNewsByMaterial(materialId);
        if(result>0){
            throw new CustomException(AppHttpCodeEnum.PICTURE_USE);
        }

        wmMaterialMapper.deleteById(materialId);
        return ResponseResult.errorResult(AppHttpCodeEnum.SUCCESS);
    }
}
