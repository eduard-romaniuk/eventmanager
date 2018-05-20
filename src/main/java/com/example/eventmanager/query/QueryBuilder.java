package com.example.eventmanager.query;

import com.example.eventmanager.query.term.Expression;
import com.example.eventmanager.query.term.Join;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {
    private List<String> columns = new ArrayList<>();
    private List<String> tables = new ArrayList<>();
    private List<Join> joins = new ArrayList<>();
    private List<Expression> wheres = new ArrayList<>();
    private List<String> orderBys = new ArrayList<>();
    private List<String> groupBys = new ArrayList<>();
    private int limit = -1;
    private int offset = -1;

    public QueryBuilder() {
    }

    public QueryBuilder column(String name) {
        columns.add(name);
        return this;
    }

    public QueryBuilder from(String table) {
        tables.add(table);
        return this;
    }

    public QueryBuilder join(Join join) {
        joins.add(join);
        return this;
    }

    public QueryBuilder where(Expression expr) {
        wheres.add(expr);
        return this;
    }

    public QueryBuilder groupBy(String expr) {
        groupBys.add(expr);
        return this;
    }

    public QueryBuilder orderBy(String name) {
        orderBys.add(name);
        return this;
    }

    public QueryBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public QueryBuilder offset(int offset) {
        this.offset = offset;
        return this;
    }

    private void appendList(StringBuilder sql, List<String> list, String init,
                            String separator) {
        boolean first = true;
        for (String s : list) {
            if (first) {
                sql.append(init);
            } else {
                sql.append(separator);
            }
            sql.append(s);
            first = false;
        }
    }

    private void appendWhere(StringBuilder sql, List<Expression> expressions) {
        List<String> expList = new ArrayList<>();

        for(Expression exp : expressions){
            expList.add(exp.generate());
        }

        appendList(sql, expList, " WHERE ", " AND ");
    }

    @Override
    public String toString() {

        StringBuilder sql = new StringBuilder("SELECT ");

        if (columns.size() == 0) {
            sql.append("*");
        } else {
            appendList(sql, columns, "", ", ");
        }

        appendList(sql, tables, " FROM ", ", ");

        for(Join join : joins){
            sql.append(join.generate());
        }

        appendWhere(sql, wheres);
        appendList(sql, groupBys, " GROUP BY ", ", ");
        appendList(sql, orderBys, " ORDER BY ", ", ");

        if (limit > -1){
            sql.append(" LIMIT ").append(limit);
        }

        if (offset > -1){
            sql.append(" OFFSET ").append(offset);
        }

        return sql.toString();
    }
}
