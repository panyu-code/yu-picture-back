package com.panyu.yupictureback.domain.dto.picture;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2025-01-02 22:00
 **/
@Data
@Accessors(chain = true)
public class PictureReviewDTO implements Serializable {

    private static final long serialVersionUID = 8013798506902826778L;
    /**
     * id
     */
    private Long id;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;


}
