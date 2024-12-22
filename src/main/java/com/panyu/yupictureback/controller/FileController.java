package com.panyu.yupictureback.controller;

import com.panyu.yupictureback.annotation.AuthCheck;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.enums.UserRoleEnum;
import com.panyu.yupictureback.exception.BusinessException;
import com.panyu.yupictureback.manager.CosManager;
import com.panyu.yupictureback.utils.ResultUtil;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-22 18:32
 **/
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    @Resource
    private CosManager cosManager;

    /**
     * 测试文件上传
     *
     * @param file
     * @return
     */
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @PostMapping("/test/upload")
    public ResponseResult<String> testUpload(@RequestPart("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String filePath = String.format("/test/%s", fileName);
        File tempFile = null;
        try {
            tempFile = File.createTempFile(filePath, null);
            file.transferTo(tempFile);
            cosManager.putObject(filePath, tempFile);
            return ResultUtil.success(filePath);
        } catch (IOException e) {
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, "上传失败！");
        } finally {
            if (tempFile != null) {
                try {
                    Files.delete(tempFile.toPath());
                } catch (IOException e) {
                    log.error("删除临时文件失败！", e);
                }
            }
        }
    }


    /**
     * 测试文件下载
     *
     * @param filepath 文件路径
     * @param response 响应对象
     */
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    @GetMapping("/test/download/")
    public void testDownloadFile(String filepath, HttpServletResponse response) throws IOException {
        COSObjectInputStream cosObjectInput = null;
        try {
            COSObject cosObject = cosManager.getObject(filepath);
            cosObjectInput = cosObject.getObjectContent();
            // 处理下载到的流
            byte[] bytes = IOUtils.toByteArray(cosObjectInput);
            // 设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filepath);
            // 写入响应
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("file download error, filepath = {}", filepath, e);
            throw new BusinessException(ErrorCodeEnum.SYSTEM_ERROR, "下载失败");
        } finally {
            if (cosObjectInput != null) {
                cosObjectInput.close();
            }
        }
    }

}



