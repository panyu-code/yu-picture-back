package com.panyu.yupictureback.manager.upload;

import cn.hutool.core.io.FileUtil;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.template.PictureUploadTemplate;
import com.panyu.yupictureback.utils.ThrowUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author: YuPan
 * @Desc: 文件图片上传
 * @create: 2025-01-04 23:21
 **/
@Service
public class FilePictureUpload extends PictureUploadTemplate {

    private static final List<String> ALLOW_FORMAT_LIST = Arrays.asList("jpeg", "jpg", "png", "webp");

    @SneakyThrows
    @Override
    protected void processFile(Object inputSource, File file) {
        MultipartFile multipartFile = (MultipartFile) inputSource;
        multipartFile.transferTo(file);
    }

    @Override
    protected String getOriginalInputSourceName(Object inputSource) {
        MultipartFile multipartFile = (MultipartFile) inputSource;
        return multipartFile.getOriginalFilename();
    }

    @Override
    protected void validInputSource(Object inputSource) {
        MultipartFile multipartFile = (MultipartFile) inputSource;
        ThrowUtil.throwIf(multipartFile == null, ErrorCodeEnum.PARAMS_ERROR, "文件不能为空");
        // 1. 校验文件大小
        long fileSize = multipartFile.getSize();
        final long ONE_M = 1024 * 1024L;
        ThrowUtil.throwIf(fileSize > 2 * ONE_M, ErrorCodeEnum.PARAMS_ERROR, "文件大小不能超过 2M");
        // 2. 校验文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        // 允许上传的文件后缀
        ThrowUtil.throwIf(!ALLOW_FORMAT_LIST.contains(fileSuffix), ErrorCodeEnum.PARAMS_ERROR, "文件类型错误");
    }
}
