package com.panyu.yupictureback.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc: 用户注册DTO
 * @create: 2024-12-14 18:56
 **/
@ApiModel("用户注册DTO")
@Data
@Accessors(chain = true)
public class UserRegisterDTO implements Serializable {

    private static final long serialVersionUID = -417362913792638993L;
    /**
     * 用户名(账号)
     */
    @ApiModelProperty("用户名")
    @Size(min = 2, max = 10, message = "用户名长度为2-10位")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$", message = "密码必须包含字母和数字，长度为8-20位")
    private String password;

    /**
     * 确认密码
     */
    @ApiModelProperty("确认密码")
    private String rePassword;
}
