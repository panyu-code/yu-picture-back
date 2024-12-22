package com.panyu.yupictureback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.domain.entity.Picture;
import com.panyu.yupictureback.domain.vo.picture.PictureVO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yupan
 * @description 针对表【picture(图片)】的数据库操作Service
 * @createDate 2024-12-22 17:04:15
 */
public interface PictureService extends IService<Picture> {
    /**
     * 上传图片
     *
     * @param multipartFile
     * @param pictureId
     * @param loginUser
     * @return
     */
    ResponseResult<PictureVO> uploadPicture(MultipartFile multipartFile, Long pictureId, UserLoginVO loginUser);
}
