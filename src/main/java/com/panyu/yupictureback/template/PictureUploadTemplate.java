package com.panyu.yupictureback.template;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.panyu.yupictureback.config.CosClientConfig;
import com.panyu.yupictureback.domain.dto.picture.PictureUploadDTO;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.exception.BusinessException;
import com.panyu.yupictureback.manager.CosManager;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2025-01-04 22:59
 **/
@Slf4j
public abstract class PictureUploadTemplate {
    @Resource
    private CosClientConfig cosClientConfig;
    @Resource
    private CosManager cosManager;

    /**
     * 上传图片
     *
     * @param inputSource      文件
     * @param uploadPathPrefix 上传路径前缀
     * @return
     */
    public PictureUploadDTO uploadPicture(Object inputSource, String uploadPathPrefix) {
        // 校验图片
        validInputSource(inputSource);
        // 图片上传地址
        String uuid = RandomUtil.randomString(16);
        String originFilename = getOriginalInputSourceName(inputSource);
        // 定义上传文件名
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid, FileUtil.getSuffix(originFilename));
        // 定义上传路径
        String uploadPath = String.format("%s/%s", uploadPathPrefix, uploadFilename);
        File file = null;
        try {
            // 创建临时文件
            file = File.createTempFile(uploadPath, null);
            processFile(inputSource, file);
            // 上传图片
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            // 封装返回结果
            return getPictureUploadDTO(imageInfo, originFilename, file, uploadPath);
        } catch (Exception e) {
            log.error("图片上传到对象存储失败", e);
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, "上传失败");
        } finally {
            this.deleteTempFile(file);
        }
    }


    /**
     * 处理输入源并处理临时文件
     *
     * @param inputSource
     * @param file
     */
    protected abstract void processFile(Object inputSource, File file);

    /**
     * 获取输入源的原始文件名
     *
     * @param inputSource
     * @return
     */
    protected abstract String getOriginalInputSourceName(Object inputSource);

    /**
     * 校验输入源
     *
     * @param inputSource
     */
    protected abstract void validInputSource(Object inputSource);


    /**
     * 删除临时文件
     */
    public void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        // 删除临时文件
        boolean deleteResult = file.delete();
        if (!deleteResult) {
            log.error("file delete error, filepath = {}", file.getAbsolutePath());
        }
    }

    /**
     * 封装返回结果
     *
     * @param imageInfo
     * @param originFilename
     * @param file
     * @param uploadPath
     * @return
     */
    private PictureUploadDTO getPictureUploadDTO(ImageInfo imageInfo, String originFilename, File file, String uploadPath) {
        PictureUploadDTO uploadPictureResult = new PictureUploadDTO();
        int picWidth = imageInfo.getWidth();
        int picHeight = imageInfo.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setName(FileUtil.mainName(originFilename));
        uploadPictureResult.setWidth(picWidth);
        uploadPictureResult.setHeight(picHeight);
        uploadPictureResult.setScale(picScale);
        uploadPictureResult.setFormat(imageInfo.getFormat());
        uploadPictureResult.setSize(FileUtil.size(file));
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
        return uploadPictureResult;
    }

}
