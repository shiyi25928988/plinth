package yi.shi.plinth.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static <T> String toJson(T t) throws JsonProcessingException {
		return objectMapper.writeValueAsString(t);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(byte[] content, Class<?> clazz) throws JsonParseException, JsonMappingException, IOException {
		return (T) objectMapper.readValue(content, clazz);
	}

	public static String getSimpleClassName(String canonicalName) {
		String[] names = canonicalName.split("\\.");
		return names[names.length - 1];
	}
}
