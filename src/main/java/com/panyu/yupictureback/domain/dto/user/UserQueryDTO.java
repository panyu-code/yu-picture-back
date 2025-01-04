package com.panyu.yupictureback.domain.dto.user;

import com.panyu.yupictureback.common.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc: 可以根据用户名、昵称，以及创建时间进行查询
 * @create: 2024-12-20 22:12
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ApiModel("用户查询DTO")
public class UserQueryDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = 1778906536777047040L;
    /**
     * 用户名(账号)
     */
    @ApiModelProperty("用户名(账号)")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;
    /**
     * 创建时间范围
     */
    @ApiModelProperty("创建时间范围")
    private String[] createTimeRange;
}
