package com.top.mini.happy.down.mybatis.dialect;

public interface Dialect {

    String getPageSql(String sql, int offset, int limit);

    String getCountSql(String sql);
}
