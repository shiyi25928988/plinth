package yi.shi.plinth.reflection;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import yi.shi.plinth.annotation.http.HttpService;

/**
 * @author yshi
 */
public final class ClassHelper {

    /**
     *
     */
    private ClassHelper() {
    }

    /**
     * @param scanPackageName
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Set<Class<?>> getControllers(String scanPackageName) throws ClassNotFoundException, IOException {
        Set<Class<?>> classSet = pickClassByAnnotation(ClassHelper.getClassSetFromPackage(scanPackageName), HttpService.class);
        return classSet;
    }

    /**
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Set<Class<?>> getClassSetFromPackage(String packageName) throws IOException {
        return Collections.unmodifiableSet(ClassUtils.getClassSet(packageName));
    }

    /**
     * @param classSet
     * @param annotationClass
     * @return
     */
    public static Set<Class<?>> pickClassByAnnotation(Set<Class<?>> classSet,
                                                      Class<? extends Annotation> annotationClass) {
        Set<Class<?>> set = new HashSet<>();
        classSet.stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotationClass))
                .forEach(c -> set.add(c));
        return set;
    }

}
