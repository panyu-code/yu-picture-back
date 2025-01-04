package com.panyu.yupictureback.manager.upload;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.exception.BusinessException;
import com.panyu.yupictureback.template.PictureUploadTemplate;
import com.panyu.yupictureback.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * @author: YuPan
 * @Desc: URL图片上传
 * @create: 2025-01-04 23:29
 **/
@Service
@Slf4j
public class UrlPictureUpload extends PictureUploadTemplate {
    private static final List<String> ALLOW_CONTENT_TYPE_LIST = Arrays.asList("image/jpeg", "image/jpg", "image/png",
            "image/webp");

    @Override
    protected void processFile(Object inputSource, File file) {
        HttpUtil.downloadFile((String) inputSource, file);
    }

    @Override
    protected String getOriginalInputSourceName(Object inputSource) {
        return FileUtil.mainName((String) inputSource);
    }

    @Override
    protected void validInputSource(Object inputSource) {
        String url = (String) inputSource;
        ThrowUtil.throwIf(CharSequenceUtil.isBlank(url), ErrorCodeEnum.PARAMS_ERROR, "文件地址不能为空");
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "url 格式错误");
        }
        ThrowUtil.throwIf(!HttpUtil.isHttp(url) && !HttpUtil.isHttps(url), ErrorCodeEnum.PARAMS_ERROR, "url格式错误");
        try (HttpResponse response = HttpUtil.createRequest(Method.HEAD, url).execute()) {
            if (!response.isOk()) {
                return;
            }
            String contentType = response.header("Content-Type");
            if (CharSequenceUtil.isNotBlank(contentType)) {
                ThrowUtil.throwIf(!ALLOW_CONTENT_TYPE_LIST.contains(contentType), ErrorCodeEnum.PARAMS_ERROR, "文件类型错误");
            }
            String contentLengthStr = response.header("Content-Length");
            if (CharSequenceUtil.isNotBlank(contentLengthStr)) {
                Long contentLength = Convert.toLong(contentLengthStr);
                final long ONE_M = 1024 * 1024L;
                ThrowUtil.throwIf(contentLength > 2 * ONE_M, ErrorCodeEnum.PARAMS_ERROR, "文件大小不能超过 2M");
            }
        } catch (Exception e) {
            log.error("url校验失败", e);
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "url校验失败");
        }
    }
}
