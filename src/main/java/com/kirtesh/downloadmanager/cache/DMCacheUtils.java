package com.kirtesh.downloadmanager.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.kirtesh.downloadmanager.utils.CommonUtils;
/**
 * 
 * @author kirteshdudawat
 *
 * 
 * DMCacheUtils is an utility class to convert input to desired format. Eg. converting String to Boolean, String to Long, String to Map etc. It has been extensively used while reading values from JVM Cache. Converting JVM Cache values to desired output format. 
 *
 */

public class DMCacheUtils {
	
	public final static Logger logger = Logger.getLogger(DMCacheUtils.class);

	/**
	 * 
	 * Returns desired value in String format.
	 * 
	 * @param properties : HashMap containing map of properties and its values from application.properties. (Same as DMCache.propertyCacheMap)
	 * @param name : Name of property whose value is required
	 * @return : Value saved against Key @param name in @param properties as String
	 */
	public static String getPropertyAsString(Map<String, String> properties, String name) {
		return properties.get(name.trim());
	}

	/**
	 * 
	 * Return Map generated from String value against required property..
	 * 
	 * @param properties : HashMap containing map of properties and its values from application.properties. (Same as DMCache.propertyCacheMap)
	 * @param name : Name of property whose value is required
	 * @return : Value saved against Key @param name in @param properties after converting it into Map from String.
	 */
	public static Map<String, String> getPropertyAsMap(Map<String, String> properties, String name) {
		String propertiesString = properties.get(name.trim());
		Map<String, String> propertyMap = new HashMap<String, String>();
		if (!CommonUtils.isStringNullOrEmpty(propertiesString)) {
			String[] arrProperties = propertiesString.split(",");
			for (String property : arrProperties) {
				propertyMap.put(property, property);
			}
		}
		return propertyMap;
	}

	/**
	 * 
	 * Returns desired value in Boolean format.
	 * 
	 * @param properties : HashMap containing map of properties and its values from application.properties. (Same as DMCache.propertyCacheMap)
	 * @param name : Name of property whose value is required
	 * @return : Value saved against Key @param name in @param properties as Boolean
	 */
	public static Boolean getPropertyAsBoolean(Map<String, String> properties, String name) {
		String value = properties.get(name.trim());
		return Boolean.valueOf(value);
	}

