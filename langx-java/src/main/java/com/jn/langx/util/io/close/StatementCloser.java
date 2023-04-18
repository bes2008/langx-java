package com.jn.langx.util.io.close;

import com.jn.langx.util.collection.Collects;

import java.sql.Statement;
import java.util.List;

public class StatementCloser extends AbstractCloser<Statement>{
    @Override
    public List<Class> applyTo() {
        return Collects.<Class>newArrayList(Statement.class);
    }

    @Override
    protected void doClose(Statement statement) throws Exception {
        statement.close();
    }
}
