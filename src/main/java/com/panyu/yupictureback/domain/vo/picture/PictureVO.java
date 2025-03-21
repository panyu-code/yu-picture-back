package com.panyu.yupictureback.domain.vo.picture;

import cn.hutool.json.JSONUtil;
import com.panyu.yupictureback.domain.entity.Picture;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-22 19:12
 **/
@Data
@Accessors(chain = true)
@ApiModel(value = "图片VO")
public class PictureVO implements Serializable {

    private static final long serialVersionUID = -1006295310357180294L;
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 图片 url
     */
    @ApiModelProperty(value = "图片 url")
    private String url;

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
     * 标签
     */
    @ApiModelProperty(value = "标签")
    private List<String> tags;

    /**
     * 分类
     */
    @ApiModelProperty(value = "分类")
    private String category;

    /**
     * 文件体积
     */
    @ApiModelProperty(value = "文件体积")
    private Long size;

    /**
     * 图片宽度
     */
    @ApiModelProperty(value = "图片宽度")
    private Integer width;

    /**
     * 图片高度
     */
    @ApiModelProperty(value = "图片高度")
    private Integer height;

    /**
     * 图片比例
     */
    @ApiModelProperty(value = "图片比例")
    private Double scale;

    /**
     * 图片格式
     */
    @ApiModelProperty(value = "图片格式")
    private String format;

    /**
     * 用户 id
     */
    @ApiModelProperty(value = "用户 id")
    private Long userId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    /**
     * 创建用户信息
     */
    @ApiModelProperty(value = "创建用户信息")
    private UserLoginVO user;


    /**
     * 封装类转对象
     */
    public static Picture voToObj(PictureVO pictureVO) {
        if (pictureVO == null) {
            return null;
        }
        Picture picture = new Picture();
        BeanUtils.copyProperties(pictureVO, picture);
        // 类型不同，需要转换  
        picture.setTags(JSONUtil.toJsonStr(pictureVO.getTags()));
        return picture;
    }

    /**
     * 对象转封装类
     */
    public static PictureVO objToVo(Picture picture) {
        if (picture == null) {
            return null;
        }
        PictureVO pictureVO = new PictureVO();
        BeanUtils.copyProperties(picture, pictureVO);
        // 类型不同，需要转换  
        pictureVO.setTags(JSONUtil.toList(picture.getTags(), String.class));
        return pictureVO;
    }
}
