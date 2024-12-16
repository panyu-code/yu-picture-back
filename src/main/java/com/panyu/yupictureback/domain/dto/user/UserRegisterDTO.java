package com.panyu.yupictureback.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc: 用户注册DTO
 * @create: 2024-12-14 18:56
 **/
@Data
@Accessors(chain = true)
@ApiModel("用户注册DTO")
public class UserRegisterDTO implements Serializable {

    private static final long serialVersionUID = -417362913792638993L;
    /**
     * 用户名(账号)
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 确认密码
     */
    @ApiModelProperty("确认密码")
    private String rePassword;
}
