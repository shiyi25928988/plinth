package yi.shi.plinth.reflection;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yshi
 *
 */
@Slf4j
public final class ClassUtils {


	/**
	 * Return a classes set by the given package name.
	 * 
	 * @param packageName
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Set<Class<?>> getClassSet(final String packageName) throws IOException {

		Set<Class<?>> classSet = new HashSet<>();

		Enumeration<URL> URLs = getClassLoader().getResources(packageName.replace(".", File.separator));

		while (URLs.hasMoreElements()) {
			URL url = URLs.nextElement();
			if (Objects.nonNull(url)) {

				switch (url.getProtocol()) {

				case "file":
					String packagePath = escapeSpace(url.getPath());
					addClass(classSet, packagePath, packageName);
					break;

				case "jar":
					JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
					if (Objects.nonNull(jarURLConnection)) {
						JarFile jarFile = jarURLConnection.getJarFile();
						if (Objects.nonNull(jarFile)) {
							Enumeration<JarEntry> jarEntries = jarFile.entries();
							while (jarEntries.hasMoreElements()) {
								JarEntry jarEntry = jarEntries.nextElement();
								String jarEntryName = jarEntry.getName();
								if (jarEntryName.endsWith(".class")) {
									String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
											.replaceAll("/", ".");
									try {
										doAddClass(classSet, className);
									} catch (ClassNotFoundException | NoClassDefFoundError e) {
										log.error(e.getLocalizedMessage());
										continue;
									}
								}
							}
						}
					}
					break;
				default:
					log.error(url.getProtocol() + "file process not supported!!");
				}
			}
		}
		return classSet;
	}

	/**
	 * @param classSet
	 * @param packagePath
	 * @param packageName
	 */
	private static void addClass(final Set<Class<?>> classSet, @NonNull final String packagePath,
			@NonNull final String packageName) {

		File[] files = new File(packagePath).listFiles(new FileFilter() {
			@Override
			public boolean accept(final File file) {

				if (file.isFile()) {
					if (file.getName().endsWith(".class")) {
						return true;
					}
				}
				if (file.isDirectory()) {
					return true;
				}
				return false;
			}
		});

		Stream.of(files).forEach(file -> {
			String fileName = file.getName();
			if (file.isFile()) {
				String className = fileName.substring(0, fileName.lastIndexOf("."));
				className = packageName.concat(".").concat(className);
				try {
					doAddClass(classSet, className);
				} catch (ClassNotFoundException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			} else if (file.isDirectory()) {
				String subPackagePath = packagePath + "/" + fileName;
				String subPackageName = packageName + "." + fileName;
				addClass(classSet, subPackagePath, subPackageName);
			}
		});
	}

	/**
	 * Get the current class loader.
	 * 
	 * @return ClassLoader
	 */
	private static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * @param className
	 * @param initialize
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static Class<?> loadClass(final String className, final boolean initialize) throws ClassNotFoundException {
		try {
			Class<?> clazz = Class.forName(className, initialize, getClassLoader());
			return clazz;
		} catch (NoClassDefFoundError e) {
		}
		return Object.class;
	}

	/**
	 * @param classSet
	 * @param className
	 * @throws ClassNotFoundException
	 */
	private static void doAddClass(final Set<Class<?>> classSet, final String className) throws ClassNotFoundException {
		Class<?> clazz = loadClass(className, false);
		classSet.add(clazz);
	}

	/**
	 * %20 is a space in URL
	 * 
	 * @param  str
	 * @return
	 */
	private static String escapeSpace(final String str) {
		String newStr = str.replace("%20", " ");
		newStr = newStr.replace("%5c", File.separator);
		return newStr;
	}
}
