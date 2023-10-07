package yi.shi.plinth.boot;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import com.google.inject.Injector;
import com.google.inject.Module;
import yi.shi.plinth.annotation.PropertiesFile;
import yi.shi.plinth.jetty.JettyBootService;
import yi.shi.plinth.modules.IocModule;
import yi.shi.plinth.modules.JettyModule;
import yi.shi.plinth.modules.ModuleRegister;
import yi.shi.plinth.properties.CoreProperties;


/**
 * @author shiyi
 *
 */
public class ServiceBooter {
	
	static {
		ModuleRegister.register(new JettyModule());
	}

	public static void startFrom(Class<?> mainClass, Module... modules) throws ClassNotFoundException, IOException {
		Injector injector;
		if (Objects.nonNull(mainClass)) {
			loadPropertiesFile(mainClass);
			IocModule.registScanPackage(mainClass);
		}
		
		if (Objects.nonNull(modules) && modules.length > 0) {
			for(Module module : modules) {
				ModuleRegister.register(module);
			}
		} 
		
		injector = ModuleRegister.getInjector();// Guice.createInjector(Stage.DEVELOPMENT, ModuleRegister.getModulesAsList());
		JettyBootService service = injector.getInstance(JettyBootService.class);
		service.start();
	}
	
	/**
	 * @PropertiesFile(files = { "application.properties" })
     * @Slf4j
     * public class Main {
     *     public static void main(String... strings) {
	 * 
	 * 
	 * @param mainClass
	 */
	private static void loadPropertiesFile(Class<?> mainClass) {
		
		PropertiesFile propertiesFile = mainClass.getAnnotation(PropertiesFile.class);
		
		if(Objects.isNull(propertiesFile)) {
			return;
		}
		
		String[] fileName = propertiesFile.files();
		if(fileName.length <= 0) {
			return;
		}
		
		Arrays.asList(fileName).forEach(pf -> {
			CoreProperties.setProperties(pf);
		});
	}

}
