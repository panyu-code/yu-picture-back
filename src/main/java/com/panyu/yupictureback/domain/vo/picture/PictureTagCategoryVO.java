package com.panyu.yupictureback.domain.vo.picture;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-29 00:54
 **/
@Data
@Accessors(chain = true)
@ApiModel(value = "图片标签分类VO")
public class PictureTagCategoryVO implements Serializable {
    private static final long serialVersionUID = 8062576797222243104L;

    /**
     * 标签列表
     */
    @ApiModelProperty(value = "标签列表")
    private List<String> tagList;
    /**
     * 分类列表
     */
    @ApiModelProperty(value = "分类列表")
    private List<String> categoryList;
}
