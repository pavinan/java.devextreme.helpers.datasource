package com.github.pavinan.java.devextreme.helpers.datasource;

public interface SQLColumnProvider {

     String getDBColumnName(String columnName);

     String getDBValueConversion(String columnName, String condition, String value);
}
