package com.example.eventmanager.query.queries;

import com.example.eventmanager.query.Query;
import com.example.eventmanager.query.term.OrExpression;
import com.example.eventmanager.query.term.WhereExpression;

public class SearchUser {
    private String queryString;
    private int limit;
    private int offset;

    public SearchUser(String queryString, int limit, int offset){
        this.queryString = queryString;
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
        return new Query.Builder("users")
                .where(searchBody())
                .limit(limit)
                .offset(offset)
                .build();
    }
}
