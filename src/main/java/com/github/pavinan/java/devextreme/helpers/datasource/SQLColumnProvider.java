package com.github.pavinan.java.devextreme.helpers.datasource;

/**
 * Creates mapping for json names and database column names
 */
public interface SQLColumnProvider {

     /**
      * Convert json name to db name.
      * @param columnName json name
      * @return dbname
      */
     String getDBColumnName(String columnName);

     /**
      * Convert json value to db value conversions if required
      * @param columnName json name
      * @param condition =, <, "contains", "not contains" etc
      * @param value json value
      * @return converted value
      */
     String getDBValueConversion(String columnName, String condition, String value);
}
