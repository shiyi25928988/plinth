package yi.shi.plinth.utils.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();

	@SuppressWarnings("unchecked")
	public static <T> T decodeMessage(String msg)
			throws ClassNotFoundException, JsonParseException, JsonMappingException, IOException {
		MessageWrapper messageWrapper = objectMapper.readValue(msg, MessageWrapper.class);
		String className = messageWrapper.getTypeName();
		String content = messageWrapper.getContent();
		return (T) objectMapper.readValue(content,
				Class.forName(className, true, Thread.currentThread().getContextClassLoader()));
	}

	public static <T> String encodeMessage(T t) throws JsonProcessingException {
		MessageWrapper messageWrapper = new MessageWrapper();
		messageWrapper.setTypeName(t.getClass().getCanonicalName().toString());
		messageWrapper.setContent(objectMapper.writeValueAsString(t));
		return objectMapper.writeValueAsString(messageWrapper);
	}
	
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
