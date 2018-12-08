package com.github.pavinan.java.devextreme.helpers.datasource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Where clause builder
 */
public class WhereClauseBuilder {

    private SQLColumnProvider columnProvider;

    /**
     * Uses default {@link DefaultSQLColumnProvider}
     */
    public WhereClauseBuilder() {

        this.columnProvider = new DefaultSQLColumnProvider();
    }

    /**
     * Provide {@link SQLColumnProvider} implementation
     * @param sqlColumnProvider
     */
    public WhereClauseBuilder(SQLColumnProvider sqlColumnProvider) {

        this.columnProvider = sqlColumnProvider;
    }

    /**
     * Creates default
     * @return {@link WhereClauseBuilder}
     */
    public static WhereClauseBuilder createDefault() {

        return new WhereClauseBuilder();
    }

    /**
     * Builds where clause
     * @param jsonArray json array
     * @return where clause sql string
     * @throws Exception Exceptions
     */
    public String buildFor(JsonArray jsonArray) throws Exception {

        StringBuilder sb = new StringBuilder();

        this.parseJArrayConditions(jsonArray, sb);

        String query = sb.toString();

        return query;
    }

    private void parseJArrayConditions(JsonArray jsonArray, StringBuilder query) throws Exception {

        if (jsonArray.size() == 2) {

            // [["col1", "=", 45], ["col2", "=", true]]
            if (jsonArray.get(0).isJsonArray()) {

                for (int i = 0; i < jsonArray.size(); i++) {

                    JsonArray jsonItem = jsonArray.get(i).getAsJsonArray();

                    this.parseJArrayConditions(jsonItem, query);

                    if (i + 1 != jsonArray.size()) {

                        query.append(" and ");
                    }
                }
            } else if (jsonArray.get(0).isJsonPrimitive() && jsonArray.get(0).getAsString().equals("!")) {

                // ["!", ["col", "=", true]]
                // or
                // ["!", [["col", "=", true], "or", ["col", "=", true]]]

                JsonArray jArray = jsonArray.get(1).getAsJsonArray();

                negateJsonArray(jArray);

                this.parseJArrayConditions(jArray, query);
            } else {

                Exception ex = new Exception("Unknown 2d array");
                throw ex;
            }
        } else if (jsonArray.get(0).isJsonPrimitive()) {
            //// ["col", "condition", "value"]
            String columnName = jsonArray.get(0).getAsString();
            String condition = jsonArray.get(1).getAsString();
            String value = jsonArray.get(2).getAsString();

            String convertedValue = this.columnProvider.getDBValueConversion(columnName, condition, value);

            String databaseColumnName = this.columnProvider.getDBColumnName(columnName);

            if (condition.equals("contains")) {

                query.append(String.format(" %s like %s ", databaseColumnName, convertedValue));
            } else if (condition.equals("not contains")) {

                query.append(String.format(" %s not like %s ", databaseColumnName, convertedValue));
            } else {

                throwIfUnknownCondition(condition);

                query.append(String.format(" %s %s %s ", databaseColumnName, condition, convertedValue));
            }
        } else if (jsonArray.get(0).isJsonArray()) {

            query.append(" ( ");

            for (int i = 0; i < jsonArray.size(); i++) {

                JsonElement jsonItem = jsonArray.get(i);

                if (jsonItem.isJsonArray()) {

                    this.parseJArrayConditions(jsonItem.getAsJsonArray(), query);
                } else if (jsonItem.isJsonPrimitive()) {
                    // and, or
                    query.append(String.format(" %s ", jsonItem.getAsString()));
                }
            }

            query.append(" ) ");
        } else {

            throw new Exception("Unknown data type in json array.");
        }
    }

    private void negateJsonArray(JsonArray jArray) throws Exception {

        if (jArray.get(0).isJsonPrimitive()) {

            String negatedValue = negateCondition(jArray.get(1).getAsString());

            jArray.set(1, new JsonPrimitive(negatedValue));
        } else {

            for (int i = 0; i < jArray.size(); i++) {

                JsonElement jToken = jArray.get(i);

                if (jToken.isJsonArray()) {

                    negateJsonArray(jToken.getAsJsonArray());
                } else {

                    String negatedValue = negateCondition(jToken.getAsString());

                    jArray.set(i, new JsonPrimitive(negatedValue));
                }
            }
        }
    }

    private String negateCondition(String condition) throws Exception {

        switch (condition) {

        case "contains":
            return "not contains";
        case "=":
            return "!=";
        case "<":
            return ">";
        case ">":
            return "<";
        case "<=":
            return ">=";
        case ">=":
            return "<=";
        case "and":
            return "or";
        case "or":
            return "and";
        default:
            throw new Exception(String.format("Unknown condition %s", condition));
        }
    }

    private void throwIfUnknownCondition(String condition) throws Exception {

        switch (condition) {

        case "=":
        case "!=":
        case "<":
        case ">":
        case "<=":
        case ">=":
            break;
        default:
            throw new Exception(String.format("Unknown condition %s", condition));
        }
    }
}