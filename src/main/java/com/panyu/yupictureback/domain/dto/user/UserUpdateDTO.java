package com.panyu.yupictureback.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc: 用户更新DTO
 * @create: 2024-12-20 22:08
 **/
@Data
@Accessors(chain = true)
@ApiModel("更新用户DTO")
public class UserUpdateDTO implements Serializable {

    private static final long serialVersionUID = 3897547517688924259L;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long id;

    /**
     * 用户名(账号)
     */
    @ApiModelProperty("用户名")
    @Size(min = 2, max = 10, message = "用户名长度为2-10位")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    @Size(min = 2, max = 10, message = "昵称长度为2-6位")
    private String nickname;
    /**
     * 头像
     */
    @ApiModelProperty("头像")
    @NotBlank
    private String avatar;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    @NotNull
    private Integer gender;
    /**
     * 年龄
     */
    @ApiModelProperty("年龄")
    @NotNull
    private Integer age;
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

}
