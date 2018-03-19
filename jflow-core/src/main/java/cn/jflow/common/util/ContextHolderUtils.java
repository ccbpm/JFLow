package cn.jflow.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

	public synchronized static ContextHolderUtils getInstance() {
		if (contextHolder == null) {
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
		try {
			RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
			if (ra instanceof ServletRequestAttributes){
				return ((ServletRequestAttributes) ra).getRequest();
			}
			else if (ra instanceof cn.jflow.common.context.ServletRequestAttributes){
				return ((cn.jflow.common.context.ServletRequestAttributes)ra).getRequest(); 
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static HttpServletResponse getResponse() {
		try {
			RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
			if (ra instanceof ServletRequestAttributes){
				return (HttpServletResponse)ra.getClass().getMethod("getResponse").invoke(ra);
			}
			else if (ra instanceof cn.jflow.common.context.ServletRequestAttributes){
				return ((cn.jflow.common.context.ServletRequestAttributes)ra).getResponse(); 
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * SpringMvc下获取session
	 * @return
	 */
	public static HttpSession getSession() {
		HttpSession session = getRequest().getSession();
//		java.util.Enumeration e = session.getAttributeNames();
//		while (e.hasMoreElements()) {
//			String name = (String) e.nextElement();
//			String value = session.getAttribute(name).toString();
//			System.out.println(name + " = " + value);
//		}
		return session;
	}

	public static void addCookie(String name, int expiry, String value) {
		Cookie cookies[] = getRequest().getCookies();
		if (cookies == null) {
			Cookie cookie = new Cookie(name, value);
			cookie.setMaxAge(expiry);
			getResponse().addCookie(cookie);
		} else {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					cookie.setValue(value);
					cookie.setMaxAge(expiry);
					getResponse().addCookie(cookie);
					return;
				}
			}
			Cookie cookie = new Cookie(name, value);
			cookie.setMaxAge(expiry);
			getResponse().addCookie(cookie);
		}

	}

	public static Cookie getCookie(String name) {
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

	public static void deleteCookie(String name) {
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

	public static void clearCookie() {
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
	public static <T> T getBean(String name) {
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
