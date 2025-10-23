package yi.shi.plinth.utils;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class JsonUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static <T> String toJson(T t) throws JacksonException {
		return objectMapper.writeValueAsString(t);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(byte[] content, Class<?> clazz) throws JacksonException {
		return (T) objectMapper.readValue(content, clazz);
	}

	public static String getSimpleClassName(String canonicalName) {
		String[] names = canonicalName.split("\\.");
		return names[names.length - 1];
	}
}
