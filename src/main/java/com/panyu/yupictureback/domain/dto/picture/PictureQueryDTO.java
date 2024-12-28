package com.panyu.yupictureback.domain.dto.picture;

import com.panyu.yupictureback.common.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-28 23:51
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PictureQueryDTO extends PageDTO implements Serializable {


    /**
     * 图片名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 文件体积
     */
    private Long size;

    /**
     * 图片宽度
     */
    private Integer width;

    /**
     * 图片高度
     */
    private Integer height;

    /**
     * 图片比例
     */
    private Double scale;

    /**
     * 图片格式
     */
    private String format;

    /**
     * 搜索词（同时搜名称、简介等）
     */
    private String searchText;

    /**
     * 用户 id
     */
    private Long userId;

    private String[] createTimeRange;

    private static final long serialVersionUID = 1L;
}
