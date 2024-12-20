package com.panyu.yupictureback.domain.dto.user;

import com.panyu.yupictureback.common.PageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: YuPan
 * @Desc: 可以根据用户名、昵称，以及创建时间进行查询
 * @create: 2024-12-20 22:12
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = 1778906536777047040L;

    private String username;

    private String nickname;

    private LocalDateTime createTimeFrom;

    private LocalDateTime createTimeTo;
}
