package yi.shi.plinth.modules;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import yi.shi.plinth.reflection.ClassHelper;
import lombok.extern.slf4j.Slf4j;
import yi.shi.plinth.servlet.DispatcherServlet;

/**
 * @author yshi
 *
 */
@Slf4j
public class IocModule extends AbstractModule {

	private static Set<Class<?>> controllerClassSet = new HashSet<>();

	public static Set<Class<?>> getControllerClassSet(){
		return controllerClassSet;
	}
	
	public IocModule() {
		try {
			controllerClassSet.addAll(ClassHelper.getControllers(IocModule.class.getPackage().getName().substring(0, IocModule.class.getPackage().getName().indexOf('.'))));
		} catch (ClassNotFoundException | IOException e) {
			log.error(e.getMessage());
			System.exit(1);
		}finally {
			if(!controllerClassSet.isEmpty()) {
				DispatcherServlet.initRestApiService();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		Names.bindProperties(binder(), System.getProperties());
	}

	/**
	 * @param clazz
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void registScanPackage(Class<?> clazz) throws ClassNotFoundException, IOException {
		registScanPackage(clazz.getPackage());
	}
	
	/**
	 * @param pack
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void registScanPackage(Package pack) throws ClassNotFoundException, IOException {
		controllerClassSet.addAll(ClassHelper.getControllers(pack.getName()));
	}
}
