package com.panyu.yupictureback.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.domain.dto.picture.*;
import com.panyu.yupictureback.domain.entity.Picture;
import com.panyu.yupictureback.domain.vo.picture.PictureTagCategoryVO;
import com.panyu.yupictureback.domain.vo.picture.PictureVO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;

/**
 * @author yupan
 * @description 针对表【picture(图片)】的数据库操作Service
 * @createDate 2024-12-22 17:04:15
 */
public interface PictureService extends IService<Picture> {
    /**
     * 上传图片
     *
     * @param inputSource
     * @param pictureId
     * @param loginUser
     * @return
     */
    ResponseResult<PictureVO> uploadPicture(Object inputSource, Long pictureId, UserLoginVO loginUser);


    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    ResponseResult<Boolean> deletePicture(Long id);

    /**
     * 更新图片（仅管理员）
     *
     * @param pictureUpdateDTO
     * @return
     */
    ResponseResult<Boolean> updatePicture(PictureUpdateDTO pictureUpdateDTO);

    /**
     * 获取单个图片（仅管理员）
     *
     * @param id
     * @return
     */
    ResponseResult<Picture> getPictureById(Long id);

    /**
     * 获取单个图片
     *
     * @param id
     * @return
     */
    ResponseResult<PictureVO> getPictureVOById(Long id);

    /**
     * 获取图片列表（仅管理员）
     *
     * @param pictureQueryDTO
     * @return
     */
    ResponseResult<Page<Picture>> listPictureByPage(PictureQueryDTO pictureQueryDTO);

    /**
     * 编辑图片（用户使用）
     *
     * @param pictureEditDTO
     * @return
     */
    ResponseResult<Boolean> editPicture(PictureEditDTO pictureEditDTO);

    /**
     * 获取图片列表（用户使用）
     *
     * @param pictureQueryDTO
     * @return
     */
    ResponseResult<Page<PictureVO>> listPictureVOByPage(PictureQueryDTO pictureQueryDTO);

    /**
     * 获取图片标签分类
     *
     * @return
     */
    ResponseResult<PictureTagCategoryVO> listPictureTagCategory();

    /**
     * 获取图片VO对象
     *
     * @param picture
     * @return
     */
    PictureVO getPictureVO(Picture picture);

    /**
     * 获取图片VO分页对象
     *
     * @param picturePage
     * @return
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage);

    /**
     * 查询条件
     *
     * @param pictureQueryDTO
     * @return
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryDTO pictureQueryDTO);

    /**
     * 图片校验
     *
     * @param picture
     */
    void validPicture(Picture picture);

    /**
     * 图片审核
     *
     * @param pictureReviewRequest
     * @param loginUser
     */
    void doPictureReview(PictureReviewDTO pictureReviewRequest, UserLoginVO loginUser);

    /**
     * 填充审核参数
     *
     * @param picture
     * @param loginUser
     */
    void fillReviewParams(Picture picture, UserLoginVO loginUser);

    /**
     * 批量上传图片
     * @param pictureBatchUploadDTO
     * @param loginUser
     * @return
     */
    Integer batchUploadPicture(PictureBatchUploadDTO pictureBatchUploadDTO, UserLoginVO loginUser);

}
