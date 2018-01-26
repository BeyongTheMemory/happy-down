package com.top.mini.happy.down.mybatis;


import com.top.mini.happy.down.mybatis.dialect.Dialect;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by xugang on 16/7/12.
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
        })
})
public class PageInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(PageInterceptor.class);

    private static int MAPPED_STATEMENT_INDEX = 0;
    private static int PARAMETER_INDEX = 1;
    private static int ROWBOUNDS_INDEX = 2;

    private Dialect dialect;
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] queryArgs = invocation.getArgs();
        final Object parameter = queryArgs[PARAMETER_INDEX];
        Pageable pageable = PageHelper.findObjectFromParams(parameter,Pageable.class);


        if(pageable == null) {
            return invocation.proceed();
        }

        final MappedStatement ms = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX];
        final BoundSql boundSql = ms.getBoundSql(parameter);
        String sql = PageHelper.removeSqlSemicolon(boundSql.getSql().trim());


        //先查询总数
        final int total = PageHelper.queryTotal(sql, ms, boundSql, dialect);



        String pageSql = dialect.getPageSql(sql, pageable.getOffset(), pageable.getPageSize());

        queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET,RowBounds.NO_ROW_LIMIT);
        queryArgs[MAPPED_STATEMENT_INDEX] = PageHelper.copyFromNewSql(ms, boundSql, pageSql);

        Object ret = invocation.proceed();
        @SuppressWarnings("unchecked")
        Page<?> pi = new Page<Object>((List<Object>) ret, pageable, total);

        //MyBatis 需要返回一个List对象，这里只是满足MyBatis而作的临时包装
        List<Page<?>> result = new ArrayList<Page<?>>(1);
        result.add(pi);

        return result;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String dbtype = properties.getProperty("dbtype");

        try {
            dialect = PageHelper.getDialect(dbtype);
        } catch (Exception e) {
            logger.error("设置dialect发生错误", e);
        }
    }
}
