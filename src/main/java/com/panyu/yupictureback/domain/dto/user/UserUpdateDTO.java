package com.panyu.yupictureback.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-20 22:08
 **/
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO implements Serializable {

    private static final long serialVersionUID = 3897547517688924259L;

    private Long id;

    @Size(min = 2, max = 10, message = "用户名长度为2-10位")
    private String username;

    @Size(min = 2, max = 10, message = "昵称长度为2-6位")
    private String nickname;

    @NotBlank
    private String avatar;

    @NotNull
    private Integer gender;

    @NotNull
    private Integer age;

    private String phone;

    private String email;

}
