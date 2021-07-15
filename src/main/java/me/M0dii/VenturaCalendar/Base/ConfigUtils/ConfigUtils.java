package me.M0dii.VenturaCalendar.Base.ConfigUtils;

import java.util.List;

public interface ConfigUtils
{
	/*
	 * Gets a String from the path out of the config. Formatted with chat color.
	 */
	String getString(String path);
	
	/*
	 * Gets an Integer from the path out of the config.
	 */
	Integer getInteger(String path);
	
	/*
	 * Gets a String from the path out of the config and casts it to Long.
	 */
	Long getLong(String path);
	
	/*
	 * Gets a Boolean from the path out of the config.
	 */
	Boolean getBoolean(String path);
	
	/*
	 * Gets a list from the path out of the config. Formatted with chat color.
	 */
	List<String> getListString(String path);
}
