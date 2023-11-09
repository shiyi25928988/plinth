package yi.shi.plinth.rest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import cn.dev33.satoken.stp.StpUtil;
import yi.shi.plinth.annotation.auth.AUTH;
import yi.shi.plinth.annotation.http.Method.DELETE;
import yi.shi.plinth.annotation.http.Method.GET;
import yi.shi.plinth.annotation.http.Method.HEAD;
import yi.shi.plinth.annotation.http.Method.OPTIONS;
import yi.shi.plinth.annotation.http.Method.POST;
import yi.shi.plinth.annotation.http.Method.PUT;

import yi.shi.plinth.annotation.Properties;
import yi.shi.plinth.annotation.http.HttpBody;
import yi.shi.plinth.annotation.http.HttpParam;
import yi.shi.plinth.annotation.http.HttpPath;
import yi.shi.plinth.exception.EmptyPathParameterException;
import yi.shi.plinth.exception.ReduplicativeMathodPathException;
import yi.shi.plinth.exception.SingleRequestBodyRequiredException;

import com.google.common.base.Strings;
import com.google.inject.Injector;
import yi.shi.plinth.http.HttpErrorRespHelper;
import yi.shi.plinth.http.HttpReqHelper;
import yi.shi.plinth.http.HttpRespHelper;
import yi.shi.plinth.http.HttpStatusCode;
import yi.shi.plinth.properties.CoreProperties;
import yi.shi.plinth.reflection.ReflectionUtils;
import yi.shi.plinth.modules.IocModule;
import yi.shi.plinth.modules.ModuleRegister;
import yi.shi.plinth.servlet.ServletHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yshi
 *
 */
@Slf4j
public class RestApiServiceImpl implements RestApiService {

	private Set<Class<?>> classSet;

	private Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

	private Map<String, Method> methodMap_GET = new ConcurrentHashMap<>();
	private Map<String, Method> methodMap_PUT = new ConcurrentHashMap<>();
	private Map<String, Method> methodMap_POST = new ConcurrentHashMap<>();
	private Map<String, Method> methodMap_DELETE = new ConcurrentHashMap<>();
	private Map<String, Method> methodMap_OPTIONS = new ConcurrentHashMap<>();
	private Map<String, Method> methodMap_HEAD = new ConcurrentHashMap<>();

	private Map<Method, List<String>> parameterMap = new ConcurrentHashMap<>();
	private Map<Method, Class<?>> requestBodyMap = new ConcurrentHashMap<>();


	public RestApiServiceImpl(){
		this(IocModule.getControllerClassSet());
	}
	/**
	 * Constructor
	 * 
	 * @param classSet
	 */
	public RestApiServiceImpl(final Set<Class<?>> classSet) {
		this.classSet = classSet;
		if (Objects.nonNull(this.classSet) && this.classSet.size() >= 1) {
			classSet.stream().forEach(clazz -> {
				Method[] methods = clazz.getDeclaredMethods();

				Stream.of(methods).forEach(m -> {
					// NO ELSE to ensure that we can add multi-annotation on the same method
					if (m.isAnnotationPresent(GET.class)) {
						try {
							setMethodAndClassMap(clazz, m, methodMap_GET);
						} catch (ReduplicativeMathodPathException | EmptyPathParameterException
								 | SingleRequestBodyRequiredException e) {
							log.error(e.getLocalizedMessage());
						}
					}

					if (m.isAnnotationPresent(PUT.class)) {
						try {
							setMethodAndClassMap(clazz, m, methodMap_PUT);
						} catch (ReduplicativeMathodPathException | EmptyPathParameterException
								| SingleRequestBodyRequiredException e) {
							log.error(e.getLocalizedMessage());
						}
					}

					if (m.isAnnotationPresent(POST.class)) {
						try {
							setMethodAndClassMap(clazz, m, methodMap_POST);
						} catch (ReduplicativeMathodPathException | EmptyPathParameterException
								| SingleRequestBodyRequiredException e) {
							log.error(e.getLocalizedMessage());
						}
					}

					if (m.isAnnotationPresent(DELETE.class)) {
						try {
							setMethodAndClassMap(clazz, m, methodMap_DELETE);
						} catch (ReduplicativeMathodPathException | EmptyPathParameterException
								| SingleRequestBodyRequiredException e) {
							log.error(e.getLocalizedMessage());
						}
					}

					if (m.isAnnotationPresent(OPTIONS.class)) {
						try {
							setMethodAndClassMap(clazz, m, methodMap_OPTIONS);
						} catch (ReduplicativeMathodPathException | EmptyPathParameterException
								| SingleRequestBodyRequiredException e) {
							log.error(e.getLocalizedMessage());
						}
					}

					if (m.isAnnotationPresent(HEAD.class)) {
						try {
							setMethodAndClassMap(clazz, m, methodMap_HEAD);
						} catch (ReduplicativeMathodPathException | EmptyPathParameterException
								| SingleRequestBodyRequiredException e) {
							log.error(e.getLocalizedMessage());
						}
					}
				});
			});
		}
	}

