package com.panyu.yupictureback.controller;

import com.panyu.yupictureback.annotation.AuthCheck;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.domain.vo.picture.PictureVO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.UserRoleEnum;
import com.panyu.yupictureback.service.PictureService;
import com.panyu.yupictureback.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

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
}
