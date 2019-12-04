package BP.Difference.Context;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;

/**
 * 获取RequestResponse过滤器
 * @author ThinkGem
 */
public class RequestResponseFilter implements Filter {
	
	private static final String REQUEST_ATTRIBUTES_ATTRIBUTE =
			RequestContextListener.class.getName() + ".REQUEST_ATTRIBUTES";
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
		ServletRequestAttributes attributes = new ServletRequestAttributes(
				(HttpServletRequest) request, (HttpServletResponse) response);
		request.setAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE, attributes);
		LocaleContextHolder.setLocale(request.getLocale());
		RequestContextHolder.setRequestAttributes(attributes);
		response.setContentType("textml;charset=UTF-8");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		((HttpServletResponse) response).setHeader("Access-Control-Max-Age", "0");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Credentials", "true");
		((HttpServletResponse) response).setHeader("XDomainRequestAllowed","1");
		filter.doFilter(request, response);
	}
	
	public void destroy() {
		RequestAttributes threadAttributes = RequestContextHolder.getRequestAttributes();
		if (threadAttributes != null) {
			LocaleContextHolder.resetLocaleContext();
			RequestContextHolder.resetRequestAttributes();
			if (threadAttributes instanceof ServletRequestAttributes) {
				((ServletRequestAttributes) threadAttributes).requestCompleted();
			}
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// 什么也不做
	}

}
