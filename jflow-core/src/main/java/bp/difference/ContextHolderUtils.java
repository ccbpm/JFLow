package bp.difference;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import bp.difference.handler.CommonUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * JFlow上下文工具类
 * @version 2016-5-9
 */
public class ContextHolderUtils implements ApplicationContextAware, DisposableBean {

	private static ContextHolderUtils contextHolder;
	private static ApplicationContext springContext;

	
	// 数据源设置
	private DataSource dataSource;
	
	// 第三方系统session中的用户编码，设置后将不用再调用登录方法，直接获取当前session进行登录。
	private String userNoSessionKey;

	private static ApplicationContext context;



	public synchronized static ContextHolderUtils getInstance() throws Exception{
		if (contextHolder == null) {
			if(springContext == null){
				//长沙获取上下文的方法
				ServletContext servletContext=getRequest().getSession().getServletContext();
				ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
				if(ctx == null)	{
					try{
						ctx = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
					}catch(Exception e){
						ctx = null;
					}

				}
				if(ctx == null)
					throw new Exception("获取上下文ApplicationContext失败");
				springContext = ctx;


			}
				
			contextHolder = springContext.getBean(ContextHolderUtils.class);
		}
		return contextHolder;
	}

	/**
	 * SpringMvc下获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return CommonUtils.getRequest();
	}

	public static HttpServletResponse getResponse() throws Exception {
        return CommonUtils.getResponse();
	}

	/**
	 * SpringMvc下获取session
	 * @return
	 */
	public static HttpSession getSession() {
		HttpSession session = getRequest().getSession();

		return session;
	}

	public static void addCookie(String name, String value) throws Exception {
		int expiry = 60 * 60 * 24 * 8;
		if(getRequest()!= null){
			Cookie cookies[] = getRequest().getCookies() ;
			if (cookies == null) {
				Cookie cookie = new Cookie(name, value);
				cookie.setMaxAge(expiry);
				getResponse().addCookie(cookie);
			} else {
				for (Cookie cookie : cookies) {
					if (name.equals(cookie.getName())) {
						cookie.setValue(value);
						cookie.setMaxAge(expiry);
						cookie.setPath("/");
						getResponse().addCookie(cookie);
						return;
					}
				}
				Cookie cookie = new Cookie(name, value);
				cookie.setMaxAge(expiry);
				cookie.setPath("/");//所有的路径都可以访问cookie
				getResponse().addCookie(cookie);
			}
		}
	}

	public static Cookie getCookie(String name) throws Exception {
		Cookie cookies[] = getRequest().getCookies();
		if (cookies == null || name == null || name.length() == 0)
			return null;
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}

	public static void deleteCookie(String name) throws Exception{
		Cookie cookies[] = getRequest().getCookies();
		if (cookies == null || name == null || name.length() == 0)
			return;
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				cookie.setValue("");
				cookie.setMaxAge(0);
				getResponse().addCookie(cookie);
			}
		}
	}

	public static void clearCookie() throws Exception{
		Cookie[] cookies = getRequest().getCookies();
		if (null == cookies)
			return;
		for (Cookie cookie : cookies) {
			cookie.setValue("");
			cookie.setMaxAge(0);
			getResponse().addCookie(cookie);

		}
	}

	/**
	 * 获取数据源
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 获取数据源
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 获取第三方系统session中的用户编码
	 * @return
	 */
	public String getUserNoSessionKey() {
		return userNoSessionKey;
	}

	/**
	 * 设置第三方系统session中的用户编码
	 * @param userNoSessionKey
	 */
	public void setUserNoSessionKey(String userNoSessionKey) {
		this.userNoSessionKey = userNoSessionKey;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws Exception {
		return (T) springContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return springContext.getBean(requiredType);
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		ContextHolderUtils.springContext = arg0;
	}

	@Override
	public void destroy() throws Exception {
		ContextHolderUtils.springContext = null;
	}

}
