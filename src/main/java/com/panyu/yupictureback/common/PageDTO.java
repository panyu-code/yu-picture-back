package com.panyu.yupictureback.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
    @NotNull
    @ApiModelProperty("当前页号")
    private int current;

    /**
     * 页面大小
     */
    @NotNull
    @ApiModelProperty("当前页码")
    private int pageSize;

}
