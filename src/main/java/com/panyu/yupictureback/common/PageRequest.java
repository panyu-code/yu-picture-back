package com.panyu.yupictureback.common;

import com.panyu.yupictureback.constant.CommonConstant;
import lombok.Data;

/**
 * @author: YuPan
 * @Desc: 分页对象
 * @create: 2024-12-08 16:06
 **/
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
