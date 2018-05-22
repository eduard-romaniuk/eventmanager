package com.example.eventmanager.query.term;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class OrExpression implements Expression {

    private List<Expression> expressions = new ArrayList<>();

    public OrExpression addExpression(Expression expression) {
        expressions.add(expression);
        return this;
    }

    @Override
    public String generate() {
        if (CollectionUtils.isEmpty(expressions)){
            throw new NullPointerException("Expressions can`t be null or empty");
        }

        StringBuilder orString = new StringBuilder("(");

        for (Expression expression : expressions){
            orString.append(expression.generate()).append(" OR ");
        }

        orString.delete(orString.length() - 4, orString.length()).append(")");

        return orString.toString();
    }
}
