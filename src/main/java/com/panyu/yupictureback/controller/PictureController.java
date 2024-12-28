package com.panyu.yupictureback.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panyu.yupictureback.annotation.AuthCheck;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.domain.dto.picture.PictureEditDTO;
import com.panyu.yupictureback.domain.dto.picture.PictureQueryDTO;
import com.panyu.yupictureback.domain.dto.picture.PictureUpdateDTO;
import com.panyu.yupictureback.domain.entity.Picture;
import com.panyu.yupictureback.domain.vo.picture.PictureTagCategoryVO;
import com.panyu.yupictureback.domain.vo.picture.PictureVO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.UserRoleEnum;
import com.panyu.yupictureback.service.PictureService;
import com.panyu.yupictureback.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-22 20:54
 **/
@RestController
@Slf4j
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private PictureService pictureService;

    @Resource
    private UserService userService;

    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @ApiOperation("图片上传")
    @PostMapping("/upload")
    public ResponseResult<PictureVO> uploadPicture(@RequestPart("file") MultipartFile multipartFile, Long pictureId) {
        UserLoginVO loginUser = userService.getCurrentUser().getData();
        return pictureService.uploadPicture(multipartFile, pictureId, loginUser);
    }

    /**
     * 删除图片
     */
    @ApiOperation("删除图片")
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Boolean> deletePicture(@PathVariable @NotNull Long id) {
        return pictureService.deletePicture(id);
    }

    /**
     * 更新图片（仅管理员可用）
     */
    @ApiOperation("更新图片（仅管理员可用）")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Boolean> updatePicture(@RequestBody @Validated PictureUpdateDTO pictureUpdateDTO) {
        return pictureService.updatePicture(pictureUpdateDTO);
    }

    /**
     * 根据 id 获取图片详细信息（仅管理员可用）
     */
    @ApiOperation("根据 id 获取图片详细信息（仅管理员可用）")
    @GetMapping("/get/{id}")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Picture> getPictureById(@PathVariable @NotNull Long id) {
        return pictureService.getPictureById(id);
    }

    /**
     * 根据 id 获取图片（封装类）
     */
    @ApiOperation("根据 id 获取图片（封装类）")
    @GetMapping("/get/vo/{id}")
    public ResponseResult<PictureVO> getPictureVOById(@PathVariable @NotNull Long id) {
        return pictureService.getPictureVOById(id);
    }

    /**
     * 分页获取图片列表（仅管理员可用）
     */
    @ApiOperation("分页获取图片列表（仅管理员可用）")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public ResponseResult<Page<Picture>> listPictureByPage(@RequestBody @Validated PictureQueryDTO pictureQueryDTO) {
        return pictureService.listPictureByPage(pictureQueryDTO);
    }

    /**
     * 分页获取图片列表（封装类）
     */
    @ApiOperation("分页获取图片列表（封装类）")
    @PostMapping("/list/page/vo")
    public ResponseResult<Page<PictureVO>> listPictureVOByPage(@RequestBody @Validated PictureQueryDTO pictureQueryDTO) {
        return pictureService.listPictureVOByPage(pictureQueryDTO);
    }

    /**
     * 编辑图片（给用户使用）
     */
    @ApiOperation("编辑图片（给用户使用）")
    @PostMapping("/edit")
    public ResponseResult<Boolean> editPicture(@RequestBody @Validated PictureEditDTO pictureEditDTO) {
        return pictureService.editPicture(pictureEditDTO);
    }

    @ApiOperation("获取图片标签和分类列表")
    @GetMapping("/tag_category")
    public ResponseResult<PictureTagCategoryVO> listPictureTagCategory() {
        return pictureService.listPictureTagCategory();
    }


}
