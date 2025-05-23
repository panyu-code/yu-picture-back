package com.panyu.yupictureback.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panyu.yupictureback.annotation.AuthCheck;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.domain.dto.picture.*;
import com.panyu.yupictureback.domain.entity.Picture;
import com.panyu.yupictureback.domain.vo.picture.PictureTagCategoryVO;
import com.panyu.yupictureback.domain.vo.picture.PictureVO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.enums.UserRoleEnum;
import com.panyu.yupictureback.service.PictureService;
import com.panyu.yupictureback.utils.ResultUtil;
import com.panyu.yupictureback.utils.ThrowUtil;
import com.panyu.yupictureback.utils.UserContextUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author: YuPan
 * @Desc: PictureController
 * @create: 2024-12-22 20:54
 **/
@RestController
@Slf4j
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 图片上传（文件）
     *
     * @param multipartFile
     * @param pictureUploadDTO
     * @return
     */
    @ApiOperation("图片上传（文件）")
    @PostMapping("/upload-file")
    public ResponseResult<PictureVO> uploadPicture(@RequestPart("file") MultipartFile multipartFile,
                                                   PictureUploadDTO pictureUploadDTO) {
        UserLoginVO loginUser = UserContextUtil.getCurrentUser();
        return pictureService.uploadPicture(multipartFile, pictureUploadDTO, loginUser);
    }

    /**
     * 图片上传（url)
     *
     * @param url
     * @param pictureUploadDTO
     * @return
     */
    @ApiOperation("图片上传（url)")
    @PostMapping("/upload-url")
    public ResponseResult<PictureVO> uploadPictureByUrl(String url,  PictureUploadDTO pictureUploadDTO) {
        UserLoginVO loginUser = UserContextUtil.getCurrentUser();
        return pictureService.uploadPicture(url, pictureUploadDTO, loginUser);
    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    @ApiOperation("删除图片")
    @DeleteMapping("/delete/{id}")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Boolean> deletePicture(@PathVariable @NotNull Long id) {
        return pictureService.deletePicture(id);
    }

    /**
     * 更新图片（仅管理员可用）
     *
     * @param pictureUpdateDTO
     * @return
     */
    @ApiOperation("更新图片（仅管理员可用）")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Boolean> updatePicture(@RequestBody @Validated PictureUpdateDTO pictureUpdateDTO) {
        return pictureService.updatePicture(pictureUpdateDTO);
    }

    /**
     * 根据 id 获取图片详细信息（仅管理员可用）
     *
     * @param id
     * @return
     */
    @ApiOperation("根据 id 获取图片详细信息（仅管理员可用）")
    @GetMapping("/get/{id}")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Picture> getPictureById(@PathVariable @NotNull Long id) {
        return pictureService.getPictureById(id);
    }

    /**
     * 根据 id 获取图片（封装类）
     *
     * @param id
     * @return
     */
    @ApiOperation("根据 id 获取图片（封装类）")
    @GetMapping("/get/vo/{id}")
    public ResponseResult<PictureVO> getPictureVOById(@PathVariable @NotNull Long id) {
        return pictureService.getPictureVOById(id);
    }

    /**
     * 分页获取图片列表（仅管理员可用）
     *
     * @param pictureQueryDTO
     * @return
     */
    @ApiOperation("分页获取图片列表（仅管理员可用）")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Page<Picture>> listPictureByPage(@RequestBody @Validated PictureQueryDTO pictureQueryDTO) {
        return pictureService.listPictureByPage(pictureQueryDTO);
    }

    /**
     * 分页获取图片列表（封装类）
     *
     * @param pictureQueryDTO
     * @return
     */
    @ApiOperation("分页获取图片列表（封装类）")
    @PostMapping("/list/page/vo")
    public ResponseResult<Page<PictureVO>> listPictureVOByPage(@RequestBody @Validated PictureQueryDTO pictureQueryDTO) {
        return pictureService.listPictureVOByPage(pictureQueryDTO);
    }

    /**
     * 编辑图片（给用户使用）
     *
     * @param pictureEditDTO
     * @return
     */
    @ApiOperation("编辑图片（给用户使用）")
    @PostMapping("/edit")
    public ResponseResult<Boolean> editPicture(@RequestBody @Validated PictureEditDTO pictureEditDTO) {
        return pictureService.editPicture(pictureEditDTO);
    }

    /**
     * 获取图片标签和分类列表
     *
     * @return
     */
    @ApiOperation("获取图片标签和分类列表")
    @GetMapping("/tag_category")
    public ResponseResult<PictureTagCategoryVO> listPictureTagCategory() {
        return pictureService.listPictureTagCategory();
    }

    /**
     * 图片审核
     *
     * @param pictureReviewDTO
     * @return
     */
    @ApiOperation("图片审核")
    @PostMapping("/review")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Boolean> doPictureReview(@RequestBody PictureReviewDTO pictureReviewDTO) {
        ThrowUtil.throwIf(pictureReviewDTO == null, ErrorCodeEnum.PARAMS_ERROR);
        UserLoginVO loginUser = UserContextUtil.getCurrentUser();
        pictureService.doPictureReview(pictureReviewDTO, loginUser);
        return ResultUtil.success(true);
    }

    /**
     * 批量抓取图片
     *
     * @param pictureBatchUploadDTO
     * @return
     */
    @ApiOperation("批量上传图片")
    @PostMapping("/batchUpload")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Integer> batchUpload(@RequestBody PictureBatchUploadDTO pictureBatchUploadDTO) {
        ThrowUtil.throwIf(pictureBatchUploadDTO == null, ErrorCodeEnum.PARAMS_ERROR);
        UserLoginVO loginUser = UserContextUtil.getCurrentUser();
        Integer count = pictureService.batchUploadPicture(pictureBatchUploadDTO, loginUser);
        return ResultUtil.success(count);
    }

}
