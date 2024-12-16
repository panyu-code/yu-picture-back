package com.panyu.yupictureback.common;

import com.panyu.yupictureback.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc: 分页对象
 * @create: 2024-12-08 16:06
 **/
@Data
@ApiModel("分页DTO")
public class PageDTO implements Serializable {

    private static final long serialVersionUID = -4021893265557824550L;
    /**
     * 当前页号
     */
    @ApiModelProperty("当前页号")
    private int current = 1;

    /**
     * 页面大小
     */
    @ApiModelProperty("当前页码")
    private int pageSize = 10;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    @ApiModelProperty("排序方式")
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
