package com.panyu.yupictureback.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc: 用户登录DTO
 * @create: 2024-12-14 20:20
 **/
@ApiModel("用户登录DTO")
@Data
@Accessors(chain = true)
public class UserLoginDTO implements Serializable {
    private static final long serialVersionUID = -7383552998314109572L;

    /**
     * 用户名(账号)
     */
    @ApiModelProperty("用户名")
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @NotBlank
    private String password;

}
