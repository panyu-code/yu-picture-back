package com.panyu.yupictureback.domain.dto.picture;

import com.panyu.yupictureback.common.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author: YuPan
 * @Desc: 图片查询DTO
 * @create: 2024-12-28 23:51
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ApiModel("图片查询DTO")
public class PictureQueryDTO extends PageDTO implements Serializable {


    /**
     * 图片名称
     */
    @ApiModelProperty("图片名称")
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

    /**
     * 文件体积
     */
    @ApiModelProperty("文件体积")
    private Long size;

    /**
     * 图片宽度
     */
    @ApiModelProperty("图片宽度")
    private Integer width;

    /**
     * 图片高度
     */
    @ApiModelProperty("图片高度")
    private Integer height;

    /**
     * 图片比例
     */
    @ApiModelProperty("图片比例")
    private Double scale;

    /**
     * 图片格式
     */
    @ApiModelProperty("图片格式")
    private String format;

    /**
     * 搜索词（同时搜名称、简介等）
     */
    @ApiModelProperty("搜索词（同时搜名称、简介等）")
    private String searchText;

    /**
     * 用户 id
     */
    @ApiModelProperty("用户 id")
    private Long userId;

    /**
     * 创建时间范围
     */
    @ApiModelProperty("创建时间范围")
    private String[] createTimeRange;

    /**
     * 状态：0-待审核; 1-通过; 2-拒绝
     */
    @ApiModelProperty("状态：0-待审核; 1-通过; 2-拒绝")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    @ApiModelProperty("审核信息")
    private String reviewMessage;

    /**
     * 审核人 id
     */
    @ApiModelProperty("审核人 id")
    private Long reviewerId;


    private static final long serialVersionUID = 1L;
}