	/**
	 * 
	 * Returns desired value in Integer format.
	 * 
	 * @param properties : HashMap containing map of properties and its values from application.properties. (Same as DMCache.propertyCacheMap)
	 * @param name : Name of property whose value is required
	 * @return : Value saved against Key @param name in @param properties as Integer
	 */
	public static Integer getPropertyAsInteger(Map<String, String> properties, String name) {
		try {
			String value = properties.get(name.trim());
			if (!CommonUtils.isStringNullOrEmpty(value)) {
				return Integer.valueOf(value);
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer().append("Exception occured in getPropertyAsInteger while converting ").append(name)
					.append(" to Integer..");
			logger.error(sb.toString());
		}
		return 0;
	}

	/**
	 * 
	 * Returns desired value in Long format.
	 * 
	 * @param properties : HashMap containing map of properties and its values from application.properties. (Same as DMCache.propertyCacheMap)
	 * @param name : Name of property whose value is required
	 * @return : Value saved against Key @param name in @param properties as Long
	 */
	public static Long getPropertyAsLong(Map<String, String> properties, String name) {
		try {
			String value = properties.get(name.trim());
			if (!CommonUtils.isStringNullOrEmpty(value)) {
				return Long.valueOf(value);
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer().append("Exception occured in getPropertyAsLong while converting ").append(name)
					.append(" to Long..");
			logger.error(sb.toString());
		}
		return null;
	}

	/**
	 * 
	 * Returns desired value in Double format.
	 * 
	 * @param properties : HashMap containing map of properties and its values from application.properties. (Same as DMCache.propertyCacheMap)
	 * @param name : Name of property whose value is required
	 * @return : Value saved against Key @param name in @param properties as Double
	 */
	public static Double getPropertyAsDouble(Map<String, String> properties, String name) {
		try {
			String value = properties.get(name.trim());
			if (!CommonUtils.isStringNullOrEmpty(value)) {
				return Double.valueOf(value);
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer().append("Exception occured in getPropertyAsDouble while converting ").append(name)
					.append(" to Double..");
			logger.error(sb.toString());
		}
		return null;
	}

	/**
	 * 
	 * Return List(String) generated from String value against required property..
	 * 
	 * @param properties : HashMap containing map of properties and its values from application.properties. (Same as DMCache.propertyCacheMap)
	 * @param name : Name of property whose value is required
	 * @return : Value saved against Key @param name in @param properties after converting it into List from String.
	 */
	public static List<String> getPropertyAsList(Map<String, String> properties, String name) {
		List<String> list = new ArrayList<String>();
		try {
			String value = properties.get(name.trim());
			if (!CommonUtils.isStringNullOrEmpty(value)) {
				StringTokenizer st = new StringTokenizer(value, ",");
				while (st.hasMoreElements()) {
					list.add(st.nextToken());
				}
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer().append("Exception occured in getPropertyAsList while converting ").append(name)
					.append(" to List<String>..");
			logger.error(sb.toString());
		}
		return list;
	}

	/**
	 * 
	 * Return List(Integer) generated from String value against required property..
	 * 
	 * @param properties : HashMap containing map of properties and its values from application.properties. (Same as DMCache.propertyCacheMap)
	 * @param name : Name of property whose value is required
	 * @return : Value saved against Key @param name in @param properties after converting it into List from String.
	 */
	public static List<Integer> getPropertyAsIntegerList(Map<String, String> properties, String name) {
		List<Integer> list = new ArrayList<Integer>();
		try {
			String value = properties.get(name.trim());
			if (!CommonUtils.isStringNullOrEmpty(value)) {
				for (String str : value.split(",")) {
					list.add(Integer.parseInt(str.trim()));
				}
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer().append("Exception occured in getPropertyAsIntegerList while converting ").append(name)
					.append(" to List<Integer>..");
			logger.error(sb.toString());
		}
		return list;
	}

	/**
	 * 
	 * Return List(Long) generated from String value against required property..
	 * 
	 * @param properties : HashMap containing map of properties and its values from application.properties. (Same as DMCache.propertyCacheMap)
	 * @param name : Name of property whose value is required
	 * @return : Value saved against Key @param name in @param properties after converting it into List from String.
	 */
	public static List<Long> getPropertyAsLongList(Map<String, String> properties, String name) {
		List<Long> list = new ArrayList<Long>();
		try {
			String value = properties.get(name.trim());
			if (!CommonUtils.isStringNullOrEmpty(value)) {
				for (String str : value.split(",")) {
					list.add(Long.parseLong(str.trim()));
				}
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer().append("Exception occured in getPropertyAsLongList while converting ").append(name)
					.append(" to List<Long>..");
			logger.error(sb.toString());
		}
		return list;
	}

	/**
	 * 
	 * Return List(Float) generated from String value against required property..
	 * 
	 * @param properties : HashMap containing map of properties and its values from application.properties. (Same as DMCache.propertyCacheMap)
	 * @param name : Name of property whose value is required
	 * @return : Value saved against Key @param name in @param properties after converting it into List from String.
	 */
	public static Float getPropertyAsFloat(Map<String, String> properties, String name) {
		try {
			String value = properties.get(name.trim());
			if (!CommonUtils.isStringNullOrEmpty(value)) {
				return Float.valueOf(value);
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer().append("Exception occured in getPropertyAsFloat while converting ").append(name)
					.append(" to Float..");
			logger.error(sb.toString());
		}
		return null;
	}

}