	/**
	 * @param clazz
	 * @param method
	 * @param methodMap
	 * @throws ReduplicativeMathodPathException
	 * @throws SingleRequestBodyRequiredException
	 * @throws EmptyPathParameterException
	 */
	private void setMethodAndClassMap(Class<?> clazz, Method method, Map<String, Method> methodMap)
			throws ReduplicativeMathodPathException, EmptyPathParameterException, SingleRequestBodyRequiredException {
		if (method.isAnnotationPresent(HttpPath.class)) {
			HttpPath httpPath = method.getAnnotation(HttpPath.class);
			if (!Strings.isNullOrEmpty(httpPath.value())) {
				if (Objects.nonNull(methodMap.get(httpPath.value()))) {
					throw new ReduplicativeMathodPathException("Request Path is already exist with : " + httpPath.value());
				}
				methodMap.put(httpPath.value(), method);
				classMap.put(httpPath.value(), clazz);
				addMethodParameter(method);
			}
		}
	}

	/**
	 * @param method
	 * @throws EmptyPathParameterException
	 * @throws SingleRequestBodyRequiredException
	 */
	private void addMethodParameter(final Method method)
			throws EmptyPathParameterException, SingleRequestBodyRequiredException {
		if (method.getParameterCount() <= 0)
			return;
		List<String> list = new LinkedList<>();

		for (Parameter param : method.getParameters()) {
			if (param.isAnnotationPresent(HttpParam.class)) {
				HttpParam httpParam = param.getAnnotation(HttpParam.class);
				if (Strings.isNullOrEmpty(httpParam.value())) {
					throw new EmptyPathParameterException();
				} else {
					list.add(httpParam.value());
				}
			} else

			if (param.isAnnotationPresent(HttpBody.class)) {
				if (Objects.nonNull(requestBodyMap.get(method))) {
					throw new SingleRequestBodyRequiredException(method.getName() + " required single parameter!!");
				}
				requestBodyMap.put(method, param.getType());
			}
		}
		parameterMap.put(method, list);
	}

	/**
	 * @param methodMap
	 * @throws Exception
	 */
	private void invoke(final Map<String, Method> methodMap) throws Exception {
		List<String> args = new ArrayList<>();
		HttpServletRequest req = ServletHelper.getRequest();
		String path = StringUtils.remove(req.getRequestURI(), req.getContextPath());
		Method method = methodMap.get(path);
		if (Objects.nonNull(method)) {
			checkAuthAndRole(method);
			Class clazz = classMap.get(path);
			List<String> parameters = parameterMap.get(method);
			Class<?> requestBodyClass = requestBodyMap.get(method);
			if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
				parameters.forEach(p -> {
					args.add(ServletHelper.getRequest().getParameter(p));
				});
				invoke(clazz, method, args.toArray());
				return;
			}
			if (Objects.nonNull(requestBodyClass)) {
				invoke(clazz, method, HttpReqHelper.getRequestPostBody(requestBodyClass));
				return;
			}
			invoke(clazz, method);
		} else {
			HttpErrorRespHelper.send404();
		}
	}

	private void checkAuthAndRole(Method method) throws IOException {

		AUTH auth = method.getAnnotation(AUTH.class);
		if (Objects.nonNull(auth)) {
			try {
				StpUtil.checkLogin();
			}catch (Exception e){
				HttpErrorRespHelper.send401();
			}
			if (auth.andRole().length > 0) {
				try {
					StpUtil.checkRoleAnd(auth.andRole());
				}catch (Exception e){
					HttpErrorRespHelper.send403("this API need these roles : " + StringUtils.join(auth.andRole(), ","));
				}
			}
			if (auth.orRole().length > 0) {
				try {
					StpUtil.checkRoleAnd(auth.orRole());
				}catch (Exception e){
					HttpErrorRespHelper.send403("this API need these roles : " + StringUtils.join(auth.orRole(), ","));
				}
			}
		}
	}
	/**
	 * @param clazz
	 * @param method
	 * @throws Exception
	 */
	private void invoke(Class<?> clazz, Method method, Object... args) throws Exception {
		if (Objects.nonNull(clazz)) {
			if (Objects.nonNull(method)) {
				Object obj = ReflectionUtils.newInstance(clazz);
				Field[] fields = clazz.getDeclaredFields();
				Injector injector = ModuleRegister.getInjector();
				Stream.of(fields).forEach(field -> {
					if (field.isAnnotationPresent(com.google.inject.Inject.class)
							|| field.isAnnotationPresent(javax.inject.Inject.class)) {
						try {
							ReflectionUtils.setField(obj, field, injector.getInstance(field.getType()));
						} catch (Exception e) {
							e.printStackTrace();
							log.error(e.getMessage());
						}
					}
					if (field.isAnnotationPresent(Properties.class)) {
						Properties properties = field.getAnnotation(Properties.class);
						String value = properties.value();
						String defaultValue = properties.defaultValue();
						try {
							ReflectionUtils.setField(obj, field, CoreProperties.getProperties(value, defaultValue));
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
				HttpRespHelper.sendResponseData(ReflectionUtils.invokeMethod(obj, method, args), HttpStatusCode.SC_OK);
			}
		}
	}

	@Override
	public void doGet() throws Exception{
		invoke(methodMap_GET);
	}

	@Override
	public void doPut() throws Exception{
		invoke(methodMap_PUT);
	}

	@Override
	public void doPost() throws Exception{
		invoke(methodMap_POST);
	}

	@Override
	public void doHead() throws Exception{
		invoke(methodMap_HEAD);
	}

	@Override
	public void doDelete() throws Exception{
		invoke(methodMap_DELETE);
	}

	@Override
	public void doOptions() throws Exception{
		invoke(methodMap_OPTIONS);
	}

	@Override
	public void doTrace() throws Exception{
		throw new Exception("TRACE http method not supported!");
	}

}
