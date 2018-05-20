package com.example.eventmanager.query.term;

public class Join {
    public static final String INNER_JOIN = "INNER JOIN";
    public static final String LEFT_JOIN = "LEFT JOIN";
    public static final String RIGHT_JOIN = "RIGHT JOIN";
    public static final String FULL_JOIN = "FULL JOIN";

    private String table;
    private String type;
    private String onOne;
    private Object onTwo;

    public Join(String table, String type, String onOne, Object onTwo) {
        this.table = table;
        this.type = type;
        this.onOne = onOne;
        this.onTwo = onTwo;
    }

    public String generate(){
        //TODO Error check
        StringBuilder joinString = new StringBuilder();

        joinString.append(" ")
                .append(type)
                .append(" ")
                .append(table)
                .append(" ON ")
                .append(onOne)
                .append(" = ")
                .append(onTwo);

        return joinString.toString();
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOnOne() {
        return onOne;
    }

    public void setOnOne(String onOne) {
        this.onOne = onOne;
    }

    public Object getOnTwo() {
        return onTwo;
    }

    public void setOnTwo(Object onTwo) {
        this.onTwo = onTwo;
    }
}
