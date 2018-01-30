package com.top.mini.happy.down.form;

import lombok.Data;

/**
 * @author xugang on 18/1/30.
 */
@Data
public class PageForm {
    /**
     * 页码,页编码从1开始
     */
    private int pageNumber = 1;
    /**
     * 每页数目大小
     */
    private int pageSize = 10;
}
