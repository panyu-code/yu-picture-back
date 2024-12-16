package com.panyu.yupictureback.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-14 20:32
 **/
@Data
@Accessors(chain = true)
@ApiModel("用户登录VO")
public class UserLoginVO implements Serializable {
    private static final long serialVersionUID = -8395462806683916314L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    public Long id;

    /**
     * 用户名(账号)
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 性别(1：男  0：女)
     */
    @ApiModelProperty("性别 1：男  0：女")
    private Integer gender;

    /**
     * 角色(1：管理员  2：普通用户)
     */
    @ApiModelProperty("角色 1：管理员  2：普通用户")
    private Integer role;

    /**
     * 年龄
     */
    @ApiModelProperty("年龄")
    private Integer age;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}