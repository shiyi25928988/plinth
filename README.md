# plinth

### 介绍
plinth是一个轻量级的web框架（随便起了个名字，它总得有个名字）


由于需要经常快速开发一些轻量级的单体web应用，所以开发了这个框架。

底层依赖了Guice 和 Jetty ,没有使用*Springboot*

使用Guice作为依赖注入框架，如果不熟悉的同学，请移步 https://github.com/google/guice


### 使用说明
1. **没有** 推送到MAVEN中央仓库，所以想要使用的情况下，只能mvn install 到本地
2. 添加依赖
```xml
    <dependencies>
        <dependency>
            <groupId>yi.shi</groupId>
            <artifactId>plinth</artifactId>
            <version>jre-8</version>
        </dependency>
    </dependencies>
```
3. 应用启动入口类
```java
@PropertiesFile(files = { "application.properties" })
@Slf4j
public class App 
{
    public static void main(String... strings) {
        try {
            ServiceBooter.startFrom(App.class, new CustomerModule());
        } catch (ClassNotFoundException | IOException e) {
            log.error(e.getMessage());
        }
    }
}
```
@PropertiesFile 注解指向的配置文件需要放在resources文件夹下，可以放置多个配置文件， 或者是一个网络位置
```
@PropertiesFile(files = { "http://192.168.*.*/somepath/application.properties" })
```
`ServiceBooter.startFrom(App.class, new CustomerModule());`中的CustomerModule是字定义module，用于配置注入，有点类似spring中的configuration类，不熟悉的同学，请移步 https://github.com/google/guice

4. 实现一个http接口

```
@HttpService
public class HelloWord {
```
需要在类名标注@HttpService
```
	@GET
	@HttpPath(value = "/hello")
	public JSON<String> hello(@HttpBody SomeType param) {
		return new JSON<String>("Hello world");
	}
```

@GET标注了http method类型，还有@POST 等
@HttpPath 标注接口路径
@HttpBody 用于标注请求体
@HttpParam 用标注URL参数 

5. 对象注入
直接使用了Guice 的 @Inject注解
```
    @Inject
    TestService testService;
```
对于服务的注入，可以在module类中声明，或者用@Implementby注解， 不熟悉的同学，请移步 https://github.com/google/guice
```
@ImplementedBy(TestServiceImpl.class)
public interface TestService {
    String test();
}
```
```
public class TestServiceImpl implements TestService {
    @Override
    public String test() {
        return "test";
    }
}
```

对于配置文件中的字符串注入，使用 @Properties 注解, 

```
    @Properties("some.value", defaultValue = "defaultValue")
    String someValue;
```
> 注意：只能注入String类型，其他 int float 等类型，请自己转换

返回类型的问题：
这个框架里定义了 HTML， JSON<T> ， BINARY 三种类型， 均继承自ReturnType接口，为了定义返回的页面、json、图片等
例如：我用J2HTML实现一个页面，就可以通过HTML类型返回
```
@HttpService
public class LoginPage {

    @GET
    @HttpPath(value = "/page/login")
    HTML loginPage(){
        HTML html = new HTML();
            html.setHtmlContent(html(
                head(
                    title("login page"),
                    ... 
```
6. 数据库的使用
mybatis提供了guice版本的，可以直接使用

添加依赖:
```
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.1</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.16</version>
        </dependency>
```
在配置中提供数据库连接的信息：
```
mybatis.environment.id=development
JDBC.driver=com.mysql.cj.jdbc.Driver
JDBC.url=jdbc:mysql://192.168.3.47:3306/somedb?serverTimezone=UTC&characterEncoding=utf-8
JDBC.username=root
JDBC.password=123456
```
写一些mapper接口：
```
@Mapper
public interface SysAccountMapper {
	
    @Select("SELECT "
    		+ "id,"
    		+ "role_id AS roleId,"
            + "account,"
            + "password,"
            + "status"
    		+ " FROM sys_account "
    		+ "WHERE account = #{account}")
    Optional<SysAccount> getAccount(@Param("account") String account);
```
到这里还没完，写成这样还不能使用，需要继承 MyBatisModule 写一个module类，放到启动类里，
```
public class DataSourceModule extends MyBatisModule {
	
	public DataSourceModule() {
	}

	@Override
	protected void initialize() {
		Names.bindProperties(binder(), System.getProperties());
		bindDataSourceProviderType(PooledDataSourceProvider.class);
		bindTransactionFactoryType(JdbcTransactionFactory.class);
		
		addMapperClass(SysAccountMapper.class);//前面的照抄，这里加自己的mapper
	}
```
7. 一些配置
通过 server.port= 指定启动端口，

还可以通过以下配置静态资源的访问
server.resources.context= //这里是静态资源的http路径， 如果不配置，默认值是 /static
server.resources.folder=  //这里是实际指向当前服务器的实际地址 ， 默认值是 代码中/resources/static路径


8. 其他
plinth 可以作为类似于nginx的web容器来使用，依仗的是jetty的静态资源服务功能，
我们曾经在这个框架上部署过一个编译好的vue项目，不需要实现任何接口，只需在配置文件中将 `server.resources.context=/` 静态资源context设置成域的根path,
然后将 `server.resources.folder=`指向存放vue项目编译出来的dist文件夹

web应用的混合模式，通过`server.hybrid=true`配置开启, 静态资源和HTTP接口共享 root context, 通过静态服务启动的web页面,可以直接调用@HttpPath声明的http接口,
打开混合模式后 server.resources.context= 配置将会失效

9. 权限认证
plint 集成了SA-TOKEN, 通过@AUTH 注解实现对 http接口的 小颗粒度的权限控制
```
    @AUTH(orRole = "admin", authUrl = "/page/login")
    @GET
    @HttpPath("/testAuth")
    public JSON<String> testAuth(){
        
```
@AUTH认证失败，默认返回401错误，也可以通过 authUrl 参数跳转到 登陆页面
@AUTH中的角色控制，角色字符串数组类型，可以传递给 orRole 或 andRole， orRole中满足一个角色即可，andRole则需满足所有角色
登陆接口可以使用sa-token的工具类StpUtil 登陆 ，或者使用 AuthHelper 的 login(Object user, String...roles)
> 框架已经引入SA-TOKEN ， 无需再自己引入依赖

10. 编译构建
没有整合build  plugin 下面贴出可用的配置，如果哪位同学觉得可以优化的，请联系我
```xml
<build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>${maven.plugin.version}</version>
                <configuration>
                    <!-- 此处按需编写更具体的配置 -->
                    <filters>
                        <filter>
                            <artifact>*:*</artifact>
                            <excludes>
                                <exclude>**/module-info.class</exclude>
                            </excludes>
                        </filter>
                    </filters>
                </configuration>
                <executions>
                    <execution>
                        <!-- 和 package 阶段绑定 -->
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>${maven.plugin.version}</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <mainClass>application.Main</mainClass>
                            <!--  这里是启动类    -->
                        </manifest>
                    </archive>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>${maven.plugin.version}</version>
                <executions>
                    <execution>
                        <id>copy</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>
                                ${project.build.directory}/lib
                            </outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <version>${maven.plugin.version}</version>
                <executions>
                    <execution>
                        <id>copy-static</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.directory}/src/main/resources/static</outputDirectory>
                            <resources>
                                <resource>
                                    <directory>${basedir}/src/main/resources/static</directory>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>${maven.plugin.version}</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                            <mainClass>application.Main</mainClass>
                            <!--  这里是启动类    -->
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
        </plugins>

    </build>
```
