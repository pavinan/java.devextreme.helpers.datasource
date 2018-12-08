package com.github.pavinan.java.devextreme.helpers.datasource;

/**
 * Creates mapping for json names and database column names
 */
public class DefaultSQLColumnProvider implements SQLColumnProvider {

	/**
	 * Some date conversions from iso formats to MSSQLServer dates
	 * @param dateString json date
	 * @return converted date
	 * @throws Exception For unknown date formats
	 */
	public static String GetDBDateValueConversion(String dateString) throws Exception {

		if (dateString.contains(" ")) {

			String date = dateString.split(" ")[0];
			return String.format("Convert(datetime,'%s', 126)", date);
		} else if (dateString.contains("+")) {

			String date = dateString.split("\\+")[0];
			return String.format("Convert(datetime,'%s', 126)", date);
		} else if (dateString.chars().filter(x -> x == '-').count() == 3) {

			String date = dateString.replaceAll("-\\d\\d:{0,1}\\d\\d$", "");
			return String.format("Convert(datetime,'%s', 126)", date);
		} else if (dateString.contains("Z")) {

			return String.format("Convert(datetime,'%s', 127)", dateString);
		} else {

			throw new Exception(String.format("Unknown date format %s.", dateString));
		}
	}

	/**
	 * Default implementation
	 */
	@Override
	public String getDBColumnName(String columnName) {
		return columnName;
	}

	/**
	 * Default implementation
	 */
	@Override
	public String getDBValueConversion(String columnName, String condition, String value) {
		if (condition.equals("contains") || condition.equals("not contains")) {
			return String.format("'%%%s%%'", value);
		}
		return String.format("'%s'", value);
	}

}
