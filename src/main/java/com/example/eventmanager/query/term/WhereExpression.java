package com.example.eventmanager.query.term;

public class WhereExpression implements Expression{
    public enum Operation {
        EQUALS("="),
        MORE(">"),
        MORE_OR_EQUALS(">="),
        LESS("<"),
        LESS_OR_EQUALS("<="),
        LIKE("LIKE");

        private final String operation;

        Operation(final String operation) {
            this.operation = operation;
        }

        @Override
        public String toString() {
            return operation;
        }
    }

    private String column;
    private Operation operation;
    private Object obj;

    public WhereExpression(String column, Operation operation, Object obj) {
        this.column = column;
        this.operation = operation;
        this.obj = obj;
    }

    private static boolean operationExist(String test) {
        for (Operation oper : Operation.values()) {
            if (oper.toString().equals(test)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String generate() {
        if (column == null || column.equals("")){
            throw new NullPointerException("Column can't be null or empty");
        }

        if (operation == null){
            throw new NullPointerException("Operation can't be null");
        }

        if(!operationExist(operation.toString())){
            throw new NullPointerException("Operation doesn`t exist");
        }

        if (obj == null || obj.equals("")){
            throw new NullPointerException("Object can't be null or empty");
        }

        StringBuilder whereString = new StringBuilder();

        if(operation.equals(Operation.LIKE)){
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

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
