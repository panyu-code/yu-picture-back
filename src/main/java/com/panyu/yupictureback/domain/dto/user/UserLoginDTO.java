package com.panyu.yupictureback.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-14 20:20
 **/
@ApiModel("用户登录DTO")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
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
    @NotBlank
    @ApiModelProperty("密码")
    private String password;

}
