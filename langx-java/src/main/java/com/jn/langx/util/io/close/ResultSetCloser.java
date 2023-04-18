package com.jn.langx.util.io.close;

import com.jn.langx.util.collection.Collects;

import java.sql.ResultSet;
import java.util.List;

public class ResultSetCloser extends AbstractCloser<ResultSet> {
    @Override
    protected void doClose(ResultSet resultSet) throws Exception {
        resultSet.close();
    }

    @Override
    public List<Class> applyTo() {
        return Collects.<Class>newArrayList(ResultSet.class);
    }
}
