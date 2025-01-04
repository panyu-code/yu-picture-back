package com.panyu.yupictureback.domain.dto.picture;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-22 19:41
 **/
@Data
@Accessors(chain = true)
@ApiModel(value = "图片上传DTO")
public class PictureUploadDTO implements Serializable {

    private static final long serialVersionUID = 5525856739220528439L;
    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String url;

    /**
     * 图片名称
     */
    @ApiModelProperty(value = "图片名称")
    private String name;

    /**
     * 文件体积
     */
    @ApiModelProperty(value = "文件体积")
    private Long size;

    /**
     * 图片宽度
     */
    @ApiModelProperty(value = "图片宽度")
    private int width;

    /**
     * 图片高度
     */
    @ApiModelProperty(value = "图片高度")
    private int height;

    /**
     * 图片宽高比
     */
    @ApiModelProperty(value = "图片宽高比")
    private Double scale;

    /**
     * 图片格式
     */
    @ApiModelProperty(value = "图片格式")
    private String format;

}
