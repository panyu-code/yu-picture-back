package com.panyu.yupictureback.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc: 管理员权限
 * @create: 2024-12-20 22:03
 **/
@ApiModel("新增用户DTO")
@Data
@Accessors(chain = true)
public class UserAddDTO implements Serializable {

    private static final long serialVersionUID = -3446676553781421068L;
    /**
     * 用户名(账号)
     */
    @ApiModelProperty("用户名")
    @Size(min = 2, max = 10, message = "用户名长度为2-10位")
    private String username;

}
