package com.panyu.yupictureback.domain.vo.picture;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author: YuPan
 * @Desc:
 * @create: 2024-12-29 00:54
 **/
@Data
@Accessors(chain = true)
public class PictureTagCategoryVO implements Serializable {
    private static final long serialVersionUID = 8062576797222243104L;

    private List<String> tagList;

    private List<String> categoryList;
}
