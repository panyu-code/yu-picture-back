package com.panyu.yupictureback.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.domain.dto.file.UploadPictureResult;
import com.panyu.yupictureback.domain.entity.Picture;
import com.panyu.yupictureback.domain.vo.picture.PictureVO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.manager.FileManager;
import com.panyu.yupictureback.mapper.PictureMapper;
import com.panyu.yupictureback.service.PictureService;
import com.panyu.yupictureback.utils.ResultUtil;
import com.panyu.yupictureback.utils.ThrowUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author yupan
 * @description 针对表【picture(图片)】的数据库操作Service实现
 * @createDate 2024-12-22 17:04:15
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {
    @Resource
    private FileManager fileManager;

    @Override
    public ResponseResult<PictureVO> uploadPicture(MultipartFile multipartFile, Long pictureId, UserLoginVO loginUser) {
        ThrowUtil.throwIf(loginUser == null, ErrorCodeEnum.NO_AUTH_ERROR);
        ThrowUtil.throwIf(multipartFile == null, ErrorCodeEnum.PARAMS_ERROR, "文件不能为空");
        // 定义上传路径前缀
        String uploadPathPrefix = String.format("public/%s", loginUser.getId());
        // 上传图片
        UploadPictureResult uploadPictureResult = fileManager.uploadPicture(multipartFile, uploadPathPrefix);
        // 封装图片实体
        Picture picture = new Picture();
        picture.setUrl(uploadPictureResult.getUrl());
        picture.setName(uploadPictureResult.getName());
        picture.setSize(uploadPictureResult.getSize());
        picture.setWidth(uploadPictureResult.getWidth());
        picture.setHeight(uploadPictureResult.getHeight());
        picture.setScale(uploadPictureResult.getScale());
        picture.setFormat(uploadPictureResult.getFormat());
        picture.setUserId(loginUser.getId());
        // 判断是新增还是修改
        if (pictureId != null) {
            picture.setId(pictureId);
        }
        boolean result = this.saveOrUpdate(picture);
        ThrowUtil.throwIf(!result, ErrorCodeEnum.OPERATION_ERROR, "数据库操作失败！");
        return ResultUtil.success(PictureVO.objToVo(picture));
    }
}




