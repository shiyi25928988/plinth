package yi.shi.plinth.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

public class ModuleRegister {

	private static Injector injector = null;
	
	private static List<Module>  MODULE_LIST = new ArrayList<>();
	
	public static int register(Module module) {
		MODULE_LIST.add(module);
		return MODULE_LIST.size();
	}
	
	public static List<Module> getModulesAsList() {
		return MODULE_LIST;
	}
	
	public static Injector getInjector() {
		if(Objects.isNull(injector)) {
			injector = Guice.createInjector(Stage.DEVELOPMENT, getModulesAsList());
		}
		return injector;
	}

}
