package com.panyu.yupictureback.domain.dto.picture;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author: YuPan
 * @Desc: 管理员更新图片信息
 * @create: 2024-12-28 23:48
 **/
@Data
@Accessors(chain = true)
@ApiModel(value = "管理员更新图片信息")
public class PictureUpdateDTO implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;

    /**
     * 图片名称
     */
    @ApiModelProperty(value = "图片名称")
    private String name;

    /**
     * 简介
     */
    @ApiModelProperty(value = "简介")
    private String introduction;

    /**
     * 分类
     */
    @ApiModelProperty(value = "分类")
    private String category;

    /**
     * 标签
     */
    @ApiModelProperty(value = "标签")
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}

