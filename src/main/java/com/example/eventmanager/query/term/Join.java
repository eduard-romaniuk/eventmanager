package com.example.eventmanager.query.term;

public class Join {
    public enum JoinType {
        INNER_JOIN("INNER JOIN"),
        LEFT_JOIN("LEFT JOIN"),
        RIGHT_JOIN("RIGHT JOIN"),
        FULL_JOIN("FULL JOIN");

        private final String type;

        JoinType(final String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    private String table;
    private JoinType type;
    private String onOne;
    private Object onTwo;

    public Join(String table, JoinType type, String onOne, Object onTwo) {
        this.table = table;
        this.type = type;
        this.onOne = onOne;
        this.onTwo = onTwo;
    }

    private static boolean typeExist(String test) {
        for (JoinType jt : JoinType.values()) {
            if (jt.toString().equals(test)) {
                return true;
            }
        }
        return false;
    }

    public String generate(){
        if (table == null || table.equals("")){
            throw new NullPointerException("Table can't be null or empty");
        }

        if (type == null){
            throw new NullPointerException("Type can't be null");
        }

        if(!typeExist(type.toString())){
            throw new NullPointerException("Type doesn`t exist");
        }

        if (onOne == null || onOne.equals("")){
            throw new NullPointerException("First argument in ON statement can't be null or empty");
        }

        if (onTwo == null || onTwo.equals("")){
            throw new NullPointerException("Second argument in ON statement can't be null or empty");
        }

        StringBuilder joinString = new StringBuilder();

        joinString.append(" ")
                .append(type.toString())
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

    public JoinType getType() {
        return type;
    }

    public void setType(JoinType type) {
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
