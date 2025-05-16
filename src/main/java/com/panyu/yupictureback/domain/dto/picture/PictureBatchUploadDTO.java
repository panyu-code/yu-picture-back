package com.panyu.yupictureback.domain.dto.picture;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: YuPan
 * @desc:
 * @create: 2025-04-06 15:44
 **/
@ApiModel("批量上传图片DTO")
@Accessors(chain = true)
@Data
public class PictureBatchUploadDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1182444004541237525L;

    @ApiModelProperty(value = "搜索关键词")
    private String searchText;

    @ApiModelProperty(value = "批量上传数量")
    private Integer batchSize = 10;

}
