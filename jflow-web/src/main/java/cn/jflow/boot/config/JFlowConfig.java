package cn.jflow.boot.config;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import bp.da.DataType;
import bp.difference.redis.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import bp.difference.ContextHolderUtils;
import bp.difference.GvtvPropertyPlaceholderConfigurer;
import bp.difference.SystemConfig;

/**
 * JFlow配置
 * @author Bryce Han
 *
 */
@Configuration
@ComponentScan(basePackages = {"bp.difference", "Controller", "cn.jflow.boot"})
public class JFlowConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(JFlowConfig.class);
	
	private static ApplicationContext applicationContext;
	
	@Autowired
	Environment env;
	
	/**
	 * 属性文件jflow.properties配置
	 * @return
	 */
	@Bean
	public static GvtvPropertyPlaceholderConfigurer propertyConfigurer() {
		GvtvPropertyPlaceholderConfigurer propertyConfigurer = new GvtvPropertyPlaceholderConfigurer();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:jflow.properties");
		propertyConfigurer.setLocation(resource);
		return propertyConfigurer;
	}
	
	/**
	 * 配置JFlow数据库属性
	 */
	public void loadJFlowDatabaseConfig() {
		//配置jflow属性
		Hashtable<String, Object> props = SystemConfig.getCS_AppSettings();
		
		String url = env.getProperty("spring.datasource.url");
		Hashtable<String, Object> dbProps = configDatabaseParams(url);
		props.putAll(dbProps);

		
		String username = env.getProperty("spring.datasource.username");
		String password = env.getProperty("spring.datasource.password");
		String testQuery = env.getProperty("spring.datasource.hikari.connection-test-query");
		props.put("JflowUser", username);
		props.put("JflowPassword", password);
		props.put("JflowTestSql", testQuery);
	}

	/**
	 * 根据url解析数据库类型和数据库名, 并配置JFlow系统设置
	 * @param url 数据源url
	 */
	private Hashtable<String, Object> configDatabaseParams(String url) {
		Hashtable<String, Object> props = new Hashtable<>();
		if(StringUtils.isNotBlank(url)) {
			String dbType = null;
			String dbDatabase = null;
			if(url.startsWith("jdbc:mysql")) {
				//mysql
				dbType = "mysql";
				String tmpUrl = url;
				if(url.contains("?")) {
					tmpUrl = url.substring(0, url.indexOf("?"));
				}
				if(tmpUrl.contains("/")) {
					dbDatabase = tmpUrl.substring(tmpUrl.lastIndexOf("/") + 1);
				}else {
					logger.error("mysql url配置错误，请检查配置。url: {}", JFlowConfig.class.getName(), url);
				}
				
			}else if(url.startsWith("jdbc:jtds:sqlserver") || url.startsWith("jdbc:microsoft:sqlserver")) {
				//sqlserver
				dbType = "mssql";
				if(url.contains("DatabaseName=")) {
					String[] params = url.split(";");
					for (String param : params) {
						if(param.startsWith("DatabaseName=")) {
							dbDatabase = param.substring(param.lastIndexOf("="));
						}
					}
				}else {
					String[] params = url.split(";");
					for (String param : params) {
						Pattern pattern = Pattern.compile("jdbc:(jtds|microsoft):sqlserver://.+:\\d/(.+)");
						Matcher matcher = pattern.matcher(param);
						if(matcher.find()) {
							dbDatabase = matcher.group(2);
						}
					}
				}
			}else if(url.startsWith("jdbc:oracle:thin")) {
				//oracle
				dbType = "oracle";
				Pattern pattern = Pattern.compile("jdbc:oracle:thin:@.+:\\d+/(.+)");
				Matcher matcher = pattern.matcher(url);
				if(matcher.find()) {
					dbDatabase = matcher.group(1);
				}
			}
			else if(url.startsWith("jdbc:kingbase8")) {
				//oracle
				dbType = env.getProperty("KingBaseVer");
				if(DataType.IsNullOrEmpty(dbType))
				   dbType="KingBaseR3";
				//dbType = "kingbase";
				Pattern pattern = Pattern.compile("jdbc:kingbase8://.+:\\d+/(.+)");
				Matcher matcher = pattern.matcher(url);
				if(matcher.find()) {
					dbDatabase = matcher.group(1);
				}
			}
			if(StringUtils.isNotBlank(dbType) && StringUtils.isNotBlank(dbDatabase)) {
				props.put("AppCenterDBType", dbType);
				props.put("AppCenterDBDatabase", dbDatabase);
			}else {
				logger.error("从url解析数据库类型和数据库名出错. url: {}, dbType: {}, dbDatabase: {}", url, dbType, dbDatabase);
			}
			
			props.put("AppCenterDSN", url);
		}
		return props;
	}
	
	/**
	 * JFlow集成上下文工具类
	 * @param dataSource 数据源
	 * @return
	 */
	@Bean
	public ContextHolderUtils jflowContextHolderUtils(DataSource dataSource,RedisUtils redisUtils) {
		loadJFlowDatabaseConfig();
		ContextHolderUtils contextHolderUtils = new ContextHolderUtils();
		contextHolderUtils.setDataSource(dataSource);
		contextHolderUtils.setRedisUtils(redisUtils);
		if(applicationContext != null) {
			contextHolderUtils.setApplicationContext(applicationContext);
		}
		
		return contextHolderUtils;
	}


	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		JFlowConfig.applicationContext = applicationContext;
	}

}
