package com.example.eventmanager.query.term;

public class WhereExpression implements Expression{
    public static final String EQUALS = "=";
    public static final String MORE = ">";
    public static final String MORE_OR_EQUALS = ">=";
    public static final String LESS = "<";
    public static final String LESS_OR_EQUALS = "<=";
    public static final String LIKE = "LIKE";

    private String column;
    private String operation;
    private Object obj;

    public WhereExpression(String column, String operation, Object obj) {
        this.column = column;
        this.operation = operation;
        this.obj = obj;
    }

    @Override
    public String generate() {
        //TODO Error check
        StringBuilder whereString = new StringBuilder();

        if(operation.equals(LIKE)){
            whereString.append("LOWER(")
                    .append(column)
                    .append(") ")
                    .append(operation)
                    .append(" '%")
                    .append(obj)
                    .append("%'");
        } else {
            whereString.append(column)
                    .append(" ")
                    .append(operation)
                    .append(" ")
                    .append(obj);
        }

        return whereString.toString();
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
