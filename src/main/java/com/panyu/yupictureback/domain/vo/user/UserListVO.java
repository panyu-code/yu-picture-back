package com.panyu.yupictureback.domain.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-20 23:04
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class UserListVO extends UserLoginVO implements Serializable {

    private static final long serialVersionUID = -8778539987710081235L;
}
