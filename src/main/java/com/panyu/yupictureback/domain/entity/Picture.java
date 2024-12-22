package com.panyu.yupictureback.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 图片
 *
 * @TableName picture
 */
@TableName(value = "picture")
@Data
@Accessors(chain = true)
public class Picture implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1859443733973518457L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 图片 url
     */
    @TableField(value = "url")
    private String url;

    /**
     * 图片名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 简介
     */
    @TableField(value = "introduction")
    private String introduction;

    /**
     * 分类
     */
    @TableField(value = "category")
    private String category;

    /**
     * 标签（JSON 数组）
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 图片体积
     */
    @TableField(value = "size")
    private Long size;

    /**
     * 图片宽度
     */
    @TableField(value = "width")
    private Integer width;

    /**
     * 图片高度
     */
    @TableField(value = "height")
    private Integer height;

    /**
     * 图片宽高比例
     */
    @TableField(value = "scale")
    private Double scale;

    /**
     * 图片格式
     */
    @TableField(value = "format")
    private String format;

    /**
     * 创建用户 id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private String updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;


}