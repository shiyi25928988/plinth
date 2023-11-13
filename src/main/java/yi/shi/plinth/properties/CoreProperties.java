package yi.shi.plinth.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.google.common.base.Strings;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

/**
 * @author shiyi
 *
 */
public class CoreProperties {
	
	//private static Map<String, String> propertiesFileRegister = new ConcurrentHashMap<>();

	public static void setProperties(String propertiesFileName) {
		//propertiesFileRegister.put(propertiesFileName, propertiesFileName);
		if(propertiesFileName.toLowerCase().startsWith("http:") || propertiesFileName.toLowerCase().startsWith("https:")){
			setRemoteProperties(propertiesFileName);
			return;
		}
		try {
			System.getProperties().load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFileName));
		} catch (IOException e) {
			e.printStackTrace();
			//propertiesFileRegister.remove(propertiesFileName);
		}
	}

	public static void setRemoteProperties(String url) {
		HttpClient httpClient = null;
		try {
			httpClient = new HttpClient();
			httpClient.start();
			ContentResponse response = httpClient.GET(url);
			InputStream inputStream = IOUtils.toInputStream(response.getContentAsString(), "UTF-8");
			System.getProperties().load(inputStream);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(Objects.nonNull(httpClient)){
				try {
					httpClient.stop();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public static String getProperties(String key) {
		return getProperties(key, "");
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getProperties(String key, String defaultValue) {
		
		if(System.getProperties().containsKey(key)) {
			String value = System.getProperties().getProperty(key);
			if(Strings.isNullOrEmpty(value)) {
				return defaultValue;
			}else {
				return value;
			}
		}else {
			return defaultValue;
		}
		
	}
	
	private class DynamicLoader implements Runnable{
		
		DynamicLoader(){
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
