package com.example.eventmanager.query.term;

public class Column {
    public enum Function {
        COUNT("COUNT"),
        AVG("AVG"),
        SUM("SUM"),
        MAX("MAX"),
        MIN("MIN");

        private final String function;

        Function(final String function) {
            this.function = function;
        }

        @Override
        public String toString() {
            return function;
        }
    }

    private String name;
    private String alias;
    private Function function;

    private Column() {}

    public static final class Builder {

        private Column column;

        public Builder() {
            column = new Column();
        }

        public Builder(String name) {
            column = new Column();
            this.column.name = name;
        }

        public Builder alias(String alias) {
            this.column.alias = alias;
            return this;
        }

        public Builder function(Function function) {
            this.column.function = function;
            return this;
        }

        public Column build() {
            return column;
        }
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public Function getFunction() {
        return function;
    }

    private static boolean functionExist(String test) {
        for (Function func : Function.values()) {
            if (func.toString().equals(test)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder col = new StringBuilder();

        if (name == null) {
            col.append("*");
        } else {
            col.append(name);
        }

        if (function != null) {
            if (!functionExist(function.toString())) {
                throw new NullPointerException("Function doesn`t exist");
            } else {
                col.insert(0, function + "(").append(")");
            }
        }

        if (alias != null) {
            col.append(" AS ").append(alias);
        }

        return col.toString();
    }
}
