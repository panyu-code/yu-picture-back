package com.panyu.yupictureback.domain.dto.picture;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author: YuPan
 * @Desc: 普通用户修改图片信息
 * @create: 2024-12-28 23:50
 **/
@Data
@Accessors(chain = true)
@ApiModel("普通用户修改图片信息")
public class PictureEditDTO implements Serializable {

    /**
     * id
     */
    @ApiModelProperty("id")
    @NotNull
    private Long id;

    /**
     * 图片名称
     */
    @ApiModelProperty("图片名称")
    @NotBlank
    private String name;

    /**
     * 简介
     */
    @ApiModelProperty("简介")
    private String introduction;

    /**
     * 分类
     */
    @ApiModelProperty("分类")
    private String category;

    /**
     * 标签
     */
    @ApiModelProperty("标签")
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}
