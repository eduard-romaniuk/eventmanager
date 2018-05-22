package com.example.eventmanager.query;

import com.example.eventmanager.query.term.Column;
import com.example.eventmanager.query.term.Expression;
import com.example.eventmanager.query.term.Join;

import java.util.ArrayList;
import java.util.List;

public class Query {
    private List<Column> columns = new ArrayList<>();
    private List<String> tables = new ArrayList<>();
    private List<Join> joins = new ArrayList<>();
    private List<Expression> wheres = new ArrayList<>();
    private List<String> orderBys = new ArrayList<>();
    private List<String> groupBys = new ArrayList<>();
    private int limit = -1;
    private int offset = -1;

    private Query() {}

    public static final class Builder {

        private Query query;

        public Builder() {
            query = new Query();
        }

        public Builder(String table) {
            query = new Query();
            this.query.tables.add(table);
        }

        public Builder column(Column name) {
            this.query.columns.add(name);
            return this;
        }

        public Builder from(String table) {
            this.query.tables.add(table);
            return this;
        }

        public Builder join(Join join) {
            this.query.joins.add(join);
            return this;
        }

        public Builder where(Expression expr) {
            this.query.wheres.add(expr);
            return this;
        }

        public Builder groupBy(String expr) {
            this.query.groupBys.add(expr);
            return this;
        }

        public Builder orderBy(String name) {
            this.query.orderBys.add(name);
            return this;
        }

        public Builder limit(int limit) {
            this.query.limit = limit;
            return this;
        }

        public Builder offset(int offset) {
            this.query.offset = offset;
            return this;
        }

        public Query build() {
//            Query builtQuery = query;
//            query = new Query();
//            return builtQuery;
            return query;
        }
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<String> getTables() {
        return tables;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public List<Expression> getWheres() {
        return wheres;
    }

    public List<String> getOrderBys() {
        return orderBys;
    }

    public List<String> getGroupBys() {
        return groupBys;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
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

    private void appendCol(StringBuilder sql, List<Column> list, String init,
                            String separator) {
        boolean first = true;
        for (Column s : list) {
            if (first) {
                sql.append(init);
            } else {
                sql.append(separator);
            }
            sql.append(s.toString());
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
            appendCol(sql, columns, "", ", ");
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
