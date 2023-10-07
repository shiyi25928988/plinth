package yi.shi.plinth.modules;

/**
 * @author yshi
 *
 */
public class ServletModule extends com.google.inject.servlet.ServletModule {
	
	/*
	 * Load the property files here, which stored in the folder '/base-WEB/src/main/resources' 
	 * */
	
	//private static DataSourceModule dataSourceModule = new DataSourceModule();
	private static IocModule iocModule = new IocModule();
	/*
	 * (non-Javadoc) 
	 * 
	 * @see com.google.inject.servlet.ServletModule#configureServlets()
	 */
	@Override
	protected void configureServlets() {
		install(iocModule);
	}
}
