package com.example.eventmanager.query.queries;

import com.example.eventmanager.query.Query;
import com.example.eventmanager.query.term.Column;
import com.example.eventmanager.query.term.OrExpression;
import com.example.eventmanager.query.term.WhereExpression;

public class SearchUser {
    private String queryString;
    private int limit;
    private int offset;

    public SearchUser(String queryString){
        this(queryString, -1, -1);
    }

    public SearchUser(String queryString, int limit, int offset){
        this.queryString = queryString.trim().replace(" ", "%");
        this.limit = limit;
        this.offset = offset;
    }

    private WhereExpression searchUserByColumn(String column){
        return new WhereExpression(column, WhereExpression.Operation.LIKE, queryString);
}

    private OrExpression searchBody(){
        return new OrExpression()
                .addExpression(searchUserByColumn("login"))
                .addExpression(searchUserByColumn("CONCAT(name, surname)"));
    }

    public Query construct() {
        Query.Builder queryBuilder = new Query.Builder("users")
                .where(searchBody());

        if(limit > -1){
            queryBuilder.limit(limit);
        }

        if(offset > -1){
            queryBuilder.offset(offset);
        }

        return queryBuilder.build();
    }

    public Query constructCount() {
        return new Query.Builder("users")
                .column(new Column.Builder().function(Column.Function.COUNT).build())
                .where(searchBody())
                .build();
    }
}
