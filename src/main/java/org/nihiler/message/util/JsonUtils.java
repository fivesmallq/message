package org.nihiler.message.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 一个简单的json工具类
 * 
 * @author yyl
 * @datetime 2005-12-11
 * @version 0.9
 */
@SuppressWarnings("rawtypes")
public class JsonUtils {

	// 排除的字段
	private final Set<String> excludeFields = new HashSet<String>();
	private static final Object EMPTY_OBJECT_ARRAY[] = new Object[0];

	public JsonUtils() {
		excludeFields.add("class");
		excludeFields.add("declaringClass");
		excludeFields.add("metaClass");
	}

	/**
	 * 转换成JSON时需要排除的字段
	 * 
	 * @param excludes
	 */
	public void setExcludeFields(String... excludes) {
		for (String exclude : excludes) {
			excludeFields.add(exclude);
		}
	}

	/**
	 * 将Java对象转化为JSON对象
	 * 
	 * @param obj
	 *            java对象
	 * @return js对象
	 */
	public static String encode(Object object) {
		return new JsonUtils().encodeBasic(object);
	}

	/**
	 * 将Java对象转化为JSON对象
	 * 
	 * @param obj
	 *            java对象
	 * @return js对象
	 */

	@SuppressWarnings("unchecked")
	private String encodeBasic(Object object) {
		// basic
		if (object == null) {
			return encodeNULL();
		} else if (object instanceof String) {
			return encodeString((String) object);
		} else if (object instanceof Boolean) {
			return encodeBoolean((Boolean) object);
		} else if (object instanceof Number) {
			return encodeNumber((Number) object);
		} else if (object instanceof Map) {
			return encodeMap((Map<String, Object>) object);
		} else if (object instanceof Iterable<?>) {
			return encodeIterable((Iterable<?>) object);
		} else if (object instanceof Object[]) {// object.getClass().isArray()
			return encodeArray((Object[]) object);
		} else if (object instanceof java.util.Date) {
			return encodeDate((java.util.Date) object);
		} else {
			Class clazz = object.getClass();

			if (clazz.isInterface()) {
				return encodeEmpty();
			}

			if (clazz.isEnum()) {
				return encodeEnum((java.lang.Enum) object);
			} else {
				return encodeAdapter(object);
			}
		}
	}

	/**
	 * 返回一个NULL对象
	 * 
	 * @return javascript null对象
	 */
	private String encodeNULL() {
		return "null";
	}

	/**
	 * 将Java-String对象转化为JSON对象
	 * 
	 * @param string
	 *            字符串对象
	 * @return javascript string对象
	 */
	private String encodeString(String string) {
		StringBuilder sbr = new StringBuilder(string.length() * 4);
		sbr.append("\"");
		// 直接追加字符串，或者使用unicode编码
		sbr.append(string);
		// sbr.append(Native2AsciiUtils.native2Ascii(string));
		sbr.append("\"");
		return sbr.toString();
	}

	/**
	 * 将Java-Boolean对象转化为JSON对象
	 * 
	 * @param obj
	 *            字符串对象
	 * @return javascript Boolean对象
	 */
	private String encodeBoolean(Boolean b) {
		return b.toString();
	}

	/**
	 * 将Java-Number对象转化为JSON对象
	 * 
	 * @param n
	 *            数字对象
	 * @return javascript Number对象
	 */
	private String encodeNumber(Number n) {
		return n.toString();
	}

	/**
	 * 将Java-Map对象转化为JSON对象
	 * 
	 * @param map
	 *            Map对象
	 * @return javascript Map对象(简单对象)
	 */
	private String encodeMap(Map map) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("{");
		boolean isFirst = true;
		for (java.util.Iterator it = map.entrySet().iterator(); it.hasNext();) {
			if (isFirst) {
				isFirst = false;
			} else {
				sbr.append(",");
			}
			Map.Entry entry = (Map.Entry) it.next();
			sbr.append(encodeBasic(entry.getKey())).append(":")
					.append(encodeBasic(entry.getValue()));
		}
		sbr.append("}");
		return sbr.toString();
	}

	/**
	 * 将Java-Iterable对象转化为JSON对象
	 * 
	 * @param iterable
	 *            Iterable对象
	 * @return javascript Array对象
	 */
	private String encodeIterable(java.lang.Iterable iterable) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("[");
		int index = 0;
		for (java.util.Iterator it = iterable.iterator(); it.hasNext();) {
			if ((index++) > 0) {
				sbr.append(",");
			}
			sbr.append(encodeBasic(it.next()));
		}
		sbr.append("]");
		return sbr.toString();
	}

	/**
	 * 将Java-数组对象转化为JSON对象
	 * 
	 * @param obj
	 *            数组对象
	 * @return javascript Array对象
	 */
	private String encodeArray(Object[] array) {
		StringBuilder sbr = new StringBuilder();
		sbr.append("[");
		for (int index = 0; index < array.length; index++) {
			if (index > 0) {
				sbr.append(",");
			}
			Object o = array[index];
			sbr.append(encodeBasic(o));
		}
		sbr.append("]");
		return sbr.toString();
	}

	/**
	 * 将Java-Date对象转化为JSON对象
	 * 
	 * @param date
	 *            日期对象
	 * @return javascript Date对象
	 */
	private String encodeDate(java.util.Date date) {
		return new StringBuilder().append("new Date(").append(date.getTime())
				.append(")").toString();
	}

	/**
	 * 将Java-Enum对象转化为JSON对象
	 * 
	 * @param e
	 *            枚举对象
	 * @return javascript Date对象
	 */
	private String encodeEnum(java.lang.Enum e) {
		return "\"" + e.name() + "\"";
	}

	/**
	 * 返回一个JSON简单对象
	 * 
	 * @return javascript 简单对象
	 */
	private String encodeEmpty() {
		return "{}";
	}

	/**
	 * 将Java对象转化为JSON对象
	 * 
	 * @param object
	 *            Java对象
	 * @return 如果适配器能解析,则返回解析后的JSON对象，否则返回一个默认Empty JSON对象
	 */
	private String encodeAdapter(Object object) {
		try {
			Map<String, Object> proxy = new HashMap<String, Object>();
			Class clazz = object.getClass();
			if (clazz == null) {
				throw new IllegalArgumentException("No bean class specified");
			}
			// proxy.put("class", clazz.getName());
			PropertyDescriptor[] descriptors = null;
			try {
				descriptors = Introspector.getBeanInfo(clazz)
						.getPropertyDescriptors();
			} catch (IntrospectionException e) {
				descriptors = new PropertyDescriptor[0];
			}
			for (int i = 0, j = descriptors.length; i < j; i++) {
				PropertyDescriptor descriptor = descriptors[i];
				String key = descriptor.getName();
				// 排除的字段
				if (excludeFields.contains(key)) {
					continue;
				}
				Method method = descriptor.getReadMethod();
				if (descriptor.getReadMethod() != null) {
					Class<?> type = descriptor.getPropertyType();

					if (type.isEnum()) {
						continue;
					}
					Object value = method.invoke(object, EMPTY_OBJECT_ARRAY);
					proxy.put(key, value);
				}
			}
			return encodeMap(proxy);
		} catch (Exception ex) {
			return encodeEmpty();
		}
	}

}
