package com.panyu.yupictureback.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-14 20:32
 **/
@ApiModel("用户登录VO")
@Data
@Accessors(chain = true)
public class UserLoginVO implements Serializable {
    @Serial
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
    private String nickname;

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
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

}
